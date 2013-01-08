#!/usr/bin/env python2.6
#coding: utf-8

import stomp
import time
import logging
import logging.config
import urllib2
import uuid
import multiprocessing
import ConfigParser
import os
import json
import sys
import re
import socket

EXIT_CODE_PROXY_FAIL = 10
MAX_PROXY_FAIL_COUNT = 10
MAX_DISPATCH_COUNT = 10
DEFAULT_TIME_OUT = 20
RE_CHARSET = re.compile(u'<meta .* charset=(?P<charset>[^"]*)"[^>]*>')
ERROR_RESPONSE = [u'您的域名未备案或是未添加白名单！', \
u'因防火墙升级，可能导致部分网站无法正常访问，当您看到此页面时，说明您域名白名单出现问题，请联系相关业务检查下白名单！']

#set default encoding to utf8
reload(sys)
sys.setdefaultencoding('utf8')


class Proxy(stomp.ConnectionListener):
    def __init__(self, tid, proxyAddr):
        self.log = logging.getLogger('ProxyPool')
        self.ppid = os.getppid()
        self.dest = None
        self.conn = None
        self.tid = tid
        self.cid = uuid.uuid4()
        self.proxyAddr = proxyAddr
        self.stampList = {'douban':0.0, 'youku':0.0, 'default':0.0}
        self.intervalList = {'douban':2.0, 'youku':2.0, 'default':2.0}
        self.failcount = 0

        #set socket timeout
        socket.setdefaulttimeout(DEFAULT_TIME_OUT)
        self.my_urllib = urllib2
        proxy_handler = urllib2.ProxyHandler( {'http' : proxyAddr} )
        opener = urllib2.build_opener(proxy_handler, urllib2.HTTPHandler)
        self.my_urllib.install_opener(opener)

    def on_error(self, headers, message):
        self.log.error('received an error:')
        self.log.error('header:\n[%s]' % headers)
        self.log.error('body:\n[%s]' % message)

    def on_message(self, header, body):
        self.log.debug(header)
        try:
            self.HandleMessage(header, body)
        except Exception, e:
            self.log.exception('!!!exception!!!\n    header: %s\n    body: %s' % (str(header), body))
            self.failcount += 1

    def HandleMessage(self, _header, _body):
        self.log.debug(_header)

        #parse json body
        msg = json.loads(_body)
        header = msg['Header']
        body = msg['Body']

        if not header.has_key('MsgType'):
            self.log.error("unknown message format, 'MsgType' is expected")
        elif not header.has_key('Module'):
            self.log.error("unknown message format, 'Module' is expected")
            self.log.error(header)
        else:
            msgType = header['MsgType']
            downloadReturn = False
            if msgType == '10005':
                downloadReturn = self.Download(header, body)
            elif msgType == '10007':
                downloadReturn = self.DownloadFile(header, body)
            else:
                self.log.error('unknown msgType: %s' % msgType)
            
            #if download fail, redispatch to another proxy
            if downloadReturn == False:
                self.ReDispatch(header, body)
        
        #send ack to jms anyway
        self.conn.ack(headers = {'subscription':self.cid, 'message-id':_header['message-id']})

    def SetLogger(self, logger):
        self.log = logger

    def Wait(self, url):
        key = 'default'
        if 'douban.com/' in url:
            key = 'douban'
        elif 'youku.com/' in url:
            key = 'youku'

        now = time.time()
        secPassed = now - self.stampList[key]
        if secPassed < self.intervalList[key]:
            time.sleep(self.intervalList[key] - secPassed)
        self.stampList[key] = time.time()

    def GetResponse(self, lib, url, retry, timeout):
        self.log.debug("downloading %s" % (url))
        response = None
        while retry > 0:
            try:
                self.Wait(url)
                response = lib.urlopen(url = url, timeout = timeout)
                break
            except urllib2.HTTPError, e:
                self.log.error('[%s] urllib2.HTTPError: %s' % (url, str(e)))
                if e.code in (404,500,502,504):
                    retry -= 1
                    continue
            except urllib2.URLError, e:
                self.log.error('[%s] urllib2.URLError: %s' % (url, str(e)))
            except Exception, e:
                self.log.exception('[%s] exception:' % url)
            retry -= 1
            self.failcount += 1
        return response

    def ReDispatch(self, header, body):
        failList = header.get('FailList', '')
        failList += '%s | ' % self.proxyAddr
        header['FailList'] = failList

        dispatchCount = header.get('DispatchCount', '0')
        count = int(dispatchCount)
        if count < MAX_DISPATCH_COUNT:
            dispatchCount = str(count + 1)
            header['DispatchCount'] = dispatchCount
            self.log.error('[%s]: Download [%s] fail, dispatch to another proxy(dispatch count %s)' \
                % (self.proxyAddr, header.get('Url'), dispatchCount))
            self.SendMsg(header, body)
        else:
            self.log.error('msg has dispatched %d times, drop it.' % count)
            self.log.error('    url: [%s]' % header['Url'])
            self.log.error('    fail proxy list: [%s]' % header['FailList'])
        
    def SendResult(self, header, body):
        header['MsgType'] = '10006'
        header['TaskType'] = header['NextTaskType']
        del(header['NextTaskType'])
        header['destination'] = '/queue/spider_%s' % header['Module']
        header['ProxyAddr'] = self.proxyAddr
        self.SendMsg(header, body)

    def Download(self, header, body):
        url = header.get('Url')
        if url is None or url == 'None' or url == '':
            self.log.error('can not read Url')
            return True
        else:
            #set retry times
            retry = 3
            if header.has_key('Retry') and header['Retry'].isdigit():
                retry = int(header['Retry'])
            
            #set timeout seconds
            timeout = DEFAULT_TIME_OUT
            if header.has_key('Timeout') and header['Timeout'].isdigit():
                timeout = int(header['Timeout'])

            #do request
            html = ''
            response = self.GetResponse(self.my_urllib, url, retry, timeout)
            if response is None:
                return False
            else:
                self.failcount = 0
                html = response.read()
            
            if html in ERROR_RESPONSE:
                return False

            #coding convert
            charset = 'utf8'
            if header.has_key('Charset'):
                charset = header['Charset']
            else:
                m = RE_CHARSET.search(html)
                if m: 
                    charset = m.group('charset')
            self.log.debug('charset: %s', charset)
            html = html.decode(charset,'ignore').encode('utf8')

            #send result
            self.SendResult(header, html)

        #should never reach here
        return True

    def DownloadFile(self, header, body):
        url = header.get('Url')
        if url is None or url == 'None' or url == '':
            self.log.error('can not read Url')
            return True
        else:
            #set savePath
            savePath = None
            if header.has_key('SavePath'):
                savePath = header['SavePath']
            else:
                self.log.error('can not read SavePath')
                return True

            #set retry times
            retry = 3
            if header.has_key('Retry') and header['Retry'].isdigit():
                retry = int(header['Retry'])
            
            #set timeout seconds
            timeout = DEFAULT_TIME_OUT
            if header.has_key('Timeout') and header['Timeout'].isdigit():
                timeout = int(header['Timeout'])

            #do request
            response = self.GetResponse(self.my_urllib, url, retry, timeout)
            if response is None:
                return False
            else:
                self.failcount = 0
                f = open(savePath, 'wb')
                f.write(response.read())
                f.close()
            
            #send result
            self.SendResult(header, '')

        #should never reach here
        return True


    def SendMsg(self, header, body):
        msg = {'Header':header, 'Body':body}
        payload = json.dumps(msg)
        self.conn.send(message = payload, headers = {'destination':header['destination']})


    def SetDest(self, dest):
        self.dest = dest

    def ConnectMQ(self, ip, port):
        self.conn = stomp.Connection([(ip, port)], version = 1.1)
        self.conn.set_listener('', self)
        self.conn.start()
        self.conn.connect()

    def Run(self):
        import os

        #activate msg handling
        self.conn.subscribe(headers = {'id':self.cid, 'destination':self.dest, 'ack':'client-individual', 'activemq.prefetchSize':'1'})

        #start work loop
        while True:
            time.sleep(1)

            #check message queue
            if not self.conn.is_connected():
                self.log.error("MQ not connected, reconnecting...")
                self.conn.connect()

            #check fail count
            if self.failcount > MAX_PROXY_FAIL_COUNT:
                self.log.error('%s has failed %d times' % (self.proxyAddr, self.failcount))
                self.conn.stop()
                return

            #check parent process
            try:
                os.kill(self.ppid, 0)
            except OSError:
                return


def Process(id, proxyAddr, logger):
    import sys
    p = Proxy(id, proxyAddr)
    #p.SetLogger(logger)
    p.SetDest('/queue/proxy_pool')
    p.ConnectMQ('10.100.1.47', 61613)
    p.Run()
    sys.exit(EXIT_CODE_PROXY_FAIL)

class ProxyPool():
    def __init__(self):
        self.log = logging.getLogger('ProxyPool')
        self.proxyList = []
        self.processList = []
        self.proxyCount = 0

    def LoadProxy(self, path):
        f = open(path, 'r')
        for line in f.readlines():
            self.proxyList.append(line.rstrip('\n\r'))
        f.close()

    def RemoveProxy(self, removelist):
        failproxy = open('./failproxy', 'a')
        for t in removelist:
            failproxy.write(self.proxyList[t[0]] + '\n')
            self.processList.remove(t)
        removelist = []
        failproxy.close()

    def SetLogger(self, logger):
        self.log = logger

    def Run(self):
        self.proxyCount = len(self.proxyList)
        for i in range(self.proxyCount):
            p = multiprocessing.Process(target = Process, args = (i, self.proxyList[i], self.log))
            p.daemon = True
            p.start()
            self.processList.append((i,p))

        while True:
            removelist = []
            for t in self.processList:
                index = t[0]
                process = t[1]
                if not process.is_alive():
                    self.log.error("proxy %d stopped with exitcode %d" % (index, process.exitcode))
                    process.join()
                    if process.exitcode == EXIT_CODE_PROXY_FAIL:
                        self.log.error("proxy %d [%s] is removed" % (index, self.proxyList[index]))
                        removelist.append(t)
                    else:
                        self.log.error("proxy %d restarting..." % index)
                        p = multiprocessing.Process(target = Process, args = (index, self.proxyList[index]))
                        p.start()
                        self.processList.remove(t)
                        self.processList.append((index, p))
            if len(removelist) > 0:
                self.RemoveProxy(removelist)
            time.sleep(1)



if __name__ == "__main__":
    logging.config.fileConfig('./log.conf')
    log = logging.getLogger('ProxyPool')

    pool = ProxyPool()
    #pool.SetLogger(log)
    pool.LoadProxy('./proxylist')
    pool.Run()
