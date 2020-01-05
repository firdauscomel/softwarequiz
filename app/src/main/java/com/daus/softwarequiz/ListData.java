package com.daus.softwarequiz;

public class ListData {

    private String userName;
    private int totalScore;


    public  ListData(){


    }

    public ListData(String userName, int totalScore) {
        this.userName = userName;
        this.totalScore = totalScore;

    }

    public String getUserName() {
        return userName;
    }

    public int getTotalScore() {
        return totalScore;
    }
}