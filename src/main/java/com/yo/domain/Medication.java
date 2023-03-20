package com.yo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

/**
 * A Medication.
 */
@Entity
@Table(name = "medication")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @Pattern(regexp="\\[a-zA-Z0-9_.-]*$")
    private String name;

    @Column(name = "weght")
    @Max(value=500)
    private Integer weght;

    @Column(name = "code")
    private String code;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JsonIgnoreProperties(value = { "medications" }, allowSetters = true)
    private Drone drone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Medication name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeght() {
        return this.weght;
    }

    public Medication weght(Integer weght) {
        this.setWeght(weght);
        return this;
    }

    public void setWeght(Integer weght) {
        this.weght = weght;
    }

    public String getCode() {
        return this.code;
    }

    public Medication code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return this.image;
    }

    public Medication image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Drone getDrone() {
        return this.drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Medication drone(Drone drone) {
        this.setDrone(drone);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medication)) {
            return false;
        }
        return id != null && id.equals(((Medication) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medication{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weght=" + getWeght() +
            ", code='" + getCode() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
