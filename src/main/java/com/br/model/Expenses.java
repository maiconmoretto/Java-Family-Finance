package com.br.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int idUser;
    private String dateCreated;
    private String description;
    private String dateUpdated;
    private int idCategory;
    private int createdBy;

    public Expenses() {
    }


	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public int getIdUser() {
		return idUser;
	}



	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}



	public String getDateCreated() {
		return dateCreated;
	}



	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getDateUpdated() {
		return dateUpdated;
	}



	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}



	public int getIdCategory() {
		return idCategory;
	}



	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
	}



	public int getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}



	public Expenses(
			int idUser,
			String dateCreated,
			String description,
			String dateUpdated,
			int idCategory,
			int createdBy) {
		this.idUser = idUser;
		this.dateCreated = dateCreated;
		this.description = description;
		this.dateUpdated = dateUpdated;
		this.idCategory = idCategory;
		this.createdBy = createdBy;
	}
	
}





