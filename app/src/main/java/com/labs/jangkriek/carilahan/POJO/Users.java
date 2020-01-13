package com.labs.jangkriek.carilahan.POJO;

public class Users {

    private int id;
    private String username;
    private String email;

    public Users(int id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
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
