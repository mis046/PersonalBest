package com.example.team13.personalbest_team13_skeleton.friendlist;

public class friendListServiceFactory extends Factory<friendListService> {
    private static friendListServiceFactory instance;
    private String userEmail;
    public friendListServiceFactory(String userEmail) {
        this.userEmail = userEmail;
        super.userEmail = userEmail;
    }
    public static friendListServiceFactory getInstance(String userEmail) {
        if (instance == null) {
            instance = new friendListServiceFactory(userEmail);
        }
        return instance;
    }
}
