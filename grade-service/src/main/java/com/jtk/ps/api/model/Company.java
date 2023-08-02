package com.jtk.ps.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "since_year")
    private Integer sinceYear;

    @Column(name = "status", nullable = false)
    private Integer status;

    // @Column(name = "account_id",nullable = false)
    // @JsonProperty("account_id")
    // private Integer accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;


}
