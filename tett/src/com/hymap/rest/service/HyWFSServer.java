package com.hymap.rest.service;

import java.io.IOException;

import com.hymap.rest.pojo.HyMapResult;
import com.hymap.rest.pojo.HyQuery;

/**
 * 
* @ClassName: HyWFSServer 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author ruanchengping 
* @date 2016-9-2 上午10:18:53 
*
 */
public interface HyWFSServer {
	
	/*
	 * WFS 属性查询，支持单个字段查询 或者组合查询
	 */
	
	HyMapResult QueryByAttributes(HyQuery query) throws  Exception;
	
	/*
	 * WFS 空间查询，支持对单个图层的（多边形、圆形、矩形）空间内位置信息查询
	 */
	
	HyMapResult QueryBySpace(HyQuery query) throws Exception;

}
