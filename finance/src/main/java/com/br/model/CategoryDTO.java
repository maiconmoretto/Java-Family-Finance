package com.br.model;

import javax.persistence.Column;

public class CategoryDTO {

        @Column(name = "description")
        String description;
        @Column(name = "created_by")
        int createdBy;
        @Column(name = "updated_by")
        int updatedBy;

        public CategoryDTO() {
        }


        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public int getCreatedBy() {
                return createdBy;
        }

        public void setCreatedBy(int createdBy) {
                this.createdBy = createdBy;
        }

        public int getUpdatedBy() {
                return updatedBy;
        }

        public void setUpdatedBy(int updatedBy) {
                this.updatedBy = updatedBy;
        }

        public CategoryDTO(String description, int updatedBy, int createdBy) {
                this.description = description;
                this.createdBy = createdBy;
                this.updatedBy = updatedBy;
        }
     
     	public Category changeToObject(){
            return new Category(description, updatedBy, createdBy);
        }
}
   
