package com.lenovo.framework;

public interface IMessage
{
    public String GetHeader(String key);
    public void SetHeader(String key, String value);
    public String GetBody();
    public void SetBody(String body);
}
