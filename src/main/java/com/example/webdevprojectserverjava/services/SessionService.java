package com.example.webdevprojectserverjava.services;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class SessionService {
	
	@GetMapping("/api/session/status")
	public int status(HttpSession session) {
		String userType = (String) session.getAttribute("type");
		if(userType == "ADMIN") {
			return 1;
		}
		else if(userType == "APPLICANT") {
			return 2;
		}
		else if(userType == "RECRUITER") {
			return 3;
		}
		return 0;
	}
	
	@PostMapping("/api/session/logout")
	public void logout(HttpSession session) {
		session.invalidate();
	}
}
