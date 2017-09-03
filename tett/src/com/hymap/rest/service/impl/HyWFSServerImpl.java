package com.hymap.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import com.hymap.rest.dao.WFSDao;
import com.hymap.rest.dao.impl.WFSDaoImpl;
import com.hymap.rest.pojo.HyMapResult;
import com.hymap.rest.pojo.HyQuery;
import com.hymap.rest.service.HyWFSServer;
/**
 * 
* @ClassName: HyWFSServerImpl 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author ruanchengping 
* @date 2016-9-2 上午10:19:03 
*
 */
public class HyWFSServerImpl implements HyWFSServer {

	private WFSDao wfsDao = new WFSDaoImpl();

	/**
	 * GIS空间分析之空间查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HyMapResult QueryBySpace(HyQuery query) throws Exception {
		
		query = this.queryStrChange(query);
		
		List result = new ArrayList();
		FeatureIterator<SimpleFeature> searchSpatial = wfsDao.searchSpatial(query).features();
		while(searchSpatial.hasNext()){
			SimpleFeature feature = searchSpatial.next();
			List<Object> attributes = feature.getAttributes();
			result.add(attributes.toString());
		}
		return HyMapResult.ok(result);
	}
	/**
	 * GIS空间分析之属性查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HyMapResult QueryByAttributes(HyQuery query) throws Exception {

		query = this.queryStrChange(query);
		
		SimpleFeatureCollection search = wfsDao.searchAttributes(query);

		if (search != null && search.size() > 0) {
			SimpleFeatureIterator features = search.features();
			List result = new ArrayList();
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				List<Object> attributes = feature.getAttributes();
				result.add(attributes.toString());
			}
			return HyMapResult.ok(result);
		} else {
			return HyMapResult.build(000, "请求返回数据为空");
		}

	}
	
	
	/**
	 * 查询条件字符处理方法,可以扩展
	 * @param query
	 * @return
	 */
	private HyQuery  queryStrChange(HyQuery query){
		if (query.getQueryCondition() != null && query.getQueryCondition().indexOf("like") != -1) {
			String str = query.getQueryCondition().replace("'", "'%");
			str = str.substring(0, str.length() - 2) + "%'";
			query.setQueryCondition(str);
		}

		if (query.getQueryCondition() != null && query.getQueryCondition().indexOf(":") != -1) {
			String str = query.getQueryCondition().replace(":", "=");
			query.setQueryCondition(str);
		}
		return query;
	}

}
