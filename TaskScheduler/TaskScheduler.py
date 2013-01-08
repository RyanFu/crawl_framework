#/usr/bin/python
#coding: utf8

import stomp
import time
import json
import logging
import sys
import uuid
import logging.config
import json


class TaskScheduler(stomp.ConnectionListener):
    def __init__(self):
        self.dest = None
        self.conn = None
        self.CountPerTask = 1000
        self.cid = uuid.uuid4()
        self.log = None

    def SetLogger(self, log):
        self.log = log

    def on_error(self, header, body):
        self.log.error('received an error:\n [%s]' % body)

    def on_message(self, header, body):
        self.log.info('received message:\n [%s]'% body)
        if not header.has_key('MsgType'):
            self.log.error("unknown message format, 'MsgType' is expected")
        else:
            msgType = header['MsgType']
            if msgType == '10001':
                self.HandleSpiderRequest(header, body)
            elif msgType == '20001':
                #self.HandleDataProcessRequest(header, body)
                self.log.error('not implement')
            else:
                self.log.error('unknown msgType: %s' % msgType)
        self.conn.ack(headers = {'subscription':self.cid, 'message-id':header['message-id']})

    def HandleSpiderRequest(self, header, body):
        if header.has_key('Module') and header.has_key('TaskType'):
            sendHeader = header
            sendHeader['MsgType'] = '10004'
            sendHeader['destination'] = '/queue/spider_%s' % header['Module']
            sendHeader['Module'] = header['Module']
            sendHeader['TaskType'] = header['TaskType']
            if header.has_key('Version'):
                sendHeader['Version'] = header['Version']
            else:
                sendHeader['Version'] = str(int(time.time()))
                time.sleep(1);
            self.SendMsg(sendHeader, body)
        else:
            self.log.error("field 'Module' and field 'TaskType' are expected")

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
        #activate msg handling
        self.conn.subscribe(headers = {'id':self.cid, 'destination':self.dest, 'ack':'client-individual', 'activemq.prefetchSize':'1'})

        #start work loop
        while True:
            time.sleep(1)
            if not self.conn.is_connected():
                self.log.error("MQ not connected, reconnecting...")
                self.conn.connect()


if __name__ == "__main__":
    logging.config.fileConfig('./log.conf')
    log = logging.getLogger('TaskScheduler')

    ts = TaskScheduler()
    ts.SetLogger(log)
    ts.SetDest('/queue/amqtest')
    ts.ConnectMQ('10.100.1.47', 61613)
    ts.Run()
