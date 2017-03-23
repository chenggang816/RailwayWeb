<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.test.railway.RailWayTest"%>
<%@page import="com.test.railway.WriteData" %>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>12306 测试项目</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!-- 可选的Bootstrap主题文件（一般不使用） -->
<link href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
 
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	</head>
  <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap-theme.min.css"></script>
 
  <body>
  <%
  	String submitRaw = request.getParameter("submit");
  	if (submitRaw != null) {
  		String submit = new String(submitRaw.getBytes("ISO-8859-1"),"UTF-8");  	
  		if (submit.equals("开始测试")) {
  			String reqNumStr = request.getParameter("requestNum");
  			if (reqNumStr != null && reqNumStr != "") {
  				int reqNum = 0;
  				try {
  					reqNum = Integer.valueOf(reqNumStr);
  				} catch (Exception e) {
  					pageContext.setAttribute("result", "输入不合法！");
  				}
  				if (reqNum > 0) {
  					RailWayTest rc = new RailWayTest();
  					String result = rc.TestRequst(reqNum);
  					pageContext.setAttribute("result", result);
  				}
  			}
  		} else if (submit.equals("重置数据")) {
  			WriteData.Write();
  			pageContext.setAttribute("result","数据已重置！");
  		}
  	}
  %>
    <center><h1>12306 购票测试项目</h1></center>
    <div style="padding:10px 100px">
    <form action="#" method="get">
    	随机购票请求数量:<input type="text" name="requestNum" class="form-control"/>
    	<input type="submit" value="开始测试" name="submit" class="btn btn-primary" style="margin:5px 0"/>
    	<input type="submit" value="重置数据" name="submit" class="btn btn-warning"/>
    </form>
    	<div>
    	<p> ${pageScope.result } </p>
    	</div>
    </div>
  </body>
</html>
