package com.daus.softwarequiz;

public class User {
    private String userName;
    private int webScore;
    private int oopScore;
    private int dataStrucScore;
    private int sadScore;
    private int totalScore;

//    public User(String userName) {
//        this.userName = userName;
//        this.webScore = 0;
//        this.oopScore = 0;
//        this.dataStrucScore = 0;
//        this.sadScore = 0;
//    }

    public User(String userName) {
        this.userName = userName;
    }

    public User(String userName, int webScore, int oopScore, int dataStrucScore, int sadScore) {
        this.userName = userName;
        this.webScore = webScore;
        this.oopScore = oopScore;
        this.dataStrucScore = dataStrucScore;
        this.sadScore = sadScore;
    }

    public User() {
    }

    public int getOopScore() {
        return oopScore;
    }

    public void setOopScore(int oopScore) {
        this.oopScore = oopScore;
    }

    public int getDataStrucScore() {
        return dataStrucScore;
    }

    public void setDataStrucScore(int dataStrucScore) {
        this.dataStrucScore = dataStrucScore;
    }

    public int getSadScore() {
        return sadScore;
    }

    public void setSadScore(int sadScore) {
        this.sadScore = sadScore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWebScore() {
        return webScore;
    }

    public void setWebScore(int webScore) {
        this.webScore = webScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore() {
        this.totalScore = getOopScore() + getWebScore() + getSadScore() + getDataStrucScore();
    }
}
