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
<form role="form" action="/board/modify" method="post">
	<div class="mb-3">
	  <label for="showBno" class="form-label">글번호</label>
	  <input type="text" class="form-control" id="showBno" name="bno" 
	  value='<c:out value="${board.bno}"/>' readonly="readonly">
	</div>

	<div class="mb-3">
	  <label for="showTitle" class="form-label">제목</label>
	  <input type="text" class="form-control" id="showTitle" name="title" 
	  value='<c:out value="${board.title}"/>'>
	</div>
	
	<div class="mb-3">
	  <label for="showContent" class="form-label">내용</label>
	  <textarea class="form-control" id="showContent" rows="3" name="content" 
	  ><c:out value="${board.content}"/></textarea>
	</div>
	
	<div class="mb-3">
	  <label for="showWriter" class="form-label">작성자</label>
	  <input type="text" class="form-control" id="showWriter" name="writer" 
	  value='<c:out value="${board.writer}"/>' readonly="readonly">
	</div>
	
	<div class="mb-3">
	  <input type="hidden" class="form-control" id="showRegDate" name="regDate" 
	  value='<fmt:formatDate pattern = "yyyy/MM/dd" value= "${board.regdate}" />' readonly="readonly">
	</div>
	
	<div class="mb-3">
	  <input type="hidden" class="form-control" id="showUpdateDate" name="updateDate" 
	  value='<fmt:formatDate pattern = "yyyy/MM/dd" value= "${board.updateDate}" />' readonly="readonly">
	</div>
	
	<!-- 첨부파일 수정 -->
	<div class="panel-body">
		<!-- 업로드 태그  -->
		<div class="form-group uploadDiv">
		  <label for="fileUpload" class="form-label">파일업로드</label><br>
		  <input type="file" id="fileUpload" name="uploadFile" multiple>
		</div>
		<!-- 결과 태그 -->
		<div class='uploadResult'>
			<ul>
				<!-- JS로 처리 -->
			</ul>
		</div>
	</div>
	
	<!-- 페이징 처리 값 추가 -->
	<input type='hidden' name='pageNum' value='<c:out value="${page.pageNum}"/>'>
	<input type='hidden' name='amount' value='<c:out value="${page.amount}"/>'>
	
	<button id="modifyBtn" data-oper="modify" type="submit" class="btn btn-primary">수정</button>
	<button id="delBtn" data-oper="remove" type="submit" class="btn btn-danger">삭제</button>
	<button id="listBtn" data-oper="list" type="submit" class="btn btn-primary">목록으로 돌아가기</button>
</form>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	<script>
	$(document).ready(function() {
	
		/* 즉시 실행 함수 */
		(function() {
			let bno = '<c:out value="${board.bno}"/>';
			
			$.getJSON("/board/getAttachList", { bno: bno }, function(arr) {
				//console.log(arr);
				
				let str = "";
				
				$(arr).each(function(i, attach) {
					
					console.log("each: ", attach);
					
					// 이미지 파일이 아니면
					if (!attach.fileType) {
						/** 
						 * 업로드 된 파일이 화면에 나타나고
						 * 클릭하면 다운로드 될 수 있도록 처리
						*/
						let fileCallPath = encodeURIComponent( attach.uploadPath+ "/" + attach.uuid + "_" + attach.fileName);

						/* 화면에서 파일 삭제 기능 */
						//let fileLing = fileCallPath.replace(new RegExp(/\\/g), "/");
						
						str += "<li data-path='"+ attach.uploadPath +"' data-uuid='"+ attach.uuid +"' data-filename='" + attach.fileName +"' data-type='" + attach.image + "'>"
							+ "<div><a href='/download?fileName="+fileCallPath+"'>"
							+ "<img src='../../resources/image/attach.png'>"
							+ attach.fileName + "</a>"
							+ "<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"
							+ "</div></li>";
					} else {
						
						console.log('이미지 입니다.');

						let fileCallPath = encodeURIComponent( attach.uploadPath+ "/s_" + attach.uuid + "_" + attach.fileName);
						
						let originPath = attach.uploadPath + "\\"+attach.uuid+"_"+attach.fileName;
						
						originPath = originPath.replace(new RegExp(/\\/g), "/");
						
						//console.log("fileCallPath: " + fileCallPath);
						//console.log("originPath: " + originPath);
						
						//직접 showImage( )를 호출하는 태그 작성
						//str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\"><img src='/display?fileName="+fileCallPath+"'></a><li>";
						
						/* 파일 삭제 span 태그 추가 */
						str += "<li data-path='"+ attach.uploadPath +"' data-uuid='"+ attach.uuid +"' data-filename='" + attach.fileName +"' data-type='" + attach.image + "'>"
						+ "<a href=\"javascript:showImage(\'"+originPath+"\')\">"
						+ "<img src='/display?fileName="+fileCallPath+"'></a>"
						+ "<p>" + attach.fileName + "</p>"
						+ "<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span>"
						+ "</li>";
						//str += "<li><li><img src='/display?fileName="+fileCallPath+"'><li>";
					}
				})
				$('.uploadResult ul').html(str);
			})
		})();
		
		//첨부파일 수정 및 삭제 처리
		$('.uploadResult').on("click", "span", function(e) {
			e.preventDefault();

			console.log("스팬태그 누름");
			
			if (confirm("정말 삭제할건가요?")) {
				//삭제 후 화면에 <li>태그도 삭제해야되니까
				let tartgetLi = $(this).closest("li");
				tartgetLi.remove();
			}
		})
	})
	</script>
	<script type="text/javascript">
	$(document).ready(function() {
		let formObj = $("form");
		
		/* $('button').click(function(e) {
			e.preventDefault();
			
			let operation = $(this).data("oper");
			console.log(operation);
			
			if (operation === 'remove') {
				formObj.attr("action", "/board/remove");
			} else if (operation === 'list') {
				formObj.attr("action", "/board/list").attr("method", "get");
				formObj.empty();
			}
			
			formObj.submit();
		}) */
		
		/*
		* 목록 버튼을 클릭하면 <form>태그에서 필요한 부분만
		* 복사(clone)한 뒤, 보관하고 <form>태그 내 모든 내용을 지운다(empty)
		* 이후, 다시 필요한 태그만 추가해 /board/list 를 호출하는 형태
		*/
		$('button').click(function(e) {
			e.preventDefault();
			
			let operation = $(this).data("oper");
			
			//console.log("operation: ", operation);
			
			if (operation === 'remove') {
				formObj.attr("action", "/board/remove");
			} else if (operation === 'list') {
				//게시글 삭제
				formObj.attr("action", "/board/list").attr("method", "get");
				let pageNumTag = $("input[name='pageNum']").clone();
				let amountTag = $("input[name='amount']").clone();
				
				formObj.empty();
				formObj.append(pageNumTag);
				formObj.append(amountTag);
			} else if (operation === 'modify') {
				
				console.log('수정 버튼 클릭');
				
				let str = "";
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
			}//수정부분 추가
			formObj.submit();
		});
	});
	
	//<파일첨부>
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
	</script>
</body>
</html>