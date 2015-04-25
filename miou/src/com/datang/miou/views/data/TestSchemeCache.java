package com.datang.miou.views.data;

import java.util.HashMap;
import java.util.Map;

/**
 * TestSchme 内部缓存
 */
public class TestSchemeCache {

    private static Map<String, ITestScheme> schemeMap = new HashMap<String, ITestScheme>();

    public static ITestScheme getScheme(String shapeId) {
        ITestScheme cachedScheme = schemeMap.get(shapeId);
        if (cachedScheme == null) return null;
        return (ITestScheme) cachedScheme.clone();
    }

    //读模版
    public static void read(String path) {
        PingScheme pingScheme = new PingScheme();
        pingScheme.read(path);
        schemeMap.put(pingScheme.id(), pingScheme);
        StreamScheme streamScheme = new StreamScheme();
        streamScheme.read(path);
        schemeMap.put(streamScheme.id(), streamScheme);

    }

    //写模版
    public static void write(String path) {
        for (String key : schemeMap.keySet()) {
            schemeMap.get(key).write(path);
        }


    }
}
