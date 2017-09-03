package com.hymap.geotools.util;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;


import com.hymap.rest.util.HYConfig;
/**
 * 
* @ClassName: GeoDataStoreUtil 
* @Description: TODO(获取DataStore数据源) 
* @author ruanchengping 
* @date 2016-9-2 上午10:14:58 
*
 */

public class GeoDataStoreUtil {
	
	private static DataStore dataStore = null;
	
	/**
	 * @descrip 单例模式请求数据源，缓存到系统内存，加快访问速度
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static DataStore getDataStore() throws Exception{
		
		if(dataStore == null){
			String getCapabilities =  new HYConfig().getProperties("WFS-URL-GETCAPABILITY");
			Map connectionParameters = new HashMap();
			connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
			dataStore = DataStoreFinder.getDataStore(connectionParameters);
		}
		return GeoDataStoreUtil.dataStore;
	}
	
	/**
	 * @descrip 当对GeoServer进行修改后，调用此方法进行同步
	 *          需要重新获取数据源 
	 * @example GeoDataStoreUtil.resetDataStore()
	 *          例如：增加图层，删除图层，修改图层属性等
	 */
	public static void resetDataStore(){
		GeoDataStoreUtil.dataStore = null;
		
	}

}
