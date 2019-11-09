package com.labs.jangkriek.carilahan.POJO;

import com.google.gson.annotations.SerializedName;

public class Admin {

    @SerializedName("value")
    private String value;

    @SerializedName("message")
    private String message;

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
}
