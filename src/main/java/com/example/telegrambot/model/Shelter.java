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
}
