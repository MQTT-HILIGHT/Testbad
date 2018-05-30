
api list

1. sub api
{"run" : "unuse",
"command" : "sub",
"testclientid" : "all",
"topic" : "test",
"client_count" : "3",
"conip" : "192.168.0.25:1883"}


2. pub api
{"run" : "unuse",
"command" : "pub",
"testclientid" : "all",
"topic" : "test",
"conip" : "192.168.0.25:1883",
"pubclientthread" : "3",
"pubrepeat" : "-1",
"pubtimeinterval" : "1000",
"message" : "message"}
//pubrepeat- pub을 반복할 횟수(-1은 무한 반복)
//pubtimeinterval- pub할 주기(ms)

3. unsub api
{"run" : "unuse",
"command" : "unsub",
"testclientid" : "all"}

4. unpub api
{"run" : "unuse",
"command" : "unpub",
"testclientid" : "all"}


5. file api
{"run" : "unuse",
"command" : "testfile",
"file" : "/pubworkload"}


//api 사용 방법
1. webContent 안에 api파일을 만든다
2. api파일은 위의 api로 리스트로 구현되며, '&'를 saperator로 한다.
3. 이름은 상관 없지만 요청을 보낼때 파일 파라미터의 이름이 같아야 함
4. api는 ajax요청으로 구동된다
5. api를 ajax로 DataController에 보낸다.
6. 예

6.1 ajax 요청 api
$.ajax({

			url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
			type : 'get',
			timeout : 1000,
			data : {
				"run" : "unuse",
				"command" : "testfile",
				"file" : "/testjson1"
			},
			error : function(xhr, status, e) {
				alert('Error');
			},
			success : function(xml) {

				parseJsonData(xml);
			}
		});
6.2 응답 처리 함수
function parseJsonData(text) {
		var cArray = text.split("&");
		for (var i = 0; i < cArray.length; i++) {
			alert(cArray[i]);

			var jsonData = JSON.parse(cArray[i]);
			//alert(data.command);
			$.ajax({

				url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
				type : 'get',
				timeout : 1000,
				data : jsonData,
				error : function(xhr, status, e) {
					alert('Error');
				},
				success : function(xml) {

					alert(xml);
				}
			});
		}
	}
	
////예제 소스는 executebyscript.jsp와 testjson1을 보면 된다.
