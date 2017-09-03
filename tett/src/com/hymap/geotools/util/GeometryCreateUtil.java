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
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.geometry.primitive.Point;
import com.hymap.rest.util.HYConfig;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
* @ClassName: GeometryCreateUtil 
* @Description: TODO(传建地理几何对象工具) 
* @author ruanchengping 
* @date 2016-9-2 上午10:15:55 
*
 */
public class GeometryCreateUtil {

	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

	public Point createPoint() {
		Coordinate coord = new Coordinate(109.013388, 32.715519);
		Point point = (Point) geometryFactory.createPoint(coord);
		return point;
	}

	public Point createPointByWKT() throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		Point point = (Point) reader.read("POINT (109.013388 32.715519)");
		return point;
	}

	public MultiPoint createMulPointByWKT() throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		MultiPoint mpoint = (MultiPoint) reader
				.read("MULTIPOINT(109.013388 32.715519,119.32488 31.435678)");
		return mpoint;
	}

	public LineString createLine() {
		Coordinate[] coords = new Coordinate[] { new Coordinate(2, 2),
				new Coordinate(2, 2) };
		LineString line = geometryFactory.createLineString(coords);
		return line;
	}

	public LineString createLineByWKT() throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		LineString line = (LineString) reader.read("LINESTRING(0 0, 2 0)");
		return line;
	}

	public MultiLineString createMLine() {
		Coordinate[] coords1 = new Coordinate[] { new Coordinate(2, 2),
				new Coordinate(2, 2) };
		LineString line1 = geometryFactory.createLineString(coords1);
		Coordinate[] coords2 = new Coordinate[] { new Coordinate(2, 2),
				new Coordinate(2, 2) };
		LineString line2 = geometryFactory.createLineString(coords2);
		LineString[] lineStrings = new LineString[2];
		lineStrings[0] = line1;
		lineStrings[1] = line2;
		MultiLineString ms = geometryFactory.createMultiLineString(lineStrings);
		return ms;
	}

	public MultiLineString createMLineByWKT() throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		MultiLineString line = (MultiLineString) reader
				.read("MULTILINESTRING((0 0, 2 0),(1 1,2 2))");
		return line;
	}

	public static Polygon createPolygonByWKT() throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		Polygon polygon = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
		return polygon;
	}
	
	public static Polygon createPolygon(String str) throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		Polygon polygon = (Polygon) reader.read("POLYGON(("+str+"))");
		return polygon;
	}


	public MultiPolygon createMulPolygonByWKT(String Str) throws Exception {
		WKTReader reader = new WKTReader(geometryFactory);
		MultiPolygon mpolygon = (MultiPolygon) reader
				.read("MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10),(30 10, 30 0, 40 10, 30 20, 30 10)))");
		return mpolygon;
	}

	public GeometryCollection createGeoCollect() throws Exception {
		LineString line = createLine();
		Polygon poly = createPolygonByWKT();
		Geometry g1 = geometryFactory.createGeometry(line);
		Geometry g2 = geometryFactory.createGeometry(poly);
		Geometry[] garray = new Geometry[] { g1, g2 };
		GeometryCollection gc = geometryFactory
				.createGeometryCollection(garray);
		return gc;
	}

	public static Polygon createCircle(double x, double y, final double RADIUS) {
		final int SIDES = 32;// 圆上面的点个数
		Coordinate coords[] = new Coordinate[SIDES + 1];
		for (int i = 0; i < SIDES; i++) {
			double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
			double dx = Math.cos(angle) * RADIUS;
			double dy = Math.sin(angle) * RADIUS;
			coords[i] = new Coordinate((double) x + dx, (double) y + dy);
		}
		coords[SIDES] = coords[0];
		LinearRing ring = geometryFactory.createLinearRing(coords);
		Polygon polygon = geometryFactory.createPolygon(ring, null);
		return polygon;
	}
	
	/**
	 * @descrip 对坐标进行处理，以达到符合创建地理几何对象的要求
	 * @param geometry
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String GeometryFormat(String geometry){
		StringBuilder points = new StringBuilder();
		String[] split = geometry.split(",");
		for (String string : split) {
			String[] split2 = string.split(" ");
			points.append(split2[1]+" "+split2[0]+",");
		}
		return points.toString().substring(0, points.toString().length()-1);
	}

	
public static void main(String args[]) throws Exception{
		
		String str = "103.49025156 25.20475534,103.492696 25.20997014,103.4957893 25.21330138,103.50126206 25.22020182,103.5045933 25.22234333,103.50697276 25.22519869,103.50744865 25.23138529,103.51244552 25.23876161,103.50887633 25.24399643,103.51030401 25.24827946,103.51030401 25.25065892,103.50672378 25.2515635,103.50627149 25.25427722,103.50958827 25.2563879,103.51019132 25.2600062,103.51079437 25.26467985,103.51275428 25.26663976,103.51365886 25.27055959,103.5159203 25.27221798,103.51968936 25.27282103,103.52029241 25.27478094,103.52225233 25.27568552,103.52526758 25.27267027,103.52632292 25.27704238,103.52692597 25.27945459,103.52587063 25.28231908,103.52828283 25.28216831,103.53054427 25.2814145,103.53310724 25.2814145,103.5344641 25.28201755,103.53491639 25.28427899,103.5344641 25.28638967,103.53642402 25.28684196,103.53913775 25.2862389,103.54139918 25.29015873,103.5433591 25.28880187,103.54501749 25.28895263,103.54803274 25.28970645,103.54803274 25.29226941,103.55044494 25.29317399,103.55059571 25.29498314,103.55195257 25.29694305,103.55180181 25.29905373,103.55270638 25.30252127,103.5546663 25.30538576,103.55557087 25.3088533,103.55738003 25.30915483,103.5594907 25.30674262,103.56160138 25.30779796,103.56295824 25.30870254,103.56491816 25.305235,103.5672822 25.31101378,103.56732779 25.31010206,103.56976674 25.30781183,103.57407951 25.3066221,103.57705384 25.30632466,103.57987945 25.30364777,103.58389479 25.30439135,103.58567938 25.30097088,103.58820756 25.30260676,103.59237162 25.30186318,103.59564338 25.30171446,103.5986177 25.30156574,103.60144331 25.30424264,103.60441764 25.30439135,103.60620224 25.3039452,103.60664838 25.30141703,103.60649967 25.29918628,103.61081244 25.29992986,103.61289447 25.30097088,103.61869441 25.29963243,103.62040167 25.30030463,103.6233522 25.29897213,103.62582684 25.29802035,103.6268738 25.29668785,103.63153755 25.2968782,103.63382183 25.29668785,103.63743861 25.29164339,103.63886629 25.28726518,103.64210235 25.28317251,103.64524324 25.28060269,103.64590949 25.27869912,103.64695645 25.27717627,103.64838413 25.27631966,103.64924074 25.27308359,103.64905038 25.27146556,103.64857449 25.26888559,103.64724199 25.26641095,103.64438664 25.26622059,103.64153128 25.26545917,103.64419628 25.26374595,103.64495771 25.2622231,103.64552878 25.25841596,103.64343485 25.25746418,103.64286378 25.25594132,103.64362521 25.25308597,103.64286378 25.25080169,103.64248307 25.24832705,103.639247 25.24775598,103.63905664 25.24623312,103.64172164 25.24337777,103.64172164 25.2414742,103.63886629 25.23995134,103.63772414 25.23785742,103.63772414 25.23005279,103.63638448 25.22335448,103.636121 25.223377,103.635316 25.223117,103.634061 25.222212,103.630905 25.219838,103.629616 25.218978,103.628017 25.217323,103.626167 25.215718,103.625011 25.214607,103.623779 25.213432,103.623185 25.212962,103.621201 25.211426,103.618806 25.209478,103.615659 25.207637,103.613252 25.206309,103.6106 25.205048,103.608067 25.204117,103.605485 25.202969,103.604188 25.20256,103.603356 25.203387,103.59953 25.202565,103.596486 25.202075,103.594884 25.200872,103.593907 25.199788,103.593304 25.198598,103.592701 25.196672,103.591992 25.1939,103.591336 25.191973,103.589993 25.19026,103.588528 25.188551,103.587765 25.187867,103.585201 25.185497,103.58305 25.183439,103.58173 25.1825,103.579792 25.18191,103.578362 25.181595,103.577065 25.181674,103.575516 25.181867,103.573479 25.182467,103.572049 25.182755,103.570378 25.18318,103.56884 25.183544,103.5681 25.184177,103.566765 25.185779,103.565926 25.18715,103.564709 25.188358,103.561722 25.190943,103.559621 25.192878,103.557965 25.194091,103.555272 25.195495,103.553487 25.196314,103.551446 25.19652,103.550084 25.196599,103.548074 25.19632,103.547284 25.196213,103.546086 25.196015,103.543908 25.195379,103.542165 25.195015,103.541299 25.19514,103.539941 25.195392,103.538403 25.196152,103.537362 25.196623,103.536561 25.196922,103.535947 25.196934,103.53478 25.196462,103.533593 25.195689,103.53227 25.194474,103.530747 25.192582,103.529866 25.191409,103.528783 25.190009,103.528276 25.189284,103.527955 25.18861,103.527696 25.187936,103.527558 25.187428,103.527406 25.186662,103.527276 25.185823,103.527077 25.182883,103.5271 25.181189,103.526932 25.179384,103.526341 25.177511,103.525261 25.175296,103.523541 25.174095,103.521638 25.173591,103.519463 25.174019,103.517094 25.175115,103.515977 25.175778,103.515603 25.17555,103.514455 25.178284,103.511693 25.181623,103.510407 25.181955,103.509046 25.18239,103.507867 25.182713,103.50636 25.183822,103.505975 25.185229,103.506082 25.186923,103.505998 25.188273,103.505799 25.189235,103.505418 25.18985,103.504231 25.190741,103.502801 25.191458,103.501622 25.191903,103.500226 25.197392,103.499974 25.197392,103.497971 25.197347,103.497734 25.197344,103.494248 25.200124,103.491906 25.202904,103.49025156 25.20475534";
		GeometryCreateUtil.createPolygon(str);
		GeometryCreateUtil.createCircle(127.22, 25.22, 100);
		
		
	}

}
