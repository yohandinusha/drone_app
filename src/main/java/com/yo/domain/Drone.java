package com.yo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yo.domain.enumeration.Model;
import com.yo.domain.enumeration.State;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Drone.
 */
@Entity
@Table(name = "drone")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Drone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private Model model;

    @Column(name = "weight_limit")
    private Long weightLimit;

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @OneToMany(mappedBy = "drone")
    @JsonIgnoreProperties(value = { "drone" }, allowSetters = true)
    private Set<Medication> medications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Drone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Drone serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return this.model;
    }

    public Drone model(Model model) {
        this.setModel(model);
        return this;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Long getWeightLimit() {
        return this.weightLimit;
    }

    public Drone weightLimit(Long weightLimit) {
        this.setWeightLimit(weightLimit);
        return this;
    }

    public void setWeightLimit(Long weightLimit) {
        this.weightLimit = weightLimit;
    }

    public Integer getBatteryCapacity() {
        return this.batteryCapacity;
    }

    public Drone batteryCapacity(Integer batteryCapacity) {
        this.setBatteryCapacity(batteryCapacity);
        return this;
    }

    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public State getState() {
        return this.state;
    }

    public Drone state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Medication> getMedications() {
        return this.medications;
    }

    public void setMedications(Set<Medication> medications) {
        if (this.medications != null) {
            this.medications.forEach(i -> i.setDrone(null));
        }
        if (medications != null) {
            medications.forEach(i -> i.setDrone(this));
        }
        this.medications = medications;
    }

    public Drone medications(Set<Medication> medications) {
        this.setMedications(medications);
        return this;
    }

    public Drone addMedication(Medication medication) {
        this.medications.add(medication);
        medication.setDrone(this);
        return this;
    }

    public Drone removeMedication(Medication medication) {
        this.medications.remove(medication);
        medication.setDrone(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Drone)) {
            return false;
        }
        return id != null && id.equals(((Drone) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Drone{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", model='" + getModel() + "'" +
            ", weightLimit=" + getWeightLimit() +
            ", batteryCapacity=" + getBatteryCapacity() +
            ", state='" + getState() + "'" +
            "}";
    }
}
