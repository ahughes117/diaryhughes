SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `diaryhughes` ;
CREATE SCHEMA IF NOT EXISTS `diaryhughes` DEFAULT CHARACTER SET utf8 ;
USE `diaryhughes` ;

-- -----------------------------------------------------
-- Table `contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact` ;

CREATE TABLE IF NOT EXISTS `contact` (
  `contactID` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) NOT NULL,
  `Surname` VARCHAR(100) NOT NULL,
  `Email` VARCHAR(255) NULL,
  `Phone` VARCHAR(13) NULL,
  `Picture` TEXT NULL,
  `Comments` TEXT NULL DEFAULT NULL,
  `DateCreated` TIMESTAMP NULL,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`contactID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `category` ;

CREATE TABLE IF NOT EXISTS `category` (
  `categoryID` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Desc` TEXT NULL DEFAULT NULL,
  `DateCreated` TIMESTAMP NULL,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`categoryID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `day`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `day` ;

CREATE TABLE IF NOT EXISTS `day` (
  `dayID` INT(11) NOT NULL AUTO_INCREMENT,
  `Date` DATE NOT NULL,
  `Summary` TEXT NULL DEFAULT NULL,
  `Sex` SMALLINT(2) NOT NULL DEFAULT '0',
  `Work` SMALLINT(2) NOT NULL DEFAULT '0',
  `Fun` SMALLINT(2) NOT NULL DEFAULT '0',
  `Special` SMALLINT(2) NOT NULL DEFAULT '0',
  `Alcohol` SMALLINT(2) NOT NULL DEFAULT '0',
  `Practice` SMALLINT(2) NOT NULL,
  `Expenses` DECIMAL(9,2) NOT NULL DEFAULT 0,
  `DateCreated` TIMESTAMP NULL,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dayID`),
  UNIQUE INDEX `Date_UNIQUE` (`Date` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `day_contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `day_contact` ;

CREATE TABLE IF NOT EXISTS `day_contact` (
  `dayID` INT(11) NOT NULL,
  `contactID` INT(11) NOT NULL,
  PRIMARY KEY (`contactID`, `dayID`),
  INDEX `fk_day-contact_day_idx` (`dayID` ASC),
  INDEX `fk_day-contact_contact_idx` (`contactID` ASC),
  CONSTRAINT `fk_day-contact_day`
    FOREIGN KEY (`contactID`)
    REFERENCES `day` (`dayID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_day-contact_contact`
    FOREIGN KEY (`contactID`)
    REFERENCES `contact` (`contactID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event` ;

CREATE TABLE IF NOT EXISTS `event` (
  `eventID` INT(11) NOT NULL AUTO_INCREMENT,
  `dayID` INT NOT NULL,
  `categoryID` INT NULL,
  `Name` VARCHAR(45) NULL,
  `Desc` TEXT NULL DEFAULT NULL,
  `Time` TIME NULL DEFAULT NULL,
  `Picture` TEXT NULL,
  `DateCreated` TIMESTAMP NULL,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`eventID`),
  INDEX `fk_event_day_idx` (`dayID` ASC),
  INDEX `fk_event_category_idx` (`categoryID` ASC),
  CONSTRAINT `fk_event_day`
    FOREIGN KEY (`dayID`)
    REFERENCES `day` (`dayID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_category`
    FOREIGN KEY (`categoryID`)
    REFERENCES `category` (`categoryID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meta_translation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meta_translation` ;

CREATE TABLE IF NOT EXISTS `meta_translation` (
  `DB` VARCHAR(30) NOT NULL,
  `English` VARCHAR(60) NULL,
  `Greek` VARCHAR(60) NULL,
  PRIMARY KEY (`DB`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tag` ;

CREATE TABLE IF NOT EXISTS `tag` (
  `tagID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tagID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event_tag` ;

CREATE TABLE IF NOT EXISTS `event_tag` (
  `eventID` INT NOT NULL,
  `tagID` INT NOT NULL,
  PRIMARY KEY (`eventID`, `tagID`),
  INDEX `fk_event_tag_event_idx` (`eventID` ASC),
  INDEX `fk_event_tag_tag_idx` (`tagID` ASC),
  CONSTRAINT `fk_event_tag_event`
    FOREIGN KEY (`eventID`)
    REFERENCES `event` (`eventID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_tag_tag`
    FOREIGN KEY (`tagID`)
    REFERENCES `tag` (`tagID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
