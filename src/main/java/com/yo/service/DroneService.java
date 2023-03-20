package com.yo.service;

import com.yo.domain.Drone;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Drone}.
 */
public interface DroneService {
    /**
     * Save a drone.
     *
     * @param drone the entity to save.
     * @return the persisted entity.
     */
    Drone save(Drone drone);

    /**
     * Updates a drone.
     *
     * @param drone the entity to update.
     * @return the persisted entity.
     */
    Drone update(Drone drone);

    /**
     * Partially updates a drone.
     *
     * @param drone the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Drone> partialUpdate(Drone drone);

    /**
     * Get all the drones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Drone> findAll(Pageable pageable);

    /**
     * Get the "id" drone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Drone> findOne(Long id);

    /**
     * Delete the "id" drone.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
