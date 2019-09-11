package com.example.webdevprojectserverjava.services;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.webdevprojectserverjava.models.Admin;
import com.example.webdevprojectserverjava.repositories.AdminRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class AdminService {

	@Autowired
	private AdminRepository aRepo;
	
	@PostMapping("/api/admin/register")
	public Admin createAdmin(@RequestBody Admin user,HttpSession session, HttpServletResponse response) {
		for(Admin u: aRepo.findAll()) {
			if(u.getUsername().equals(user.getUsername())) {
				response.setStatus(204);
				return null;
			}
		}
		
		aRepo.save(user);
		Admin loggedUser = this.getUserByUsername(user.getUsername());
		session.setAttribute("loggedIn", user.getUsername());
		session.setAttribute("type", "ADMIN");
		response.setStatus(200);
		return loggedUser;
	}
	
	public Admin getUserByUsername(String uname) {
		return aRepo.findByUsername(uname);
	}
	
	@PostMapping("/api/admin/login")
	public Admin login(@RequestBody Admin user_details, HttpSession session, HttpServletResponse response) {
		for(Admin u: aRepo.findAll()) {
			if(user_details.getUsername().equals(u.getUsername())
					&& user_details.getPassword().equals(u.getPassword())
					) {
				session.setAttribute("loggedIn", user_details.getUsername());
				session.setAttribute("type", "ADMIN");
				response.setStatus(200);
				return u;
			}
		}
		response.setStatus(204);
		return null;
	}
	
	@PostMapping("/api/admin/logout")
	public void logout(HttpSession session) {
		session.invalidate();
	}
	
	@GetMapping("/api/admin/profile")
	public Admin profile(HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Admin user = this.getUserByUsername(username);
		return user;
	}
	
}
