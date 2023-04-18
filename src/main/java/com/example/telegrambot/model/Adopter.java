package com.example.telegrambot.model;

import com.example.telegrambot.constants.AdopterStatus;
import com.example.telegrambot.constants.animalsConst.PetType;
import jakarta.persistence.*;
import lombok.*;

import static com.example.telegrambot.constants.AdopterStatus.*;

/**
 * Усыновители.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "adopters")
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "pet_type")
    private PetType petType;
    @Column(name = "adopter_status")
    private AdopterStatus status;

    public Adopter(String firstName,
                   String lastName,
                   String phoneNumber,
                   String carNumber,
                   String email,
                   String address,
                   PetType petType,
                   AdopterStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
        this.email = email;
        this.address = address;
        this.petType = petType;
        this.status = status;
    }

    public Adopter(String firstName,
                   String lastName,
                   String phoneNumber,
                   String email,
                   String address,
                   PetType petType,
                   AdopterStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.petType = petType;
        this.status = status;
    }
}
