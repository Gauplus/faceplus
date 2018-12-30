package edu.njust.cn.faceplus;

import android.app.Application;

public class Data extends Application {
    private String tid;

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTid() {
        return tid;
    }
}
