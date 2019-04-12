<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style type="text/css">
body {
	padding: 0;
	margin: 0;
	background: white;
}


#result {
	margin: 0px 0 0 300px;
}
</style>


<html>
<head>
<title>Food recognition</title>
</head>
<body>

	<h1>upload the image</h1>
	<input type='file' multiple /><br/>
	<img id="image" placeholder="hahaha" style="margin-top:20px;">
	<button onclick="sendImage()" style="margin-top:20px">upload and test</button>
	<div id="result">
		result：<font size="18" id="digit"></font>
	</div>
</body>
</html>
<script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<!-- <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script> -->
<script>

  document.querySelector('input').onchange = function() {
    var reader = new FileReader();

    reader.onload = function() {
       var dataURL = reader.result;
      $("#image").attr("src",dataURL);
    };

    reader.readAsDataURL(this.files[0]);
  };
  function sendImage() {
		$.ajax({
			url : "digitalRecognition/sendImage",
			type : "post",
			data : {
				"img" : $("#image").attr("src").substring($("#image").attr("src").indexOf(",") + 1)
			},
			success : function(response) {
				console.log("upload finished");
				$("#digit").html(response);
			},
			error : function() {
			}
		});
	}
</script>


