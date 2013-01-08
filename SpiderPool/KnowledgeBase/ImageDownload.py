#/usr/bin/python
#coding: UTF-8

import logging
import uuid
import os
import re
import sys
import MySQLdb
import magic

log = logging.getLogger('SpiderPool')

SQL_LOAD_CONTENT_IMG="select IMG_URL, CONTENT_IMG_ID as ID from CONTENT_IMG where IMG_NAME is null;"

SQL_LOAD_PERSON_IMG="select IMG_URL, PERSON_IMG_ID as ID from PERSON_IMG where IMG_NAME is null;"

SQL_UPDATE_CONTENT_IMG_PATH="update CONTENT_IMG set IMG_NAME = %s where CONTENT_IMG_ID = %s;"

SQL_UPDATE_PERSON_IMG_PATH="update PERSON_IMG set IMG_NAME = %s where PERSON_IMG_ID = %s;"

SAVE_PATH_ROOT = '/fileserver/pic'

def HandleTask(header, body):
    taskType = header['TaskType']
    if taskType == 'LoadImageUrl':
        LoadImageUrl(header, body, SendMsgFunc)
    elif taskType == 'AfterImageDownload':
        AfterImageDownload(header, body, SendMsgFunc)


SendMsgFunc = None
def RegisterSelf(registerFunc, sendMsgFunc):
    global SendMsgFunc
    SendMsgFunc = sendMsgFunc
    registerFunc('ImageDownload', HandleTask)


conn = MySQLdb.connect(host = '10.100.1.42', port = 3306, user = 'ics', passwd = 'system', db = 'RAW_DB', charset = 'utf8')
conn.ping(True)



def ForEachRow(conn, sql, handler, param):
    c=conn.cursor()
    c.execute(sql)
    result = c.fetchone()
    row = {}
    while result is not None:
        for i in range(len(result)):
            row[c.description[i][0]] = result[i]
        handler(c.rownumber, c.rowcount, row, param)
        result = c.fetchone()
    c.close()


def ContentImgHandler(rownumber, rowcount, row, param):
    sendMsg = param
    header = {}
    header['MsgType'] = '10007'
    header['Module'] = 'ImageDownload'
    header['Table'] = 'CONTENT_IMG'
    header['Url'] = row['IMG_URL']
    header['IMG_ID'] = row['ID']
    header['SavePath'] = '%s/content/%s/%s.jpg' % (SAVE_PATH_ROOT, row['ID'][0].lower(), row['ID'].lower())
    header['NextTaskType'] = 'AfterImageDownload'
    header['destination'] = '/queue/proxy_pool'
    sendMsg(body = '', header = header)

def PersonImgHandler(rownumber, rowcount, row, param):
    if len(row['ID']) == 0:
        return

    sendMsg = param
    header = {}
    header['MsgType'] = '10007'
    header['Module'] = 'ImageDownload'
    header['Table'] = 'PERSON_IMG'
    header['Url'] = row['IMG_URL']
    header['IMG_ID'] = row['ID']
    header['SavePath'] = '%s/person/%s/%s.jpg' % (SAVE_PATH_ROOT, row['ID'][0].lower(), row['ID'].lower())
    header['NextTaskType'] = 'AfterImageDownload'
    header['destination'] = '/queue/proxy_pool'
    sendMsg(body = '', header = header)

def LoadImageUrl(header, body, SendMsgFunc):
    ForEachRow(conn, SQL_LOAD_CONTENT_IMG, ContentImgHandler, SendMsgFunc)
    ForEachRow(conn, SQL_LOAD_PERSON_IMG, PersonImgHandler, SendMsgFunc)

def AfterImageDownload(header, body, SendMsgFunc):
    if header.has_key('SavePath'):
        if os.path.isfile(header['SavePath']):
            mimetype = magic.from_buffer(open(header['SavePath']).read(1024), mime=True)
            if mimetype == 'image/jpeg':
                log.info('download [%s] to [%s] finished' % (header['Url'], header['SavePath']))
                UpdateImagePath(header, body, SendMsgFunc)
            else:
                log.error('file type error: [%s] is [%s], delete it' % (header['SavePath'], mimetype))
                os.remove(header['SavePath'])



def UpdateImagePath(header, body, SendMsgFunc):
    c=conn.cursor()
    imgId = header['IMG_ID']
    if header['Table'] == 'CONTENT_IMG':
        c.execute(SQL_UPDATE_CONTENT_IMG_PATH, ('content/%s/%s.jpg' % (imgId[0].lower(), imgId.lower()), imgId))
    elif header['Table'] == 'PERSON_IMG':
        c.execute(SQL_UPDATE_PERSON_IMG_PATH, ('person/%s/%s.jpg' % (imgId[0].lower(), imgId.lower()), imgId))
    c.close()
    conn.commit()
