#/usr/bin/python
#coding: utf8

import stomp
import time
import logging
import logging.config
import sys
import MySQLdb
import uuid
import json
import warnings

#set default encoding to utf8
reload(sys)
sys.setdefaultencoding('utf8')

logging.raiseExceptions = True
log = None

DEST='/queue/db_operation'

FIELD_MAP = {
    'RAW_EPG' : {
        #name in message	#name in table		#format string	#default value	#on duplicate key update
        'FIELD_EPG_ID':		('EPG_ID', 		'%s', 		uuid.uuid4,	False),
        'FIELD_CONTENT_ID':	('CONTENT_ID',		'%s',		None,		False),
        'FIELD_SOURCE':		('SOURCE', 		'%s', 		None,		False),
        'FIELD_NAME':		('NAME', 		'%s', 		None,		True),
        'FIELD_MAIN_URL':	('MAIN_URL', 		'%s', 		None,		True),
        'FIELD_TYPE':		('TYPE', 		'%s', 		None,		True),
        'FIELD_ACTOR':		('ACTOR', 		'%s',		None,		True),
        'FIELD_DIRECTOR':	('DIRECTOR', 		'%s', 		None,		True),
        'FIELD_PRESENTER':	('PRESENTER', 		'%s', 		None,		True),
        'FIELD_WRITER':		('WRITER', 		'%s', 		None,		True),
        'FIELD_PRODUCER':	('PRODUCER', 		'%s', 		None,		True),
        'FIELD_GUEST':		('GUEST', 		'%s', 		None,		True),
        'FIELD_SESSION':	('SESSION', 		'%s', 		None,		True),
        'FIELD_EPISODE':	('EPISODE', 		'%s', 		None,		True),
        'FIELD_EPISODE_TOTAL':	('EPISODE_TOTAL', 	'%s', 		None,		True),
        'FIELD_CHANNEL':	('CHANNEL', 		'%s', 		None,		True),
        'FIELD_AREA':		('AREA', 		'%s', 		None,		True),
        'FIELD_BEGIN_TIME':	('BEGIN_TIME', 		'%s', 		None,		True),
        'FIELD_DESCRIPTION':	('DESCRIPTION', 	'%s', 		None,		True),
        'FIELD_VERSION':	('VERSION',		'%s', 		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME', 	'now()', 	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME', 	'now()', 	None,		True),
    },

    'RAW_WEB_RANKING' : {
        'FIELD_SOURCE':		('SOURCE', 		'%s', 		None,		False),
        'FIELD_SOURCE_ID':	('SOURCE_ID',		'%s', 		None,		False),
        'FIELD_MAIN_URL':	('MAIN_URL', 		'%s', 		None,		True),
        'FIELD_NAME':		('NAME', 		'%s', 		None,		True),
        'FIELD_SESSION':	('SESSION', 		'%s', 		None,		True),
        'FIELD_EPISODE':	('EPISODE', 		'%s', 		None,		True),
        'FIELD_TYPE':		('TYPE', 		'%s', 		None,		True),
        'FIELD_ACTOR':		('ACTOR', 		'%s', 		None,		True),
        'FIELD_DIRECTOR':	('DIRECTOR', 		'%s', 		None,		True),
        'FIELD_PRESENTER':	('PRESENTER', 		'%s', 		None,		True),
        'FIELD_GUEST':		('GUEST', 		'%s', 		None,		True),
        'FIELD_DESCRIPTION':	('DESCRIPTION', 	'%s', 		None,		True),
        'FIELD_POINT':		('POINT', 		'%s', 		None,		True),
        'FIELD_VERSION':	('VERSION',		'%s', 		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME', 	'now()', 	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME', 	'now()', 	None,		True),
    },

    'RAW_CONTENT' : {
        'FIELD_CONTENT_ID':	('CONTENT_ID',		'%s',		None,		False),
        'FIELD_PARENT_ID':	('PARENT_ID',		'%s',		'',		False),
        'FIELD_SOURCE':		('SOURCE',		'%s',		None,		False),
        'FIELD_SOURCE_ID':	('SOURCE_ID',		'%s',		None,		False),
        'FIELD_MAIN_URL':	('MAIN_URL',		'%s',		None,		False),
        'FIELD_NAME':		('NAME',		'%s',		None,		True),
        'FIELD_TITLE':		('TITLE',		'%s',		None,		True),
        'FIELD_ALIAS':		('ALIAS',		'%s',		None,		True),
        'FIELD_TYPE':		('TYPE',		'%s',		None,		True),
        'FIELD_TAG':		('TAG',			'%s',		'',		True),
        'FIELD_ACTOR':		('ACTOR',		'%s',		None,		True),
        'FIELD_DIRECTOR':	('DIRECTOR',		'%s',		None,		True),
        'FIELD_PRESENTER':	('PRESENTER',		'%s',		None,		True),
        'FIELD_WRITER':		('WRITER',		'%s',		None,		True),
        'FIELD_PRODUCER':	('PRODUCER',		'%s',		None,		True),
        'FIELD_GUEST':		('GUEST',		'%s',		None,		True),
        'FIELD_SESSION':	('SESSION',		'%s',		None,		True),
        'FIELD_EPISODE':	('EPISODE',		'%s',		None,		True),
        'FIELD_EPISODE_TOTAL':	('EPISODE_TOTAL',	'%s',		None,		True),
        'FIELD_PUBLISH_NUM':	('PUBLISH_NUM',		'%s',		None,		True),
        'FIELD_DURATION':	('DURATION',		'%s',		None,		True),
        'FIELD_GRADE':		('GRADE',		'%s',		None,		True),
        'FIELD_AIR_DATE':	('AIR_DATE',		'%s',		None,		True),
        'FIELD_HEAT_DEGREE':	('HEAT_DEGREE',		'%s',		None,		True),
        'FIELD_LANGUAGE':	('LANGUAGE',		'%s',		None,		True),
        'FIELD_SOURCE_QUALITY':	('SOURCE_QUALITY',	'%s',		None,		True),
        'FIELD_DESCRIPTION':	('DESCRIPTION',		'%s',		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME',		'now()',	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME',		'now()',	None,		True),
    },

    'CONTENT_COMMENT' : {
        'FIELD_COMMENT_ID':	('COMMENT_ID',		'%s',		None,		False),
        'FIELD_UID':		('UID',			'%s',		None,		True),
        'FIELD_NAME':		('NAME',		'%s',		None,		True),
        'FIELD_CONTENT_ID':	('CONTENT_ID',		'%s',		None,		False),
        'FIELD_TITLE':		('TITLE',		'%s',		None,		True),
        'FIELD_CONTENT':	('CONTENT',		'%s',		None,		True),
        'FIELD_SOURCE':		('SOURCE',		'%s',		None,		False),
        'FIELD_SOURCE_ID':	('SOURCE_ID',		'%s',		None,		False),
        'FIELD_STATUS':		('STATUS',		'%s',		None,		True),
        'FIELD_PUB_TIME':	('PUB_TIME',		'%s',		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME',		'now()',	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME',		'now()',	None,		True),
    },

    'RAW_PERSON' : {
        'FIELD_PERSON_ID':	('PERSON_ID',		'%s',		None,		False),
        'FIELD_SOURCE':		('SOURCE',		'%s',		None,		False),
        'FIELD_SOURCE_ID':	('SOURCE_ID',		'%s',		None,		False),
        'FIELD_MAIN_URL':	('MAIN_URL',		'%s',		None,		False),
        'FIELD_NAME':		('NAME',		'%s',		None,		True),
        'FIELD_ENGLISH_NAME':	('ENGLISH_NAME',	'%s',		None,		True),
        'FIELD_ALIAS':		('ALIAS',		'%s',		None,		True),
        'FIELD_GENDER':		('GENDER',		'%s',		None,		True),
        'FIELD_DESCRIPTION':	('DESCRIPTION',		'%s',		None,		True),
        'FIELD_BIRTHDAY':	('BIRTHDAY',		'%s',		None,		True),
        'FIELD_COUNTRY':	('COUNTRY',		'%s',		None,		True),
        'FIELD_NATION':		('NATION',		'%s',		None,		True),
        'FIELD_PROFESSION':	('PROFESSION',		'%s',		None,		True),
        'FIELD_CONSTELLATION':	('CONSTELLATION',	'%s',		None,		True),
        'FIELD_BLOOD_TYPE':	('BLOOD_TYPE',		'%s',		None,		True),
        'FIELD_BIRTHLAND':	('BIRTHLAND',		'%s',		None,		True),
        'FIELD_WORKS':		('WORKS',		'%s',		None,		True),
        'FIELD_SCHOOL':		('SCHOOL',		'%s',		None,		True),
        'FIELD_GRADE':		('GRADE',		'%s',		None,		True),
        'FIELD_COMMENT':	('COMMENT',		'%s',		None,		True),
        'FIELD_MICRO_SLOG':	('MICRO_SLOG',		'%s',		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME',		'now()',	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME',		'now()',	None,		True),
    },

    'CONTENT_IMG' : {
        'FIELD_CONTENT_IMG_ID':	('CONTENT_IMG_ID',	'%s',		None,		False),
        'FIELD_CONTENT_ID':	('CONTENT_ID',		'%s',		None,		False),
        'FIELD_IMG_URL':	('IMG_URL',		'%s',		None,		False),
        'FIELD_IMG_NAME':	('IMG_NAME',		'%s',		None,		False),
        'FIELD_IMG_TYPE':	('IMG_TYPE',		'%s',		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME',		'now()',	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME',		'now()',	None,		True),
    },

    'PERSON_IMG' : {
        'FIELD_PERSON_IMG_ID':	('PERSON_IMG_ID',	'%s',		None,		False),
        'FIELD_PERSON_ID':	('PERSON_ID',		'%s',		None,		False),
        'FIELD_SOURCE_URL':	('SOURCE_URL',		'%s',		None,		True),
        'FIELD_IMG_URL':	('IMG_URL',		'%s',		None,		True),
        'FIELD_IMG_NAME':	('IMG_NAME',		'%s',		None,		False),
        'FIELD_IMG_TYPE':	('IMG_TYPE',		'%s',		None,		True),
        'FIELD_IMG_DESC':	('IMG_DESC',		'%s',		None,		True),
        'FIELD_CREATE_TIME':	('CREATE_TIME',		'now()',	None,		False),
        'FIELD_UPDATE_TIME':	('UPDATE_TIME',		'now()',	None,		True),
    },
}

class DBOperation(stomp.ConnectionListener):
    def __init__(self, ip, port = 3306, db = None, user = 'ics', passwd = 'system', charset = 'utf8'):
        self.conn = MySQLdb.connect(host = ip, port = port, user = user, passwd = passwd, db = db, charset = charset)
        self.conn.ping(True)
        self.c = self.conn.cursor()
        self.log = logging.getLogger('DBOperation')

    def on_error(self, header, body):
        self.log.error('received an error %s' % body)

    def on_message(self, header, body):
        try:
            self.HandleMessage(header, body)
        except Exception, e:
            self.log.exception('exception')

    def HandleMessage(self, _header, _body):
        #parse json body
        msg = json.loads(_body)
        header = msg['Header']
        body = msg['Body']

        self.log.debug('#'*30)
        self.log.debug(header)
        if header.has_key('MsgType') \
            and header.has_key('DB') \
            and header.has_key('Table') \
            and header.has_key('Fields'):
            if header['MsgType'] == '40001':
                self.Insert(header, body)
        else:
            self.log.error('unknown message format')


    def Insert(self, header, body):
        table = header['Table']
        if not FIELD_MAP.has_key(table):
            self.log.error('unknown table %s', table)
            return
        row = []
        update = []
        for f in header.keys():
            if f.upper() in FIELD_MAP[table]:
                header[f.upper()] = header.pop(f)

        fields = header['Fields'].split('/')
        for f in fields:
            k = 'FIELD_' + f.upper()
            if header.has_key(k) and FIELD_MAP[table].has_key(k) and header[k] is not None:
                v = header[k]
                self.log.debug('    %s:\t\t[%s]' % (k, v))
                m = FIELD_MAP[table][k]
                if m[3]:
                    update.append((m[0], '%s', v))
                row.append((m[0], '%s', v))

        for m in FIELD_MAP[table].values():
            #in FIELD_MAP but not in message
            if (m[1] != '%s' or m[2] is not None) and m[0] not in [x[0] for x in row]:
                #use default value in FIELD_MAP
                v = None
                if callable(m[2]):
                    v = str(m[2]())
                else:
                    v = m[2]
                if m[3] and m[0] not in [x[0] for x in update]:
                    update.append((m[0], m[1], v))
                row.append((m[0], m[1], v))

        sql = "INSERT INTO `%s`("%table + ', '.join([x[0] for x in row]) \
            + ") VALUES (" + ', '.join([x[1] for x in row]) + ") " \
            + "ON DUPLICATE KEY UPDATE " + ', '.join(['%s = %s' % (x[0], x[1]) for x in update])
        param = tuple([x[2] for x in row if x[2] is not None] + [x[2] for x in update if x[2] is not None])

        self.log.debug(sql)
        self.log.debug(param)
        try:
            with warnings.catch_warnings():
                warnings.simplefilter("error")
                self.c.execute(sql, param)
                self.conn.commit()
        except MySQLdb.Error, e:
            self.log.error(str(e))
        except Warning, w:
            self.log.error('warning: %s' % w)
            self.log.error(sql)
            for p in param:
                self.log.error('    [%s]' % str(p))

def Main():
    logging.config.fileConfig('./log.conf')
    log = logging.getLogger('DBOperation')

    dbop = DBOperation('10.100.1.42', 3306, 'RAW_DB')

    conn=stomp.Connection([('10.100.1.47',61613)])
    conn.set_listener('', dbop)
    conn.start()
    conn.connect()
    conn.subscribe(destination=DEST, ack='auto')
    
    while True:
        time.sleep(1)
        if not conn.is_connected():
            log.error("MQ not connected, reconnecting...")
            #conn.start()
            conn.connect()
            #conn.subscribe(destination=DEST, ack='auto')


if __name__ == "__main__":
    Main()
