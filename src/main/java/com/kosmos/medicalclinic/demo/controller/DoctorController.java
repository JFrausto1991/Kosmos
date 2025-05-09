package com.kosmos.medicalclinic.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kosmos.medicalclinic.demo.dao.ApiResponse;
import com.kosmos.medicalclinic.demo.dao.Doctor;
import com.kosmos.medicalclinic.demo.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	@Autowired
	DoctorService doctorService;

	@PostMapping
	public ResponseEntity<ApiResponse<Doctor>> createDoctor(@Validated @RequestBody Doctor doctor) {
		Doctor savedDoctor = doctorService.save(doctor);
		ApiResponse<Doctor> response = new ApiResponse<>("success", "Doctor created successfully", savedDoctor);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<Doctor>>> getAll() {
		List<Doctor> doctors = doctorService.findAll();
		ApiResponse<List<Doctor>> response = new ApiResponse<>("success", "Doctors retrieved successfully", doctors);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-by-id")
	public ResponseEntity<ApiResponse<?>> getDoctorById(@RequestParam("id") Long id) {
		Optional<Doctor> doctor = doctorService.findById(id);

		if (doctor.isPresent()) {
			ApiResponse<Doctor> response = new ApiResponse<>("success", "Doctor retrieved successfully", doctor.get());
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Doctor not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

	}

	@PutMapping()
	public ResponseEntity<ApiResponse<String>> updateDoctor(@RequestParam("id") Long id, @RequestBody Doctor doctor) {
		Optional<Doctor> existingDoctorOpt = doctorService.findById(id);

		if (existingDoctorOpt.isPresent()) {
			doctorService.delete(id);
			ApiResponse<String> response = new ApiResponse<>("success", "Doctor deleted successfully", null);
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Doctor not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@DeleteMapping()
	public ResponseEntity<ApiResponse<String>> deleteDoctor(@RequestParam("id") Long id) {
		return doctorService.findById(id).map(existingDoctor -> {
			doctorService.delete(id);
			ApiResponse<String> response = new ApiResponse<>("success", "Doctor deleted successfully", null);
			return ResponseEntity.ok(response);
		}).orElseGet(() -> {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Doctor not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		});
	}

}
