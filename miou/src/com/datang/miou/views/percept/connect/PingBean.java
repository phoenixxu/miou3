package com.datang.miou.views.percept.connect;

/**
 * Created by leo on 5/17/15.
 */
public class PingBean {
    String url = "";
    String name = "";
    String time = "-";
    String sucess = "-";
    int progress = 0;
    String result = "很快";

    public PingBean(String url) {
        this.url = url;
    }

    public String getResult() {
        if (time.equals("-") || time.length() == 0) return "--";
       String t =  time.substring(0,time.length()-2);
        double d = Double.parseDouble(t.trim());
        if (d > 500) {
            return "缓慢";
        } else {
            return "很快";
        }
    }
}
