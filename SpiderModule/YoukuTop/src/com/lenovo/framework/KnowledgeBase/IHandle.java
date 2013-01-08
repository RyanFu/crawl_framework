package com.lenovo.framework.KnowledgeBase;

import java.util.List;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-7 下午2:24:29
 * @version : V1.0
 */
public interface IHandle {

    public List<IMessage> handle(IMessage in, IBridge bridge);

}
