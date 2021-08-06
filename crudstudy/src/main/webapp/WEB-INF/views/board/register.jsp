<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../resources/css/style.css">
</head>
<body>
	<h1>게시글 등록 페이지</h1>
<form role="form" action="/board/register" method="post">
	<div class="mb-3">
	  <label for="inputTitle" class="form-label">제목</label>
	  <input type="text" class="form-control" id="inputTitle" name="title">
	</div>
	
	<div class="mb-3">
	  <label for="inputContent" class="form-label">내용</label>
	  <textarea class="form-control" id="inputContent" rows="3" name="content"></textarea>
	</div>
	
	<div class="mb-3">
	  <label for="inputWriter" class="form-label">작성자</label>
	  <input type="text" class="form-control" id="inputWriter" name="writer">
	</div>
	
	<button type="submit" class="btn btn-primary">등록</button>
	<button type="reset" class="btn btn-primary">초기화</button>
</form>

<div class="panel-body">
	<div class="form-group uploadDiv">
	  <label for="fileUpload" class="form-label">파일업로드</label><br>
	  <input type="file" id="fileUpload" name="uploadFile" multiple>
	</div>
	
	<div class='uploadResult'>
		<ul>
			<!-- JS로 처리 -->
		</ul>
	</div>
</div>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	<script>
	//등록 버튼을 클릭했을 때, 첨부파일 관련 처리 할 수 있게 동작을 막습니다.
	$(document).ready(function(event) {
		
		let formObj = $("form[role='form']");

		//기존에 <form>태그에 input type=hidden 태그를 추가해서 전송하기로 함
		$("button[type='submit']").on("click", function(e) {
			e.preventDefault();

			console.log("등록 버튼 클릭");

			let str = "";

			//li 태그가 생성된 만큼 forEach
			$(".uploadResult ul li").each(function(i, obj) {
				let jobj = $(obj);

				console.dir(jobj);

				str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
			})
			
			console.log("완성된 문자열: ", str);


			formObj.append(str).submit();
		})

		//첨부파일 변경 처리
		$('.uploadResult').on("click", "span", function(e) {
			e.preventDefault();

			console.log("스팬태그 누름");

			let targetFile = $(this).data("file");
			let type = $(this).data("type");

			//삭제 후 화면에 <li>태그도 삭제해야되니까
			let tartgetLi = $(this).closest("li");

			console.log("선택된파일: " + targetFile, "선택된파일의_타입: " + type);

			$.ajax({
				url: '/deleteFile',
				data: { fileName: targetFile, type: type },
				dataType: 'text',
				type: "POST",
				success: function(result) {
					alert(result);
					tartgetLi.remove();
				}
			})
		})

		//파일 업로드의 버튼을 따로 두지 않고 <input>태그가 변하면 그 것을 감지해 처리하도록 함
		
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

		//업로드된 결과를 화면에 섬네일 등을 만들어 처리하는 부분
		function showUploadResult(uploadResultArr) {
			/* result == uploadResultArr */
			console.log("showUploadFile: ", uploadResultArr);

			//파라미터가 없거나 0이면 그대로 종료
			if (!uploadResultArr || uploadResultArr.length == 0) {
				return;
			}

			let uploadUL = $(".uploadResult ul");

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
						
					str += "<li data-path='"+ obj.uploadPath +"' data-uuid='"+ obj.uuid +"' data-filename='" + obj.fileName +"' data-type='" + obj.image + "'>"
						+ "<div><a href='/download?fileName="+fileCallPath+"'>"
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
					str += "<li data-path='"+ obj.uploadPath +"' data-uuid='"+ obj.uuid +"' data-filename='" + obj.fileName +"' data-type='" + obj.image + "'>"
					+ "<a href=\"javascript:showImage(\'"+originPath+"\')\">"
					+ "<img src='/display?fileName="+fileCallPath+"'></a>"
					+ "<p>" + obj.fileName + "</p>"
					+ "<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span>"
					+ "</li>";
					//str += "<li><li><img src='/display?fileName="+fileCallPath+"'><li>";
				}
			});
			uploadUL.append(str);
		}

		//input태그가 바뀐다면
		$("input[type='file']").change(function(e) {

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
					showUploadResult(result);
					
					//등록하고 redirect되기 때문에 필요없음
					//$('.uploadDiv').html(cloneObj.html());
				}
			})
		})
	})
	</script>
</body>
</html>