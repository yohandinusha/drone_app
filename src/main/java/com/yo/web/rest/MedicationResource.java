package com.yo.web.rest;

import com.yo.domain.Medication;
import com.yo.repository.MedicationRepository;
import com.yo.service.MedicationService;
import com.yo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yo.domain.Medication}.
 */
@RestController
@RequestMapping("/api")
public class MedicationResource {

    private final Logger log = LoggerFactory.getLogger(MedicationResource.class);

    private static final String ENTITY_NAME = "droneMedication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicationService medicationService;

    private final MedicationRepository medicationRepository;

    public MedicationResource(MedicationService medicationService, MedicationRepository medicationRepository) {
        this.medicationService = medicationService;
        this.medicationRepository = medicationRepository;
    }

    /**
     * {@code POST  /medications} : Create a new medication.
     *
     * @param medication the medication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medication, or with status {@code 400 (Bad Request)} if the medication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medications")
    public ResponseEntity<Medication> createMedication(@RequestBody Medication medication) throws URISyntaxException {
        log.debug("REST request to save Medication : {}", medication);
        if (medication.getId() != null) {
            throw new BadRequestAlertException("A new medication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medication result = medicationService.save(medication);
        return ResponseEntity
            .created(new URI("/api/medications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medications/:id} : Updates an existing medication.
     *
     * @param id the id of the medication to save.
     * @param medication the medication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medication,
     * or with status {@code 400 (Bad Request)} if the medication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medications/{id}")
    public ResponseEntity<Medication> updateMedication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Medication medication
    ) throws URISyntaxException {
        log.debug("REST request to update Medication : {}, {}", id, medication);
        if (medication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Medication result = medicationService.update(medication);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medication.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medications/:id} : Partial updates given fields of an existing medication, field will ignore if it is null
     *
     * @param id the id of the medication to save.
     * @param medication the medication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medication,
     * or with status {@code 400 (Bad Request)} if the medication is not valid,
     * or with status {@code 404 (Not Found)} if the medication is not found,
     * or with status {@code 500 (Internal Server Error)} if the medication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Medication> partialUpdateMedication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Medication medication
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medication partially : {}, {}", id, medication);
        if (medication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Medication> result = medicationService.partialUpdate(medication);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medication.getId().toString())
        );
    }

    /**
     * {@code GET  /medications} : get all the medications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medications in body.
     */
    @GetMapping("/medications")
    public ResponseEntity<List<Medication>> getAllMedications(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Medications");
        Page<Medication> page = medicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medications/:id} : get the "id" medication.
     *
     * @param id the id of the medication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medications/{id}")
    public ResponseEntity<Medication> getMedication(@PathVariable Long id) {
        log.debug("REST request to get Medication : {}", id);
        Optional<Medication> medication = medicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medication);
    }

    /**
     * {@code DELETE  /medications/:id} : delete the "id" medication.
     *
     * @param id the id of the medication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        log.debug("REST request to delete Medication : {}", id);
        medicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
