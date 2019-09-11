package com.example.webdevprojectserverjava.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.webdevprojectserverjava.models.Admin;

public interface AdminRepository extends CrudRepository <Admin, Integer> {
	public Admin findByUsername(String Username);
}
