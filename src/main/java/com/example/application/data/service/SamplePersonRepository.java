package com.example.application.data.service;

import com.example.application.data.entity.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

}