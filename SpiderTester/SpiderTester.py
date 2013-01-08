#/usr/bin/python
#coding: utf8

import stomp
import time
import logging
import logging.config
import sys
import warnings
import uuid
import json
import getopt
import urllib2

#set default encoding to utf8
reload(sys)
sys.setdefaultencoding('utf8')

#ignore deprecation warning from stomp
warnings.filterwarnings("ignore", category=DeprecationWarning)

class Tester():
    def __init__(self, proxyAddr = None):
        import KnowledgeBase as KB
        self.moduleNames = KB.LoadModules('./KnowledgeBase', self.SendMsg)
        self.kb = KB
        self.log = logging.getLogger()

        self.my_urllib = urllib2
        if proxyAddr is not None:
            proxy_handler = urllib2.ProxyHandler( {'http' : proxyAddr} )
            opener = urllib2.build_opener(proxy_handler, urllib2.HTTPHandler)
            self.my_urllib.install_opener(opener)

    def SendMsg(self, header, body):
        self.log.debug('send message, the header is:')
        self.log.debug(header)

    def RecvMsg(self, header, body):
        self.log.debug('recv message, the header is:')
        self.log.debug(header)

        if not header.has_key('MsgType'):
            self.log.error("unknown message format, need 'MsgType'!")
        else:
            msgType = header['MsgType']
            if msgType == '10004' or msgType == '10006':
                try:
                    self.kb.HandleTask(header, body)
                except:
                    self.log.exception('exception')
            else:
                self.log.error('unknown MsgType: %s' % msgType)

    def Download(self, url):
        html = None

        #set retry times
        retry = 5

        #set timeout seconds
        timeout = 20

        self.log.info("downloading %s" % (url))

        while retry > 0:
            try:
                response = self.my_urllib.urlopen(url = url, timeout = timeout)
                html = response.read()
                html = html.decode('utf8','ignore').encode('utf8')
                break
            except urllib2.HTTPError, e:
                self.log.error('urllib2.HTTPError:' + str(e))
            except urllib2.URLError, e:
                self.log.error('urllib2.URLError:' + str(e))
            except Exception, e:
                self.log.exception('exception')
            retry -= 1

        return html


    def DoTest(self, url, moduleName, taskType, context):
        html = self.Download(url)
        if html is not None:
            header = context
            header['MsgType'] = '10006'
            header['Module'] = moduleName
            header['TaskType'] = taskType
            self.RecvMsg(header, html)


if __name__ == "__main__":
    logging.config.fileConfig('./log.conf')
    logging.raiseExceptions = True

    optlist, args =  getopt.getopt(sys.argv[1:], 'u:p:m:t:c:h')
    url = None
    proxyAddr = None
    taskType = None
    moduleName = None
    context = {}

    for opt, param in optlist:
        if opt == '-h':
            print ' -u http://www.abc.com/efg.html  -p http://proxy:8080  -m ModuleName  -t TaskType -c key1:value1,key2:value2...'
            sys.exit()
        elif opt == '-u':
            print 'url: %s' % param
            url = param
        elif opt == '-p':
            print 'proxy: %s' % param
            proxyAddr = param
        elif opt == '-t':
            print 'task type: %s' % param
            taskType = param
        elif opt == '-m':
            print 'module name: %s' % param
            moduleName = param
        elif opt == '-c':
            print 'context:'
            for kv in param.split(','):
                k, v = kv.split(':')
                print '%s = %s' % (k, v)
                context[k] = v

    if moduleName and taskType and url:
        t = Tester(proxyAddr)
        t.DoTest(url, moduleName, taskType, context)
    else:
        print 'need args, use -h show useage.'

