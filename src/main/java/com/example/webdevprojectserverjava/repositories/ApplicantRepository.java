package com.example.webdevprojectserverjava.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.webdevprojectserverjava.models.Applicant;

public interface ApplicantRepository extends CrudRepository <Applicant, Integer>{
	public Applicant findByUsername(String Username);
}


