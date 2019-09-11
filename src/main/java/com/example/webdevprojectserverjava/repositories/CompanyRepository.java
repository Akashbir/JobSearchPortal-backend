package com.example.webdevprojectserverjava.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.webdevprojectserverjava.models.Company;

public interface CompanyRepository extends CrudRepository <Company, Integer>{

}
