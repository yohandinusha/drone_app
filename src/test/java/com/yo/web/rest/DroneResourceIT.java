package com.yo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yo.IntegrationTest;
import com.yo.domain.Drone;
import com.yo.domain.enumeration.Model;
import com.yo.domain.enumeration.State;
import com.yo.repository.DroneRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DroneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DroneResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Model DEFAULT_MODEL = Model.Lightweight;
    private static final Model UPDATED_MODEL = Model.Middleweight;

    private static final Long DEFAULT_WEIGHT_LIMIT = 1L;
    private static final Long UPDATED_WEIGHT_LIMIT = 2L;

    private static final Integer DEFAULT_BATTERY_CAPACITY = 1;
    private static final Integer UPDATED_BATTERY_CAPACITY = 2;

    private static final State DEFAULT_STATE = State.IDLE;
    private static final State UPDATED_STATE = State.LOADING;

    private static final String ENTITY_API_URL = "/api/drones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDroneMockMvc;

    private Drone drone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drone createEntity(EntityManager em) {
        Drone drone = new Drone()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .model(DEFAULT_MODEL)
            .weightLimit(DEFAULT_WEIGHT_LIMIT)
            .batteryCapacity(DEFAULT_BATTERY_CAPACITY)
            .state(DEFAULT_STATE);
        return drone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drone createUpdatedEntity(EntityManager em) {
        Drone drone = new Drone()
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .model(UPDATED_MODEL)
            .weightLimit(UPDATED_WEIGHT_LIMIT)
            .batteryCapacity(UPDATED_BATTERY_CAPACITY)
            .state(UPDATED_STATE);
        return drone;
    }

    @BeforeEach
    public void initTest() {
        drone = createEntity(em);
    }

    @Test
    @Transactional
    void createDrone() throws Exception {
        int databaseSizeBeforeCreate = droneRepository.findAll().size();
        // Create the Drone
        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drone)))
            .andExpect(status().isCreated());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeCreate + 1);
        Drone testDrone = droneList.get(droneList.size() - 1);
        assertThat(testDrone.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDrone.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDrone.getWeightLimit()).isEqualTo(DEFAULT_WEIGHT_LIMIT);
        assertThat(testDrone.getBatteryCapacity()).isEqualTo(DEFAULT_BATTERY_CAPACITY);
        assertThat(testDrone.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createDroneWithExistingId() throws Exception {
        // Create the Drone with an existing ID
        drone.setId(1L);

        int databaseSizeBeforeCreate = droneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drone)))
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDrones() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList
        restDroneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drone.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].weightLimit").value(hasItem(DEFAULT_WEIGHT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].batteryCapacity").value(hasItem(DEFAULT_BATTERY_CAPACITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    void getDrone() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get the drone
        restDroneMockMvc
            .perform(get(ENTITY_API_URL_ID, drone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(drone.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.weightLimit").value(DEFAULT_WEIGHT_LIMIT.intValue()))
            .andExpect(jsonPath("$.batteryCapacity").value(DEFAULT_BATTERY_CAPACITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDrone() throws Exception {
        // Get the drone
        restDroneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDrone() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        int databaseSizeBeforeUpdate = droneRepository.findAll().size();

        // Update the drone
        Drone updatedDrone = droneRepository.findById(drone.getId()).get();
        // Disconnect from session so that the updates on updatedDrone are not directly saved in db
        em.detach(updatedDrone);
        updatedDrone
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .model(UPDATED_MODEL)
            .weightLimit(UPDATED_WEIGHT_LIMIT)
            .batteryCapacity(UPDATED_BATTERY_CAPACITY)
            .state(UPDATED_STATE);

        restDroneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDrone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDrone))
            )
            .andExpect(status().isOk());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
        Drone testDrone = droneList.get(droneList.size() - 1);
        assertThat(testDrone.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDrone.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDrone.getWeightLimit()).isEqualTo(UPDATED_WEIGHT_LIMIT);
        assertThat(testDrone.getBatteryCapacity()).isEqualTo(UPDATED_BATTERY_CAPACITY);
        assertThat(testDrone.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, drone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(drone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(drone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDroneWithPatch() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        int databaseSizeBeforeUpdate = droneRepository.findAll().size();

        // Update the drone using partial update
        Drone partialUpdatedDrone = new Drone();
        partialUpdatedDrone.setId(drone.getId());

        partialUpdatedDrone.model(UPDATED_MODEL).weightLimit(UPDATED_WEIGHT_LIMIT).batteryCapacity(UPDATED_BATTERY_CAPACITY);

        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDrone))
            )
            .andExpect(status().isOk());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
        Drone testDrone = droneList.get(droneList.size() - 1);
        assertThat(testDrone.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDrone.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDrone.getWeightLimit()).isEqualTo(UPDATED_WEIGHT_LIMIT);
        assertThat(testDrone.getBatteryCapacity()).isEqualTo(UPDATED_BATTERY_CAPACITY);
        assertThat(testDrone.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void fullUpdateDroneWithPatch() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        int databaseSizeBeforeUpdate = droneRepository.findAll().size();

        // Update the drone using partial update
        Drone partialUpdatedDrone = new Drone();
        partialUpdatedDrone.setId(drone.getId());

        partialUpdatedDrone
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .model(UPDATED_MODEL)
            .weightLimit(UPDATED_WEIGHT_LIMIT)
            .batteryCapacity(UPDATED_BATTERY_CAPACITY)
            .state(UPDATED_STATE);

        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDrone))
            )
            .andExpect(status().isOk());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
        Drone testDrone = droneList.get(droneList.size() - 1);
        assertThat(testDrone.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDrone.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDrone.getWeightLimit()).isEqualTo(UPDATED_WEIGHT_LIMIT);
        assertThat(testDrone.getBatteryCapacity()).isEqualTo(UPDATED_BATTERY_CAPACITY);
        assertThat(testDrone.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, drone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(drone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(drone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(drone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDrone() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        int databaseSizeBeforeDelete = droneRepository.findAll().size();

        // Delete the drone
        restDroneMockMvc
            .perform(delete(ENTITY_API_URL_ID, drone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
