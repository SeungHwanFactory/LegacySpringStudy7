<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ajax로 업로드 처리</title>
<link rel="stylesheet" href="../../resources/css/style.css">
</head>
<body>
	<h1>Ajax로 업로드</h1>
	
	<div class="uploadDiv">
		<input type = 'file' name = 'uploadFile' multiple>
	</div>

	<button id='uploadBtn'>업로드</button>

	<!-- 파일 이름 출력 -->
	<div class="uploadResult">
		<ul>
			<!-- JS 함수로 목록 처리 -->
		</ul>
	</div>

	<!-- 이미지 보여줌 -->
	<div class="bigPictureWrapper">
		<div class='bigPicture'>
			<!-- JS 함수로 처리 -->
		</div>
	</div>

</div>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	<script>

		/**
		 * 원본 이미지 보여주는 함수 작성
		 * <a> 태그에서 직접 showImage()를 호출하는 형태로 하기 위해
		 * $(document).ready( ) 바깥쪽에 선언
		 * */
		function showImage(fileCallPath) {
			//alert(fileCallPath);
			$(".bigPictureWrapper").css("display", "flex").show();
			
			$(".bigPicture").html("<img src='/display?fileName="+encodeURI(fileCallPath)+"'>").animate({ width: '100%', height: '100%' }, 1000);
			
		}
		
		/**
		 * 원본 이미지 보여주는 태그 클릭 시 숨기기 처리
		 * */
		$(".bigPictureWrapper").click(function(e) {
			$('.bicPicture').animate({ width: '0%', height: '0%' }, 1000);
			setTimeout(() => {
				$(this).hide();
			}, 1000);
		})

		$(document).ready(function() {
			//업로드 확장자 제한
			let regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			//업로드 사이즈 제한
			let maxSize = 10485760; //10MB
			
			//업로드 전 로직 수행
			function checkExtension(fileName, fileSize) {
				//업로드 사이즈 초과시
				if (fileSize >= maxSize) {
					alert("파일 사이즈 초과");
					return false;
				}
				
				//제한된 확장자 업로드 시
				if (regex.test(fileName)) {
					alert("해당 확장자는 업로드 불가");
					return false;
				}
				
				return true;
			}
			
			//<input type='file'>은 readonly라 내용을 수정할 수 없어서 별도로 초기화해서 사용
			//이걸 왜쓰냐면 새로 복제해서 업로드 후에 다시 업로드할 수 있는 창을 보여주기 위함이다.
			let cloneObj = $('.uploadDiv').clone();
			
			$('#uploadBtn').click(function(e) {
				e.preventDefault();
				
				let formData = new FormData();
				let inputFile = $("input[name='uploadFile']");
				let files = inputFile[0].files;
				
				console.log('파일: ', files);
				
				//업로드한 파일 개수 만큼 반복해서 formData에 추가
				for(let i = 0; i< files.length; i++) {
					
					//formData.append("uploadFile", files[i]);
					
					//아무런 제한 없이 파일을 보냈지만 checkExtension함수를 호출해서 velidate한다.
					//checkExtension이 false를 반환했다면, 확장자나 업로드 사이즈가 제한됐다는 것
					//true일때만 formData에 append한다.
					if (!checkExtension(files[i].name, files[i].size) ) {
						return false;
					}
					
					formData.append("uploadFile", files[i]);
				}
				
				//formData확인
				for (let value of formData.values()) {
					console.log(value);
				}
				
				/*
				* processData, contentType은 반드시 'false'로 지정
				* 
				*/
				$.ajax({
					url: '/uploadAjaxAction',
					processData: false,
					contentType: false,
					data: formData,
					type: 'POST',
					dataType: 'json',
					success: function(result) {
						alert("업로드 성공");
						console.log('피드백 받은 결과: ', result);
						
						/* 파일 이름 출력 함수 호출 */
						showUploadFile(result);
						
						$('.uploadDiv').html(cloneObj.html());
					}
				})
			})
			
			/* 파일 이름 출력 함수 */
			/**
			* GET방식으로 첨부파일 이름을 사용할 때 공백 문자나 한글 이름이 문제가 될 수 있음
			* encodeURIComponent( )를 이용해 URI 호출에 적합한 문자열로 인코딩 처리
			*/
			let uploadResult = $('.uploadResult ul');
			
			function showUploadFile(uploadResultArr) {
				/* result == uploadResultArr */
				console.log("showUploadFile: ", uploadResultArr);
				let str = "";
				
				// <li>태그, <img>태그 추가
				$(uploadResultArr).each(function(i, obj) {
					console.log(obj);
					// 이미지 파일이 아니면
					if (!obj.image) {
						/** 
						 * 업로드 된 파일이 화면에 나타나고
						 * 클릭하면 다운로드 될 수 있도록 처리
						*/
						let fileCallPath = encodeURIComponent( obj.uploadPath+ "/" + obj.uuid + "_" + obj.fileName);

						/* 화면에서 파일 삭제 기능 */
						let fileLing = fileCallPath.replace(new RegExp(/\\/g), "/");
						
						/* str += "<li><a href='/download?fileName="+fileCallPath+"'>"
							+ "<img src='../../resources/image/attach.png'>"
							+ obj.fileName + "</a></li>"; */
							
						str += "<li><div><a href='/download?fileName="+fileCallPath+"'>"
							+ "<img src='../../resources/image/attach.png'>"
							+ obj.fileName + "</a>"
							+ "<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"
							+ "</div></li>";
					} else {
						//str += "<li>" + obj.fileName + "</li>";
						
						console.log('이미지 입니다.');

						let fileCallPath = encodeURIComponent( obj.uploadPath+ "/s_" + obj.uuid + "_" + obj.fileName);
						
						let originPath = obj.uploadPath + "\\"+obj.uuid+"_"+obj.fileName;
						
						originPath = originPath.replace(new RegExp(/\\/g), "/");
						
						//console.log("fileCallPath: " + fileCallPath);
						//console.log("originPath: " + originPath);
						
						//직접 showImage( )를 호출하는 태그 작성
						//str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\"><img src='/display?fileName="+fileCallPath+"'></a><li>";
						
						/* 파일 삭제 span 태그 추가 */
						str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"
						+ "<img src='/display?fileName="+fileCallPath+"'></a>"
						+ "<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span>"
						+ "<li>";
						//str += "<li><li><img src='/display?fileName="+fileCallPath+"'><li>";
					}
				});
				uploadResult.append(str);
			}

			/**
			 * <span> 태그 클릭시 파일 삭제 (Ajax처리)
			 * */
			$('.uploadResult').on("click", "span", function(e) {
				let targetFile = $(this).data("file");
				let type = $(this).data("type");

				console.log("선택된파일: " + targetFile, "선택된파일의_타입: " + type);

				$.ajax({
					url: '/deleteFile',
					data: { fileName: targetFile, type: type },
					dataType: 'text',
					type: "POST",
					success: function(result) {
						alert(result);
					}
				})
			})

		})
	</script>
</body>
</html>