<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	System.out.println(path);
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	System.out.println(basePath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>提交订单</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript"
	src="<%=basePath%>assets/js/jquery-1.11.0.min.js"></script>
<style type="text/css">
.h1 {
	margin: 0 auto;
}

#producer {
	width: 48%;
	border: 1px solid blue;
	height: 80%;
	align: center;
	margin: 0 auto;
}

body {
	text-align: center;
}

div {
	text-align: center;
}

textarea {
	width: 80%;
	height: 100px;
	border: 1px solid gray;
}

button {
	background-color: rgb(62, 156, 66);
	border: none;
	font-weight: bold;
	color: white;
	height: 30px;
}
</style>
<script type="text/javascript">
	var queryProgress;
        function send(){
        	console.log("send");
            $.ajax({
                type: 'get',
                url:'<%=basePath%>submitOrder?orderNumber='
                +$("#orderNumber").val()+'&taskName='+$("#taskName").val(),
                dataType:'text',
                success:function(data){
                    if(data=="suc"){
                        alert("插入成功！");
                    }else{
                        alert("插入失败！");
                    }
                 /*    setInterval(queryProgess, 1000);
                    setInterval(queryDetail, 1000); */
                },
                error:function(data){
                    alert("插入错误！");
                }

            });
        }

        function queryProgess(){
        	var url = "<%=basePath%>queryProgess?query_taskName="+$("#query_taskName").val();
            $.get(url,function (data) {
                $("#progess").html(data);
            })
        }


        function queryDetail(){
        	console.log("queryDetail...");
        	var url = "<%=basePath%>queryDetail?query_taskName="+$("#query_taskName").val();
            $.get(url, function(data) {
            	clearInterval(queryProgress);
				console.log(data);
			/* $("#detail").html(data); */
			})
		}
        
        function download() {
        	var url2 = "<%=basePath%>download?query_taskName="+$("#query_taskName").val();
    		console.log(url2);
    		window.location.href=url2;
        }
        
        function queryTaskProgress(){
        	console.log("queryTaskProgress...");
        	var url = "<%=basePath%>queryTaskProgress?query_taskName="+$("#query_taskName").val();
            $.get(url, function(data) {
				console.log(data);
            	window.clearInterval(queryProgress);
            	
            	if (data){
            		var url2 = "<%=basePath%>download?query_taskName="+$("#query_taskName").val();
            		console.log(url2);
            		window.location.href=url2;
            	}
            	
			/* $("#detail").html(data); */
			})
		}

		function query() {
			queryProgress = setInterval(queryTaskProgress, 1000);
			/* queryProgess();
			queryDetail(); */
			/* setInterval(queryProgess, 1000);
			setInterval(queryDetail, 1000); */
		}
</script>
</head>

<body>
	<h1>导出文档</h1>
	<div id="producer">
		文档个数：<input type="text" id="orderNumber" value="500" /> <br>
		任务名称：<input type="text" id="taskName" value="计量设备信息" /> <br /> <br />
		<button onclick="send()">导出</button>
		<br> 任务名称：<input type="text" id="query_taskName" value="计量设备信息" />
		<br /> <br />
		<button onclick="query()">查 询</button><br/>
		<br /> <button onclick="download()">下载</button>
		<div id="progess"></div>
		<br>
		<div id="detail"></div>
	</div>

</body>
</html>
