#/usr/bin/python
#coding: utf8

import jpype
import logging

log = logging.getLogger('SpiderPool')

class Bridge:
    def __init__(self, registerFunc, sendMsgFunc):
        self.registerFunc = registerFunc
        self.module = None
        self.sendMsgFunc = sendMsgFunc
        self.tempMsg = None

    def RegisterModule(self, moduleName, module):
        self.module = module
        self.registerFunc(moduleName, self.HandleTask)
        
    def HandleTask(self, header, body):
        log.debug('HandleTask: module = %s  tasktype = %s' % (header['Module'], header['TaskType']))
        msg = Msg(header, body)
        try:    
            self.module.HandleTask(jpype.JProxy("com.lenovo.framework.IMessage", inst = msg))
        except jpype.JavaException, e:
            log.error(e.javaClass(), e.message())
            log.error(e.stacktrace())

    def CreateMsg(self):
        self.tempMsg = Msg({}, None)
        return jpype.JProxy("com.lenovo.framework.IMessage", inst = self.tempMsg)
        
    def SendMsg(self, msg):
        self.sendMsgFunc(self.tempMsg.header, self.tempMsg.body)

    def LogDebug(self, msg):
        log.debug(msg)

    def LogInfo(self, msg):
        log.info(msg)

    def LogWarning(self, msg):
        log.warning(msg)

    def LogError(self, msg):
        log.error(msg)

    def LogCritical(self, msg):
        log.critical(msg)
    

class Msg:
    def __init__(self, header, body):
        self.header = header
        self.body = body

    def GetHeader(self, key):
        if self.header.has_key(key):
            return self.header[key]
        return None

    def SetHeader(self, key, value):
        self.header[str(key)] = str(value)

    def GetBody(self):
        return self.body

    def SetBody(self, body):
        self.body = body
    
    def HeaderSize(self):
        return len(self.header)

    def GetHeaderName(self, index):
        return self.header.keys()[index]

def RegisterSelf(registerFunc, sendMsgFunc, moduleName):
    import os
    import platform

    delimiter = ':'
    if platform.system() == 'Windows':
        delimiter = ';'

    if not jpype.isJVMStarted():
        classpath = []
        for root, dirs, files in os.walk(os.curdir):
            for f in files:
                if f.endswith('.jar'):
                    classpath.append(os.sep.join((root,f)))
        param = '-Djava.class.path=' + delimiter.join(classpath)
        jpype.startJVM(jpype.getDefaultJVMPath(), param)
    bridge = Bridge(registerFunc, sendMsgFunc)
    module = None
    try:
        module = jpype.JClass('com.lenovo.framework.KnowledgeBase.%s' % moduleName)()
        if 'RegisterSelf' in dir(module):
            module.RegisterSelf(jpype.JProxy("com.lenovo.framework.IBridge", inst = bridge))
    except jpype.JavaException, e:
        log.info(str(e.javaClass()) + str(e.message()))
        log.info(str(e.stacktrace()))
    return module
