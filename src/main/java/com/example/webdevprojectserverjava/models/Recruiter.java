package com.example.webdevprojectserverjava.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("RECRUITER")
public class Recruiter extends User{
	
	@OneToMany(mappedBy="recruiter", orphanRemoval = true)
	private List<Job> jobs = new ArrayList<>();

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	} 
}
