package com.example.telegrambot.model;

import com.example.telegrambot.constants.ShelterType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Приюты.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shelters")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "address_shelter")
    private String addressShelter;

    @Column(name = "time_work")
    private String timeWork;

    @Column(name = "driving_directions")
    private String drivingDirections;

    @Column(name = "phone_shelter")
    private String phoneShelter;

    @Column(name = "phone_security")
    private String phoneSecurity;

    @Column(name = "shelter_type")
    private ShelterType shelterType;

    public Shelter(String addressShelter,
                   String timeWork,
                   String drivingDirections,
                   String phoneShelter,
                   String phoneSecurity,
                   ShelterType shelterType) {
        setAddressShelter(addressShelter);
        setTimeWork(timeWork);
        setDrivingDirections(drivingDirections);
        setPhoneShelter(phoneShelter);
        setPhoneSecurity(phoneSecurity);
        this.shelterType = shelterType;
    }

    public void setAddressShelter(String addressShelter) {
        if (addressShelter == null || addressShelter.isEmpty() || addressShelter.isBlank()) {
            throw new RuntimeException("Адрес введен некорректно!");
        } else {
            this.addressShelter = addressShelter;
        }
    }

    public void setTimeWork(String timeWork) {
        if (timeWork == null || timeWork.isEmpty() || timeWork.isBlank()) {
            throw new RuntimeException("Время работы введено некорректно!");
        } else {
            this.timeWork = timeWork;
        }
    }

    public void setDrivingDirections(String drivingDirections) {
        if (drivingDirections == null || drivingDirections.isEmpty() || drivingDirections.isBlank()) {
            throw new RuntimeException("Месторасположение введено некорректно!");
        } else {
            this.drivingDirections = drivingDirections;
        }
    }

    public void setPhoneShelter(String phoneShelter) {
        if (phoneShelter == null || phoneShelter.isEmpty() || phoneShelter.isBlank()) {
            throw new RuntimeException("Телефон приюта введен некорректно!");
        } else {
            this.phoneShelter = phoneShelter;
        }
    }

    public void setPhoneSecurity(String phoneSecurity) {
        if (phoneSecurity == null || phoneSecurity.isEmpty() || phoneSecurity.isBlank()) {
            throw new RuntimeException("Телефон охраны введен некорректно!");
        } else {
            this.phoneSecurity = phoneSecurity;
        }
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }
}
