package com.kosmos.medicalclinic.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.kosmos.medicalclinic.demo.dao.ConsultingRoom;
import com.kosmos.medicalclinic.demo.repository.ConsultingRoomRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {

	private final ConsultingRoomRepository consultingRoomRepository;

    public ConsultingRoom save(ConsultingRoom consultingRoom) {
        return consultingRoomRepository.save(consultingRoom);
    }

    public List<ConsultingRoom> findAll() {
        return consultingRoomRepository.findAll();
    }
    
    public Optional<ConsultingRoom> findById(Long id) {
        return consultingRoomRepository.findById(id);
    }

    public ConsultingRoom update(Long id, ConsultingRoom consultingRoom) {
        ConsultingRoom existingRoom = consultingRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulting Room not found"));
        existingRoom.setRoomNumber(consultingRoom.getRoomNumber());
        existingRoom.setFloor(consultingRoom.getFloor());
        return consultingRoomRepository.save(existingRoom);
    }

    public void delete(Long id) {
        consultingRoomRepository.deleteById(id);
    }

}
