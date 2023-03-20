package com.yo.service;

import com.yo.domain.Medication;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Medication}.
 */
public interface MedicationService {
    /**
     * Save a medication.
     *
     * @param medication the entity to save.
     * @return the persisted entity.
     */
    Medication save(Medication medication);

    /**
     * Updates a medication.
     *
     * @param medication the entity to update.
     * @return the persisted entity.
     */
    Medication update(Medication medication);

    /**
     * Partially updates a medication.
     *
     * @param medication the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Medication> partialUpdate(Medication medication);

    /**
     * Get all the medications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Medication> findAll(Pageable pageable);

    /**
     * Get the "id" medication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Medication> findOne(Long id);

    /**
     * Delete the "id" medication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
