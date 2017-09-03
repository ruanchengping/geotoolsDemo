package com.hymap.rest.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hymap.geotools.util.GeometryCreateUtil;
import com.hymap.rest.pojo.HyMapResult;
import com.hymap.rest.pojo.HyQuery;
import com.hymap.rest.service.impl.HyWFSServerImpl;
import com.hymap.rest.util.JsonArrayUtils;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Servlet implementation class WFSServiceServlet
 */
public class WFSServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HyWFSServerImpl hyWFSServer = new HyWFSServerImpl(); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WFSServiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
	    PrintWriter mtPrint = response.getWriter();
		try {
			String method = request.getParameter("M");
			//属性查询
			if (method.equalsIgnoreCase("search")) {
				String layername = new String(request.getParameter("layername").getBytes("ISO-8859-1"),"UTF-8");
				String condition = new String(request.getParameter("condition").getBytes("ISO-8859-1"),"UTF-8");
				HyMapResult result = hyWFSServer.QueryByAttributes(new HyQuery(layername, condition,"search"));
				mtPrint.print(JsonArrayUtils.ArrayToJson(result));
			//空间查询	
			}else if(method.equalsIgnoreCase("spatial")){
				String layername = new String(request.getParameter("layername").getBytes("ISO-8859-1"),"UTF-8");
				String geometry = new String(request.getParameter("geometry").getBytes("ISO-8859-1"),"UTF-8");
				String condition = new String(request.getParameter("condition").getBytes("ISO-8859-1"),"UTF-8");
				
				geometry = GeometryCreateUtil.GeometryFormat(geometry);
				Polygon createPolygon = GeometryCreateUtil.createPolygon(geometry);
				HyMapResult result = hyWFSServer.QueryBySpace(new HyQuery(layername, condition,null,createPolygon));
				System.out.println(JsonArrayUtils.ArrayToJson(result));
				mtPrint.print(JsonArrayUtils.ArrayToJson(result));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			mtPrint.print(HyMapResult.build(500, "服务器内部异常"));
		}
	}

}
