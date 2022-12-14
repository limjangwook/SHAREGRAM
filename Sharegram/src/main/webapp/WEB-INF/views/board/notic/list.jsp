<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

	<jsp:include page="../../layout/header.jsp">
		<jsp:param value="SHAREGRAM" name="title" />
	</jsp:include>
<style>
        @import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css');
</style>

<style>
	html {
	  height: 100%;
	}
	body {
	  	margin:0;
	  	padding:0;
	  	font-family: sans-serif;
	  	background: linear-gradient(#4e73df, #f8f9fc);
	}
	
	.noticbrd {
		position: absolute;
		top: 50%;
		left: 50%;
		width: 800px;
		padding: 40px;
		transform: translate(-50%, -50%);
		background: rgba(0,0,0,.5);
		box-sizing: border-box;
		box-shadow: 0 15px 25px rgba(0,0,0,.6);
		border-radius: 10px;	
	}

	table {
	  	border-collapse: collapse;
	  	border-spacing: 0;
	}
	.noticbrd {
		padding: 80px 0;
		background: #FFFFFF;
	}
	
	.notic_title {
		margin-bottom: 60px;
	}
	
	.notic_title h2 {
		 font-size: 28px;
	 	 color: #324b96;
	 	 font-weight: 800;
	 	 text-align: center;
	}
	
	.brd_thead th {
	     background: #324b96;
		 font-size: 15px;
	 	 color: #FFFFFF;
	 	 font-weight: 700; /* 글자 굵기 */
	 	 text-align: center;
	 	 line-height: 2;
	}
	
	.brd_tbody td {
		 font-size: 14px;
	 	 color: #4e73df;
	 	 font-weight: 400;
	 	 text-align: center;
	 	 line-height: 2;
	}
	
	.brd_tbody tr:hover {
		background:#d3dcf5; 	/* 색상값은 조정 예정 */
	}
	
	
	#frm_search .search_area {
		padding: 15px 0;
 		background-color: #f9f7f9;
	}
	
	.board_notic {
	  	position: relative;
	  	margin: 0 auto;
	 	width: 80%;
	 	max-width: 900px;
	}
	
	.search_area input {
		height: 40px;
 		width: 100%;
  		font-size: 14px;
 		padding: 7px 14px;
  		border: 1px solid #ccc;
	}
	
	.search_area input:focus {
		border-color: #333;
	 	outline: 0;
	  	border-width: 1px;
	}
	
	.btn_primary {
		 background:#4e73df;
	     color:white;
	  /*   display:block; */
	     
	     /*width:92.5%;
	     max-width:680px; */
	     height:25px;	/* 버튼 높이(크기) 조정 */
	     border-radius:8px;
	     /* margin:0 auto; 버튼 가운데 정렬*/
	     margin-right: 5px;   /* 버튼 사이 간격 조정 */
	     border:none;
	     cursor:pointer;
	     font-size:13px;
	     font-weight: 600;
	     font-family: 'Montserrat', sans-serif;
	     box-shadow:0 15px 30px rgba(#ffffff,.36);
	     transition:.2s linear;
	}
	
	.btn_primary:hover {
		background:#324b96;  /* RGB 70 115 163 */
	}
	
	.searchBox {
		/* width: 50%;  /* 원하는 너비 설정 */  */
		/* height: auto;  /* 높이값 초기화 */ */
		/* line-height : normal;  /* line-height 초기화 */ */
		/* padding: .8em .5em; /* 원하는 여백 설정, 상하단 여백으로 높이를 조절 */  */
		font-family: 'Montserrat', sans-serif;  /* 폰트 상속 */
		/* border: 1px solid #4673a3; */
		border-radius: 8px;  
		/* outline-style: none;  /* 포커스시 발생하는 효과 제거 필요시*/ */
		-webkit-appearance: none;  /* 브라우저별 기본 스타일링 제거 */
		-moz-appearance: none;
		appearance: none;
	}
	
	.selectBox {
		font-family: 'Montserrat', sans-serif;
		border-radius: 8px; 
		/* border-bottom: 1px dashed rgb(70, 115, 163) */;
		transition: .1s;
		
	}
	
	.recordPerPage {
		color: =#141e30;
		font-size:12px;
	
	}
	
	.noticList a:link {
		color : #324b96;
	}
	.noticList a:hover {
		color : #141e30;
	}
	.noticList a:visited {
		color : #8694c0;
	}
	
	.paging {
	    font-size:14px;
	    color: #324b96;
	}
	
	.flag {
	    color: #324b96;
	}
     
</style>
<script src="${contextPath}/resources/js/jquery-3.6.1.min.js"></script>

	<div class="noticbrd">
		<div class="brd_notic">
			<div class="noticHeader">
				<div class="notic_title">
					<h2><strong>전사 공지</strong></h2>
				</div>
					<div class="board_notic">
								<%-- <!-- (하단으로 이동 예정) -->
								<div>
									<button id="btn_write" class="btn_primary" onclick="location.href='${contextPath}/board/notic/write'">게시글작성</button>
									<button id="btn_first" class="btn_primary" onclick="location.href='${contextPath}/board/noticList'">첫페이지로</button>
								</div> --%>
						<!-- 배치 위치 조정 필요 -->
						<div class="board_notic_auth">
			                <div class="btn_auth">
			                	<!-- 직급권한만(대표이사, 이사, 부장) 보여지는 공지 등록/해제 버튼 -->
			                	 <c:if test="${ loginEmp.jobNo eq '1' || loginEmp.jobNo eq '2' }">  <!--  합친 후 수정 -->
				                    <button type="button" class="btn_primary" id="notNotic" style='float:right;'>공지해제</button>
				                    <button type="button" class="btn_primary" id="setNotic" style='float:right;'>공지등록</button>
			                     </c:if> 
			                </div>
			            </div>
						
								<%-- 하단으로 이동
								<div>
									<form id="frm_search" action="${contextPath}/board/notic/search">
										<select id="column" name="column" class="select">
											<option value="">::선택::</option>
											<option value="BOARD_TITLE">제목</option>
											<option value="EMP_NO">사원번호</option>
										</select>
										<span id="search_area">
											<input type="text" id="search_area" name="search_area" class="input searchBox" placeholder="검색어를 입력해주세요." >
										</span>
										<span>
											<input type="button" value="검색" id="btn_search" class="btn_primary">
											<input type="button" value="전체조회" id="btn_all" class="btn_primary"> 
										</span>
									</form>
								</div> --%>
						
						<div class="selectBox">							
							<select id="recordPerPage">
								<option value="5">5</option>
								<option value="10">10</option>
								<option value="15">15</option>
								<option value="20">20</option>
								<option value="25">25</option>
								<option value="30">30</option>
							</select>


<%-- 미로그인 시, 작성      <c:if test="${loginUser != null }">     합친 후 수정--%>
								<button id="btn_write" class="btn_primary" style='float:right;' onclick="location.href='${contextPath}/board/notic/write'">게시글작성</button>
<%--                        </c:if> --%>
								&nbsp;&nbsp;
								<button id="btn_first" class="btn_primary" style='float:right;' onclick="location.href='${contextPath}/board/noticList'">첫페이지로</button>

						</div>
						
						<hr>
					
	<!-- 				<script>	
					function checkAll(checkAll)  {
						  const checkboxes 
						       = document.getElementsByName('animal');
						  
						  checkboxes.forEach((checkbox) => {
						    checkbox.checked = checkAll.checked;
						  })
						}
					</script>	
						 -->
						
						
				
				<div class="noticList">
					<table class="noticBoard" align="center" id="noticBoardList">
						<thead class="brd_thead">
							<tr class="brd_thead_name">
								<th width="3%"><input type="checkbox" id="checkAll" name="chk" value="checkall" onclick="checkAll()"></th>
								<th width="7%">번호</th>
								<th width="40%">글제목</th>
								<th width="13%">아이디</th>
								<th width="13%">작성일</th>
								<th width="7%">조회수</th>
							</tr>
						</thead>
						<tbody class ="brd_tbody" id="result">
							<c:if test="${ empty noticBoardList }">
	                        		<tr>
	                        			<td colspan="6">등록된 게시글이 없습니다.</td>
	                        		</tr>
	                        	</c:if>
	                        	<c:forEach var="n" items="${noticBoardList}">
		                        	<c:if test="${ n.boardTop eq 'Y'}">
				                        <tr style="background:rgb(211, 220, 245)">	<!-- background 수정 예정  -->
				                            <td onclick="event.stopPropagation()"><input type="checkbox" class="checkcheck" name="chkNoticNo" value="${n.noticNo}"></td>
				                            <td class="noticNo">${ n.noticNo }</td>
				                            <td height="5%">
				                            	<%-- <c:if test="${loginUser.empNo != n.empNo}"> 합친 후 수정--%>
				                            		<i class="fa-solid fa-flag"></i>&nbsp;<a href="${contextPath}/board/notic/detail?noticNo=${ n.noticNo }">${ n.boardTitle }</a> <!-- 작성자는 조회수 증가x -->
				                            	<%-- <a href="${contextPath}/board/notic/incrase/hit?noticNo=${ n.noticNo }">${ n.boardTitle }</a> --%>
				                            	<%-- </c:if>
				                            	<c:if test="${loginUser.empNo != n.empNo}">
													<a href="${contextPath}//board/notic/incrase/hit?noticNo=${ n.noticNo }">${ n.boardTitle }(작성회원번호 ${ n.empNo })</a>
												</c:if> 합친 후 수정--%>
				                            </td>
				                            <td height="5%">${ n.empNo }</td>
				                            <td height="5%">${ n.createDate }</td>
				                            <td height="5%">${ n.hit }</td>
				                        </tr>
		                        	</c:if>
				                </c:forEach>
	                        	<c:forEach var="n" items="${noticBoardList}">
			                        <c:if test="${ n.boardTop eq 'N'}">
				                        <tr>
				                            <td onclick="event.stopPropagation()"><input type="checkbox" class="checkcheck" name="chkNoticNo" value="${n.noticNo}"></td>
				                            <td class="no">${ n.noticNo }</td>
				                            <td><a href="${contextPath}/board/notic/detail?noticNo=${ n.noticNo }">${ n.boardTitle }</a></td>
				                            <td>${ n.empNo }</td>
				                            <td>${ n.createDate }</td>
				                            <td>${ n.hit }</td>
				                        </tr>
			                        </c:if>
			                    </c:forEach>
	                    </tbody>
	                    
	     				<tfoot>
							<tr>
								<td colspan="6" id="paging" align="center">
									<hr>
									<div class="paging">${paging}</div>
								</td>
							</tr>
						</tfoot>
						
					</table>
				</div>
				
				
				<div style="margin:auto;text-align:center;" class="searchArea">
					<form style="display:inline-block;" id="frm_search" action="${contextPath}/board/notic/search">
						<select id="column" name="column" class="select">
							<option value="" align="center">::선택::</option>
							<option value="BOARD_TITLE">제목</option>
							<option value="EMP_NO">사원번호</option>
						</select>
						<span id="search_area">
							<input type="text" id="search_area" name="search_area" class="searchBox" placeholder="검색어를 입력해주세요." size=30>
						</span>
						<span>
							<input type="button" value="검색" id="btn_search" class="btn_primary">
							<input type="button" value="전체조회" id="btn_all" class="btn_primary"> 
						</span>
					</form>
				</div>
				
			</div>
		</div>
	</div>
	
	<script>
	$(function(){
		fn_allList();
		fn_write();
		fn_checkNotic();
		fn_searchList();
	})
	
		function fn_noticList(){
			var page;
			if($('#pageNo').val() == 0) {
				page = 1;
			} else {
				page = $('#pageNo').val();
			}
		
			$.ajax({	
				type : 'get',
				url : '${contextPath}/board/noticList',
				dataType: 'json',
				data : 'pageNo=' + page ,
				success: function(resData){
					
					var str = '' ;	
			 	    if(resData.beginPage != 1){
						str += '<a href=${contextPath}/board/noticList?pageNo=' + Number(resData.beginPage -1) + '>' + '◀' + '</a>';
					}  
				 	for(let p = resData.beginPage; p <= resData.endPage; p++){
					 	if(p == resData.page){
					 		str += '<strong>' + p + '</strong>' ;
						}  
						else {
							str += '<a href=${contextPath}/board/noticList?pageNo=' + p +'>'+ p + '</a>';
						} 
					} 
					if(resData.endPage != resData.totalPage) {
						str += '<a href=${contextPath}/board/noticList?pageNo=' + Number(resData.endPage + 1) + '>' + '▶' + '</a>';
						//console.log(str);
					}     
					 
					   
					 $('#paging').append(str);   	
				},
				error: function(jqXHR){
					alert('리스트 불러오기가 실패했습니다.');
				}
		})
	} 
	
	function fn_searchList(){
		$('#search_area').click(function (){
			$.ajax({
				type : 'get',
				url : '${contextPath}/board/notic/autoComplete',
				data : 'pageNo=' + ${pageNo} + '&column=' + $('#column').val() + '&query=' + $('#query').val(),
				dataType : 'json',
				success : function (resData){
					//console.log(resData)
					$('#paging').empty();
					$('#result').empty();
					
/* 					$.each(resData.noticList, function( i , noticList){
						$('<tr>')
						.append( $('<td class="content_line center_font">').text(noticList.noticNo) )
						.append( $('<td class="content_line center_font">').text(noticList.empNo) )
						.append( $('<td class="content_line">').append( $('<a>').text(noticList.boardTitle).attr('href' ,'${contextPath}/board/notic/incrase/hit?noticNo=' + noticList.noticNo ) ) )
						.append( $('<td class="content_line center_font">').text(noticList.createDate) )
						
						.append( $('<td class="content_line center_font">').text(noticList.hit) )
						.appendTo('#result');	
					
					})	 */
							
					var str = '' ;	
			 	    if(resData.beginPage != 1){
						str += '<a href=${contextPath}/board/noticList?pageNo=' + Number(resData.beginPage -1) + '>' + '◀' + '</a>';
					}  
				 	for(let p = resData.beginPage; p <= resData.endPage; p++){
					 	if(p == resData.page){
					 		str += '<strong>' + p + '</strong>' ;
						}  
						else {
							 str += '<a href=${contextPath}/board/notic/search?pageNo=' + p + '&column=' + resData.column + '&query=' + resData.query  + '>'+ p + '</a>'; 
						} 
					} 
					if(resData.endPage != resData.totalPage) {
						str += '<a href=${contextPath}/board/noticList?pageNo=' + Number(resData.endPage + 1) + '>' + '▶' + '</a>';
						//console.log(str);
					}    
					 
					   
					 $('#paging').append(str);   	
				},
				error: function(jqXHR){
					alert('리스트 불러오기에 실패했습니다.');
				
				} 
			})
		})		
	}
	
	function fn_allList(){
		$('#btn_all').click(function (){
			fn_noticList();	
		})
	}
	
	function fn_write(){
		$('#btn_write').click(function (){
			location.href = '${contextPath}/board/notic/write';
		})
	}
	
	// 페이징 처리
	$(function(){
		$('#btn_write').click(function (){
			location.href = '${contextPath}/board/notic/write';
		})
		
		$('#btn_first').click(function (){
			location.href = '${contextPath}/board/noticList';
		});
	})
		
	$(function(){	
		// 5개 목록씩 보기
		if('${recordPerPage}' != ''){
			$('#recordPerPage').val(${recordPerPage});			
		} else {
			$('#recordPerPage').val(5);
		}
		
		$('#recordPerPage').change(function(){
			location.href = '${contextPath}/board/noticList?recordPerPage=' + $(this).val();
		});
		
	});
	
	
	// 게시글 전체 선택
   	$("#noticBoardList").on("click", "#checkAll", function(){
   		$("input[id^=checkNo]").attr("selected", true);
   		if($("#checkAll").is(":checked")){
   			$("input[name=chkNoticNo]").prop("checked", true);
   		}else{
   			$("input[name=chkNoticNo]").prop("checked", false);
   		}
   	})
   	
   	// -----------체크박스 선택하면 값 가져오기-----------
   	$("input[name=chkNoticNo]").click(function(){
        var count = $("input[name='chkNoticNo']").length;
        var checked = $("input[name='chkNoticNo']:checked").length;
        
        // 체크한 체크박스의 개수와 전체 체크박스 개수가 같으면 전체 선택 체크박스가 체크된다.
        if(count != checked){
            $("#checkAll").prop("checked", false);
        } else{
            $("#checkAll").prop("checked", true);
        }
        
    });
   	
   	let checkList = "";
   	// 체크박스 선택시 값 가져오기
   	$("input[type=checkbox]").change(function(){
   		checkList = ""; // 여기서 한번 비워서 중복요소 제거
   		$("input:checkbox[name=chkNoticNo]:checked").each(function(){
   			checkList += ($(this).val()) + ",";
   		})
			//console.log(checkList);
			checkList = checkList.substring(0,checkList.lastIndexOf(",")); // 맨 뒤 콤마 불요
   	})
   	
   	// 공지 설정 ajax처리
	function fn_checkNotic(){
   		//console.log(checkList);
   		// isYN : 1 -> 공지 등록, 2-> 공지 해제
   		var noticNo = new Array();
   		$('.checkcheck').click(function(){
   			var no = $(this).parent().next().text();
   			noticNo.push(no);
   			//console.log(noticNo);
   		});
   		
        
   		$('#setNotic').click(function(){
	   		var objParam = {
	   				isYN : 'Y',
	   				"noticNo" : noticNo
	   		}
			$.ajax({
				url:"noticTop.no",
				data: objParam,
				success(result){
					alert("공지 등록 처리 되었습니다");
					location.reload();
				},error(){
					//console.log("ajax통신 실패");
				}
			})
   		})
   		
   		$('#notNotic').click(function(){
	   		var objParam = {
	   				isYN : 'N',
	   				"noticNo" : noticNo
	   		}
			$.ajax({
				url:"noticTop.no",
				data: objParam,
				success(result){
					alert("공지 해제 처리 되었습니다");
					location.reload();
				},error(){
					//console.log("ajax통신 실패");
				}
			})
   		})
   	}
   	
   	// searchBox 포커스
   	$(document).ready(function() {
   	  var placeholderTarget = $('.searchArea input[type="text"]');
   	  
   	  //포커스시
   	  placeholderTarget.on('focus', function(){
   	    $(this).siblings('label').fadeOut('fast');
   	  });

   	  //포커스아웃시
   	  placeholderTarget.on('focusout', function(){
   	    if($(this).val() == ''){
   	      $(this).siblings('label').fadeIn('fast');
   	    }
   	  });
   	});
	</script>
	
	<input type="hidden" id="pageNo" value="${pageNo}">
	
</div>
<%@ include file="../../layout/footer.jsp" %>