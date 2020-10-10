package com.br.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Expense {

    @Id
    @GeneratedValue
    private int id;
    private String description;
    private String createdAt;
    private int createdBy;
    private int updatedBy;
    private int categoryId;
    private double value;
    

}
