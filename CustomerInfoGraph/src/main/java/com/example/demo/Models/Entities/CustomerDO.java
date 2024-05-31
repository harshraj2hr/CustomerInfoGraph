package com.example.demo.Models.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "customers")
public class CustomerDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    private String email;

    @Column(name = "linkSequence")
    private Integer linkSequence;

    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    private Date updatedAt;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedAt")
    private Date deletedAt;

    public enum LinkPrecedence {PRIMARY, SECONDARY}
}
