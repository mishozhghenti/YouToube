package com.test.youtube.repository;

import com.test.youtube.model.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private File file;

    public DataHelper(String usersDataFileName) throws IOException {
        file = new File(usersDataFileName);
        file.createNewFile();
    }

    public void addData(Record record) throws IOException {
        FileOutputStream oFile = new FileOutputStream(file, true);
        oFile.write((record.toString()).getBytes());
    }

    public List<Record> getUserData(String userName) throws IOException {
        List<Record> userMedia = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();

        while (line != null) {
            String[] split = line.split(" ");
            String currentUserName = split[1];
            if (currentUserName.equals(userName)) {
                String recordId = split[0];
                String videoURL = split[2];
                String topCommentURL = split[3];
                userMedia.add(new Record(recordId, currentUserName, videoURL, topCommentURL));
            }
            line = bufferedReader.readLine();
        }
        return userMedia;
    }
}
