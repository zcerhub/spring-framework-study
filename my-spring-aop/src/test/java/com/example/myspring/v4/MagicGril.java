package com.example.myspring.v4;

public class MagicGril implements Gril {
    private String name;
    private Boy friend;
    public MagicGril(){}
    public MagicGril(String name) {
        this.name = name;
    }

    public Boy getFriend() {
        return friend;
    }
    public void setFriend(Boy friend) {
        this.friend = friend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MagicGril{" +
                "name='" + name + '\'' +
                '}';
    }
    
    public void start() {
        System.out.println("开始调用初始化方法");
    }
    
    public void end() {
        System.out.println("开始调用销毁方法");
    }
}
