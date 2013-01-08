#/usr/bin/python
#coding: UTF-8

import logging
import uuid
import re
import sys
from BeautifulSoup import BeautifulSoup

DATETIME_FORM1 = re.compile(u'^[0-9]+[^-]*$')
DATETIME_FORM2 = re.compile(u'^[0-9]+-[0-9]+[^-]*$')

log = logging.getLogger('SpiderPool')

def DoubanMovie(header, body, sendMsgFunc):
    log.debug(header['TaskType'])
    sendMsgFunc(body = body, header = {'MsgType':'10005', \
        'Module':'Douban', \
        'Url':u'http://movie.douban.com/tag/', \
        'NextTaskType':'DoubanMovieTag', \
        'destination':'/queue/proxy_pool'})




def DoubanMovieTag(header, body, sendMsgFunc):
    log.debug(header['TaskType'])
    soup = BeautifulSoup(body)
    nodelist = soup('table', attrs={"class" : "tagCol"})
    for node in nodelist:
        for item in node('td'):
            tag = item.a.text
            sendMsgFunc(body = '', header = {'MsgType':'10005', \
                'Module':'Douban', \
                'Tag':tag, \
                'Start':'0', \
                'Url':u'http://movie.douban.com/tag/%s?type=T' % tag, \
                'NextTaskType':'DoubanMovieSubject', \
                'destination':'/queue/proxy_pool'})




def DoubanMovieSubject(header, body, sendMsgFunc):
    log.debug(header['TaskType'])
    soup = BeautifulSoup(body)
    nodelist = soup('tr', attrs={"class" : "item"})
    if len(nodelist) == 0:
        return None
    else:
        #if have next list, continue request
        tag = header['Tag']
        start = str(int(header['Start']) + 20)
        header['MsgType'] = '10005'
        header['Start'] = start
        header['Url'] = u'http://movie.douban.com/tag/%s?start=%s&type=T' % (tag, start)
        header['NextTaskType'] = 'DoubanMovieSubject'
        header['destination'] = '/queue/proxy_pool'
        sendMsgFunc(body = '', header = header)

    for node in nodelist:
        temp = node('a')[0]
        header['Url'] = temp['href']
        header['DoubanMainUrl'] = temp['href']
        header['DoubanId'] = header['Url'].split('/')[4]
        header['MsgType'] = '10005'
        header['NextTaskType'] = 'DoubanMovieSubjectDetail'
        header['destination'] = '/queue/proxy_pool'
        #for k in header.keys():
        #    print '%s:%s'%(k,header[k])
        sendMsgFunc(body = '', header = header)



RE_CELEBRITY_HREF = re.compile('/celebrity/[0-9]+/')
RE_CELEBRITY_ID = re.compile('/celebrity/(?P<cid>[0-9]+)/')
def DoubanMovieSubjectDetail(header, body, sendMsgFunc):
    soup = BeautifulSoup(body)
    nodelist = soup('div', attrs={"id" : "info"})
    info = None
    infonode = None
    if len(nodelist) > 0:
        infonode = nodelist[0]
        info = infonode.text
    else:
        log.error('unknown format:')
        log.error(body)
        log.error(header.get('Url','no url'))
        log.error(header.get('ProxyAddr','no proxy'))
        return None
    nodelist = infonode('span', attrs={"class" : "pl"})
    marklist = []
    for node in nodelist:
        mark = node.text
        if not mark.endswith(':'):
            mark += ':'
        info = info.replace(mark, '\n')
        marklist.append(mark)
    itemlist = info.split('\n')
    detail = {}
    for i in range(len(marklist)):
        detail[marklist[i][:-1]] = itemlist[i+1]

    # find person url
    nodelist = infonode('a', attrs={"href" : RE_CELEBRITY_HREF})
    celebritylist = {}
    for node in nodelist:
        m = RE_CELEBRITY_ID.search(node['href'])
        if m:
            celebritylist[node.text] = '%s-%s' % (node.text, m.group('cid'))
            #send person task
            tempHeader = {'MsgType':'10005', \
                'Module':'Douban', \
                'NextTaskType':'DoubanMoviePerson', \
                'destination':'/queue/proxy_pool'}
            tempHeader['DoubanPersonId'] = m.group('cid')
            tempHeader['DoubanPersonName'] = node.text
            tempHeader['Url'] = 'http://movie.douban.com/celebrity/%s/' % m.group('cid')
            sendMsgFunc(body = '', header = tempHeader)

    # find celebrity id
    directors = detail.get(u'导演', '').split('/')
    actors = detail.get(u'主演', '').split('/')
    writers = detail.get(u'编剧', '').split('/')
    for k in celebritylist.keys():
        if k in directors:
            directors.remove(k)
            directors.append(celebritylist[k])
        if k in actors:
            actors.remove(k)
            actors.append(celebritylist[k])
        if k in writers:
            writers.remove(k)
            writers.append(celebritylist[k])
    detail[u'导演'] = '/'.join(directors)
    detail[u'主演'] = '/'.join(actors)
    detail[u'编剧'] = '/'.join(writers)

    # find pic url #####################
    detail['pic'] = soup('img', attrs={"rel" : "v:image"})[0]['src']
    
    # find title #####################
    detail['title'] = soup('title')[0].text.replace(u' (豆瓣)', '')

    # find name and id  #####################
    contentId = str(uuid.uuid3(uuid.NAMESPACE_DNS, ('Douban.%s' % header['DoubanId']).encode('utf8')))
    detail['name'] = soup('span', attrs={"property" : "v:itemreviewed"})[0].text
    header['ContentId'] = contentId

    # find grade #####################
    temp = soup('strong', attrs={"class" : "ll rating_num"})
    if len(temp) > 0:
        if len(temp[0].text) == 0:
            rating = None
        else:
            rating = int(float(temp[0].text) * 10)
            if rating > 100:rating = 100
            if rating < 0: rating = 0
    else:
        rating = None
    detail['rating'] = rating
                
    # find description #####################
    node = soup('span', attrs={"class" : "all hidden"})
    if len(node) > 0:
        detail['desc'] = node[0].text
    else:   
        node = soup('span', attrs={"property" : "v:summary"})
        if len(node) > 0:
            node = node[0]
            if node.span:
                node.span.extract()
            detail['desc'] = node.text
        else:
            detail['desc'] = None

    # find recommendation ######################
    node = soup('div', attrs={"class" : "content clearfix"})
    recommend = []
    if len(node) > 0:
        nodelist = node[0]('dl')
        for node in nodelist:
            if node.dd is not None:
                if node.dd.a is not None:
                    recommend.append(node.dd.a['href'].split('/')[4])
    detail['recommend'] = recommend


    # find episode ######################
    node = soup('div', attrs={"class" : "episode_list"})
    if len(node) > 0:
        count = len(node[0]('a', attrs={'href' : re.compile(".*subject.*episode.*")}))
        detail['type'] = u'电视剧'
        detail['count'] = str(count)

    log.info('-'*20)
    for key in detail.keys():
        log.info('    %s:\t\t%s' % (key, detail[key]))

    # find airdate ######################
    AIR_DATE = None
    if detail.has_key(u'首播日期'):
        AIR_DATE = detail.get(u'首播日期')
    elif detail.has_key(u'上映日期'):
        AIR_DATE = detail.get(u'上映日期')
    if AIR_DATE is not None:
        m = re.compile('(?P<Y>[0-9]{2,4})((-|.|/)(?P<M>[0-9]{1,2}))?((-|.|/)(?P<D>[0-9]{1,2}))?').search(AIR_DATE)
        if m:
            Y = m.group('Y')
            M = m.group('M')
            D = m.group('D')
            if Y and M and D:
                AIR_DATE = '%s-%s-%s' % (Y, M, D)
            elif Y and M and D is None:
                AIR_DATE = '%s-%s-00' % (Y, M)
            elif Y and M is None and D is None:
                AIR_DATE = '%s-00-00' % (Y)


    # find duration ######################
    DURATION = detail.get(u'单集片长')
    if DURATION is None:
        DURATION = detail.get(u'片长')

    if DURATION is not None:
        m = re.compile(u'(?P<min>[0-9]+)\s*(分钟|[mM][iI][nN]|分鐘|分)').search(DURATION)
        if m:
            DURATION = m.group('min')


    # commit to db ######################

    row = {}
    row['CONTENT_ID'] = contentId 
    row['SOURCE_ID'] = header['DoubanId']
    row['MAIN_URL'] = header['DoubanMainUrl']
    row['NAME'] = detail['name']
    row['TITLE'] = detail['title']
    row['ALIAS'] = detail.get(u'又名')
    row['TYPE'] = detail.get('type', u'电影')
    row['TAG'] = detail.get(u'类型')
    row['ACTOR'] = detail.get(u'主演')
    row['DIRECTOR'] = detail.get(u'导演')
    row['WRITER'] = detail.get(u'编剧')
    row['EPISODE_TOTAL'] = detail.get('count')
    row['DURATION'] = DURATION
    row['GRADE'] = detail.get('rating')
    row['AIR_DATE'] = AIR_DATE
    row['LANGUAGE'] = detail.get(u'语言')
    row['DESCRIPTION'] = detail.get('desc')

    Insert('RAW_CONTENT', row, SendMsgFunc)

    # send episode task ######################
    tempHeader = {'MsgType':'10005', \
        'Module':'Douban', \
        'NextTaskType':'DoubanMovieEpisode', \
        'destination':'/queue/proxy_pool', \
        'ContentId':contentId, \
        'DoubanId':header['DoubanId'], \
        'DoubanSubjectName':detail['title']}

    count = int(detail.get('count', '0'))
    for i in range(count):
        tempHeader['EpisodeNum'] = str(i + 1)
        tempHeader['Url'] = u'http://movie.douban.com/subject/%s/episode/%d/' % (header['DoubanId'],i)
        sendMsgFunc(body = '', header = tempHeader)

    # send review task ######################
    tempHeader = {'MsgType':'10005', \
        'Module':'Douban', \
        'NextTaskType':'DoubanMovieReview', \
        'destination':'/queue/proxy_pool', \
        'ContentId': contentId}
    tempHeader['Url'] = 'http://movie.douban.com/subject/%s/reviews' % header['DoubanId']
    sendMsgFunc(body = '', header = tempHeader)

    # send picture task ######################
    tempHeader = {'MsgType':'10005', \
        'Module':'Douban', \
        'NextTaskType':'DoubanMoviePic', \
        'destination':'/queue/proxy_pool', \
        'ContentId': contentId}
    tempHeader['Url'] = 'http://movie.douban.com/subject/%s/all_photos' % header['DoubanId']
    sendMsgFunc(body = '', header = tempHeader)





def DoubanMovieEpisode(header, body, sendMsgFunc):
    soup = BeautifulSoup(body)
    node = soup('span', attrs={"class" : "all"})
    num = header['EpisodeNum']
    desc = None
    if len(node) > 0:
        desc = node[0].text
    log.info('-'*20)
    log.info('EpisodeNum: %s' % num)
    log.info('Desc: %s' % desc)

    row = {}
    row['PARENT_ID'] = header['ContentId']
    row['SOURCE_ID'] = header['DoubanId'] + '_%s' % num
    row['MAIN_URL'] = header['Url']
    row['EPISODE'] = num
    row['TYPE'] = header.get('type', u'电视剧')
    row['DESCRIPTION'] = desc
    row['NAME'] = header['DoubanSubjectName']
    row['CONTENT_ID'] = str(uuid.uuid3(uuid.NAMESPACE_DNS, ('Douban.%s' % row['SOURCE_ID']).encode('utf8')))
    Insert('RAW_CONTENT', row, SendMsgFunc)







def DoubanMovieReview(header, body, SendMsgFunc):
    soup = BeautifulSoup(body)
    nodelist = soup('div', attrs={"class" : 'rr'})
    for node in nodelist:
        id = node['id'].split('-')[-1]
        tempHeader = {'MsgType':'10005', \
            'Module':'Douban', \
            'NextTaskType':'DoubanMovieReviewFull', \
            'destination':'/queue/proxy_pool', \
            'ContentId':header['ContentId']}
        tempHeader['DoubanReviewId'] = id
        tempHeader['Url'] = 'http://movie.douban.com/review/%s/' % id
        SendMsgFunc(body = '', header = tempHeader)






def DoubanMovieReviewFull(header, body, SendMsgFunc):
    soup = BeautifulSoup(body)
    nodelist = soup('span', attrs={"property" : 'v:description'})
    if len(nodelist) > 0:
        row = {}
        row['CONTENT'] = ''.join([str(x).decode('utf8', 'ignore').encode('utf8') for x in nodelist[0].contents])
        row['NAME'] = soup('span', attrs={"property" : 'v:reviewer'})[0].text
        row['TITLE'] = soup('span', attrs={"property" : 'v:summary'})[0].text
        row['PUB_TIME'] = soup('span', attrs={"property" : 'v:dtreviewed'})[0].text
        row['CONTENT_ID'] = header['ContentId']
        row['SOURCE_ID'] = header['DoubanReviewId']
        row['COMMENT_ID'] = str(uuid.uuid3(uuid.NAMESPACE_DNS, ('Douban.Review.%s' % row['SOURCE_ID']).encode('utf8')))
        Insert('CONTENT_COMMENT', row, SendMsgFunc)
        






def DoubanMoviePerson(header, body, SendMsgFunc):
    soup = BeautifulSoup(body)
    nodelist = soup('div', attrs={"class" : "info"})
    info = None
    infonode = None
    if len(nodelist) > 0:
        infonode = nodelist[0]
        info = infonode.text.replace('\n', '')
    else:
        log.error('unknown format:')
        log.error(body)
        log.error(header.get('Url','no url'))
        log.error(header.get('ProxyAddr','no proxy'))
        return None
    nodelist = infonode('span')
    marklist = []
    for node in nodelist:
        mark = node.text
        if not mark.endswith(':'):
            mark += ':'
        info = info.replace(mark, '\n')
        marklist.append(mark)
    itemlist = info.split('\n')
    detail = {}
    for i in range(len(marklist)):
        detail[marklist[i][:-1]] = itemlist[i+1].strip(' \t')

    # find description #####################
    node = soup('span', attrs={"class" : "all hidden"})
    if len(node) > 0:
        detail['desc'] = node[0].text
    else:
        node = soup('div', attrs={"id" : "intro"})
        if len(node) > 0:
            node = node[0]('div', attrs={"class" : "bd"})
            if len(node) > 0:
                detail['desc'] = node[0].text
        else:
            detail['desc'] = None
    
    # fix data #########################
    BIRTHDAY = detail.get(u'出生日期')
    if BIRTHDAY is not None:
        m = re.compile('(?P<Y>[0-9]{2,4})((-|.|/)(?P<M>[0-9]{1,2}))?((-|.|/)(?P<D>[0-9]{1,2}))?').search(BIRTHDAY)
        if m:
            Y = m.group('Y')
            M = m.group('M')
            D = m.group('D')
            if Y and M and D:
                BIRTHDAY = '%s-%s-%s' % (Y, M, D)
            elif Y and M and D is None:
                BIRTHDAY = '%s-%s-00' % (Y, M)
            elif Y and M is None and D is None:
                BIRTHDAY = '%s-00-00' % (Y)


    # commit to db #####################
    row = {}
    row['SOURCE_ID'] = header['DoubanPersonId']
    row['MAIN_URL'] = header['Url']
    row['NAME'] = header['DoubanPersonName']
    row['ENGLISH_NAME'] = detail.get(u'更多外文名')
    row['ALIAS'] = detail.get(u'更多中文名')
    row['GENDER'] = '0'
    if detail.has_key(u'性别'):
        if detail[u'性别'] == u'男':
            row['GENDER'] = '1'
        elif detail[u'性别'] == u'女':
            row['GENDER'] = '2'
    row['DESCRIPTION'] = detail.get('desc')
    row['BIRTHDAY'] = BIRTHDAY
    row['BIRTHLAND'] = detail.get(u'出生地')
    row['PROFESSION'] = detail.get(u'职业')
    row['CONSTELLATION'] = detail.get(u'星座')
    row['PERSON_ID'] = str(uuid.uuid3(uuid.NAMESPACE_DNS, ('Douban.Person.%s' % row['SOURCE_ID']).encode('utf8')))
    Insert('RAW_PERSON', row, SendMsgFunc)

    # send person picture task
    tempHeader = {'MsgType':'10005', \
        'Module':'Douban', \
        'NextTaskType':'DoubanMoviePersonPic', \
        'destination':'/queue/proxy_pool', \
        'DoubanPersonId': header['DoubanPersonId']}
    tempHeader['Url'] = 'http://movie.douban.com/celebrity/%s/photos/' % header['DoubanPersonId']
    SendMsgFunc(body = '', header = tempHeader)




def DoubanMoviePersonPic(header, body, SendMsgFunc):
    soup = BeautifulSoup(body)
    nodelist = soup('div', attrs={"class" : "cover"})
    for node in nodelist:
        href = node.a['href']
        thumbUrl = node.a.img['src']
        imgId = thumbUrl.split('/p')[-1].split('.')[0]

        # commit to db #####################
        row = {}
        row['PERSON_IMG_ID'] = str(uuid.uuid3(uuid.NAMESPACE_DNS, ('Douban.person.pic.%s' % imgId).encode('utf8')))
        row['PERSON_ID'] = header['DoubanPersonId']
        row['SOURCE_URL'] = href
        row['IMG_NAME'] = None
        row['IMG_URL'] = thumbUrl.replace('thumb', 'photo')
        row['IMG_DESC'] = None
        row['IMG_TYPE'] = '1'
        Insert('PERSON_IMG', row, SendMsgFunc)




RE_PIC_SRC = re.compile('http://img[0-9]\.douban\.com/view/photo/albumicon/public/(?P<picid>[a-z0-9]+)\.jpg')
def DoubanMoviePic(header, body, SendMsgFunc):
    soup = BeautifulSoup(body)
    nodelist = soup('div', attrs={"class" : "mod"})
    for node in nodelist:
        pictype = None
        if node.h2:
            if node.h2.text.startswith(u'海报'):
                pictype = '1'
                rawpictype = '2'
            elif node.h2.text.startswith(u'剧照'):
                pictype = '3'
                rawpictype = '4'

        if pictype:
            piclist = node('img', attrs={"src" : RE_PIC_SRC})
            for pic in piclist:
                row = {}
                picid = RE_PIC_SRC.search(pic['src']).group('picid')

                row['CONTENT_IMG_ID'] = str(uuid.uuid3(uuid.NAMESPACE_DNS, ('Douban.rawpic.%s' % picid).encode('utf8')))
                row['CONTENT_ID'] = header['ContentId']
                row['SOURCE_URL'] = header['Url']
                row['IMG_URL'] = pic['src'].replace('/albumicon/', '/raw/')
                row['IMG_NAME'] = None
                row['IMG_TYPE'] = rawpictype
                Insert('CONTENT_IMG', row, SendMsgFunc)
                



def Insert(table, dict, sendMsgFunc):
    dict['SOURCE'] = 'Douban'
    header = {}
    header['MsgType'] = '40001'
    header['DB'] = 'RAW_DB'
    header['Table'] = table
    header['Fields'] = '/'.join(dict.keys())
    header['destination'] = '/queue/db_operation'
    for k in dict.keys():
        if dict[k] is not None:
            header['FIELD_%s' % k] = dict[k]

    sendMsgFunc(body = '', header = header)





def HandleTask(header, body):
    taskType = header['TaskType']
    if taskType == 'DoubanMovie':
        DoubanMovie(header, body, SendMsgFunc)
    elif taskType == 'DoubanMovieTag':
        DoubanMovieTag(header, body, SendMsgFunc)
    elif taskType == 'DoubanMovieSubject':
        DoubanMovieSubject(header, body, SendMsgFunc)
    elif taskType == 'DoubanMovieSubjectDetail':
        DoubanMovieSubjectDetail(header, body, SendMsgFunc)
    elif taskType == 'DoubanMovieEpisode':
        DoubanMovieEpisode(header, body, SendMsgFunc)
    elif taskType == 'DoubanMovieReview':
        DoubanMovieReview(header, body, SendMsgFunc)
    elif taskType == 'DoubanMovieReviewFull':
        DoubanMovieReviewFull(header, body, SendMsgFunc)
    elif taskType == 'DoubanMoviePerson':
        DoubanMoviePerson(header, body, SendMsgFunc)
    elif taskType == 'DoubanMoviePic':
        DoubanMoviePic(header, body, SendMsgFunc)
    elif taskType == 'DoubanMoviePersonPic':
        DoubanMoviePersonPic(header, body, SendMsgFunc)






SendMsgFunc = None
def RegisterSelf(registerFunc, sendMsgFunc):
    global SendMsgFunc
    SendMsgFunc = sendMsgFunc
    registerFunc('Douban', HandleTask)
