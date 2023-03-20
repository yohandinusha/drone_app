package com.yo.service.impl;

import com.yo.domain.Drone;
import com.yo.repository.DroneRepository;
import com.yo.service.DroneService;
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

    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public Drone save(Drone drone) {
        log.debug("Request to save Drone : {}", drone);
        return droneRepository.save(drone);
    }

    @Override
    public Drone update(Drone drone) {
        log.debug("Request to update Drone : {}", drone);
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
