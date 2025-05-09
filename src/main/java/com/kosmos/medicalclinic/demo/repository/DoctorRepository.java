package com.kosmos.medicalclinic.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosmos.medicalclinic.demo.dao.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
