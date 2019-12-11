package com.labs.jangkriek.carilahan.POJO;

public class PointLatLong {

    private int id;
    private int no_point;
    private Double longitude;
    private Double latitude;
    private String id_user;
    private String created_at;

    public PointLatLong (int id, int no_point, double latitude, double longitude, String id_user, String created_at){
        this.id = id;
        this.no_point = no_point;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id_user = id_user;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo_point() {
        return no_point;
    }

    public void setNo_point(int no_point) {
        this.no_point = no_point;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
