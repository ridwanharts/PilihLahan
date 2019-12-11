package com.labs.jangkriek.carilahan.POJO;

import java.util.List;

public class Respon {

    String value;
    String message;
    List<Lokasi> result;
    List<PointLatLong> point;

    public List<PointLatLong> getPoint() {
        return point;
    }

    public void setPoint(List<PointLatLong> point) {
        this.point = point;
    }

    public List<Lokasi> getLokasiList() {
        return result;
    }

    public void setLokasiList(List<Lokasi> lokasiList) {
        this.result = lokasiList;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
