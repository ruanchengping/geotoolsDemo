package com.hymap.rest.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import com.hymap.geotools.util.GeoDataStoreUtil;
import com.hymap.geotools.util.GeoSpatialAnalysisEnum;
import com.hymap.geotools.util.GeotoolsFilterUtil;
import com.hymap.rest.dao.WFSDao;
import com.hymap.rest.pojo.HyQuery;
import com.vividsolutions.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
/**
 * 
* @ClassName: WFSDaoImpl 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author ruanchengping 
* @date 2016-9-2 上午10:17:54 
*
 */
public class WFSDaoImpl implements WFSDao {

	@Override
	public void add() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
   /**
    * 属性查询
    */
	@Override
	public SimpleFeatureCollection searchAttributes(HyQuery query)
			throws Exception {
		SimpleFeatureSource simpleFeatureSource = (SimpleFeatureSource) GeoDataStoreUtil
				.getDataStore().getFeatureSource(query.getLayerName());
		Filter filter = GeotoolsFilterUtil.whereclauseToFilter(query
				.getQueryCondition());
		SimpleFeatureCollection featureCollection = simpleFeatureSource
				.getFeatures(filter);
		return featureCollection;
	}
	/**
	 * 空间查询
	 */
	@Override
	public SimpleFeatureCollection searchSpatial(HyQuery query)
			throws Exception {
      
		if (query.getQueryCondition() == null || query.getQueryCondition().equalsIgnoreCase("")) {
			  //不带属性条件的空间查询，例如 ：查询以天安门为中心，周边1000米范围为半径的缓冲区范围内，所有酒店信息。
			return this.topuSearch(query.getGeometry(), query.getLayerName(),
					GeoSpatialAnalysisEnum.spatial.intersect);
		} else {
			  //带属性条件的空间查询,例如 ：查询以天安门为中心，周边1000米范围为半径的缓冲区范围内，属性为"和平饭店"的酒店信息。
			return this.topuSearch(query.getQueryCondition(),query.getGeometry(), query.getLayerName(),
					GeoSpatialAnalysisEnum.spatial.intersect);
		}

	}

	// 普通的拓扑查询
	public SimpleFeatureCollection topuSearch(Geometry refGeo,
			String layerName, GeoSpatialAnalysisEnum.spatial relType)
			throws Exception {
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
		SimpleFeatureSource featureSource = (SimpleFeatureSource) GeoDataStoreUtil
				.getDataStore().getFeatureSource(layerName);
		SimpleFeatureType schema = featureSource.getSchema();
		String geometryAttributeName = schema.getGeometryDescriptor()
				.getLocalName();
		Filter filter1 = GeotoolsFilterUtil.getGeoFilter(ff,
				geometryAttributeName, refGeo, relType);
		SimpleFeatureCollection result = featureSource.getFeatures(filter1);
		return result;
	}

	// 联合属性的拓扑查询
	public SimpleFeatureCollection topuSearch(String condition,
			Geometry refGeo, String layerName,
			GeoSpatialAnalysisEnum.spatial relType) throws Exception {
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
		SimpleFeatureSource featureSource = (SimpleFeatureSource) GeoDataStoreUtil
				.getDataStore().getFeatureSource(layerName);
		SimpleFeatureType schema = featureSource.getSchema();
		String geometryAttributeName = schema.getGeometryDescriptor()
				.getLocalName();
		Filter filter1 = GeotoolsFilterUtil.getGeoFilter(ff,
				geometryAttributeName, refGeo, relType);
		Filter filter2 = GeotoolsFilterUtil.whereclauseToFilter(condition);
		List<Filter> match = new ArrayList<Filter>();
		match.add(filter1);
		match.add(filter2);
		Filter filter = ff.and(match);
		SimpleFeatureCollection result = featureSource.getFeatures(filter);
		return result;
	}
}
