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
<form id="operForm" role="form" action="/board/modify" method="get">
	<div class="mb-3">
	  <label for="showBno" class="form-label">글번호</label>
	  <input type="hidden" class="form-control" id="showBno" name="bno" 
	  value='<c:out value="${board.bno}"/>' readonly="readonly">
	</div>

	<div class="mb-3">
	  <label for="showTitle" class="form-label">제목</label>
	  <input type="text" class="form-control" id="showTitle" name="title" 
	  value='<c:out value="${board.title}"/>' readonly="readonly">
	</div>
	
	<div class="mb-3">
	  <label for="showContent" class="form-label">내용</label>
	  <textarea class="form-control" id="showContent" rows="3" name="content" 
	  readonly="readonly"><c:out value="${board.content}"/></textarea>
	</div>
	
	<div class="mb-3">
	  <label for="showWriter" class="form-label">작성자</label>
	  <input type="text" class="form-control" id="showWriter" name="writer" 
	  value='<c:out value="${board.writer}"/>' readonly="readonly">
	</div>
	
	<button id="modifyBtn" data-oper="modify" type="submit" class="btn btn-primary">수정</button>
	<button id="listBtn" data-oper="list" type="submit" class="btn btn-primary">목록으로 돌아가기</button>
	
	<!-- 페이징처리 부분 hidden으로 감싼다. -->
	<input type='hidden' name='pageNum' value='<c:out value="${page.pageNum}"/>'>
	<input type='hidden' name='amount' value='<c:out value="${page.amount}"/>'>
</form>

<!-- 첨부파일 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel-heading">첨부파일</div>
		<div class="panel-body">
			<div class="uploadResult">
				<ul>
					<!-- JS처리 -->
				</ul>
			</div>
		</div>
	</div>
</div>

<!-- 이미지 보여줌 -->
<div class="bigPictureWrapper">
	<div class='bigPicture'>
		<!-- JS 함수로 처리 -->
	</div>
</div>

<!-- 댓글 작성 -->
<form id="replyForm">
	<div class="mb-3">
	  <label for="showReplyer" class="form-label">작성자</label>
	  <input type="text" class="form-control" id="showReplyer" name="replyer">
	</div>
	<div class="mb-3">
	  <label for="showReply" class="form-label">댓글내용</label>
	  <textarea class="form-control" id="showReply" rows="3" name="reply"></textarea>
	</div>
	<div class="mb-3">
	  <label for="showReplyDate" class="form-label">작성시간</label>
	  <input type="text" class="form-control" id="showReplyDate" name="replyDate">
	</div>
 	<button id="addReplyBtn" type="submit" class="btn btn-primary">등록</button>
</form>

<!-- 댓글 보여주기 -->
<ul class="chat">
</ul>

<!-- 댓글수정 모달 -->
<div class="modal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">댓글 모달</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <!-- input태그 삽입 -->
        <div class="form-group">
	        <label>댓글</label>
	        <input class="form-control" name="reply">
        </div>
        <div class="form-group">
	        <label>작성자</label>
	        <input class="form-control" name="replyer">
        </div>
        <div class="form-group">
	        <label>등록일</label>
	        <input class="form-control" name="replyDate" value="">
        </div>
        
      </div>
      <div class="modal-footer">
        <button id="modalCloseBtn" type="submit" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button id="modalModifyBtn" type="submit" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

<!-- 페이징 처리 -->
<div class="panel-footer">
<h1>여기는 페이징 처리</h1>
</div>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	<!-- 댓글 모듈 사용 -->
	<script type="text/javascript" src="/resources/reply.js"></script>
	<!-- 첨부파일 데이터 가져오는 script -->
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
						
						str += "<li data-path='"+ attach.uploadPath +"' data-uuid='"+ attach.uuid +"' data-filename='" + attach.fileName +"' data-type='" + attach.fileType + "'>"
							+ "<div><a href='/download?fileName="+fileCallPath+"'>"
							+ "<img src='../../resources/image/attach.png'>"
							+ attach.fileName + "</a>"
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
						str += "<li data-path='"+ attach.uploadPath +"' data-uuid='"+ attach.uuid +"' data-filename='" + attach.fileName +"' data-type='" + attach.fileType + "'>"
						+ "<img src='/display?fileName="+fileCallPath+"'></a>"
						+ "<p>" + attach.fileName + "</p>"
						+ "</li>";
						//str += "<li><li><img src='/display?fileName="+fileCallPath+"'><li>";
					}
				})
				$('.uploadResult ul').html(str);
			})
		})();

		//이미지 보여주는 함수
		function showImage(fileCallPath) {
			alert(fileCallPath);
			$(".bigPictureWrapper").css("display", "flex").show();
			
			$(".bigPicture")
			.html("<img src='/display?fileName="+fileCallPath+"'>")
			.animate({ width: '100%', height: '100%' }, 1000);
			
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

		//첨부파일 클릭 이벤트
		$('.uploadResult').on('click', 'li', function(e) {
			console.log('이미지 보기');

			let liObj = $(this);

			let path = encodeURIComponent(liObj.data("path")+"/" + liObj.data("uuid") + "_" + liObj.data("filename"));

			console.log("클릭: ", liObj.data("type"));

			if(!liObj.data("type")) {
				//다운로드
				self.location = "/download?fileName="+path;
			} else {
				showImage(path.replace(new RegExp(/\\/g), "/"));
			}
		})
	})
	</script>
	<script type="text/javascript">
	$(document).ready(function() {
		/* 
		$('button').click(function(e) {
			e.preventDefault();
			
			let operation = $(this).data("oper");
			console.log(operation);
			
			if (operation === 'modify') {
				self.location.href="/board/modify?bno=<c:out value="${board.bno}"/>";
			} else {
				self.location = "/board/list";
				return;
			}
		}) */
		let operForm = $("#operForm");
		$("button[data-oper='modify']").click(function(e) {
			operForm.attr("action", "/board/modify").submit();
		});
		
		$("button[data-oper='list']").click(function(e) {
			operForm.find("#bno").remove();
			operForm.attr("action", "/board/list");
			operForm.submit();
		});
		
		//댓글 모듈 동작 확인
		console.log(replyService);
		//replyService.add() 호출
		let bnoValue = '<c:out value="${board.bno}"/>';
		let replyUL = $(".chat");
		let btnStatus = false;
		
		//showList 함수 호출
		showList(1);
		
		//댓글 보여주기
		function showList(page) {
			//콜백 파라미터로 댓글 개수와 목록을 받는다.
			replyService.getList({bno:bnoValue, page: page||1}, function(replyCnt, list) {
				
				let str = "";
				
				console.log('리스트:', list);
				
				//임의로 음수의 페이지번호로 가려고 하면 자동으로 마지막 댓글이 달린 페이지 번호로 초기화 시켜주고 list보여줌
				if (page == -1) {
					pageNum = Math.ceil(replyCnt/10.0);
					showList(pageNum);
					return;
				}
				
				if (list == null || list.length == 0) {
					replyUL.html("");
					
					return;
				}
				
				//댓글 태그만드는 for 로직
				for (let i = 0, len = list.length || 0; i < len; i++) {
					str += "<li class='list-group-item' data-rno='"+list[i].rno+"'>";
					str += "<div><div class='header'><strong class='primary-font'>"+list[i].replyer+"</strong>";
					str += "<small>"+replyService.displayTime(list[i].replyDate)+"</small></div>";
					str += "<p>"+list[i].reply+"</p></div>";
					str += "<a class='modify' href='"+ list[i].rno +"'>수정</a>";
					str += "&nbsp;&nbsp;&nbsp;";
					str += "<a class='remove' href='"+ list[i].rno +"'>삭제</a>";
				}
				
				replyUL.html(str);
				
				//페이징 처리 함수 호출
				showReplyPage(replyCnt);
			})
		}
		
		//댓글 추가 로직
		$('#addReplyBtn').click(function(e) {
			e.preventDefault();
			console.log("일단 이벤트 막음");
			let replyer = $("#showReplyer").val();
			let reply = $("#showReply").val();
			
			let replyForm = {
					reply: reply,
					replyer: replyer,
					bno: bnoValue
			};
			
			replyService.add(replyForm, function(result) {
				alert(result);
				//showList(1);
				
				showList(-1);//페이지를 -1로 받아서 댓글 추가 후 마지막페이지를 찾아 호출하게함
			})
			
		})
		
		//모달 전역변수 관리
		let modal = $('.modal');
		let modalInputReply = modal.find("input[name='reply']");
		let modalInputReplyer = modal.find("input[name='replyer']");
		let modalInputReplyDate = modal.find("input[name='replyDate']");
		
		let modalModifyBtn = $("#modalModifyBtn");
		let modalCloseBtn = $("#modalCloseBtn");
		
		
		//댓글 수정 클릭 시, 해당 댓글 정보 가져오고 수정
		replyUL.on("click", "a.modify", function(e) {
			e.preventDefault();
			let rnoValue = $(this).attr('href');
			console.log('a.modify');
			
			//특정 번호 댓글 조회
			replyService.get(rnoValue, function(reply) {
				modalInputReply.val(reply.reply);
				modalInputReplyer.val(reply.replyer);
				modalInputReplyDate.val(replyService.displayTime( reply.replyDate ))
				.attr("readonly", "readonly");
				//솔루션
				modal.data("rno", reply.rno);
				
				modal.modal("show");
			})
		})
		
		modalCloseBtn.click(function(e) {
			e.preventDefault();
			modal.modal("hide");
		
		})
	
		modalModifyBtn.click(function(e) {
			e.preventDefault();
			let replyForm = {
					rno: modal.data("rno"),
					reply: modalInputReply.val(),					
			}
			
			replyService.update(replyForm, function(result) {
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			})
		})
		
		
		//댓글 삭제 클릭 시, 해당 댓글 정보 가져오고 삭제
		replyUL.on("click", "a.remove", function(e) {
			e.preventDefault();
			let rnoValue = $(this).attr('href');
			replyService.remove(rnoValue, function(result) {
				alert(result);
				showList(pageNum);
			})
		})
		
		
		
		//동적으로 생성된 태그에 이벤트 걸기
		$(document).on('click', '#finallymodify', function(e) {
			e.preventDefault();
			let rnoValue = $('a.modify').attr('href');
			let replyData = $('textarea#modify').val();
			let replyForm = { rno: rnoValue, reply: replyData };
			
			replyService.update(replyForm, function(result) {
				alert(result);
				showList(pageNum);
				btnStatus = !btnStatus;
			})
		})
		
		//댓글 페이징 처리
		let pageNum = 1;
		let replyPageFooter = $(".panel-footer"); //전역변수 선언 및 초기화
		
		function showReplyPage(replyCnt) {
			let endNum = Math.ceil(pageNum / 10.0) * 10; //페이지 끝
			let startNum = endNum - 9; //페이지 시작
			
			let prev = startNum != 1; //이전 : 1이 아니면 true 1이면 false
			let next = false; //다음
			
			//실제 댓글 개수보다 끝 페이지 수가 크거나 같으면 실제 댓글 개수로 초기화
			if(endNum * 10 >= replyCnt) {
				endNum = Math.ceil(replyCnt / 10.0);
			}
			
			//실제 댓글 개수가 끝 페이지 수보다 크면 다음에 보여줄 댓글이 있기 때문에 next=true 초기화
			if(endNum * 10 < replyCnt) {
				next = true;
			}
			
			let str = "<ul class=pagination>";
			
			//1페이지가 아니면 이전 <li>태그 생성
			if(prev) {
				str += "<li class='page-item'><a class='page-link' href='"+(startNum -1)+"'>이전</a></li>";
			}
			
			//시작페이지~끝페이지 반복 로직 수행
			for (let i = startNum; i <= endNum; i++) {
				//현재 클릭된 페이지에 active속성 추가
				let active = (pageNum == i ? "active" : "");
				
				str += "<li class='page-item' "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
			}
			
			//다음에 보여줄 댓글이 있다면
			if (next) {
				str += "<li class='page-item'><a class='page-link' href='"+(endNum + 1)+"'>다음</a></li>";
			}
			
			str += "</ul></div>";
			
			//console.log(str);
			
			replyPageFooter.html(str);
		}
		
		//페이지 번호를 클릭했을 때, 새로운 댓글을 보여주는 작업
		replyPageFooter.on('click', 'li a', function(e) {
			e.preventDefault();
			
			let targetPageNum = $(this).attr("href");
			
			console.log("타겟된페이지번호: " + targetPageNum);
			
			pageNum = targetPageNum;//pageNum은 전역변수로 선언함
			
			showList(pageNum);
		})
		
		//댓글 등록 테스트
		// replyService.add(
		// 		{reply:"ajax테스트", replyer:"테스트", bno:bnoValue},
		// 		function (result) {
		// 			alert("결과: " + result);
		// 		}
		// );

		//댓글 조회 테스트
		 /* replyService.getList({bno:bnoValue, page:1}, function(list) {
		 	for(let i = 0, len = list.length || 0; i < len; i++) {
		 		console.log(list[i]);
		 	}
		 }) */

		//rno=12 댓글 삭제 테스트
		// replyService.remove(12, function(count) {
		// 	console.log("콜백 호출 결과", count);

		// 	if (count === "success") {
		// 		alert("삭제성공");
		// 	}
		// }, function(err) {
		// 	alert("삭제실패");
		// });

		//댓글 수정 테스트
		/* replyService.update({
			rno: 6,
			bno: bnoValue,
			reply: "ajax로 댓글 수정 테스트"
		}, function(result) {
			alert("수정 완료");
		}) */

		//특정 댓글 조회 테스트
		/* replyService.get(6, function(data) {
			console.log(data);
		}) */
		
	});
	</script>
</body>
</html>