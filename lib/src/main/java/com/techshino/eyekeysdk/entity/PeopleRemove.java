package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class PeopleRemove {

    private String message;

    private String res_code;

    private boolean success;

    private int face_removed;

    private int fingerprint_removed;

    private int iris_removed;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getFace_removed() {
        return face_removed;
    }

    public void setFace_removed(int face_removed) {
        this.face_removed = face_removed;
    }

    public int getFingerprint_removed() {
        return fingerprint_removed;
    }

    public void setFingerprint_removed(int fingerprint_removed) {
        this.fingerprint_removed = fingerprint_removed;
    }

    public int getIris_removed() {
        return iris_removed;
    }

    public void setIris_removed(int iris_removed) {
        this.iris_removed = iris_removed;
    }

    @Override
    public String toString() {
        return "PeopleRemove{" +
                "message='" + message + '\'' +
                ", res_code='" + res_code + '\'' +
                ", success=" + success +
                ", face_removed=" + face_removed +
                ", fingerprint_removed=" + fingerprint_removed +
                ", iris_removed=" + iris_removed +
                '}';
    }
}
