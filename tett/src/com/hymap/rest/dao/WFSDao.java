package com.hymap.rest.dao;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Geometry;
import com.hymap.rest.pojo.HyQuery;
/**
 * 
* @ClassName: WFSDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author ruanchengping 
* @date 2016-9-2 上午10:17:42 
*
 */
public interface WFSDao {
	
	//添加要素 
	void add();
	//删除要素
	void delete();
	//修改要素 
	void update();
	//属性查询
	SimpleFeatureCollection searchAttributes(HyQuery query) throws Exception;
	//空间查询
	SimpleFeatureCollection searchSpatial(HyQuery query) throws  Exception;

}
