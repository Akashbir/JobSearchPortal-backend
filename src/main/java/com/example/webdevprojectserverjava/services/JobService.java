package com.example.webdevprojectserverjava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.webdevprojectserverjava.models.Applicant;
import com.example.webdevprojectserverjava.models.Job;
import com.example.webdevprojectserverjava.models.Recruiter;
import com.example.webdevprojectserverjava.repositories.ApplicantRepository;
import com.example.webdevprojectserverjava.repositories.JobRepository;
import com.example.webdevprojectserverjava.repositories.RecruiterRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class JobService {
	@Autowired
	private JobRepository jRepo;
	
	@Autowired
	private ApplicantRepository aRepo;
	
	@Autowired
	private RecruiterRepository rRepo;
	
//	public Job createJob(Recruiter r, Job j) {
//		j.setRecruiter(r);
//		jRepo.save(j);
//		return j;
//	}
	
	@PostMapping("/api/job/apply")
	public Job applyToJob(@RequestBody Job j, HttpSession session, HttpServletResponse response) {
		Optional<Job> data = jRepo.findById(j.getId());
		String username = (String) session.getAttribute("loggedIn");
		Applicant ap = aRepo.findByUsername(username);
		
		if(!data.isPresent()) {
			jRepo.save(j);
		}
		
		if(data.isPresent() && ap != null) {
			Job j1 = data.get();
			List<Applicant> applicants = j1.getApplicants();
			if(!applicants.contains(ap)) {
				applicants.add(ap);
				j1.setApplicants(applicants);
				jRepo.save(j1);
				response.setStatus(200);
				return j1;
			}
			response.setStatus(204);
			return j1;
		}
		
		return null;
	}
	
	@GetMapping("/api/job/all")
	public List<Job> getAllJobs(HttpSession session) {
		List<Job> listJobs = new ArrayList<>();
		for(Job j: jRepo.findAll()) {
			listJobs.add(j);
		}
		return listJobs;
	}
	
	@GetMapping("/api/job")
	public List<Job> getJobsByLocationAndPostion(@RequestParam(name = "location", defaultValue = "") String location, @RequestParam(name = "title", defaultValue = "") String title, @RequestParam(name = "type", defaultValue = "") String type, @RequestParam(name = "company", defaultValue = "") String company, HttpSession session) {
		return jRepo.findJobsByLocationAndPosition(location.toLowerCase(), title.toLowerCase(), type.toLowerCase(), company.toLowerCase());
	}
	
	@GetMapping("/api/job/{jobId}/applicant")
	public List<Applicant> getapplicantsForJob(@PathVariable("jobId") String id, HttpSession session) {
		Optional<Job> data = jRepo.findById(id);
		if(data.isPresent()) {
			Job j1 = data.get();
			return j1.getApplicants();
		}
		return null;
	}
	
	@DeleteMapping("/api/job/{jobId}")
	public void deleteJobById(@PathVariable("jobId") String id, HttpSession session) {
		if(jRepo.existsById(id))
			jRepo.deleteById(id);
	}
	
	@PutMapping("/api/job")
	public void updateJobById(@RequestBody Job j, HttpSession session) {
		String username = (String) session.getAttribute("loggedIn");
		Recruiter rec = rRepo.findByUsername(username);
		j.setRecruiter(rec);
		jRepo.save(j);
	}
	
}
