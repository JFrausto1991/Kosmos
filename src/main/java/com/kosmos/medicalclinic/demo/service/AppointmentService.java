package com.kosmos.medicalclinic.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kosmos.medicalclinic.demo.dao.Appointment;
import com.kosmos.medicalclinic.demo.dao.ConsultingRoom;
import com.kosmos.medicalclinic.demo.dao.Doctor;
import com.kosmos.medicalclinic.demo.enums.AppointmentStatus;
import com.kosmos.medicalclinic.demo.repository.AppointmentRepository;
import com.kosmos.medicalclinic.demo.repository.ConsultingRoomRepository;
import com.kosmos.medicalclinic.demo.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	ConsultingRoomRepository consultingRoomRepository;

	public Appointment save(Appointment appointment) {
		validateAppointment(appointment);
		appointment.setEndTime(appointment.getStartTime().plusHours(1));
		appointment.setStatus(AppointmentStatus.ACTIVE);
		return appointmentRepository.save(appointment);
	}

	public Optional<Appointment> findById(Long id) {
		return appointmentRepository.findById(id);
	}
	
	public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

	public Appointment updateAppointment(Long id, Appointment updated) {
		Appointment existing = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

		if (existing.getStatus() == AppointmentStatus.CANCELLED) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update a cancelled appointment");
		}

		updated.setId(id);
		validateAppointment(updated);
		updated.setEndTime(updated.getStartTime().plusHours(1));
		updated.setStatus(AppointmentStatus.ACTIVE);
		return appointmentRepository.save(updated);
	}

	public void cancelAppointment(Long id) {
		Appointment existing = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

		if (existing.getStartTime().isBefore(LocalDateTime.now())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel a past appointment");
		}

		existing.setStatus(AppointmentStatus.CANCELLED);
		appointmentRepository.save(existing);
	}

	public List<Appointment> getAppointments(LocalDate date, Long doctorId, Long roomId) {
		
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.atTime(LocalTime.MAX);

		if (doctorId != null) {
			Doctor doctor = doctorRepository.findById(doctorId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
			return appointmentRepository.findByStartTimeBetweenAndDoctor(start, end, doctor);
		}

		if (roomId != null) {
			ConsultingRoom room = consultingRoomRepository.findById(roomId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
			return appointmentRepository.findByStartTimeBetweenAndConsultingRoom(start, end, room);
		}

		return appointmentRepository.findByStartTimeBetween(start, end);
	}

	public void delete(Long id) {
		doctorRepository.deleteById(id);
	}

	private void validateAppointment(Appointment a) {
		LocalDateTime start = a.getStartTime();
		LocalDateTime end = start.plusHours(1);
		LocalDate date = start.toLocalDate();

		List<Appointment> roomConflicts = appointmentRepository
				.findByConsultingRoomAndStartTime(a.getConsultingRoom(), start).stream()
				.filter(ap -> ap.getStatus() == AppointmentStatus.ACTIVE).toList();

		if (!roomConflicts.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is already booked at this time");
		}

		List<Appointment> doctorConflicts = appointmentRepository.findByDoctorAndStartTime(a.getDoctor(), start)
				.stream().filter(ap -> ap.getStatus() == AppointmentStatus.ACTIVE).toList();

		if (!doctorConflicts.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor is already booked at this time");
		}

		LocalDateTime dayStart = date.atStartOfDay();
		LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

		List<Appointment> patientSameDay = appointmentRepository
				.findByPatientNameAndStartTimeBetween(a.getPatientName(), dayStart, dayEnd).stream()
				.filter(ap -> ap.getStatus() == AppointmentStatus.ACTIVE).toList();

		for (Appointment other : patientSameDay) {
			if (Math.abs(start.until(other.getStartTime(), java.time.temporal.ChronoUnit.MINUTES)) < 120) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Patient already has an appointment within 2 hours");
			}
		}

		List<Appointment> dailyAppointments = appointmentRepository
				.findByDoctorAndStartTimeBetweenAndStatus(a.getDoctor(), dayStart, dayEnd, AppointmentStatus.ACTIVE);

		if (dailyAppointments.size() >= 8) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Doctor has reached the maximum of 8 appointments per day");
		}
	}

}
