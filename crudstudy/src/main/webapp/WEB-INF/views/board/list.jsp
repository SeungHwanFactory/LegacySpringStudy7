<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>게시글 목록 페이지</h1>
	<div>
		<button id="regBtn" type="button" class="btn btn-outline-primary">글쓰기</button>
	</div>
	<table class="table">
	  	<thead>
		    <tr>
		      <th scope="col">#번호</th>
		      <th scope="col">제목</th>
		      <th scope="col">작성자</th>
		      <th scope="col">작성일</th>
		      <th scope="col">수정일</th>
		    </tr>
	  	</thead>
		<tbody>
			<!-- items: 모델 key값 / var: 변수명 -->
			<c:forEach items="${list}" var="board">
				<tr>
					<td><c:out value="${board.bno}"></c:out></td>
					<!-- a태그에는 이동하려는 게시물의 번호만 가지게 수정(이벤트 핸들링 편하게 하기 위해 class지정) -->
					<td>
						<a class="move" href='<c:out value="${board.bno}"/>'>
						<c:out value="${board.title}"/></a>
					</td>
					<td><c:out value="${board.writer}"></c:out></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate}"/></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.updateDate}"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<nav aria-label="Page navigation example">
		<ul class="pagination">
		    <c:if test="${pageMaker.prev}">
				<li class="page-item"><a class="page-link" href="${pageMaker.startPage -1}">이전</a></li>
			</c:if>
			
			<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
				<li class="page-item" ${pageMaker.page.pageNum == num ? "active":""} ">
					<a class="page-link" href="${num}">${num}</a>
				</li>
			</c:forEach>
			
			<c:if test="${pageMaker.next}">
				<li class="page-item"><a class="page-link" href="${pageMaker.endPage + 1}">다음</a></li>
			</c:if>
		</ul>
	</nav>
	
	<!-- <a>태그가 동작하지 못하도록 이벤트 막은 뒤, <form>태그를 이용해 처리 -->
	<form id='actionForm' action="/board/list" method="get">
		<input type='hidden' name="pageNum" value='${pageMaker.page.pageNum}'>
		<input type='hidden' name="amount" value='${pageMaker.page.amount}'>
	</form>
	
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		let result = '<c:out value="${result}"/>';
		
		/*
		* 글쓰기 버튼 제어
		*/
		$("#regBtn").click(function() {
			self.location = "/board/register";
		});
		
		/*
		* 페이징 처리 버튼 제어
		*/
		$(".page-link").click(function(e) {
			e.preventDefault();
			console.log("이벤트가 정상적으로 중단되면 이 글자가 보입니다.");
			
			$("#actionForm").find("input[name='pageNum']").val($(this).attr("href"));
			$("#actionForm").submit();
		});
		
		/*
		* 게시글 제목 a태그 제어
		*/
		$('.move').click(function(e) {
			e.preventDefault();
			console.log("정상작동");
			$("#actionForm").append("<input type='hidden' name='bno' value='"+
					$(this).attr("href")+"'>");
			$("#actionForm").attr("action", "/board/get");
			$("#actionForm").submit();
		});
	});
	</script>
</body>
</html>