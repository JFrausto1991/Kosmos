package com.kosmos.medicalclinic.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.kosmos.medicalclinic.demo.dao.ApiResponse;
import com.kosmos.medicalclinic.demo.dao.Appointment;
import com.kosmos.medicalclinic.demo.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping
	public ResponseEntity<ApiResponse<?>> createAppointment(@Validated @RequestBody Appointment appointment) {
		Appointment saved = appointmentService.save(appointment);
		ApiResponse<Appointment> response = new ApiResponse<>("success", "Appointment created successfully", saved);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/get-by-id")
	public ResponseEntity<ApiResponse<?>> getAppointmentById(@RequestParam("id") Long id) {
		Optional<Appointment> appointment = appointmentService.findById(id);

		if (appointment.isPresent()) {
			ApiResponse<Appointment> response = new ApiResponse<>("success", "Appointment retrieved successfully",
					appointment.get());
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Appointment not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@GetMapping("")
	public ResponseEntity<ApiResponse<List<Appointment>>> getAppointments(
			@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
			@RequestParam(name = "doctorId", required = false) Long doctorId,
			@RequestParam(name = "roomId", required = false) Long roomId) {
		List<Appointment> appointments = appointmentService.getAppointments(date, doctorId, roomId);
		ApiResponse<List<Appointment>> response = new ApiResponse<>("success", "Appointments retrieved successfully",
				appointments);
		return ResponseEntity.ok(response);
	}

	@PutMapping()
	public ResponseEntity<ApiResponse<Appointment>> updateAppointment(@RequestParam("id") Long id,
			@Validated @RequestBody Appointment appointment) {
		Appointment updated = appointmentService.updateAppointment(id, appointment);
		ApiResponse<Appointment> response = new ApiResponse<>("success", "Appointment updated successfully", updated);
		return ResponseEntity.ok(response);

	}

	@PatchMapping("/cancel")
	public ResponseEntity<ApiResponse<String>> cancelAppointment(@RequestParam("id") Long id) {
		appointmentService.cancelAppointment(id);
		ApiResponse<String> response = new ApiResponse<>("success", "Appointment cancelled successfully", null);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping()
	public ResponseEntity<ApiResponse<String>> deleteAppointment(@RequestParam("id") Long id) {
		Optional<Appointment> existingAppointmentOpt = appointmentService.findById(id);

		if (existingAppointmentOpt.isPresent()) {
			appointmentService.delete(id);
			ApiResponse<String> response = new ApiResponse<>("success", "Appointment deleted successfully", null);
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Appointment not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

}
