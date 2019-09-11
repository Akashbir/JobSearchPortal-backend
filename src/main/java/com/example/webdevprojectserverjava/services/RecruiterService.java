package com.example.webdevprojectserverjava.services;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.example.webdevprojectserverjava.repositories.JobRepository;
import com.example.webdevprojectserverjava.repositories.RecruiterRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class RecruiterService {

	@Autowired
	private RecruiterRepository rRepo;
	
	@Autowired
	private JobRepository jRepo;
	
	@PostMapping("/api/recruiter/register")
	public Recruiter createRecruiter(@RequestBody Recruiter user,HttpSession session, HttpServletResponse response) {
		for(Recruiter u: rRepo.findAll()) {
			if(u.getUsername().equals(user.getUsername())) {
				response.setStatus(204);
				return null;
			}
		}
		
		rRepo.save(user);
		Recruiter loggedUser = this.getUserByUsername(user.getUsername());
		session.setAttribute("loggedIn", user.getUsername());
		session.setAttribute("type", "RECRUITER");
		response.setStatus(200);
		return loggedUser;
	}
	
	public Recruiter getUserByUsername(String uname) {
		return rRepo.findByUsername(uname);
	}
	
	@PostMapping("/api/recruiter/login")
	public Recruiter login(@RequestBody Recruiter user_details, HttpSession session, HttpServletResponse response) {
		for(Recruiter u: rRepo.findAll()) {
			if(user_details.getUsername().equals(u.getUsername())
					&& user_details.getPassword().equals(u.getPassword())
					) {
				session.setAttribute("loggedIn", user_details.getUsername());
				session.setAttribute("type", "RECRUITER");
				response.setStatus(200);
				return u;
			}
		}
		response.setStatus(204);
		return null;
	}
	
	@PostMapping("/api/recruiter/logout")
	public void logout(HttpSession session) {
		session.invalidate();
	}
	
	@GetMapping("/api/recruiter/profile")
	public Recruiter profile(HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Recruiter user = this.getUserByUsername(username);
		return user;
	}
	
	@PostMapping("/api/recruiter/job")
	public Job createJobOpening(@RequestBody Job j, HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Recruiter user = this.getUserByUsername(username);
		
		if(user != null) {
			j.setRecruiter(user);
			jRepo.save(j);
			List<Job> jobs = user.getJobs();
			jobs.add(j);
			user.setJobs(jobs);
			rRepo.save(user);
			return j;
		}
		
		return null;
	}
	
	@GetMapping("/api/recruiter/job")
	public List<Job> getJobByRecruiter(HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Recruiter user = this.getUserByUsername(username);
		
		if(user != null) {
			List<Job> jobs = user.getJobs();
			return jobs;
		}
		return null;
	}
	
	@PutMapping("/api/recruiter/{userId}")
	public Recruiter updateUser(@PathVariable("userId") Integer id, @RequestBody Recruiter user) {
		rRepo.save(user);
		return user;
	}
	
	@GetMapping("/api/recruiter")
	public List<Recruiter> getAllRecruiters(HttpSession session) {
		List<Recruiter> listUsers = new ArrayList<>();	
		for(Recruiter r: rRepo.findAll()) {
			listUsers.add(r);
		}
		return listUsers;
	}
	
	
	@GetMapping("/api/recruiter/interestedApplicants")
	public List<Applicant> getInterestedApplicants(HttpSession session) {
		
		Set<Applicant> setUsers = new HashSet<>();	
		for(Job j: this.getJobByRecruiter(session)) {
			for(Applicant a: j.getApplicants())
				setUsers.add(a);
		}
		List<Applicant> listUsers = new ArrayList<>();
		listUsers.addAll(setUsers);
		return listUsers;
	}
	
}
