package com.lenovo.framework;

public interface IModule {
    public void RegisterSelf(IBridge bridge);

    public void HandleTask(IMessage msg);
}
