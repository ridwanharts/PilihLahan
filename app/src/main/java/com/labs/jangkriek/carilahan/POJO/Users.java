package com.labs.jangkriek.carilahan.POJO;

public class Users {

    private int id;
    private String username;
    private String email;
    private String no_hp;

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public Users(int id, String username, String email, String no_hp){
        this.id = id;
        this.username = username;
        this.email = email;
        this.no_hp = no_hp;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
