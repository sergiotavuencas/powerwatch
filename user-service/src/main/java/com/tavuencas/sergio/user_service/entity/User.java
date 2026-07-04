package com.tavuencas.sergio.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String address;
    private boolean alerting;

    @Column(name = "energy_alerting_threshold")
    private double energyAlertingThreshold;
}
