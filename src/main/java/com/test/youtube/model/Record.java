package com.test.youtube.model;

import lombok.Getter;

@Getter
public class Record {
    private String recordId;
    private String username;
    private String videoURL;
    private String topCommentURL;

    public Record(String recordId, String username, String videoURL, String topCommentURL) {
        this.recordId = recordId;
        this.username = username;
        this.videoURL = videoURL;
        this.topCommentURL = topCommentURL;
    }

    @Override
    public String toString() {
        return recordId + " " + username + " " + videoURL + " " + topCommentURL + "\n";
    }
}
