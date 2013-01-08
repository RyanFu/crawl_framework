#/usr/bin/python
#coding: utf8

import stomp
import time
import Queue
import json
import logging

logging.basicConfig()

class Worker(stomp.ConnectionListener):
    def __init__(self, id):
        self.dest = None
        self.conn = None
        self.id = id

    def on_error(self, header, message):
        print('received an error:\n [%s]' % message)

    def on_message(self, header, message):
        print('worker %d received message [%s]'% (self.id, header['message-id']))
        if not header.has_key('MsgType'):
            print 'unknown message format'
        else:
            msgType = header['MsgType']
            if msgType == '10004':
                self.HandleTask(header, body)
            else:
                print 'unknown msgType: %s' % msgType
        self.conn.ack(headers = {'message-id':header['message-id']})


    def HandleTask(self, header, body):
        time.sleep(1)

    def SetDest(self, dest):
        self.dest = dest

    def ConnectMQ(self, ip, port):
        self.conn = stomp.Connection([(ip, port)], version = 1.1)
        self.conn.set_listener('', self)
        self.conn.start()
        self.conn.connect()

    def Run(self):
        #activate msg handling
        self.conn.subscribe(headers = {'destination':self.dest, 'ack':'client-individual', 'activemq.prefetchSize':'1'})

        #start work loop
        while True:
            time.sleep(1)
            if not self.conn.is_connected():
                print "MQ not connected, reconnecting..."
                self.conn.connect()



class WorkerPool():
    def __init__(self):
        self.workerList = []
        self.workerCount = 10

    def Process(self, i):
        w = Worker(i)
        w.SetDest('/queue/worker_pool')
        w.ConnectMQ('10.100.1.47', 61613)
        w.Run()

    def Run(self):
        import multiprocessing
        for i in range(self.workerCount):
            p = multiprocessing.Process(target = self.Process, args = (i,))
            p.start()
            self.workerList.append((i,p))

        while True:
            for t in self.workerList:
                if not t[1].is_alive():
                    print "worker %d stopped, restart" % t[0]
                    p = multiprocessing.Process(target = self.Process, args = (t[0],))
                    p.start()
                    self.workerList.remove(t)
                    self.workerList.append((t[0], p))
            time.sleep(1)



if __name__ == "__main__":
    wp = WorkerPool()
    wp.Run()
