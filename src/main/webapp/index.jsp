<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style type="text/css">
body {
	padding: 0;
	margin: 0;
	background: white;
}

#canvas {
	margin: 100px 0 0 300px;
}

#canvas>span {
	color: white;
	font-size: 14px;
}

#result {
	margin: 0px 0 0 300px;
}
</style>
<html>
<head>
<title>Digital recognition</title>
</head>
<body>
	<canvas id="canvas" width="280" height="280"></canvas>
	<button onclick="predict()">predict</button>
	<div id="result">
		result：<font size="18" id="digit"></font>
	</div>
</body>
</html>
<script src="/digitalrecognition/js/jquery-3.2.1.min.js"></script>
<script src="https://raw.githubusercontent.com/caleb531/jcanvas/v21.0.1/dist/min/jcanvas.min.js"></script>
<script type="text/javascript">
	/*获取绘制环境*/
	var canvas = $('#canvas')[0].getContext('2d');
	canvas.strokeStyle = "white";//线条的颜色
	canvas.lineWidth = 10;//线条粗细
	canvas.fillStyle = 'black'
	canvas.fillRect(0, 0, 280, 280);
	$('#canvas').on('mousedown', function() {
		/*开始绘制*/
		canvas.beginPath();
		/*设置动画绘制起点坐标*/
		canvas.moveTo(event.pageX - 300, event.pageY - 100);
		
		$('#canvas').on('mousemove', function() {
			/*设置下一个点坐标*/
			canvas.lineTo(event.pageX - 300, event.pageY - 100);
			/*画线*/
			canvas.stroke();
		});
	}).on('mouseup', function() {
		$('#canvas').off('mousemove');
	});
	function predict() {
		var img = $('#canvas')[0].toDataURL("image/png");
		$.ajax({
			url : "/digitalrecognition/digitalRecognition/predict",
			type : "post",
			data : {
				"img" : img.substring(img.indexOf(",") + 1)
			},
			success : function(response) {
				$("#digit").html(response);
			},
			error : function() {
			}
		});
	}
</script>