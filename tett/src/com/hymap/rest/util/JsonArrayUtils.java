package com.hymap.rest.util;

import net.sf.json.JSONArray;


/**
 * json工具
 */
public class JsonArrayUtils {

    public static String ArrayToJson(Object arr){
    	JSONArray resultStr = JSONArray.fromObject(arr);
		return resultStr.toString();
    }
    
}
