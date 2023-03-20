package com.yo.service.impl;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.yo.domain.Drone;
import com.yo.domain.Medication;
import com.yo.domain.enumeration.Model;
import com.yo.domain.enumeration.State;
import com.yo.repository.DroneRepository;
import com.yo.service.DroneService;
import com.yo.service.MedicationService;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Drone}.
 */
@Service
@Transactional
public class DroneServiceImpl implements DroneService {

    private final Logger log = LoggerFactory.getLogger(DroneServiceImpl.class);

    private final DroneRepository droneRepository;

    private final MedicationService medicationService;

    public DroneServiceImpl(DroneRepository droneRepository,MedicationService medicationService) {
        this.droneRepository = droneRepository;
        this.medicationService = medicationService;
    }

    @Override
    public Drone save(Drone drone) {
        log.debug("Request to save Drone : {}", drone);

        if(drone.getWeightLimit() > 400 && drone.getWeightLimit() <= 500)
            drone.setModel(Model.Heavyweight);
        else if(drone.getWeightLimit() > 300 && drone.getWeightLimit() <= 400)
            drone.setModel(Model.Cruiserweight); 
        else if(drone.getWeightLimit() > 200 && drone.getWeightLimit() <= 300)
            drone.setModel(Model.Middleweight);
        else 
            drone.setModel(Model.Lightweight); 
            
        drone.setState(State.IDLE);    
        return droneRepository.save(drone);
    }

    @Override
    public Drone update(Drone drone) {
        log.debug("Request to update Drone : {}", drone);
        return droneRepository.save(drone);
    }

    @Override
    public Drone updateMedication(Long id, Medication medication) {
        log.debug("Request to update Drone : {}", medication);
        Drone drone = droneRepository.findById(id).get();
        medication = medicationService.save(medication);
        drone = drone.addMedication(medication);
        log.debug("RMeditation Loading : {}", medication, drone.getMedications().size());
        return droneRepository.save(drone);
    }

    @Override
    public Optional<Drone> partialUpdate(Drone drone) {
        log.debug("Request to partially update Drone : {}", drone);

        return droneRepository
            .findById(drone.getId())
            .map(existingDrone -> {
                if (drone.getSerialNumber() != null) {
                    existingDrone.setSerialNumber(drone.getSerialNumber());
                }
                if (drone.getModel() != null) {
                    existingDrone.setModel(drone.getModel());
                }
                if (drone.getWeightLimit() != null) {
                    existingDrone.setWeightLimit(drone.getWeightLimit());
                }
                if (drone.getBatteryCapacity() != null) {
                    existingDrone.setBatteryCapacity(drone.getBatteryCapacity());
                }
                if (drone.getState() != null) {
                    existingDrone.setState(drone.getState());
                }

                return existingDrone;
            })
            .map(droneRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Drone> findAll(Pageable pageable) {
        log.debug("Request to get all Drones");
        return droneRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Drone> findOne(Long id) {
        log.debug("Request to get Drone : {}", id);
        return droneRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Drone : {}", id);
        droneRepository.deleteById(id);
    }
}
