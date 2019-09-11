package com.example.webdevprojectserverjava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.webdevprojectserverjava.models.Applicant;
import com.example.webdevprojectserverjava.models.Job;
import com.example.webdevprojectserverjava.models.Recruiter;
import com.example.webdevprojectserverjava.models.User;
import com.example.webdevprojectserverjava.repositories.ApplicantRepository;
import com.example.webdevprojectserverjava.repositories.JobRepository;
import com.example.webdevprojectserverjava.repositories.RecruiterRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class ApplicantService {
	
	@Autowired
	private ApplicantRepository aRepo;
	
	@Autowired
	private JobRepository jRepo;
	

	@PostMapping("/api/applicant/register")
	public Applicant createApplicant(@RequestBody Applicant user,HttpSession session, HttpServletResponse response) {
		for(Applicant u: aRepo.findAll()) {
			if(u.getUsername().equals(user.getUsername())) {
				response.setStatus(204);
				return null;
			}
		}
		
		aRepo.save(user);
		Applicant loggedUser = this.getUserByUsername(user.getUsername());
		session.setAttribute("loggedIn", user.getUsername());
		session.setAttribute("type", "APPLICANT");
		response.setStatus(200);
		return loggedUser;
	}
	
	public Applicant getUserByUsername(String uname) {
		return aRepo.findByUsername(uname);
	}

	@GetMapping("/api/applicant/username/{username}")
	public Applicant getUserByUsernameService(@PathVariable("username") String username, HttpSession session) {
		return aRepo.findByUsername(username);
	}
	
	@PostMapping("/api/applicant/login")
	public Applicant login(@RequestBody Applicant user_details, HttpSession session, HttpServletResponse response) {
		for(Applicant u: aRepo.findAll()) {
			if(user_details.getUsername().equals(u.getUsername())
					&& user_details.getPassword().equals(u.getPassword())
					) {
				session.setAttribute("loggedIn", user_details.getUsername());
				session.setAttribute("type", "APPLICANT");
				response.setStatus(200);
				return u;
			}
		}
		response.setStatus(204);
		return null;
	}
	
	@PostMapping("/api/applicant/logout")
	public void logout(HttpSession session) {
		session.invalidate();
	}
	
	@GetMapping("/api/applicant/profile")
	public Applicant profile(HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Applicant user = this.getUserByUsername(username);
		return user;
	}
	
	@GetMapping("/api/applicant/status")
	public boolean status(HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		if(username != null) {
			return false;
		}
		return true;
	}
	
	@PutMapping("/api/applicant/{userId}")
	public Applicant updateUser(@PathVariable("userId") Integer id, @RequestBody Applicant user) {
		aRepo.save(user);
		return user;
	}
	
	@GetMapping("/api/applicant/jobs")
	public List<Job> getJobs(HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Applicant user = this.getUserByUsername(username);
		List<Job> listJobs = new ArrayList<>();
		for(Job j: jRepo.findAll()) {
			if(j.getApplicants().contains(user)) {
				listJobs.add(j);
			}
		}
		return listJobs;
		
	}

	@GetMapping("/api/applicant/{username}/jobs")
	public List<Job> getJobsForApplicant(@PathVariable("username") String username, HttpSession session) {
		Applicant user = this.getUserByUsername(username);
		List<Job> listJobs = new ArrayList<>();
		for(Job j: jRepo.findAll()) {
			if(j.getApplicants().contains(user)) {
				listJobs.add(j);
			}
		}
		return listJobs;
	}
	
	@GetMapping("/api/applicant")
	public List<Applicant> getAllApplicants(HttpSession session) {
		List<Applicant> listUsers = new ArrayList<>();
		for(Applicant a: aRepo.findAll()) {
			listUsers.add(a);
		}
		return listUsers;
	}
	
	@PostMapping("/api/applicant/follow/{username}")
	public boolean followUser(@PathVariable("username") String username, HttpSession session) {
		String usernameLoggedIn = (String) session.getAttribute("loggedIn");
		Applicant userLoggedIn = this.getUserByUsername(usernameLoggedIn);
		
		Applicant userFollowed = this.getUserByUsername(username);
		
		if(userLoggedIn != null && userFollowed != null) {
			Set<Applicant> u = userLoggedIn.getFollowing();
			u.add(userFollowed);
			userLoggedIn.setFollowing(u);
			aRepo.save(userLoggedIn);
			return true;
		}
		return false;
	}
	
	@PostMapping("/api/applicant/unfollow/{username}")
	public boolean unfollowUser(@PathVariable("username") String username, HttpSession session) {
		String usernameLoggedIn = (String) session.getAttribute("loggedIn");
		Applicant userLoggedIn = this.getUserByUsername(usernameLoggedIn);
		
		Applicant userFollowed = this.getUserByUsername(username);
		
		if(userLoggedIn != null && userFollowed != null) {
			Set<Applicant> u = userLoggedIn.getFollowing();
			if(u.contains(userFollowed)) {
				u.remove(userFollowed);
			}
			userLoggedIn.setFollowing(u);
			aRepo.save(userLoggedIn);
			return true;
		}
		return false;
	}
	
	@GetMapping("/api/applicant/following/{username}")
	public List<Applicant> getFollowing(@PathVariable("username") String username,HttpSession session) {
		Applicant user = this.getUserByUsername(username);
		if(user != null) {
			List<Applicant> users = new ArrayList<>();
			users.addAll(user.getFollowing());
			return users;
		}
		return null;
	}
	
	@GetMapping("/api/applicant/followed/{username}")
	public List<Applicant> getFollowed(@PathVariable("username") String username,HttpSession session) {
		Applicant user = this.getUserByUsername(username);
		if(user != null) {
			List<Applicant> users = new ArrayList<>();
			users.addAll(user.getFollowedBy());
			return users;
		}
		return null;
	}
	
	@GetMapping("/api/applicant/isFollowing/{username}")
	public boolean isFollowing(@PathVariable("username") String username,HttpSession session) {
		Applicant user = this.getUserByUsername(username);
		String usernameLoggedIn = (String) session.getAttribute("loggedIn");
		Applicant userLoggedIn = this.getUserByUsername(usernameLoggedIn);
		if(userLoggedIn != null && user != null) {
			if(userLoggedIn.getFollowing().contains(user)) {
				return true;
			}
		}
		return false;
	}
	
}
