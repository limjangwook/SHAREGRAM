package com.group.sharegram.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import com.group.sharegram.user.domain.EmployeesDTO;
import com.group.sharegram.user.service.EmpService;

public class KeepLoginInterceptor implements HandlerInterceptor {

	@Autowired
	private EmpService empService;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmp") == null) {
			
			Cookie cookie = WebUtils.getCookie(request, "keepLogin");
			if(cookie != null) {
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sessionId", cookie.getValue());
				
				EmployeesDTO loginEmp = empService.getEmpBySessionId(map);
				if(loginEmp != null) {
					session.setAttribute("loginEmp", loginEmp);
				}
				
			}
			
		}
		
		return true;  
		
	}
	
}