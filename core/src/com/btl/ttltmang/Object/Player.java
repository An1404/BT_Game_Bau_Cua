package com.btl.ttltmang.Object;

import com.badlogic.gdx.graphics.Texture;
import com.btl.ttltmang.Main;

public class Player {
    private String id;
    private String name;
    private String phone;
    private int room;
    private long coin = 2000000;
    private int avatar;

    public Player() {
    }

    public Player(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Player(String id, String name, String phone,long coin) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.coin = coin;
    }

    public Player(String id, String name, int room, long coin) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.coin = coin;
    }

    public Player(String id, String name, int room, long coin, int avatar) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.coin = coin;
        this.avatar = avatar;
    }

    public Player(String id, String name, String phone, int room, long coin, int avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.room = room;
        this.coin = coin;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
