package com.kosmos.medicalclinic.demo.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kosmos.medicalclinic.demo.dao.ApiResponse;
import com.kosmos.medicalclinic.demo.dao.ConsultingRoom;
import com.kosmos.medicalclinic.demo.service.ConsultingRoomService;

@RestController
@RequestMapping("/api/consulting-rooms")
@RequiredArgsConstructor
public class ConsultingRoomController {

	@Autowired
	ConsultingRoomService consultingRoomService;

	@PostMapping
	public ResponseEntity<ApiResponse<ConsultingRoom>> createConsultingRoom(
			@RequestBody ConsultingRoom consultingRoom) {
		ConsultingRoom savedConsultingRoom = consultingRoomService.save(consultingRoom);
		ApiResponse<ConsultingRoom> response = new ApiResponse<>("success", "Consulting room created successfully",
				savedConsultingRoom);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/get-by-id")
	public ResponseEntity<ApiResponse<?>> getConsultingRoomById(@RequestParam("id") Long id) {
		Optional<ConsultingRoom> consultingRoom = consultingRoomService.findById(id);

		if (consultingRoom.isPresent()) {
			ApiResponse<ConsultingRoom> response = new ApiResponse<>("success",
					"Consulting room retrieved successfully", consultingRoom.get());
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Consulting room not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<ConsultingRoom>>> getAllConsultingRooms() {
		List<ConsultingRoom> consultingRooms = consultingRoomService.findAll();
		ApiResponse<List<ConsultingRoom>> response = new ApiResponse<>("success",
				"Consulting rooms retrieved successfully", consultingRooms);
		return ResponseEntity.ok(response);
	}

	@PutMapping()
	public ResponseEntity<ApiResponse<?>> updateConsultingRoom(@RequestParam("id") Long id,
			@RequestBody ConsultingRoom consultingRoom) {
		Optional<ConsultingRoom> existingConsultingRoomOpt = consultingRoomService.findById(id);

		if (existingConsultingRoomOpt.isPresent()) {
			ConsultingRoom existingConsultingRoom = existingConsultingRoomOpt.get();
			existingConsultingRoom.setRoomNumber(consultingRoom.getRoomNumber());
			existingConsultingRoom.setFloor(consultingRoom.getFloor());
			ConsultingRoom updatedConsultingRoom = consultingRoomService.save(existingConsultingRoom);
			ApiResponse<ConsultingRoom> response = new ApiResponse<>("success", "Consulting room updated successfully",
					updatedConsultingRoom);
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Consulting room not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@DeleteMapping()
	public ResponseEntity<ApiResponse<String>> deleteConsultingRoom(@RequestParam("id") Long id) {
		Optional<ConsultingRoom> existingConsultingRoomOpt = consultingRoomService.findById(id);

		if (existingConsultingRoomOpt.isPresent()) {
			consultingRoomService.delete(id);
			ApiResponse<String> response = new ApiResponse<>("success", "Consulting room deleted successfully", null);
			return ResponseEntity.ok(response);
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>("error", "Consulting room not found", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

}
