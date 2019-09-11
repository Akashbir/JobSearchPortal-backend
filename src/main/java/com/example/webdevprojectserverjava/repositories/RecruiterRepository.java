package com.example.webdevprojectserverjava.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.webdevprojectserverjava.models.Recruiter;

public interface RecruiterRepository extends CrudRepository <Recruiter, Integer>{
	public Recruiter findByUsername(String Username);
}
