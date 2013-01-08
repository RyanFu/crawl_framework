package com.lenovo.framework.KnowledgeBase.common;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-7 下午3:12:29
 * @version : V1.0
 */
public interface HeadTag {
    /**
     * 下载消息时使用属性
     */
    public static String DESTINATION = "destination";
    public static String MODULE = "Module";
    public static String MSGTYPE = "MsgType";
    public static String URL = "Url";
    public static String NEXTTASKTYPE = "NextTaskType";
    // 以下为自定义下载消息属性标签
    public static String URLNAME = "urlName";

    /**
     * 更新数据库使用属性
     */
    public static String MSGID = "MsgID";
    // public static String MSGTYPE = "MsgType";
    public static String DB = "DB";
    public static String TABLE = "Table";
    public static String FIELDS = "Fields";
    public static String FIELDPREFIX = "FIELD_";

    public static String WEBRANKINGRAW = "RAW_WEB_RANKING";
    public static String WEBRANKINGRAWFIELDS = "SOURCE/SOURCE_ID/MAIN_URL/"
            + "NAME/SESSION/EPISODE/TYPE/ACTOR/DIRECTOR/PRESENTER/GUEST/DESCRIPTION/"
            + "POINT/VERSION/CREATE_TIME/UPDATE_TIME";

}
