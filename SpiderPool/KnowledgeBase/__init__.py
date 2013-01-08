#/usr/bin/python
#coding: utf8
import imp
import os
import sys
import JavaBridge
import logging

_handlerList = {}

log = logging.getLogger('SpiderPool')

def LoadModules(path, sendMsgFunc):
    for root, dirs, files in os.walk(path):
        #ignore jar in lib
        if root.endswith('lib'):
            continue
        for f in files:
            #load python modules
            if f.endswith('.py') and f not in ['__init__.py', 'JavaBridge.py']:
                name = f.split('.')[-2]
                m = imp.load_source(name, os.path.join(root, f))
                if 'RegisterSelf' in dir(m):
                    m.RegisterSelf(_RegisterTaskHandler, sendMsgFunc)
            elif f.endswith('.jar'):
                name = f.split('.')[-2]
                JavaBridge.RegisterSelf(_RegisterTaskHandler, sendMsgFunc, name)
    return _handlerList.keys()

def _RegisterTaskHandler(moduleName, handler):
    _handlerList[moduleName] = handler
    log.debug('%s module registed' % moduleName)

def HandleTask(header, body):
    if not header.has_key('Module'):
        log.error("unknown message format")
        return True
    moduleName = header['Module']
    if _handlerList.has_key(moduleName):
        handleFunc = _handlerList[moduleName]
        handleFunc(header, body)
        return True
    else:
        log.debug("not handler for %s" % moduleName)
        return False
    return True
