package com.datang.miou.views.data;

import java.util.Map;

/**
 */
public abstract  class AbstractScheme implements ITestScheme{

    protected String name="";
    protected String id="";

    @Override
    public String name() {
        return name;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public abstract  Map<String, String> values() ;

    @Override
    public abstract Map<String, String> keys();

    @Override
    public abstract void read(String path) ;

    @Override
    public abstract void write(String path);

    @Override
    public Object clone() {
        Object clone = null;

        try {
            clone = super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }
}
