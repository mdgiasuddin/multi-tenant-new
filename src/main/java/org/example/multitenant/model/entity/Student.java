package org.example.multitenant.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table
@Getter
@Setter
@ToString(doNotUseGetters = true)
public class Student {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
}
