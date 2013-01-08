#/usr/bin/python
#coding: utf8

import stomp
import time
import logging
import logging.config
import sys
import warnings
import json
import os

#set default encoding to utf8
reload(sys) 
sys.setdefaultencoding('utf8')

#ignore deprecation warning from stomp
warnings.filterwarnings("ignore", category=DeprecationWarning) 

 
class Spider(stomp.ConnectionListener):
    def __init__(self, tid):
        self.conn = None
        self.tid = tid
        if 'getppid' in dir(os):
            self.ppid = os.getppid()
        else:
            self.ppid = None
        self.kb = None
        self.moduleNames = []
        self.log = logging.getLogger('SpiderPool')

    def on_connected(self, header, body):
        #load modules in listener thread
        try:
            import KnowledgeBase as KB
            self.moduleNames = KB.LoadModules('./KnowledgeBase', self.SendMsg)
            self.kb = KB
        except:
            self.log.exception('exception')

    def on_error(self, header, body):
        self.log.error('received an error:\n [%s]' % body)

    def on_message(self, header, body):
        try:
            self.HandleMessage(header, body)
        except:
            self.log.exception('exception')

    def HandleMessage(self, _header, _body):
        self.log.debug('Spider %d received message [%s]'% (self.tid, str(_header)))
       
        #parse json body 
        msg = json.loads(_body)
        header = msg['Header']
        body = msg['Body']

        #handle msg
        ack = True
        if not header.has_key('MsgType'):
            self.log.error('unknown message format')
        else:
            msgType = header['MsgType']
            if msgType == '10004' or msgType == '10006':
                if not self.HandleSpiderTask(header, body):
                    ack = False
            else:
                self.log.error('unknown msgType: %s' % msgType)

        if ack:
            self.conn.ack(headers = {'subscription':header['Module'], 'message-id':_header['message-id']})
        else:
            self.conn.nack(headers = {'subscription':header['Module'], 'message-id':_header['message-id']})

    def SendMsg(self, header, body):
        self.log.debug(header)
        msg = {'Header':header, 'Body':body}
        payload = json.dumps(msg)

        self.conn.send(message = payload, headers = {'destination':header['destination']})

    def HandleSpiderTask(self, header, body):
        return self.kb.HandleTask(header, body)


    def ConnectMQ(self, ip, port):
        self.conn = stomp.Connection([(ip, port)], version = 1.1)
        self.conn.set_listener('', self)
        self.conn.start()
        self.conn.connect()


    def Run(self):
        #wait connection establishe
        while True:
            if self.conn.is_connected():
                break
            time.sleep(1)

        #activate msg handling
        for name in self.moduleNames:
            dest = '/queue/spider_%s' % name
            self.conn.subscribe(headers = {'id':name, 'destination':dest, 'ack':'client-individual', 'activemq.prefetchSize':'1'})

        #start work loop
        while True:
            time.sleep(1)

            #check mq connection
            if not self.conn.is_connected():
                self.log.error("MQ not connected, reconnecting...")
                self.conn.connect()

                #reactivate msg handling
                for name in self.moduleNames:
                    dest = '/queue/spider_%s' % name
                    self.conn.subscribe(headers = {'id':name, 'destination':dest, 'ack':'client-individual', 'activemq.prefetchSize':'1'})

            #check parent process
            if self.ppid is not None:
                try:
                    os.kill(self.ppid, 0)
                except OSError:
                    return

def Process(id):
    logging.config.fileConfig('./log.conf')
    logging.raiseExceptions = True
    p = Spider(id)
    p.ConnectMQ('10.100.1.47', 61613)
    p.Run()

class SpiderPool():
    def __init__(self):
        self.processList = []
        self.spiderCount = 10
        self.log = logging.getLogger('SpiderPool')

    def Run(self):
        import multiprocessing

        for i in range(self.spiderCount):
            p = multiprocessing.Process(target = Process, args = (i,))
            p.start()
            self.processList.append((i,p))

        while True:
            for t in self.processList:
                if not t[1].is_alive():
                    self.log.error("spider %d stopped, restart" % t[0])
                    p = multiprocessing.Process(target = Process, args = (t[0],))
                    p.start()
                    self.processList.remove(t)
                    self.processList.append((t[0], p))
            time.sleep(1)



if __name__ == "__main__":
    logging.config.fileConfig('./log.conf')
    logging.raiseExceptions = True

    pool = SpiderPool()
    pool.Run()
