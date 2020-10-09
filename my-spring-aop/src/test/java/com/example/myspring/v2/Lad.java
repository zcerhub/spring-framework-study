package com.example.myspring.v2;

public class Lad implements Boy {

    private String name;
    private Girl friend;
    private Money money;

    public Lad() {
    }

    public Lad(String name) {
        this.name = name;
    }

    public Lad(String name, Girl friend) {
        this.name = name;
        this.friend = friend;
        System.out.println("调用了包含girl的构造函数");
    }

    public Lad(String name, MagicGirl magicGirl) {
        this.name = name;
        this.friend = magicGirl;
        System.out.println("调用了包含magicGirl的构造函数");
    }

    public Lad(String name, Money money) {
        this.name = name;
        this.money = money;
    }


    public Girl getFriend() {
        return friend;
    }

    public void setFriend(Girl friend) {
        this.friend = friend;
    }

    @Override
    public void sayLove() {
        if (friend != null) {
            System.out.println("i have a friend"+friend+"\n"+hashCode());
        }else{
            System.out.println("i have not a friend."+hashCode());
        }
    }

    @Override
    public void play() {
        if (money == null) {
            System.out.println("i have not money,let's play");
        }else{
            System.out.println("i have much money,let's play");
        }
    }

    public void init() {
        System.out.println("执行lad的初始化方法");
    }

    public void destroy() {
        System.out.println("执行lad的destroy方法");
    }
}
