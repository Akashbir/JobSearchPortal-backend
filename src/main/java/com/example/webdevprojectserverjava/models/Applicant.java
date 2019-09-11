package com.example.webdevprojectserverjava.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.example.webdevprojectserverjava.models.Job;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue("APPLICANT")
public class Applicant extends User {
	private String location;
	private String position;
	
	@ManyToMany
	@JoinTable(name="following", joinColumns = { @JoinColumn(name = "followerId", referencedColumnName="id") }, inverseJoinColumns = { @JoinColumn(name = "followingId", referencedColumnName="id") })
	@JsonIgnore
	private Set<Applicant> following = new HashSet<>();
	
	@ManyToMany(mappedBy = "following")
	@JsonIgnore
	private Set<Applicant> followedBy = new HashSet<>();
	
	public Set<Applicant> getFollowing() {
		return following;
	}
	public void setFollowing(Set<Applicant> following) {
		this.following = following;
	}
	public Set<Applicant> getFollowedBy() {
		return followedBy;
	}
	public void setFollowedBy(Set<Applicant> followedBy) {
		this.followedBy = followedBy;
	}
	@ManyToMany(mappedBy = "applicants")
	@JsonIgnore
	private List<Job> jobsApplied = new ArrayList<>();
	
	public List<Job> getJobsApplied() {
		return jobsApplied;
	}
	public void setJobsApplied(List<Job> jobsApplied) {
		this.jobsApplied = jobsApplied;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
}
