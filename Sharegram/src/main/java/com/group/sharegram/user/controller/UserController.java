package com.group.sharegram.user.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.group.sharegram.user.service.EmpService;

@Controller
public class UserController {
	
	@Autowired
	private EmpService empService;
	
	
	
	@GetMapping("/")
	public String login() {
		return "login";
	}
	
	@GetMapping("/main")
	public String main() {
		return "main";
	}
	
	
	@GetMapping("/user/join/write")
	public String joinWrite() {
		return "user/join";
	}
	
	@PostMapping("/user/join")
	public void join(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) {
//		System.out.println("================================================");
//		System.out.println(multipartRequest);
		empService.join(multipartRequest, response);
	}
	
	
	@GetMapping("/user/login/form")
	public String loginForm(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getHeader("referer"));
		return "user/login";
	}
	
	@PostMapping("/user/login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		empService.login(request, response);
		
	}
	
	@GetMapping("/user/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		empService.logout(request, response);
		return"redirect:/";
	}
	
	@GetMapping("/user/check/form")
	public String requiredLogin_checkForm() {
		return "user/check";
	}
	
	@ResponseBody
	@PostMapping(value="user/check/pw", produces = "application/json")
	public Map<String, Object> requiredLogin_checkPw(HttpServletRequest request) {
		return empService.confirmPw(request);
	}
	
	@GetMapping("/user/mypage")
	public String requiredLogin_mypage() {
		return "user/mypage";
	}
	
	
	@PostMapping("/user/modify/pw")
	public void requiredLogin_modifyPw(HttpServletRequest request, HttpServletResponse response) {
		empService.modifyMyPassword(request, response);
	}
	
	@PostMapping("/user/modify/info")
	public void modifyInfo(HttpServletRequest request, HttpServletResponse response) {
		empService.modifyMyinfo(request, response);
	}
	
	
	@PostMapping("/user/retire")
	 public void retire(HttpServletRequest request,  HttpServletResponse response) { 
		 empService.retire(request,response); 
	 }
	 
	 
	 
	
	@GetMapping("/user/list") 													 // ??????????????? ????????? ?????? ????????? ???????????? ??? ??????. (?????? ?????? /??? ?????????, ????????? ??????)
	public String list(HttpServletRequest request, Model model) {
		empService.findAllEmp(request, model);
		return "user/list";  											// ???????????? ?????? / ??? ?????????, ??? ????????? ?????????
	}
	
	@GetMapping("/user/search")
	public String search(HttpServletRequest request, Model model) {
		empService.findEmp(request, model);
		return "user/list";	// ????????? list.jsp????????? ????????????
	}
	
	
	@GetMapping("/user/retirelist")  														// ??????????????? ????????? ?????? ????????? ???????????? ??? ??????. (?????? ?????? /??? ?????????, ????????? ??????)
	public String retirelist(HttpServletRequest request, Model model) {
		empService.findAllRetire(request, model);
		return "user/retirelist";  														// ???????????? ?????? / ??? ?????????, ??? ????????? ?????????
	}
	
	@GetMapping("/user/reitresearch")
	public String retiresearch(HttpServletRequest request, Model model) {
		empService.findRetire(request, model);
		return "user/retirelist";	// ????????? list.jsp????????? ????????????
	}
	
	
	
	
	
	
	
	
}


	

