<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

	<jsp:include page="../layout/header.jsp">
		<jsp:param value="SHAREGRAM" name="title" />
	</jsp:include>
	
<script src="${contextPath}/resources/js/moment-with-locales.js"></script>
<script src="${contextPath}/resources/summernote-0.8.18-dist/summernote-lite.js"></script>
<script src="${contextPath}/resources/summernote-0.8.18-dist/lang/summernote-ko-KR.min.js"></script>
<link rel="stylesheet" href="${contextPath}/resources/summernote-0.8.18-dist/summernote-lite.css">
</head>
<style>

}

span {

 height: 40px;
 width: 400px;
 border: 1px solid #1b5ac2;
 background: #ffffff;

}
 
body {
  margin:0;
  padding:0;
  font-family: sans-serif;
  background-color: #fff;
}

table {
  border: 1px #fff solid;
  font-size: .9em;
  box-shadow: 0 2px 5px rgba(0,0,0,.25);
  width: 97%;
  border-collapse: collapse;
  border-radius: 5px;
  overflow: hidden;
  margin-left:auto; 
  margin-right:auto;

}

th {
  text-align: left;
}
  
thead {
  font-weight: bold;
  color: #fff;
  background: #4e73df;
}
  
 td, th {
  padding: 1em .5em;
  vertical-align: middle;
  text-align: center;
}
  
 td {
  border-bottom: 1px solid rgba(0,0,0,.1);
  background: #fff;
}

a {
  color: #73685d;
}
  
 @media all and (max-width: 768px) {
    
  table, thead, tbody, th, td, tr {
    display: block;
    min-width: 300px;
  }
  
  th {
    text-align: right;
  }
  
  table {
    position: relative; 
    padding-bottom: 0;
    border: none;
    box-shadow: 0 0 10px rgba(0,0,0,.2);
    
  }
  
  thead {
    float: left;
    white-space: nowrap;
    color:fff;
  }
  
  tbody {
    overflow-x: auto;
    overflow-y: hidden;
    position: relative;
  }
  
  tr {
    display: inline-block;
    vertical-align: top;
  }
  
  th {
    border-bottom: 1px solid #4e73df;
  }
  
  td {
    border-bottom: 1px solid #4e73df;
  }
 
 

 

</style>

<script>
$(function(){
	fn_leave();
});


	$(document).ready(function(){
		// area1, area2 ??????
		// ?????? ?????? : area1, area2 ??? ??? ??????
		$('#area1, #area2').css('display', 'none');
		
		// column ????????? ?????? area1, area2 ??????
		$('#column').change(function() {     // #column == this
			let combo = $(this);
			if(combo.val() == '') {
				$('#area1, #area2').css('display', 'hidden');
			} else if(combo.val() == 'JOIN_DATE' || combo.val() == 'SALARY') {
				$('#area1').css('display', 'none');
				$('#area2').css('display', 'inline');
			} else {
				$('#area1').css('display', 'inline');
				$('#area2').css('display', 'none');
			}
		})
		

		$('#btn_all').click(function() {
			location.href = '${contextPath}/user/list';
		})
		
		fn_detail();
	})
	
	let employeesNo = 0;
	function fn_detail(){
	$(document).on('click', '.btn_detail', function(){
		$('#exampleModal').modal('toggle');
		employeesNo = $(this).data('emp_no'); 
	});
}
	
	
function fn_leave(){
		$('#btn_retire').click(function() {
			if(confirm('?????? ?????????????????????????')){
				$('#frm_retire').attr('action', '${contextPath}/user/retire?empNo=' + employeesNo );
				$('#frm_retire').submit();
			}else {
				alert('?????? ????????? ??????????????????.');
			}
		});
	}	
	
</script>
<body>



<!-- Button trigger modal -->
<!-- Button trigger modal -->

<!-- Modal -->
<div class="modal fade exampleModal" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" >
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">?????? ?????????</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      	<table id="myInfo">
      	
      	<tr>
      		<th>
      			<td>
      				<img src="${contextPath}/resources/images/defaultProfile.png" width="200px">
				</td>
		</tr>
					
		<tr>
		
		<tr>
			<th style="width:20%;">??????</th>
			<td>?????????3</td>
		</tr>
		
		<tr>
			<th>????????????</th>
			<td>23010011</td>
		</tr>
		
		<tr>
			<th style="width:30%;">????????????</th>
			<td>010-5548-7951</td>
		</tr>
		
		<tr>
			<th style="width:30%;">??????</th>
			<td>???????????????</td>
		</tr>
		
		<tr>
			<th style="width:30%;">??????</th>
			<td>??????</td>
		</tr>
		
		<tr>
			<th>?????????</th>
			<td>2023-01-02</td>
		</tr> 
	
	</table>
      

		<div>
			<form id="frm_retire"  method="post">
				<button type="submit"  id="btn_retire" class="btn btn-danger">??????</button>
			</form>
		</div>

      </div>
      <div class="modal-footer">
     
        <!-- <button type="button" onclick="fn_init()"> ????????? </button> -->
        <button type="button" class="btn btn-secondary" data-dismiss="modal">??????</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal" id="btn_modify">?????? ??????</button>
      </div>
    </div>
  </div>
</div> 

	<div>
		<h3>?????? ??????</h3>
		<hr>
	<!-- 
		<selcet name="column">
		<input name="query">     -> ??? ????????? ???????????? DB??? ?????? ??? ???
		
		 /emp/search??? ????????? ???????????? 4??? = column, query, start, stop
	-->
		<form id="frm_search" action="${contextPath}/user/search">
			<select id="column" name="column">  <!-- submit??? ??? ???????????? ?????? name???! -->
				<option value="">:::??????:::</option>   <!-- ??????????????? value????????? -->
				<option value="EMP_NO">????????????</option>     
				<option value="DEPT_NAME">?????????</option> 
				<option value="JOB_NAME">??????</option>
				<option value="NAME">??????</option>   	
				<option value="PHONE">?????????</option> 
				<option value="JOIN_DATE">?????????</option>
			</select>
			<span id="area1">
				<input type="text" id="query" name="query">
			</span>
			
			<span id="">
				<input type="submit" value="??????">
				<input type="button" value="???????????? ??????" id="btn_all">
			</span>
		</form>
		
		<!-- /emp/search??? ????????? ???????????? 4??? = column, query, start, stop -->
	</div>
	
	<hr>

	<div>
		<table border="1">
			<thead>
				<tr>
					<th>No.</th>
					<th>????????????</th>
					<th>?????????</th>
					<th>????????????</th>
					<th>?????????</th>
					<th>??????</th>
					<th>????????????</th>
					<th>??????</th>
					<th>??????</th>
					<!-- ?????????(departmentName) ????????? ????????? ?????????. ??????: pk??? ?????? ????????? fk??? departmentId??? ??????~ -->
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${emp}" var="emp" varStatus="vs">  <!-- varStatus : 0?????? ???????????? index ?????? ?????? ??????! ??????vs?????? index ???????????? -->
					<tr>
						<td>${beginNo - vs.index}</td>  <!-- 107, 106, 105 ... ?????? ???????????? -->
						<td>${emp.empNo}</td>
						<td>${emp.name}</td>
						<td>${emp.phone}</td>
						<td>${emp.departmentsDTO.deptName}</td>	
						<td>${emp.positionDTO.jobName}</td>	
						<td>${emp.joinDate}</td>
						<td>${emp.salary}</td>
					    <td><input type="button" value="??????" class="btn_detail" data-emp_no="${emp.empNo}"></td>
					</tr>
				
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="10">
					${paging}
					
					
	
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
	
<%@ include file="../layout/footer.jsp" %>
