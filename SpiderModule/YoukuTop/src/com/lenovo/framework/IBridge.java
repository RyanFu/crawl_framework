package com.lenovo.framework;

public interface IBridge {
    void RegisterModule(String moduleName, IModule module);

    IMessage CreateMsg();

    void SendMsg(IMessage msg);
    
    void LogDebug(String msg);
    
    void LogInfo(String msg);
    
    void LogWarning(String msg);
    
    void LogError(String msg);
    
    void LogCritical(String msg);
    
}
