package com.test.youtube;

import com.test.youtube.model.Record;
import com.test.youtube.model.User;
import com.test.youtube.repository.DataHelper;
import com.test.youtube.repository.UserHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class Job {
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    DataHelper dataHelper;
    UserHelper userHelper;
    List<String> history = new ArrayList<>();
    private SimpMessagingTemplate socket;
    String googleApiToken;
    String jobId;
    long lastRunTime = 0;
    private User user;

    private String videoRequestURLFormat = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=%s&key=%s";
    private String commentRequestURLFormat = "https://www.googleapis.com/youtube/v3/commentThreads?chart=mostPopular&part=snippet&videoId=%s&key=%s&textFormat=plainText";
    private String videoResponseURLFormat = "https://www.youtube.com/embed/%s";

    public Job(User user, SimpMessagingTemplate socket, DataHelper dataHelper, UserHelper userHelper, String googleApiToken) {
        this.jobId = UUID.randomUUID().toString();
        this.user = user;
        this.socket = socket;
        this.dataHelper = dataHelper;
        this.userHelper = userHelper;
        this.googleApiToken = googleApiToken;
    }

    private String process() throws IOException {
        Record newRecord = new Record("NULL", this.getUser().getUserName(), "NULL", "NULL");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String videoResponse = restTemplate.getForObject(String.format(videoRequestURLFormat, this.getUser().getRegion(), this.googleApiToken), String.class);
            JSONObject userJSON = new JSONObject(videoResponse);
            JSONArray videoJsonArray = (JSONArray) userJSON.get("items");
            JSONObject currentVideoDetails = (JSONObject) videoJsonArray.get(0);
            String videoID = currentVideoDetails.getString("id");
            String comment = "$COMMENT_URL$";
            try {
                String commentResponse = restTemplate.getForObject(String.format(commentRequestURLFormat, videoID, this.googleApiToken), String.class);
                JSONObject commentJSON = new JSONObject(commentResponse);
                JSONArray commentJsonArray = (JSONArray) commentJSON.get("items");

                JSONObject currentObject = (JSONObject) commentJsonArray.get(0);
                JSONObject snippet = (JSONObject) currentObject.get("snippet");

                JSONObject topLevelCommentJSON = (JSONObject) snippet.get("topLevelComment");

                JSONObject snpJSON = (JSONObject) topLevelCommentJSON.get("snippet");
                comment = snpJSON.getString("textDisplay").replaceAll("[\\n\\t ]", "");
            } catch (Exception ignored) {
            }
            newRecord = new Record(UUID.randomUUID().toString(), this.getUser().getUserName(), String.format(videoResponseURLFormat, videoID), comment);
            dataHelper.addData(newRecord);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return newRecord.toString();
    }

    public Optional<String> processRunner() {
        Optional<String> res = Optional.empty();
        if (System.currentTimeMillis() - lastRunTime > this.getUser().getRate().get()) {
            lastRunTime = System.currentTimeMillis();
            try {
                res = Optional.of(process());
                socket.convertAndSend("/topic/" + user.getUserName(), res.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public void changeParams(User updated) {
        try {
            this.userHelper.editUser(updated); // saves to file==db
            this.getUser().setRate(updated.getRate().get());
            this.getUser().setRegion(updated.getRegion());
        } catch (Exception e) {
        }
    }
}
