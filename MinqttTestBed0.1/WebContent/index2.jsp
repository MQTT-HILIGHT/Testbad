<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>



	$(document).ready(function() {

		$.ajax({

			url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
			type : 'get',
			timeout : 1000,
			data : {
				"run" : "ok"
			},
			error : function(xhr, status, e) {
				alert('Error');
			},
			success : function(xml) {

				alert(xml);
			}
		});
		$("#testsend").click(function() {

			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : {
					"run" : "unuse",
					"command" : "sub",
					"testclientid" : "all",
					"topic" : "test",
					"client_count" : "3",
					"conip" : "192.168.0.25:1883"
				},
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});

		$("#getdata").click(function(){
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				
				data : {
					"run" : "unuse",
					"command" : "getdata"
				},
				error : function(xhr, status, e) {
					
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});

		$("#executeline").click(function() {

			
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : {
					"run" : "unuse",
					"command" : $("#cline").val()
				},
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});
		$("#btnSub").click(function(){
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : {
					"run" : "unuse",
					"command" : "sub",
					"testclientid" : "all",
					"conip" : $("#subbrokerurl").val(),
					"topic" : $("#subtopic").val(),
					"client_count" : $("#client_count").val()
				},
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});
		$("#btnUnSub").click(function(){
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : {
					"run" : "unuse",
					"command" : "unsub",
					"testclientid" : "all"
				},
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});
		$("#btnUnPub").click(function(){
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : {
					"run" : "unuse",
					"command" : "unpub",
						"testclientid" : "all"
					
				},
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});
		$("#btnPub").click(function(){
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : {
					"run" : "unuse",
					"command" : "pub",
					"testclientid" : "all",
					"conip" : $("#pubbrokerurl").val(),
					"topic" : $("#pubtopic").val(),
					"pubclientthread" : $("#pubclientthread").val(),
					"pubrepeat" : $("#pubrepeat").val(),
					"pubtimeinterval" : $("#pubtimeinterval").val(),
					"message" : $("#pubmessage").val()
				},
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		});
	});
	
	

	
</script>

</head>
<body>
	<h1>Hello! This is min's TestBed</h1>

	<button type="button" id="testsend">test button</button>

	<br>
	<br>
	<br>
	<br>

	



	<div>
		<label for="count">command line</label> <input type="text" id="cline">
	</div>
	<button type="button" id="executeline">execute</button>
	
	<br><br><br><br><br><br><h2>Sub Control</h2>
	<div>
	<label for="client_count">count </label> <input type="text" id="client_count">
	<label for="topic">topic </label> <input type="text" id="subtopic">
	<label for="brokerurl">broker url </label> <input type="text" id="subbrokerurl">
	<button type="button" id="btnSub">sub</button>
	
	<br>
	<button type="button" id="btnUnSub">unsub</button>
	</div>
	
	
	<br><br><br><br><br><br><h2>pub Control</h2>
	<div>
	<label for="client_count">pubclientthread </label> <input type="text" id="pubclientthread">
	<label for="topic">topic </label> <input type="text" id="pubtopic">
	<label for="brokerurl">broker url </label> <input type="text" id="pubbrokerurl">
	<label for="pubrepeat">pubrepeat </label> <input type="text" id="pubrepeat">
	<label for="pubtimeinterval">pubtimeinterval </label> <input type="text" id="pubtimeinterval">
	<label for="pubmessage">pubmessage </label> <input type="text" id="pubmessage">
	
	<button type="button" id="btnPub">pub</button>
	
	<br>
	<button type="button" id="btnUnPub">unpub</button>
	</div>

<button type="button" id="getdata">getdata</button>



</body>
</html>