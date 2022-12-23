package com.example.application.data.service;

import com.example.application.data.entity.SampleAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SampleAddressRepository extends JpaRepository<SampleAddress, UUID> {

}