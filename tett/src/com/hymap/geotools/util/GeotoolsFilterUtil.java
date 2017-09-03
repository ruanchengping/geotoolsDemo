package com.hymap.geotools.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.AndImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.IsEqualsToImpl;
import org.geotools.filter.IsNotEqualToImpl;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.NotImpl;
import org.geotools.filter.OrImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import com.hymap.rest.util.HYConfig;
import com.vividsolutions.jts.geom.Geometry;

/**
* @ClassName: GeotoolsFilterUtil 
* @Description: TODO(要素查询过滤器工具类) 
* @author ruanchengping 
* @date 2016-9-2 上午10:16:57 
*
 */
public class GeotoolsFilterUtil {

	private static char startMark = 's'; //0x02;
	private static char endMark =  'e'; //0x03;
	
	// 空间查询操作 类型
	
	
	// 构建拓扑查询的filter
	public static Filter getGeoFilter(FilterFactory2 ff, String geometryAttributeName,
			Geometry refGeo, GeoSpatialAnalysisEnum.spatial relType) {
		switch (relType) {
		case intersect:
			return ff.intersects(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case contains:
			return ff.contains(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case within:
			return ff.within(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case cross:
			return ff.crosses(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case overlaps:
			return ff.overlaps(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case touches:
			return ff.touches(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case equals:
			return ff.equals(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		case disjoint:
			return ff.disjoint(ff.property(geometryAttributeName), ff
					.literal(refGeo));
		default:
			return null;
		}
	}

	

	public static Filter whereclauseToFilter(String where) {
		
		System.out.println(where);
	
		Filter filter = null;
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0, count = where.length(); i < count; i++) {
				char c = where.charAt(i);
				if (c < 256) {
					sb.append(c);
				} else {
					String enc = URLEncoder.encode(String.valueOf(c), "UTF-8");
					enc = enc.replaceAll("\\%", "");
					sb.append(startMark + enc + endMark);
				}
			}
			String encode = sb.toString();
			Filter f = ECQL.toFilter(encode);
			decodeFilter(f);
			filter = f;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return filter;
	}

	@SuppressWarnings("unchecked")
	private static void decodeFilter(Filter filter) throws UnsupportedEncodingException {
		if (filter instanceof OrImpl) {
			OrImpl impl = (OrImpl) filter;
			for (Iterator<Filter> itr = impl.getFilterIterator(); itr.hasNext();) {
				Filter f = itr.next();
				decodeFilter(f);
			}
		} else if (filter instanceof AndImpl) {
			AndImpl impl = (AndImpl) filter;
			for (Iterator<Filter> itr = impl.getFilterIterator(); itr.hasNext();) {
				Filter f = itr.next();
				decodeFilter(f);
			}
		} else if (filter instanceof NotImpl) {
			NotImpl impl = (NotImpl) filter;
			Filter f = impl.getFilter();
			decodeFilter(f);
		} else if (filter instanceof LikeFilterImpl) {
			LikeFilterImpl impl = (LikeFilterImpl) filter;
			String encode = impl.getLiteral();
			impl.setLiteral(decodeString(encode));
			
			decodeExpression(impl.getExpression());
		} else if (filter instanceof IsEqualsToImpl) {
			IsEqualsToImpl impl = (IsEqualsToImpl) filter;
			decodeExpression(impl.getExpression1());
			decodeExpression(impl.getExpression2());
		} else if (filter instanceof IsNotEqualToImpl) {
			IsNotEqualToImpl impl = (IsNotEqualToImpl) filter;
			decodeExpression(impl.getExpression1());
			decodeExpression(impl.getExpression2());
		}
	}

	private static void decodeExpression(Expression exp) throws UnsupportedEncodingException {
		/**
		 * 还原属性名为中文
		 */
		if (exp instanceof AttributeExpressionImpl) {
			AttributeExpressionImpl impl = (AttributeExpressionImpl) exp;
			String encode = String.valueOf(impl.getPropertyName());
			//System.out.println("encode2:"+encode+" decodeString(encode)2:"+decodeString(encode));
			impl.setPropertyName(decodeString(encode));
		}
		
		/**
		 * 还原属性值为中文
		 */
		if (exp instanceof LiteralExpressionImpl) {
			LiteralExpressionImpl impl = (LiteralExpressionImpl) exp;
			String encode = String.valueOf(impl.getValue());
			//System.out.println("encode1:"+encode+" decodeString(encode)1:"+decodeString(encode));
			impl.setValue(decodeString(encode));
		}
		
		
	}

	private static String decodeString(String encode) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		int i = 0, count = encode.length();
		while (i < count) {
			int start = encode.indexOf(startMark, i);
			if (start >= 0) {
				sb.append(encode.substring(i, start));
			} else {
				sb.append(encode.substring(i));
				return sb.toString();
			}

			int end = encode.indexOf(endMark, i);
			if (end > 0) {
				i = end + 1;

				String enc = encode.substring(start + 1, end);
				StringBuilder sbEnc = new StringBuilder();
				for (int j = 0, l = enc.length(); j < l; j += 2) {
					sbEnc.append("%" + enc.charAt(j) + enc.charAt(j + 1));
				}
				String dec = URLDecoder.decode(sbEnc.toString(), "UTF-8");
				sb.append(dec);
			} else {
				sb.append(encode.substring(i + 1));
				i = count;
			}
		}
		return sb.toString();
	}

	
	public static void main(String args[]) throws IOException{
		
		//连接geoserver
		String getCapabilities = new HYConfig().getProperties("WFS-URL-GETCAPABILITY");
		Map connectionParameters = new HashMap();
		//获取要素
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
		DataStore ds = DataStoreFinder.getDataStore(connectionParameters);
		SimpleFeatureSource ff = (SimpleFeatureSource)ds.getFeatureSource("bb:cc");
		
		 String filterCondition = "NAME like '%%'"; 
		  
		 Filter filter =  GeotoolsFilterUtil.whereclauseToFilter(filterCondition);
		 Iterator<SimpleFeature>  co = ff.getFeatures(filter).iterator();
		 while(co.hasNext()){
			 
			 SimpleFeature f =  co.next();
			 
			 System.out.println(f.getAttribute("NAME"));
		 }
		 //System.out.println(filter.toString());
	}
}
