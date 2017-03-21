package com.ct.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertiesUtil {

	public static String getPropertie(String param, String propFile) {
		Properties prop = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream(propFile);
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String serverStr = prop.getProperty(param);
		if (StringUtils.isEmpty(serverStr)) {
			return "";
		}
		return serverStr.trim();
	}

	// 获取游戏信息
	public static String getGameInfo(String propFile) {
		Properties prop = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream(propFile);
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return prop.toString();
	}

	/**
	 * @author ljx 获取区服信息
	 * @param propFile
	 *            文件名称
	 * @return Map<String, String>
	 */
	public static Map<String, String> getCityInfo(String propFile) {
		Properties prop = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream(propFile);
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String city_info = prop.toString();
		city_info = city_info.replace("}", "}'");
		city_info = city_info.replace("={", ":'{");
		city_info = city_info.substring(0, city_info.length() - 1);
		// 转换map
		Map<String, String> cityMap = JsonUtil.json2Map(city_info);
		return cityMap;
	}

	
	/**
	 * 资源文件中的value对应的某一列的所有的值，前提条件：该资源文件每一行的格式都一样
	 * @param filePath--文件所在路径
	 * 对应的格式如下：
	 * 10001={serverCode="slave_changsha_db",gameName="四川",isEnable="Y"}
	 * @throws IOException
	 */
	public static List<Map<String,String>> getAllValueListByValueName(String filePath) throws IOException  {
		Properties pps = new Properties();
		InputStream in =PropertiesUtil.class.getResourceAsStream(filePath);
		pps.load(in);
		in.close();
		Enumeration en = pps.propertyNames(); // 得到配置文件的名字

		List<String> list=new ArrayList<String>();
		while (en.hasMoreElements()) {
			String strKey = (String) en.nextElement();
			String strValue = pps.getProperty(strKey);
			list.add(strValue);
		}
		
		List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
		for(int i=0;i<list.size();i++){
			String str=list.get(i);
			
			Map<String,String> map=JsonUtil.json2Map(str);
			listMap.add(map);
		}
		
		return listMap;
	}
	
	/**
	 * 获取所有key值
	 * @param filePath--文件所在路径
	 * 对应的格式如下：
	 * 10001={serverCode="slave_changsha_db",gameName="四川",isEnable="Y"}
	 * @throws IOException
	 */
	public static List<String> getAllKeyListByValueName(String filePath) throws IOException  {
		Properties pps = new Properties();
		InputStream in =PropertiesUtil.class.getResourceAsStream(filePath);
		pps.load(in);
		in.close();
		Enumeration en = pps.propertyNames(); // 得到配置文件的名字

		List<String> list=new ArrayList<String>();
		while (en.hasMoreElements()) {
			String strKey = (String) en.nextElement();
			list.add(strKey);
		}
		
		
		
		return list;
	}
	
	/**
     * 根据value中的某一列，获取key
     * @param filePath
     * @return
     * @throws IOException
     */
	public static String getKeyByValueColumn(String filePath,String columnValue)throws IOException {
        Properties pps = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream(filePath);
        pps.load(in);
        in.close();
        Enumeration en = pps.propertyNames(); //得到配置文件的名字
        
        String returnKey="";
        while(en.hasMoreElements()) {
       	 String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
       	 if(strValue.indexOf(columnValue)!= -1){
       		 returnKey=strKey;
       		 break;
       	 }            
        }
        return returnKey;
        
    }

	public static void main(String[] args) throws IOException {
		List<Map<String,String>> list=PropertiesUtil.getAllValueListByValueName("/gameconfig/game_server.properties");
		for (int i=0;i<list.size();i++){
			System.out.println(list.get(i).get("gameName"));
		}

	}
}
