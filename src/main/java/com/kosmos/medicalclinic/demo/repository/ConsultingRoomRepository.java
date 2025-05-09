package com.kosmos.medicalclinic.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosmos.medicalclinic.demo.dao.ConsultingRoom;

@Repository
public interface ConsultingRoomRepository extends JpaRepository<ConsultingRoom, Long> {

}
