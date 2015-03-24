/**
 * 
 */
package com.datang.adc.testplan;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.datang.adc.util.StringUtils;

/**
 * @author dingzhongchang
 *
 */
public final class TestPlanVerParser {

  private static String ver = "";

  public static String getPlanVer(String planXml) {
    if (!StringUtils.isEmpty(ver))
      return ver;
    parserXML(planXml);
    return ver;
  }

  private static void parserXML(String planXml) {
    XmlPullParser parser = null;
    try {
      XmlPullParserFactory XmlParser = XmlPullParserFactory.newInstance();
      if (XmlParser == null)
        return;
      parser = XmlParser.newPullParser();
      if (parser == null)
        return;
      parser.setInput(new StringReader(planXml));
      int eventType = parser.getEventType();
      boolean isFound = false;
      // 处理事件，不碰到文档结束就一直处理
      while (eventType != XmlPullParser.END_DOCUMENT || isFound) {
        // 因为定义了一堆静态常量，所以这里可以用switch
        switch (eventType) {
          case XmlPullParser.START_DOCUMENT:
            break;

          case XmlPullParser.START_TAG:
            // 给当前标签起个名字
            String tagName = parser.getName();
            // 看到感兴趣的标签个计数
            if (tagName.equals("Version")) {
              ver = parser.getText();
              isFound = true;
            }
            break;
          case XmlPullParser.END_TAG:
            break;
          case XmlPullParser.END_DOCUMENT:
            break;
        }

        // 别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
        eventType = parser.next();
      }

    } catch (XmlPullParserException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
