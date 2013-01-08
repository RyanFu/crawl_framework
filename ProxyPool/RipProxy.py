#coding=utf8

import sys
import re

RE_PROXY = re.compile('http://([0-9]{1,3}\.){3}[0-9]{1,3}:[0-9]{1,5}')

path = sys.argv[1]
proxylist = set()

#read new proxys
f = open(path, 'r')
for l in f.readlines():
    fields = l.split(' ')
    if len(fields) > 2:
        proxy = 'http://%s:%s' % (fields[1], fields[2])
        proxy = proxy.strip('\n\r ')
        if RE_PROXY.match(proxy):
            proxylist.add(proxy)


#read exists proxys
f = open('./proxylist', 'r')
for l in f.readlines():
    proxy = l.strip('\n\r ')
    if RE_PROXY.match(proxy):
        proxylist.add(proxy)

#remove bad proxys
f = open('./failproxy', 'r')
for l in f.readlines():
    badproxy = l.strip('\n\r ')
    if badproxy in proxylist:
       proxylist.remove(badproxy)

#save proxy list
f = open('./proxylist', 'w')
for proxy in proxylist:
    print proxy
    f.write(proxy + '\n')
f.close()
