package com.yo.web.rest;

import com.yo.domain.Drone;
import com.yo.domain.Medication;
import com.yo.repository.DroneRepository;
import com.yo.service.DroneService;
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
 * REST controller for managing {@link com.yo.domain.Drone}.
 */
@RestController
@RequestMapping("/api")
public class DispatchController {

    private final Logger log = LoggerFactory.getLogger(DispatchController.class);

    private static final String ENTITY_NAME = "droneDrone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DroneService droneService;

    private final DroneRepository droneRepository;

    public DispatchController(DroneService droneService, DroneRepository droneRepository) {
        this.droneService = droneService;
        this.droneRepository = droneRepository;
    }

    /**
     * {@code POST  /drones} : Create a new drone.
     *
     * @param drone the drone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new drone, or with status {@code 400 (Bad Request)} if the drone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/drones/register")
    public ResponseEntity<Drone> createDrone(@RequestBody Drone drone) throws URISyntaxException {
        log.debug("REST request to save Drone : {}", drone);
        if (drone.getId() != null) {
            throw new BadRequestAlertException("A new drone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Drone result = droneService.save(drone);
        return ResponseEntity
            .created(new URI("/api/drones/register" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /drones/:id} : Updates an existing drone.
     *
     * @param id the id of the drone to save.
     * @param drone the drone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drone,
     * or with status {@code 400 (Bad Request)} if the drone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the drone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/drones/load/{id}")
    public ResponseEntity<Drone> updateDrone(@PathVariable(value = "id", required = true) final Long id, @RequestBody Medication medication)
        throws URISyntaxException {
        log.debug("REST request to update Drone with medication : {}, {}", id, medication);


        Drone result = droneService.updateMedication(id,medication);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medication.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /drones/:id} : get the "id" drone.
     *
     * @param id the id of the drone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the drone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/drones/load-medication/{id}")
    public ResponseEntity<Drone> getDrone(@PathVariable Long id) {
        log.debug("REST request to get Drone : {}", id);
        Optional<Drone> drone = droneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(drone);
    }
    
}
