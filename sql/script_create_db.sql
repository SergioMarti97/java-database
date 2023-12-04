-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema example_person
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `example_person` ;

-- -----------------------------------------------------
-- Schema example_person
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `example_person` DEFAULT CHARACTER SET utf8 ;
USE `example_person` ;

-- -----------------------------------------------------
-- Table `example_person`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `example_person`.`person` ;

CREATE TABLE IF NOT EXISTS `example_person`.`person` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `height` DECIMAL(5,2) NULL,
  `birthday` DATE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `example_person`.`person_has_friend`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `example_person`.`person_has_friend` ;

CREATE TABLE IF NOT EXISTS `example_person`.`person_has_friend` (
  `person_id` INT UNSIGNED NOT NULL,
  `friend_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`person_id`, `friend_id`),
  INDEX `fk_person_has_person_person1_idx` (`friend_id` ASC) VISIBLE,
  INDEX `fk_person_has_person_person_idx` (`person_id` ASC) VISIBLE,
  CONSTRAINT `fk_person_has_person_person`
    FOREIGN KEY (`person_id`)
    REFERENCES `example_person`.`person` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_person_has_person_person1`
    FOREIGN KEY (`friend_id`)
    REFERENCES `example_person`.`person` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
