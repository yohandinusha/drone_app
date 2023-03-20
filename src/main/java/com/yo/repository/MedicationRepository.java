package com.yo.repository;

import com.yo.domain.Medication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {}
