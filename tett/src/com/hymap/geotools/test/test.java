package com.hymap.geotools.test;

import net.sf.json.JSONArray;

import com.hymap.geotools.util.GeometryCreateUtil;
import com.hymap.rest.pojo.HyMapResult;
import com.hymap.rest.pojo.HyQuery;
import com.hymap.rest.service.impl.HyWFSServerImpl;
import com.vividsolutions.jts.geom.Polygon;

public class test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		Polygon circle = GeometryCreateUtil.createCircle(25.27276,103.57282,0.01);
		
		

		String str = "25.250385141278 103.66396470793,25.397327279953 103.670831163005, 25.404193735028 103.52388902433,25.257251596352997 103.517022569255,25.250385141278 103.66396470793";
		
		StringBuilder points = new StringBuilder();
		String[] split = str.split(",");
		for (String string : split) {
			String[] split2 = string.split(" ");
			points.append(split2[1]+" "+split2[0]+",");
		}
		str = points.toString().substring(0, points.toString().length()-1);
		System.out.println(str);
		Polygon createPolygon = GeometryCreateUtil.createPolygon(str);
		
		HyWFSServerImpl dd = new HyWFSServerImpl();
		HyQuery query = new HyQuery("ml:xiangzhendian",null,null,createPolygon);
		HyMapResult queryBySpace = dd.QueryBySpace(query);
		
		JSONArray resultStr = JSONArray.fromObject(queryBySpace);
        System.out.println(resultStr.toString());
	}

}
