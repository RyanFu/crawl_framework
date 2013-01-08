package com.lenovo.framework.KnowledgeBase;

import java.util.List;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;
import com.lenovo.framework.IModule;
import com.lenovo.framework.KnowledgeBase.common.HeadTag;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-7 下午1:50:01
 * @version : V1.0
 */
public class YoukuTop implements IModule {

    private IBridge bridge = null;

    public final static String url = "http://index.youku.com/vr_top/cate_search.html";
    public final static String module = "YoukuTop";

    @Override
    public void RegisterSelf(IBridge bridge) {
        this.bridge = bridge;
        bridge.RegisterModule(module, this);
        bridge.LogInfo("RegisterSelf YoukuTop");
    }

    @Override
    public void HandleTask(IMessage msg) {
        String className = msg.GetHeader("TaskType");
        bridge.LogInfo("recv msg TaskType [ " + className + "]");
        if (null == className || "".equals(className)) {
            return;
        }

        if (className.equals("ListRoot")) {
            IMessage outMsg = MsgFactory.getDefaultDownMsg(bridge, module, url,
                    "com.lenovo.framework.KnowledgeBase.YoukuTopList");
            bridge.LogInfo("send AMQ IMessage->url\t" + url);
            outMsg.SetHeader("Version", msg.GetHeader("Version"));
            bridge.SendMsg(outMsg);
        } else {
            List<IMessage> outMsgs = null;
            try {
                Class<?> _class = Class.forName(className.trim());
                Object object = _class.newInstance();
                if (!(object instanceof IHandle)) {
                	bridge.LogInfo("Calss instance error :" + className);
                    throw new Exception("class:<" + className
                            + "> don't Handle!");
                    // return;
                }
                IHandle parser = (IHandle) object;
                outMsgs = parser.handle(msg, this.bridge);

                if (null == outMsgs)
                    return;
                for (IMessage outMsg : outMsgs) {
//                	bridge.LogInfo("YoukuTop send AMQ IMessage\t" + outMsg);
                    bridge.LogInfo("YoukuTop send AMQ HeadTag.DESTINATION\t"
                            + outMsg.GetHeader(HeadTag.DESTINATION));
                    bridge.LogInfo("YoukuTop send AMQ HeadTag.MSGTYPE\t"
                            + outMsg.GetHeader(HeadTag.MSGTYPE));
                    outMsg.SetHeader("Version", msg.GetHeader("Version"));
                    bridge.SendMsg(outMsg);
                }
            } catch (Exception e) {
            	bridge.LogInfo("handle YoukuTop error..." + e);
                e.printStackTrace();
            }
        }

    }
}
