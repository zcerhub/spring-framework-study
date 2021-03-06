package com.example.myspring.v2;

import java.util.Objects;

public class MagicGirl implements Girl {
    private String name;
    private Boy friend;

    public MagicGirl() {
    }

    public MagicGirl(String name, Boy friend) {
        this.name = name;
        this.friend = friend;
    }

    public MagicGirl(String name) {
        this.name = name;
    }

    public String getName() {
        System.out.println("执行magic的getName方法");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boy getFriend() {
        System.out.println("执行magic的getFriend方法");
        return friend;
    }

    public void setFriend(Boy friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "MagicGirl{" +
                "name='" + name + '\'' +
                ", friend=" + friend +
                '}';
    }
}
