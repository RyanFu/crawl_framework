package com.lenovo.framework.KnowledgeBase;

import java.util.List;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;
import com.lenovo.framework.IModule;
import com.lenovo.framework.KnowledgeBase.common.HeadTag;

public class BaiduTop implements IModule {

	private final static String url = "http://top.baidu.com/category.php?p=yule";
	public final static String module = "BaiduTop";
	public final static String PROXY_ERROR_RESPONSE1 = "您的域名未备案或是未添加白名单！";
	public final static String PROXY_ERROR_RESPONSE2 = "因防火墙升级，可能导致部分网站无法正常访问，当您看到此页面时，说明您域名白名单出现问题，请联系相关业务检查下白名单！";

	private IBridge bridge = null;

	public void RegisterSelf(IBridge bridge) {
		this.bridge = bridge;
		bridge.RegisterModule("BaiduTop", this);
	}

	public void HandleTask(IMessage msg) {
		String className = msg.GetHeader("TaskType");
		bridge.LogInfo("[java] TaskType [ " + className + "]");
		if (null == className || "".equals(className)) {
			return;
		}
		
		if(msg.GetBody().equals(PROXY_ERROR_RESPONSE1) || msg.GetBody().equals(PROXY_ERROR_RESPONSE2)) {
			bridge.LogWarning(String.format("proxy [%s] response error, redispatch: %s", msg.GetHeader("ProxyAddr"), msg.GetBody()));
			
			if (msg.GetHeader("DispatchCount") == null) {
				msg.SetHeader("DispatchCount", "1");
			} else {
				Integer dispatchCount = Integer.parseInt(msg.GetHeader("DispatchCount")) + 1;
				msg.SetHeader("DispatchCount", String.valueOf(dispatchCount));
			}
			
			IMessage outMsg = bridge.CreateMsg();
			for(int i = 0; i < msg.HeaderSize(); i++) {
				String headerName = msg.GetHeaderName(i);
				if (headerName.equals("TaskType")) {
					outMsg.SetHeader("NextTaskType", msg.GetHeader(headerName));
				} else {
					outMsg.SetHeader(headerName, msg.GetHeader(headerName));
				}
			}
			outMsg.SetHeader("destination", "/queue/proxy_pool");
			bridge.SendMsg(outMsg);
			return;
		}

		if (className.equals("TopRoot")) {
			IMessage outMsg = MsgFactory.getDefaultDownMsg(bridge, module, url,
					"com.lenovo.framework.KnowledgeBase.BaiduTopList");
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
					throw new Exception("class:<" + className + "> don't Handle!");
				}
				IHandle parser = (IHandle) object;
				outMsgs = parser.handle(msg, this.bridge);

				if (null == outMsgs) return;
				for (IMessage outMsg : outMsgs) {
					bridge.LogInfo("BaiduTop send AMQ HeadTag.DESTINATION\t"
							+ outMsg.GetHeader(HeadTag.DESTINATION));
					bridge.LogInfo("BaiduTop send AMQ HeadTag.MSGTYPE\t"
							+ outMsg.GetHeader(HeadTag.MSGTYPE));
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					bridge.SendMsg(outMsg);
				}
			} catch (Exception e) {
				bridge.LogError(String.format("handle [%s] error: %s", msg.GetHeader("Url"), e));
				bridge.LogError(String.format("body: %s", msg.GetBody()));
				e.printStackTrace();
			}
		}

	}
}
