package com.test.youtube.repository;

import com.test.youtube.model.User;

import java.io.*;
import java.util.HashMap;


public class UserHelper {
    private File file;

    public UserHelper(String usersFileName) throws IOException {
        file = new File(usersFileName);
        file.createNewFile();
    }

    public void addNewUser(User newUser) throws IOException {
        FileOutputStream oFile = new FileOutputStream(file, true);
        oFile.write((newUser.toString()).getBytes());
    }

    public User logIn(String userName, String password) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        User res = null;
        while (line != null) {
            String[] split = line.split(" ");
            String currentUserName = split[0]; // at 0 index in the file is saved username
            String currentPassword = (split[3]).split("\n")[0];

            if (currentUserName.equals(userName)) {
                if (currentPassword.equals(password)) {// at 3 index in the file is saved user;s password
                    int currentPeriod = Integer.parseInt(split[1]);
                    String currentRegion = split[2];
                    res = new User(currentUserName, currentPeriod, currentRegion, currentPassword);
                } else {
                    res = null;
                }
            }
            line = bufferedReader.readLine();
        }
        return res;
    }

    public HashMap<String, User> getUsers() throws IOException {
        HashMap<String, User> allUsers = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();

        while (line != null) {
            String[] split = line.split(" ");
            String currentUserName = split[0]; // at 0 index in the file is saved username
            int currentPeriod = Integer.parseInt(split[1]);
            String currentRegion = split[2];
            String currentPassword = (split[3]).split("\n")[0];

            allUsers.put(currentUserName, new User(currentUserName, currentPeriod, currentRegion, currentPassword));
            line = bufferedReader.readLine();
        }
        return allUsers;
    }
}
