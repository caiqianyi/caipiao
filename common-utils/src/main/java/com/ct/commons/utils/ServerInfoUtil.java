package com.ct.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServerInfoUtil {

	public static List<String> getServerCodes() {
		// 根据服务区编号获取 服务区的相关信息
		Map<String, String> servers = PropertiesUtil
				.getCityInfo("/gameconfig/city_server.properties");
		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : servers.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}
}
