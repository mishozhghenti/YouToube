package com.test.youtube.model;

import lombok.Data;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Data
public class User {
    private String userName;
    private AtomicLong rate = new AtomicLong();
    private String region;
    private String password;

    public User(String userName, long rate, String region) {
        this.userName = userName;
        this.rate.set(rate);
        this.region = region;
    }

    public User(String userName, long rate, String region, String password) {
        this.userName = userName;
        this.rate.set(rate);
        this.region = region;
        this.password = password;
    }


    public void setRate(long newRate) {
        this.rate.set(newRate);
    }

    public void setRegion(String newRegion) {
        this.region = newRegion;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        User userObj = (User) obj;
        long currentRate = this.getRate().get();
        long objectUserRate = userObj.getRate().get();
        return this.getUserName().equals(userObj.getUserName()) && this.getRegion().equals(userObj.getRegion()) &&
                this.getPassword().equals(userObj.getPassword()) && currentRate == objectUserRate;
    }

    @Override
    public String toString() {
        return this.userName + " " + this.rate + " " + this.region + " " + this.password + "\n";
    }
}
