package com.hymap.rest.util;


import java.io.IOException;
import java.util.Properties;

public abstract class BaseConfig {
	protected static Properties properties =null;
	
	public BaseConfig() {
		if (properties != null)return;
	    
	    properties = new Properties();
	    try {
			properties.load(getClass().getResourceAsStream("/map.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isLoad(){
		return this.properties!=null;
	}
	public abstract String getProperties(String key);

}
