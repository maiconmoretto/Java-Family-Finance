package com.br.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class SharedFinance {

    @Id
    @GeneratedValue
    private int id;
    private int userId;
    private int sharedUserId;
    private String createdAt;
    private boolean accepted;

}
