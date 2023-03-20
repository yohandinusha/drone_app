package com.yo.repository;

import com.yo.domain.Drone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Drone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {}
