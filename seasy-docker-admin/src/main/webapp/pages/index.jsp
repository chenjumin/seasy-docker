<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>seasy docker admin</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <link rel="stylesheet" href="element-ui/theme-chalk/index.css">
  <script type="text/javascript" src="element-ui/vue.min.js"></script>
  <script type="text/javascript" src="element-ui/index.js"></script>
  <script type="text/javascript" src="js/axios.min.js"></script>
  <script type="text/javascript" src="js/jquery.min-v1.11.3.js"></script>

  <style>
		body {
			margin: 0;
			padding: 0;
		}

		.el-dialog__title {
			font-weight: 600;
		}
		
		/* header */
		.el-header {
			padding: 0 10px;
		}
		
		.header {
			background-color: #FCFCFC;
			padding-top: 13px;
			border-bottom: 1px solid #e6e6e6;
		}
		
		.logo-title {
			width: 220px;
			height: 100%;
			font-size: 24px;
			font-weight: 600;
			font-family: 黑体;
			float: left;
			color: #4682B4;
		}
		
		.top-menu {
			width: 600px;
			float: left;
		}
		
		.dropdown-menu {
			width: 130px;
			float:right;
		}
		
		.el-menu {
			background-color: #FCFCFC;
			border-right: 0;
		}
		
		/* left menu */
		.el-menu-item, .el-submenu__title {
			height: 45px;
			line-height: 45px;
		}
		
		.el-submenu .el-menu-item {
			height: 45px;
			line-height: 45px;
		}
		
		.el-menu--horizontal>.el-menu-item {
			height: 47px;
			line-height: 47px;
		}
		
		/* main container */
		.el-main {
			padding: 10px 10px 10px 10px;
		}

		/* dialog */
		#app .el-dialog__body {
			padding: 10px 10px;
		}

		#app .el-dialog__footer {
			padding: 0 10px 10px 0;
		}

		/* xhz */
		#xhz .el-dialog__body {
			padding: 10px;
		}

		#xhz .bh {
			width: 100%;
			border:1px solid #d9d9d9;
			border-collapse: collapse;
		}

		#xhz .bh td {
			border:1px solid #d9d9d9;
		}
		
		#xhz .word_detail {
			border-collapse: collapse;
		}

		#xhz .word_detail td{
			border:1px solid #d9d9d9;
			border-collapse: collapse;
			padding: 10px 5px;
			font-size: 15px;
		}
  </style>
</head>
<body>
	<div id="app">
		<el-container>
			<el-header height="60px" class="header">
				<div class="logo-title">电子课本学习系统</div>
				
				<div class="top-menu">
					<el-menu :default-active="currentCourseIndex" 
						active-text-color="#63B8FF" 
						mode="horizontal" 
						@select="topSelectHandler">
						<el-menu-item :index="index" v-for="(item, index) in dirInfos">
							{{ item.text }}
						</el-menu-item>
					</el-menu>
				</div>
				
				<div class="dropdown-menu">
					<el-dropdown split-button placement="bottom-start" size="medium" type="primary" @command="handleCommand">
  						功能菜单
					  	<el-dropdown-menu slot="dropdown">
						    <el-dropdown-item command="refreshPage">刷新页面</el-dropdown-item>
						    
						    <el-dropdown-item divided command="translateTool">内容翻译工具</el-dropdown-item>
						    <el-dropdown-item command="voiceTool">语音合成工具</el-dropdown-item>
						    <el-dropdown-item command="learnWord">学汉字</el-dropdown-item>
						    
						    <el-dropdown-item divided><a href="http://www.dzkbw.com/" target="_blank" style="color:blue;">电子课本网</a></el-dropdown-item>
						    <el-dropdown-item><a href="http://www.xuezizhai.com/" target="_blank" style="color:blue;">学子斋课堂</a></el-dropdown-item>
					  	</el-dropdown-menu>
					</el-dropdown>
				</div>
			</el-header>

			<el-container>
				<el-aside style="width:220px; border-right:solid 1px #e6e6e6; background-color: #FCFCFC;">
					<el-menu :style="{'height': menuHeight + 'px'}"
						active-text-color="#63B8FF"
						unique-opened="true" 
						@select="selectHandler">
						<el-submenu :index="index1" v-for="(item1, index1) in dirInfos[currentCourseIndex].children">
							<template slot="title">
								<i class="el-icon-menu"></i>
								<span>{{ item1.text }}</span>
							</template>

							<el-menu-item :index="index1 + '-' + index2" v-for="(item2, index2) in item1.children">
								{{ item2.text }}
							</el-menu-item>
						</el-submenu>
					</el-menu>
				</el-aside>
				
				<el-main id="mainContainer">
					
				</el-main>
			</el-container>
		</el-container>

		
		<!-- 内容翻译工具 -->
		<el-dialog title="内容翻译工具" :visible.sync="translateToolVisible" width="60%">
		<table width="100%">
			<tr>
				<td>
					<el-input type="textarea" rows="15" v-model.trim="transFrom"></el-input>
				</td>
				<td width="70" align="center">
					<el-button type="primary" size="small" @click="translateForContent('zh')">中文</el-button><br><br><br>
					<el-button type="primary" size="small" @click="translateForContent('en')">英文</el-button>
				</td>
				<td>
					<el-input type="textarea" rows="15" v-model.trim="transTo"></el-input>
				</td>
			</tr>
		</table>
		</el-dialog>
	</div>

	<script type="text/javascript">
		var vm = new Vue({
			el: '#app',
			data: function() {
				return {
					menuHeight: document.documentElement.clientHeight - 60,
					dirInfos: [],
					currentCourseIndex: 0,

					translateToolVisible: false,
					transFrom: "",
					transTo: "",

					voiceToolVisible: false,
					voiceText: "",
					toolVoiceFileURL: "",
					
					learnWordVisible: false,
					bhShow: false,
					word: "",
					wordInfo: {},
					wordVoiceFileURL: ""
				}
			},
			watch: { 
				word: function(val){
					if(val == ""){
						this.bhShow = false;
						this.wordInfo = {};
						this.wordVoiceFileURL = "";
					}
				}
			},
			methods: {
				topSelectHandler: function(index, indexPath){ //选择顶部菜单
					this.currentCourseIndex = parseInt(index);
				},
				selectHandler: function(index, indexPath){ //选择右侧菜单
					const _self = this;
					var arr = (indexPath+"").split("-");
					
					var path = this.dirInfos[this.currentCourseIndex].children[parseInt(arr[0])].children[parseInt(arr[1])].path;
					loadModulePage(path);					
				},
				handleCommand: function(command){ //选择下拉菜单项
					if("refreshPage" == command){
						window.location.reload();
					}else if("translateTool" == command){
						this.transFrom = "";
						this.transTo = "";
						this.translateToolVisible = true;
					}else if("voiceTool" == command){
						this.voiceText = "";
						this.toolVoiceFile = "";
						this.voiceToolVisible = true;
					}else if("learnWord" == command){
						this.word = "";
						this.wordInfo = {};
						this.wordVoiceFileURL = "";
						this.learnWordVisible = true;
					}
				},
				bhShowList: function(){
					this.bhShow = (this.bhShow==false);
				}
			},
			mounted: function () { //挂载完成后调用
				const that = this;
				window.onresize = function(){
					that.menuHeight = document.documentElement.clientHeight - 60;
				}
			}
		});

		vm.dirInfos = [{
			"text": "name1",
			"children": [
				{
					"text":"name1_1", 
					"children": [
						{"text":"name1_1_1", "path":"void(0)"},
						{"text":"name1_1_2", "path":"void(0)"},
						{"text":"name1_1_3", "path":"void(0)"}
					]
				},
				{
					"text":"name1_2", 
					"children":[
						{"text":"name1_2_1", "path":"void(0)"},
						{"text":"name1_2_2", "path":"void(0)"},
						{"text":"name1_2_3", "path":"void(0)"}
					]
				}
			]
		}];

		function loadModulePage(filePath){
			var actionURL = "";
			var courseType = "";
			
			var courseText = vm.dirInfos[vm.currentCourseIndex].text;
			if(courseText.indexOf("英语") >= 0){
				actionURL = "englishPage";
				courseType = "1";
			}else if(courseText.indexOf("语文") >= 0){
				actionURL = "englishPage";
				courseType = "2";
			}else if(courseText.indexOf("数学") >= 0){
				actionURL = "englishPage";
				courseType = "3";
			}else if(courseText.indexOf("学汉字") >= 0){
				actionURL = "wordPage";
				courseType = "4";
			}

			axios.post(actionURL, {
				filePath: filePath,
				courseType: courseType
			})
			.then(function (response) {
				jQuery("#mainContainer").html(response.data);
			})
			.catch(function (error) {
				console.log(error);
			});
		}
	</script>
	
</body>
</html>