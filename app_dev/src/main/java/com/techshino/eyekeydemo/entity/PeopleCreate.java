package com.techshino.eyekeydemo.entity;

/**
 * Created by wangzhi on 2015/12/10.
 */
public class PeopleCreate {

    private int added_crowd;

    private int added_face;

    private int added_fingerprint;

    private int added_iris;

    private String people_id;

    private String people_name;

    private String res_code;

    public void setAdded_crowd(int added_crowd) {
        this.added_crowd = added_crowd;
    }

    public int getAdded_crowd() {
        return this.added_crowd;
    }

    public void setAdded_face(int added_face) {
        this.added_face = added_face;
    }

    public int getAdded_face() {
        return this.added_face;
    }

    public void setAdded_fingerprint(int added_fingerprint) {
        this.added_fingerprint = added_fingerprint;
    }

    public int getAdded_fingerprint() {
        return this.added_fingerprint;
    }

    public void setAdded_iris(int added_iris) {
        this.added_iris = added_iris;
    }

    public int getAdded_iris() {
        return this.added_iris;
    }

    public void setPeople_id(String people_id) {
        this.people_id = people_id;
    }

    public String getPeople_id() {
        return this.people_id;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getPeople_name() {
        return this.people_name;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getRes_code() {
        return this.res_code;
    }

    @Override
    public String toString() {
        return "PeopleCreate{" +
                "added_crowd=" + added_crowd +
                ", added_face=" + added_face +
                ", added_fingerprint=" + added_fingerprint +
                ", added_iris=" + added_iris +
                ", people_id='" + people_id + '\'' +
                ", people_name='" + people_name + '\'' +
                ", res_code='" + res_code + '\'' +
                '}';
    }
}
