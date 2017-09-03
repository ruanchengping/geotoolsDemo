package com.hymap.rest.util;


import org.apache.log4j.LogManager;


public class HYConfig extends BaseConfig {
	private static org.apache.log4j.Logger logger = LogManager.getLogger(HYConfig.class.getName());
	public HYConfig() {
		super();
	}

	public String getProperties(String key) {
		String value="";
		if(this.isLoad()){
			value=this.properties.getProperty(key,"");
		}
		return value;
	}
	
	public static void main(String [] arg){
		
		HYConfig config=new HYConfig();
		System.out.println("<>:"+config.getProperties("map.plotServerPath"));
	}
}
