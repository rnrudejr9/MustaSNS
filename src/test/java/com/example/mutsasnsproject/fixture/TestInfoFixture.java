package com.example.mutsasnsproject.fixture;

import lombok.Getter;
import lombok.Setter;


public class TestInfoFixture {
    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setPostId(1);
        info.setUserId(1);
        info.setUserName("user");
        info.setPassword("password");
        info.setTitle("title");
        info.setBody("body");
        return info;
    }

    @Getter
    @Setter
    public static class TestInfo {
        private long postId;
        private long userId;
        private String userName;
        private String password;
        private String title;
        private String body;
    }

}

