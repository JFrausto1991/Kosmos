package com.kosmos.medicalclinic.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosmos.medicalclinic.demo.dao.Appointment;
import com.kosmos.medicalclinic.demo.dao.ConsultingRoom;
import com.kosmos.medicalclinic.demo.dao.Doctor;
import com.kosmos.medicalclinic.demo.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByDoctorAndStartTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);

	List<Appointment> findByConsultingRoomAndStartTime(ConsultingRoom room, LocalDateTime time);

	List<Appointment> findByDoctorAndStartTime(Doctor doctor, LocalDateTime time);

	List<Appointment> findByPatientNameAndStartTimeBetween(String patientName, LocalDateTime start, LocalDateTime end);

	List<Appointment> findByDoctorAndStartTimeBetweenAndStatus(Doctor doctor, LocalDateTime start, LocalDateTime end,
			AppointmentStatus status);

	List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

	List<Appointment> findByStartTimeBetweenAndDoctor(LocalDateTime start, LocalDateTime end, Doctor doctor);

	List<Appointment> findByStartTimeBetweenAndConsultingRoom(LocalDateTime start, LocalDateTime end,
			ConsultingRoom room);

}
