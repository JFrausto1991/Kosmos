package com.kosmos.medicalclinic.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kosmos.medicalclinic.demo.dao.Doctor;
import com.kosmos.medicalclinic.demo.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

	private final DoctorRepository doctorRepository;

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
    
    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor update(Long id, Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        existingDoctor.setFirstName(doctor.getFirstName());
        existingDoctor.setLastName(doctor.getLastName());
        existingDoctor.setSpecialty(doctor.getSpecialty());
        return doctorRepository.save(existingDoctor);
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }
    
}
