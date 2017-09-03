package com.hymap.rest.pojo;

import com.vividsolutions.jts.geom.Geometry;
/**
 * 
* @ClassName: HyQuery 
* @Description: TODO(包装查询条件) 
* @author ruanchengping 
* @date 2016-9-2 上午10:18:23 
*
 */
public class HyQuery {
	
	//查询图层
	String layerName = null;
	//属性查询条件
	String queryCondition = null;
	//查询类型
	String queryType = null;
	//空间查询条件
	Geometry geometry = null;
	
	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
	public String getQueryCondition() {
		return queryCondition;
	}
	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}
	

	public HyQuery(String layerName, String queryCondition, String queryType,
			Geometry geometry) {
		super();
		this.layerName = layerName;
		this.queryCondition = queryCondition;
		this.queryType = queryType;
		this.geometry = geometry;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public HyQuery(String layerName, String queryCondition, String queryType) {
		super();
		this.layerName = layerName;
		this.queryCondition = queryCondition;
		this.queryType = queryType;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}
