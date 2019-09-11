package com.example.webdevprojectserverjava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.webdevprojectserverjava.models.Job;

public interface JobRepository extends CrudRepository <Job, String>{
	@Query("SELECT j FROM Job j WHERE LOWER(j.location) LIKE %:location% AND LOWER(j.title) LIKE %:title% AND LOWER(j.type) LIKE %:type% AND LOWER(j.company) LIKE %:company%")
    public List<Job> findJobsByLocationAndPosition(@Param("location") String location, @Param("title") String title, @Param("type") String type, @Param("company") String company);
}
