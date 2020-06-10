-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema family_finance
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema family_finance
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `family_finance` ;
USE `family_finance` ;

-- -----------------------------------------------------
-- Table `family_finance`.`expenses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `family_finance`.`expenses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `created_by` INT NOT NULL,
  `id_category` INT NOT NULL,
  `value` DOUBLE NOT NULL,
  `updated_by` INT NULL,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `created_by` (`created_by` ASC) ,
  CONSTRAINT `created_by`
    FOREIGN KEY (`created_by`)
    REFERENCES `family_finance`.`user` (`id`)
	ON DELETE CASCADE,
  INDEX `updated_by` (`updated_by` ASC) ,
  CONSTRAINT `updated_by`
    FOREIGN KEY (`updated_by`)
    REFERENCES `family_finance`.`user` (`id`)
	ON DELETE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `family_finance`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `family_finance`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,  
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `family_finance`.`income`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `family_finance`.`income` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `created_by` INT NOT NULL,
  `value` DOUBLE NOT NULL,
  `updated_by` INT NULL,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `created_by` (`created_by` ASC) ,
  CONSTRAINT `created_by`
    FOREIGN KEY (`created_by`)
    REFERENCES `family_finance`.`user` (`id`)
	ON DELETE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `family_finance`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `family_finance`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL,
  `created_by` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `created_by` (`created_by` ASC) ,
  CONSTRAINT `created_by`
    FOREIGN KEY (`created_by`)
    REFERENCES `family_finance`.`user` (`id`)
	ON DELETE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
