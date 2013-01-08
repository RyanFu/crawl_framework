package com.lenovo.framework.KnowledgeBase;

import java.util.List;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;

public interface IHandle {

    public List<IMessage> handle(IMessage in, IBridge bridge);

}
