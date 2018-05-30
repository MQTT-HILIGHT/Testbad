<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(document).ready(function() {

		$.ajax({

			url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
			type : 'get',
			
			data : {
				"run" : "ok"
			},
			
			success : function(xml) {

				
			}

		});

		$("#btnPub").click(function() {
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				
				data : {
					"run" : "unuse",
					"command" : "testfile",
					"file" : "/pubworkload"
				},
				
				success : function(xml) {

					parseJsonData(xml);
				}
			});
		});
		$("#btnSub").click(function() {
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				
				data : {
					"run" : "unuse",
					"command" : "testfile",
					"file" : "/subworkload"
				},
				
				success : function(xml) {

					parseJsonData(xml);
				}
			});
		});
		$("#btnClear").click(function() {
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				
				data : {
					"run" : "unuse",
					"command" : "unsub",
					"testclientid" : "all"
				},
				
				success : function(xml) {

					//parseJsonData(xml);
				}
			});
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
			
				data : {
					"run" : "unuse",
					"command" : "unpub",
					"testclientid" : "all"
				},
				
				success : function(xml) {

					//parseJsonData(xml);
				}
			});
		});
	});
	function parseJsonData(text) {
		var cArray = text.split("&");
		for (var i = 0; i < cArray.length; i++) {
		    var timer = setInterval(function() {   
		        
		            clearInterval(timer);  
		     
		    }, 100);  

			var jsonData = JSON.parse(cArray[i]);
			
			//alert(cArray[i] );
			//alert(JSON.stringify(jsonData));
			
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				
				data : jsonData,
				
				success : function(xml) {

					
				}
			});
		}
	}
</script>
</head>
<body>
<h2>pub test</h2>
<p>10개의 가상 키트가 브로커에 펍</p>
<button type="button" id="btnPub">pub</button>
<button type="button" id="btnSub">sub</button>
<button type="button" id="btnClear">clear</button>
</body>
</html>