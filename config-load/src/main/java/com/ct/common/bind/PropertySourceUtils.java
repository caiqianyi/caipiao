package com.ct.common.bind;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

/**
 * Convenience class for manipulating PropertySources.
 *
 * @author Dave Syer
 * @see PropertySource
 * @see PropertySources
 */
public abstract class PropertySourceUtils {

	/**
	 * Return a Map of all values from the specified {@link PropertySources} that start
	 * with a particular key.
	 * @param propertySources the property sources to scan
	 * @param keyPrefix the key prefixes to test
	 * @return a map of all sub properties starting with the specified key prefixes.
	 * @see PropertySourceUtils#getSubProperties(PropertySources, String, String)
	 */
	public static Map<String, Object> getSubProperties(Properties properties,
			String keyPrefix) {
		return PropertySourceUtils.getSubProperties(properties, null, keyPrefix);
	}

	/**
	 * Return a Map of all values from the specified {@link PropertySources} that start
	 * with a particular key.
	 * @param propertySources the property sources to scan
	 * @param rootPrefix a root prefix to be prepended to the keyPrefix (can be
	 * {@code null})
	 * @param keyPrefix the key prefixes to test
	 * @return a map of all sub properties starting with the specified key prefixes.
	 * @see #getSubProperties(PropertySources, String, String)
	 */
	public static Map<String, Object> getSubProperties(Properties properties,
			String rootPrefix, String keyPrefix) {
		RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
		Map<String, Object> subProperties = new LinkedHashMap<String, Object>();
		Enumeration<Object> eks = properties.keys();
		while(eks.hasMoreElements()){
			String name = (String) eks.nextElement();
			String key = PropertySourceUtils.getSubKey(name, rootPrefix,
					keyPrefixes);
			if (key != null && !subProperties.containsKey(key)) {
				subProperties.put(key, properties.getProperty(name));
			}
		}
		return Collections.unmodifiableMap(subProperties);
	}
	
	/**
     * 根据value中的某一列，获取key
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getKeyByValueColumn(Properties pps,String columnValue)throws IOException {
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

	private static String getSubKey(String name, String rootPrefixes,
			RelaxedNames keyPrefix) {
		rootPrefixes = (rootPrefixes == null ? "" : rootPrefixes);
		for (String rootPrefix : new RelaxedNames(rootPrefixes)) {
			for (String candidateKeyPrefix : keyPrefix) {
				if (name.startsWith(rootPrefix + candidateKeyPrefix)) {
					return name.substring((rootPrefix + candidateKeyPrefix).length());
				}
			}
		}
		return null;
	}

}