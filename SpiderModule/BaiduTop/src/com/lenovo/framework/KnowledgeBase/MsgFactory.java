package com.lenovo.framework.KnowledgeBase;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;
import com.lenovo.framework.KnowledgeBase.common.HeadTag;

public class MsgFactory {
    /**
     * 
     * @param bridge
     * @param module
     * @param url
     * @param nextTaskType
     * @return
     */
    public static synchronized IMessage getDefaultDownMsg(IBridge bridge,
            String module, String url, String nextTaskType) {
        if (null == bridge)
            return null;
        IMessage outMsg = bridge.CreateMsg();
        outMsg.SetHeader(HeadTag.DESTINATION, "/queue/proxy_pool");
        outMsg.SetHeader(HeadTag.MODULE, module);
        outMsg.SetHeader(HeadTag.MSGTYPE, "10005");
        outMsg.SetHeader(HeadTag.URL, url);
        outMsg.SetHeader(HeadTag.NEXTTASKTYPE, nextTaskType);
        return outMsg;
    }

    /**
     * 
     * @param bridge
     * @param uuid
     * @param tableName
     * @param tableFields
     * @return
     */
    public static synchronized IMessage getDefaultDBMsg(IBridge bridge,
            String uuid, String tableName) {
        if (null == bridge)
            return null;
        IMessage outMsg = bridge.CreateMsg();
        outMsg.SetHeader(HeadTag.DESTINATION, "/queue/db_operation");
        outMsg.SetHeader(HeadTag.MSGTYPE, "40001");

        outMsg.SetHeader(HeadTag.MSGID, uuid);
        outMsg.SetHeader(HeadTag.DB, "RAW_DB");
        outMsg.SetHeader(HeadTag.TABLE, tableName);
        return outMsg;
    }

}
