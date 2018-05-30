
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
//pubrepeat- pub�� �ݺ��� Ƚ��(-1�� ���� �ݺ�)
//pubtimeinterval- pub�� �ֱ�(ms)

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


//api ��� ���
1. webContent �ȿ� api������ �����
2. api������ ���� api�� ����Ʈ�� �����Ǹ�, '&'�� saperator�� �Ѵ�.
3. �̸��� ��� ������ ��û�� ������ ���� �Ķ������ �̸��� ���ƾ� ��
4. api�� ajax��û���� �����ȴ�
5. api�� ajax�� DataController�� ������.
6. ��

6.1 ajax ��û api
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
6.2 ���� ó�� �Լ�
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
	
////���� �ҽ��� executebyscript.jsp�� testjson1�� ���� �ȴ�.
