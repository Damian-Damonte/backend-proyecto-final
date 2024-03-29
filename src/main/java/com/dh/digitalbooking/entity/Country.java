package com.dh.digitalbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Country")
@Table(
        name = "countries",
        uniqueConstraints = {
                @UniqueConstraint(name = "country_name_unique", columnNames = "name")
        }
)
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
