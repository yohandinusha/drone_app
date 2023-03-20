package com.yo.service.impl;

import com.yo.domain.Medication;
import com.yo.repository.MedicationRepository;
import com.yo.service.MedicationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Medication}.
 */
@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final Logger log = LoggerFactory.getLogger(MedicationServiceImpl.class);

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Medication save(Medication medication) {
        log.debug("Request to save Medication : {}", medication);
        return medicationRepository.save(medication);
    }

    @Override
    public Medication update(Medication medication) {
        log.debug("Request to update Medication : {}", medication);
        return medicationRepository.save(medication);
    }

    @Override
    public Optional<Medication> partialUpdate(Medication medication) {
        log.debug("Request to partially update Medication : {}", medication);

        return medicationRepository
            .findById(medication.getId())
            .map(existingMedication -> {
                if (medication.getName() != null) {
                    existingMedication.setName(medication.getName());
                }
                if (medication.getWeght() != null) {
                    existingMedication.setWeght(medication.getWeght());
                }
                if (medication.getCode() != null) {
                    existingMedication.setCode(medication.getCode());
                }
                if (medication.getImage() != null) {
                    existingMedication.setImage(medication.getImage());
                }

                return existingMedication;
            })
            .map(medicationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medication> findAll(Pageable pageable) {
        log.debug("Request to get all Medications");
        return medicationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medication> findOne(Long id) {
        log.debug("Request to get Medication : {}", id);
        return medicationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medication : {}", id);
        medicationRepository.deleteById(id);
    }
}
