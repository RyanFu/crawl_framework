package com.lenovo.framework.KnowledgeBase.common;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TvMaoUrlCache {
	
	public Object urlCacheLocker = new Object();
	private Map<Long, HashMap<Long,String>> urlCache = new HashMap<Long, HashMap<Long, String>>();//201210250815 => crc64 : url_tasktype 
	public static final SimpleDateFormat keyFormat = new SimpleDateFormat("yyyyMMdd");
	//key : url_tasktype 
	public void add(String key){
	   long keyNum= TvMaoUrlCrc64.GenCrc64L(key);		    
	   long mapKey = getMapKey();
	   if (mapKey == 0)
		   return;		   
		synchronized(urlCacheLocker){
			HashMap<Long,String> mapper = (HashMap<Long,String>)urlCache.get(mapKey);
			if (mapper == null){	
				mapper = new HashMap<Long,String>();	
				urlCache.put(mapKey, mapper);
			}
			mapper.put(keyNum, key);			
			//urlCache clear
			if (urlCache.size() > 1){
				Set<Long> keySet = urlCache.keySet();
				for (Long i:keySet){
					if (i.equals(mapKey)){
						mapper.remove(i);
					}
				}
			}
		}
	}
	//key : url_tasktype 
	public Boolean containKey(String key){
		long keyNum= TvMaoUrlCrc64.GenCrc64L(key);
		long mapKey = getMapKey();
		if (mapKey == 0)
			   return false;	
		Boolean ret =false;
		synchronized(urlCacheLocker){
			HashMap<Long,String> mapper = (HashMap<Long,String>)urlCache.get(mapKey);
			if (mapper != null){					
				ret = mapper.containsKey(keyNum);
			}			
		}
		return ret;
	}
	  
	private long getMapKey(){
	   long key=0; 	
	   try{
		   key =Long.parseLong( keyFormat.format(System.currentTimeMillis()));
	   }catch(Exception e){
		   
	   }
	   return key;
	}
	
	public static void main(String[] args) {
		TvMaoUrlCache cache = new TvMaoUrlCache();
		cache.add("http://www.baidu.com_tasktype1");
		cache.add("http://www.sina.com_tasktype2");
		cache.add("http://www.google.com_tasktype3");
		
		Boolean ret = cache.containKey("http://www.baidu.com_tasktype1");
		 ret = cache.containKey("http://www.baidu.com_tasktype12");
		 ret = cache.containKey("");
		int ffff=0;
	}	  
}
