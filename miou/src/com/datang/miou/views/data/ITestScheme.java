package com.datang.miou.views.data;

import java.util.Map;

/**
 * 每个TestScheme的抽象接口
 */
public interface ITestScheme extends Cloneable {

    /**
     * 名字
     * @return
     */
    String name();

    /**
     * TestScheme ID
     * @return
     */
    String id();

    /**
     * 返回每个参数对应的值
     * @return
     */
    Map<String,String> values();

    /**
     * 返回每个参数对应的描述信息，用于界面显示
     * @return
     */
    Map<String,String> keys();

    /**
     * 加在对应路径下 当前ID的TestScheme
     * @param path
     */
    void read(String path);

    /**
     * 将当前Scheme保存在对应路径下
     * @param path
     */
    void write(String path);

    /**
     * clone 自己，不需要解析
     * @return
     */
    Object clone();

}
