package my.test;

import java.lang.reflect.Field;
 
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
 

 
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgInfo;

public class CTest {

	/**
	 * @param args
	 */
	public Boolean UnSerializeMsg(String ss,  Object model) {
    	Boolean bRet=true;
    	try {
			Field[] field = model.getClass().getDeclaredFields(); 
			for (int j = 0; j < field.length; j++) { 
				String name = field[j].getName(); 
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				String type = field[j].getType().toString(); 
				String strValue =ss;				 
				if (type.equals("class java.lang.String")) { 
					Method m = model.getClass().getMethod("set" + name, String.class);
					 m.invoke(model, strValue);
				} else if (type.equals("int")) {
					Method m = model.getClass().getMethod("set" + name, int.class);					
					 m.invoke(model, Integer.parseInt(strValue));				 
				}else if (type.equals("long")) {
					Method m = model.getClass().getMethod("set" + name, long.class);
					m.invoke(model, Long.parseLong(strValue));					 
				}				
			}
		} catch (Exception e) {
			bRet = false;
			e.printStackTrace();
		}
    	return bRet;
    }
	
	public Boolean GenerateMsg(Object model) {
		Boolean bRet = true;
		try {
			Field[] field = model.getClass().getDeclaredFields(); 
			for (int j = 0; j < field.length; j++) { 
				String name = field[j].getName(); 
				name = name.substring(0, 1).toUpperCase() + name.substring(1); 
				String type = field[j].getType().toString();
				String strValue = "";
				if (type.equals("class java.lang.String")) {
					Method m = model.getClass().getMethod("get" + name);
					String value = (String) m.invoke(model);
					if (value != null) {
						strValue = value;
					}
				} else if (type.equals("int")) {
					Method m = model.getClass().getMethod("get" + name);
					Integer value = (Integer) m.invoke(model);
					if (value != null) {
						strValue = Integer.toString(value);
					}
				} else if (type.equals("short")) {
					Method m = model.getClass().getMethod("get" + name);
					Short value = (Short) m.invoke(model);
					if (value != null) {
						strValue = Short.toString(value);
					}
				} else if (type.equals("double")) {
					Method m = model.getClass().getMethod("get" + name);
					Double value = (Double) m.invoke(model);
					if (value != null) {
						strValue = Double.toString(value);
					}
				} else if (type.equals("boolean")) {
					Method m = model.getClass().getMethod("get" + name);
					Boolean value = (Boolean) m.invoke(model);
					if (value != null) {
						strValue = Boolean.toString(value);
					}
				} else if (type.equals("long")) {
					Method m = model.getClass().getMethod("get" + name);
					Long value = (Long) m.invoke(model);
					if (value != null) {
						strValue = Long.toString(value);
					}
				}
				System.out.println(name + ":" + type + ":" + strValue);
			}
		} catch (Exception e) {
			bRet = false;
			e.printStackTrace();
		}
		return bRet;
	}
	
     public String  GetObjectStrFieldValue(String strFieldName,  Object model) {
    	String value="";
		try {
		//	Field field = model.getClass().getField(strFieldName);
			strFieldName = strFieldName.substring(0, 1).toUpperCase()+ strFieldName.substring(1); 
			Method m = model.getClass().getMethod("get" + strFieldName);			 
			value = (String) m.invoke(model); 
		} catch (Exception e) {
			value = "";
			e.printStackTrace();
		}
    	return value;
    }
    
 	public String MergeUrl(String strBaseUrl, String strRedirectUrl) {
		URL baseUrl;
		URL destUrl;
		try {
			baseUrl = new URL(strBaseUrl);
			destUrl = new URL(baseUrl, strRedirectUrl);
		} catch (MalformedURLException e1) {
			return "";
		}
		return destUrl.toString();
	}
 	
	public static void main(String[] args) {
		String strss="美國邊境保衛戰:打擊偷渡 《普》(按#切換5.1聲道)"; 
		strss = strss.replace("《普》", "");		
		
		  CTest test1=new CTest();
		String strBaseUrl="/www.baidu.com/11/";
		String strRedirectUrl ="http://www.baidu.com/12/abcd/1.html";
		   test1.MergeUrl( strBaseUrl,  strRedirectUrl) ; 
		 
		Map<String, String> mapDBFieldToEPGFirstPage = new HashMap<String, String>();
		 mapDBFieldToEPGFirstPage.put("EPG_ID", "strUUID");
	        mapDBFieldToEPGFirstPage.put("SOURCE", "strRelUrl");
	        mapDBFieldToEPGFirstPage.put("NAME", "strProgramName");
	        mapDBFieldToEPGFirstPage.put("TYPE", "strType");
	        mapDBFieldToEPGFirstPage.put("ACTOR", "strLeadingRoles");
	        mapDBFieldToEPGFirstPage.put("DIRECTOR", "strDirectors");
	        mapDBFieldToEPGFirstPage.put("PRESENTER", "strPresenters");
	        mapDBFieldToEPGFirstPage.put("WRITER", "strWriters");
	       mapDBFieldToEPGFirstPage.put("PRODUCER", "");
	       mapDBFieldToEPGFirstPage.put("SESSION", "");
	        mapDBFieldToEPGFirstPage.put("EPISODE", "strCurrentSet");
	        mapDBFieldToEPGFirstPage.put("EPISODE_TOTAL", "strTotalSet");        
	        mapDBFieldToEPGFirstPage.put("CHANNEL", "strChannelName");
	        mapDBFieldToEPGFirstPage.put("AREA", "strAreaName");
	        mapDBFieldToEPGFirstPage.put("BEGIN_TIME", "strStartTime");
	        mapDBFieldToEPGFirstPage.put("DESCRIPTION", "strDescription");
	        
	        String strFields = "";
	    	int mapsize = mapDBFieldToEPGFirstPage.size();
			Iterator it = mapDBFieldToEPGFirstPage.entrySet().iterator();
			for (int i = 0; i < mapsize; i++){
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				String strValueTmp = (String)entry.getValue();
				if (strValueTmp.equals(""))	{
					//outMsg.SetHeader("Field_" + key,  "");		
				}else{
					//outMsg.SetHeader( "Field_" + key, strValueTmp);		
				}		
				strFields += key + "/";
			}	
			if (!strFields.equals("")){
				strFields = strFields.substring(0,strFields.length()-1);
			}
	        
		 
		  TvMaoEpgInfo car=new TvMaoEpgInfo();
		  car.setAreaName("beijing");
		  car.setLevel (123);
		  car.setChannelGroupName ( "grp_x");
		  
		  CTest test=new CTest();
		  String ss=test.GetObjectStrFieldValue("strChannelGroupName", car);
		  String ss2=test.GetObjectStrFieldValue("strAreaName", car);
		  String ss332=test.GetObjectStrFieldValue("strAreaName222", car);
			Boolean ret =	test.UnSerializeMsg("123987", car);
		  ret =	test.GenerateMsg(car);	 
		
		 int f=0;
	}

}
