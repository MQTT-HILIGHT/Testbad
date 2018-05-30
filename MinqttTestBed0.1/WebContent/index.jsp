<!DOCTYPE html>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>HILIGHT</title>

<!-- Bootstrap -->
<link href="vendors/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Font Awesome -->
<link href="vendors/font-awesome/css/font-awesome.min.css"
	rel="stylesheet">
<!-- NProgress -->
<link href="vendors/nprogress/nprogress.css" rel="stylesheet">
<!-- bootstrap-daterangepicker -->
<link href="vendors/bootstrap-daterangepicker/daterangepicker.css"
	rel="stylesheet">

<!-- Custom Theme Style -->
<link href="build/css/custom.min.css" rel="stylesheet">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.0.1/mqttws31.min.js"
	type="text/javascript"></script>

<!-- jQuery -->
<script src="vendors/jquery/dist/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>


<script>
	function minGetTime() {
		timestamp = new Date();
		return timestamp.getHours() + "/" + timestamp.getMinutes() + "/"
				+ timestamp.getSeconds();
	}
	var brokerIp = "";
	var timeX = [];
	var timeY = [];
	var myLineChart;
	var currentId = "";
	function loadChart() {

		//timeX.push(minGetTime());
		//var ctx = document.getElementById("myChart").getContext('2d');
		var data = {

			labels : timeX,

			datasets : [

			{

				label : "",

				fillColor : "rgba(220,220,220,0.2)",

				strokeColor : "rgba(220,220,220,1)",

				pointColor : "rgba(220,220,220,1)",

				pointStrokeColor : "#fff",

				pointHighlightFill : "#fff",

				pointHighlightStroke : "rgba(220,220,220,1)",

				data : timeY

			}

			]

		};

		var ctx = document.getElementById("myChart").getContext("2d");

		myLineChart = new Chart(ctx, {
			type : 'line',
			data : data

		});

	}

	$(document)
			.ready(
					function() {

						loadChart();
						$
								.ajax({

									url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
									type : 'get',

									data : {
										"run" : "ok"
									},

									success : function(xml) {

									}

								});
						$("#btnBrokerIp").click(function() {
							brokerIp = $("#brokerip").val();
							$("#c_brokerip").html(brokerIp);
						});

						$("#btnDisconnect")
								.click(
										function() {

											$
													.ajax({

														url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
														type : 'get',

														data : {
															"run" : "unuse",
															"command" : "disconnect",
															"testclientid" : currentId
														},

														success : function(xml) {

														}
													});
										});
						$("#btnAddPub")
								.click(
										function() {
											var count = $("#add_count").val();
											if (count < 1) {
												count = 0;
											}
											$
													.ajax({

														url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
														type : 'get',

														data : {
															"run" : "unuse",
															"command" : "pub",
															"testclientid" : "all",
															"topic" : "work/load",
															"conip" : brokerIp,
															"pubclientthread" : count,
															"pubrepeat" : "-1",
															"pubtimeinterval" : "1000",
															"message" : "test"
														},

														success : function(xml) {

														}
													});
										});
						$("#btnAddSub")
								.click(
										function() {
											var count = $("#add_count").val();
											if (count < 1) {
												count = 0;
											}
											$
													.ajax({

														url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
														type : 'get',

														data : {
															"run" : "unuse",
															"command" : "sub",
															"testclientid" : "all",
															"topic" : "work/load",
															"client_count" : count,
															"conip" : brokerIp
														},

														success : function(xml) {

														}
													});
										});
						$("#btnPub")
								.click(
										function() {
											$
													.ajax({

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
						$("#btnSub")
								.click(
										function() {
											$
													.ajax({

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
						$("#btnClear")
								.click(
										function() {
											$
													.ajax({

														url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
														type : 'get',

														data : {
															"run" : "unuse",
															"command" : "unsub",
															"testclientid" : "all"
														},

														success : function(xml) {

															$
																	.ajax({

																		url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
																		type : 'get',

																		data : {
																			"run" : "unuse",
																			"command" : "unpub",
																			"testclientid" : "all"
																		},

																		success : function(
																				xml) {

																			//parseJsonData(xml);
																		}
																	});
														}
													});

										});
					});
	function parseJsonData(text) {
		var cArray = text.split("&");
		for (var i = 0; i < cArray.length; i++) {
			var timer = setInterval(function() {

				clearInterval(timer);

			}, 1000);

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

	$(document)
			.ready(
					function() {

						$
								.ajax({

									url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
									type : 'get',

									data : {
										"run" : "ok"
									},
									error : function(xhr, status, e) {
										alert('Error');
									},
									success : function(xml) {

									}
								});

						setInterval(
								function() {
									$
											.ajax({

												url : 'http://localhost:8080/MinqttTestBed0.1/DataContorller',
												type : 'get',

												data : {
													"run" : "unuse",
													"command" : "getdata",
												},
												error : function(xhr, status, e) {
													alert('Error');
												},
												success : function(json) {
													$("#client_list").html("");
													var data = JSON.parse(json);
													$("#c_broker_ip").html(
															brokerIp);
													for ( var i in data) {

														if (timeX.length > 12
																&& timeY.length > 12) {
															timeX.shift();
															timeY.shift();
														}
														timeX
																.push(minGetTime());
														timeY
																.push(data[i].spenttime);
														myLineChart.update();
														var topic = " ";
														for ( var t in data[i].pub) {
															topic = topic
																	+ "  "
																	+ data[i].pub[t].topic;
														}

														$("#client_list")
																.html(
																		"<tr id="+data[i].id+"><th>"
																				+ data[i].id
																				+ "</th><th>"
																				+ data[i].status
																				+ "</th></tr>");
														document
																.getElementById(
																		data[i].id)
																.addEventListener(
																		'click',
																		function(
																				event) {
																			currentId = data[i].id;

																			$(
																					"#c_pub_count")
																					.html(
																							0);
																			$(
																					"#c_sub_count")
																					.html(
																							0);
																			$(
																					"#c_message")
																					.html(
																							0);

																			$(
																					"#c_id")
																					.html(
																							data[i].id);

																			$(
																					"#c_topic")
																					.html(
																							topic);
																			$(
																					"#c_work_count")
																					.html(
																							data[i].total_count);
																			$(
																					"#c_pub_count")
																					.html(
																							data[i].pub[0].count);
																			$(
																					"#c_sub_count")
																					.html(
																							data[i].sub[0].count);

																		});

														/* $("#127.0.0.1").click(function() {

															alert("click");
														}); */
													}

												}
											});

								}, 5000);

					});
</script>

</head>

<body class="nav-md">
	<div class="container body">
		<div class="main_container">

			<div class="col-md-3 left_col">

				<div class="left_col scroll-view">
					<div class="navbar nav_title" style="border: 0;">

						<a class="site_title"><i class="fa fa-paw"></i> <span>HILIGHT!</span></a>
					</div>

					<div class="clearfix"></div>

					<!-- menu profile quick info -->
					<div class="profile clearfix">
						<div class="profile_pic">
							<img src="images/HILIGHT.png" alt="..."
								class="img-circle profile_img">
						</div>
						<div class="profile_info">
							<span>Welcome,</span>
							<h2>
								HILIGHT<br>관리 페이지
							</h2>
						</div>
					</div>
					<!-- /menu profile quick info -->

					<br />

					<!-- sidebar menu -->
					<div id="sidebar-menu"
						class="main_menu_side hidden-print main_menu">
						<div class="menu_section">
							<h3>General</h3>
							<ul class="nav">
								<li><a href="hilight.html" style="color: white;"><i
										class="fa fa-desktop"></i>HILIGHT Dashboard</a></li>
							</ul>

						</div>
						<div class="menu_section">
							<ul class="nav side-menu">
								<li><a><i class="fa fa-windows"></i> Extras <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu">
										<li><a href="page_403.html">403 Error</a></li>
										<li><a href="page_404.html">404 Error</a></li>
										<li><a href="page_500.html">500 Error</a></li>
										<li><a href="plain_page.html">Plain Page</a></li>
										<li><a href="login.html">Login Page</a></li>
										<li><a href="pricing_tables.html">Pricing Tables</a></li>
									</ul></li>
							</ul>
						</div>

					</div>
					<!-- /sidebar menu -->

					<!-- /menu footer buttons -->
					<div class="sidebar-footer hidden-small">
						<a data-toggle="tooltip" data-placement="top" title="FullScreen"
							style="width: 100%" onclick="setFullScreen()"> <span
							class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
						</a>
					</div>
					<!-- /menu footer buttons -->



				</div>
			</div>

			<!-- top navigation -->
			<div class="top_nav">

				<div class="nav_menu">
					<nav>
						<div class="nav toggle">
							<a id="menu_toggle"><i class="fa fa-bars"></i></a>
						</div>

						<ul class="nav navbar-nav navbar-right">


							<li><span> <a href="#" class="btn btn-primary"
									style="margin: 13px 15px 12px 15px;" data-toggle="modal"
									data-target="#serverIPModal">Change Broker IP</a>
							</span>
								<div class="modal fade" id="serverIPModal" tabindex="-1"
									role="dialog" aria-labelledby="myServerModalLabel"
									aria-hidden="true">
									<!--modal start -->
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"
													aria-label="Close">
													<span aria-hidden="true">&times;</span>
												</button>
												<h4 class="modal-title" id="myServerModalLabel">Change
													Broker IP</h4>
											</div>
											<div class="modal-body">
												<form class="form-horizontal form-label-left input_mask">
													<div class="form-group">
														<label class="control-label col-md-3 col-sm-3 col-xs-12">Broker
															IP</label>
														<div class="col-md-9 col-sm-9 col-xs-12">
															<input type="text" class="form-control"
																placeholder="localhost" id="brokerip">
														</div>
													</div>
												</form>
											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">Close</button>
												<button type="button" class="btn btn-primary"
													id="btnBrokerIp" data-dismiss="modal">Save changes</button>
											</div>
										</div>
									</div>
									<!--modal end -->
								</div></li>


							<!-- <li class=""><a class="user-profile " aria-expanded="false">
									<label for="test_file">Broker IP </label> <input type="text"
									id="brokerip" placeholder="127.0.0.1:1883">
									<button type="button" id="btnBrokerIp">SET</button>
							</a></li> -->


							<li class=""><a class="user-profile " aria-expanded="false">
									<span id="c_brokerip"
									style="margin: 13px 0px 12px 0px; padding: 6px 12px;">
										aaaaaa </span> <img src="images/HILIGHT.png" alt="">HILIGHT
							</a></li>

						</ul>
					</nav>

				</div>

			</div>

			<!-- /top navigation -->

			<!-- page content -->
			<div class="right_col" role="main">
				<div class="">
					<div class="row" id="chart_tab_row">
						<!-- panels -->
						<div style="margin-right: 52%;">

							<div class="chart_tab_panel">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="x_panel">
										<div class="x_title">

											<h2 onclick="changleToggle(this.children[1].value)">
												HILIGHT test client<input type="hidden">
											</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content visual_content">

											<div class="col-md-12 col-sm-12 col-xs-12">


												<div class="container">
													<div style=""></div>
													<h2>test client list</h2>
													<p></p>
													<table id="test_table"
														class="table table-dark table-striped">
														<thead>
															<tr>
																<th>id</th>

																<th>state</th>
															</tr>
														</thead>
														<tbody id="client_list">
															<tr>

															</tr>

														</tbody>
													</table>
												</div>
											</div>

											<!-- min add end -->



										</div>

									</div>
									<div class="x_panel">
										<div class="x_title">

											<h2 onclick="changleToggle(this.children[1].value)">
												HILIGHT Testset <input type="hidden">
											</h2>

											<div class="clearfix"></div>
										</div>

										<div class="x_content visual_content">

											<div class="col-md-12 col-sm-12 col-xs-12">
												<span>
													<button type="button" id="btnPub">pub</button>
													<button type="button" id="btnSub">sub</button>
													<button type="button" id="btnClear">clear</button>
													&emsp;&emsp;&emsp;&emsp;
													<button type="button" id="btnAddPub">add pub</button>
													<button type="button" id="btnAddSub">add sub</button> count<input
													type="text" id="add_count">

												</span> <br>
												<div>
													<label for="test_file">Test file </label> <input
														type="text" id="test_file">
													<button type="button" id="btnCustom">execute file</button>
												</div>



											</div>




										</div>

									</div>
								</div>






							</div>

						</div>

						<div style="margin-left: 52%;">
							<div class="chart_tab_panel">
								<div class="col-md-12 col-sm-12 col-xs-12">





									<div class="x_panel">
										<div class="x_title">

											<h2 onclick="changleToggle(this.children[1].value)">
												HILIGHT Test-Client<small>Detail</small><input type="hidden">
											</h2>

											<div class="clearfix"></div>
										</div>

										<div class="x_content visual_content">

											<div class="col-md-12 col-sm-12 col-xs-12">


												<div class="container">
													<div style=""></div>
													<h2>Detail Test Client</h2>
													<p>Detail</p>
													<table class="table table-dark table-striped">

														<tbody>
															<tr>
																<td>id</td>
																<td id="c_id">Doe</td>
															</tr>
															<tr>
																<td>work count</td>
																<td id="c_work_count">Doe</td>
															</tr>
															<tr>
																<td>topic</td>
																<td id="c_topic">Doe</td>
															</tr>
															<tr>
																<td>pub count</td>
																<td id="c_pub_count">Doe</td>
															</tr>
															<tr>
																<td>sub count</td>
																<td id="c_sub_count">Doe</td>
															</tr>
															<tr>
																<td>Broker IP</td>
																<td id="c_broker_ip">Doe</td>
														</tbody>
														<tfoot>
															<tr>
																<td colspan="2">
																	<button type="button" style="width: 100%;"
																		id="btnDisconnect">Disconnect</button>
																</td>
															</tr>
														</tfoot>
													</table>
												</div>
											</div>




										</div>

									</div>
								</div>

							</div>
						</div>


						<div class="chart_tab_panel">
							<div class="col-md-12 col-sm-12 col-xs-12">





								<div class="x_panel">
									<div class="x_title">

										<h2 onclick="changleToggle(this.children[1].value)">
											HILIGHT DashBoard <small>ver 1.0</small><input type="hidden">
										</h2>

										<div class="clearfix"></div>
									</div>

									<div class="x_content visual_content">

										<div class="col-md-12 col-sm-12 col-xs-12">


											<canvas id="myChart" width="80%" height="30%"></canvas>

										</div>




									</div>

								</div>
							</div>

						</div>


						<!-- min add end -->

						<!-- panels -->
					</div>
				</div>
			</div>


			<!-- /page content -->

			<!-- footer content -->
			<footer>
				<div class="pull-right">
					HILIGHT IoT View Template by <a href="https://colorlib.com">YS</a>
				</div>
				<div class="clearfix"></div>
			</footer>
			<!-- /footer content -->
		</div>
	</div>



	</div>
	<script src="vendors/Chart.js/dist/Chart.min.js"></script>
	<script src="vendors/Chart.js/utils.js"></script>
	<script>
		var config2 = {
			type : 'line',
			data : {
				labels : [ "0" ],
				datasets : [ {
					label : "My First dataset",
					fill : false,
					backgroundColor : window.chartColors.red,
					borderColor : window.chartColors.red,
					data : [ 0 ],
				}
				/* , {
				              label: "My Second dataset",
				              fill: false,
				              backgroundColor: window.chartColors.blue,
				              borderColor: window.chartColors.blue,
				              data: [
				                10
				              ],
				          } */
				]
			},
			options : {
				responsive : true,
				title : {
					display : true,
					text : 'HILIGHT Line Chart'
				},
				tooltips : {
					mode : 'index',
					intersect : false,
				},
				hover : {
					mode : 'nearest',
					intersect : true
				},
				scales : {
					xAxes : [ {
						display : true,
						scaleLabel : {
							display : true,
							labelString : '시간'
						}
					} ],
					yAxes : [ {
						display : true,
						scaleLabel : {
							display : true,
							labelString : '온도'
						}
					} ]
				}
			}
		};
	</script>

	<!-- Bootstrap -->
	<script src="vendors/bootstrap/dist/js/bootstrap.min.js"></script>
	<!-- FastClick -->
	<script src="vendors/fastclick/lib/fastclick.js"></script>
	<!-- NProgress -->
	<script src="vendors/nprogress/nprogress.js"></script>
	<!-- jQuery Sparklines -->
	<script src="vendors/jquery-sparkline/dist/jquery.sparkline.min.js"></script>
	<!-- Flot -->
	<script src="vendors/Flot/jquery.flot.js"></script>
	<script src="vendors/Flot/jquery.flot.pie.js"></script>
	<script src="vendors/Flot/jquery.flot.time.js"></script>
	<script src="vendors/Flot/jquery.flot.stack.js"></script>
	<script src="vendors/Flot/jquery.flot.resize.js"></script>
	<!-- Flot plugins -->
	<script src="vendors/flot.orderbars/js/jquery.flot.orderBars.js"></script>
	<script src="vendors/flot-spline/js/jquery.flot.spline.min.js"></script>
	<script src="vendors/flot.curvedlines/curvedLines.js"></script>



	<!-- DateJS -->
	<script src="vendors/DateJS/build/date.js"></script>
	<!-- bootstrap-daterangepicker -->
	<script src="vendors/moment/min/moment.min.js"></script>
	<script src="vendors/bootstrap-daterangepicker/daterangepicker.js"></script>

	<!-- Custom Theme Scripts -->
	<script src="build/js/custom.min.js"></script>
</body>

</html>
