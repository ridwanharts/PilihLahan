package com.labs.jangkriek.carilahan.POJO;

import com.google.gson.annotations.SerializedName;

public class ResponUsers {

    @SerializedName("value")
    private String value;

    @SerializedName("message")
    private String message;

    @SerializedName("id_user")
    private int id_user;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
