/**
 * 즉시 실행함수와 '{}'를 이용해서 객체를 구성한다.
 */
console.log('=====댓글 모듈======');

let replyService = (function() {

	/**
	 * 댓글 등록 함수
	 */
	function add(reply, callback, error) {
		console.log("add 댓글...");
		/**
		 * add 함수는 Ajax를 이용해서 POST방식으로 작성
		 */
		$.ajax({
			type : "post",
			url : "/replies/new",
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, err) {
				if (error) {
					error(err);
				}
			}
		})
	}

	/**
	 * 댓글 조회 함수
	 */
	function getList(param, callback, error) {
		let bno = param.bno;
		let page = param.page || 1;

		$.getJSON("/replies/pages/" + bno + "/" + page + ".json",
			function(data) {
				if (callback) {
					//callback(data);//댓글 목록만 가져오는 경우
					callback(data.replyCnt, data.list);//댓글 개수와 목록을 가져오는 경우
				}
			}).fail(function(xhr, status, err) {
				if (error) {
					error();
				}
			});
	}


	/**
	 * 댓글 수정 함수
	 */
	function update(reply, callback, error) {

		console.log("RNO: " + reply.rno);

		$.ajax({
			type: 'put',
			url: '/replies/' + reply.rno,
			data: JSON.stringify(reply),
			contentType: "application/json; charset=utf-8",
			success: function(result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error: function(xhr, status, err) {
				if (error) {
					error(err);
				}
			}
		})
	}

	/**
	 * 댓글 삭제 함수
	 */
	function remove(rno, callback, error) {
		$.ajax({
			type: 'delete',
			url: '/replies/' + rno,
			success: function (deleteResult, status, xhr) {
				if (callback) {
					callback(deleteResult);
				}
			},
			error: function(xhr, status, err) {
				if (error) {
					error(err);
				}
			}
		});
	}

	/**
	 * 특정 번호의 댓글 조회
	 */
	function get(rno, callback, error) {
		$.get("/replies/" + rno  + ".json", function(result) {
			if (callback) {
				callback(result);
			}
		}).fail(function(xhr, status, err) {
			if (error) {
				error(err);
			}
		}) 
	}

	/**
	 * 시간 변환 처리
	 */
	function displayTime(timeValue) {
		let today = new Date();

		let gap = today.getTime() - timeValue;

		let dateObj = new Date(timeValue);

		let str = "";

		if (gap < (1000 * 60 * 60 * 24)) {
			let hh = dateObj.getHours();
			let mi = dateObj.getMinutes();
			let ss = dateObj.getSeconds();

			return [ (hh > 9 ? '' : '0') + hh, ":", (mi > 9 ? '' : '0') + mi, ":", (ss > 9 ? "" : "0") + ss].join('');
		} else {
			let yy = dateObj.getFullYear();
			let mm = dateObj.getMonth() + 1;
			let dd = dateObj.getDate();

			return [ yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd].join('');
		}
	}

	return { 
		add : add,
		getList : getList,
		update : update,
		remove : remove,
		get : get,
		displayTime : displayTime
	};
})();

