package com.datang.miou.views.percept.connect;

/**
 * Created by leo on 5/17/15.
 */
public class PingBean {
    String url="";
    String name = "";
    String time = "0";
    String sucess = "100";
    int progress = 10;
    String result = "很快";

    public PingBean(String url) {
        this.url = url;
    }
}