SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `phonediary` ;
CREATE SCHEMA IF NOT EXISTS `phonediary` DEFAULT CHARACTER SET utf8 ;
USE `phonediary` ;

-- -----------------------------------------------------
-- Table `contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact` ;

CREATE  TABLE IF NOT EXISTS `contact` (
  `ContactID` INT(11) NOT NULL ,
  `Name` VARCHAR(100) NULL DEFAULT NULL ,
  `Surname` VARCHAR(100) NULL DEFAULT NULL ,
  `Comments` TEXT NULL DEFAULT NULL ,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ContactID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `email`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `email` ;

CREATE  TABLE IF NOT EXISTS `email` (
  `EmailID` INT(11) NOT NULL ,
  `EmailAddress` VARCHAR(100) NULL DEFAULT NULL ,
  `Comments` VARCHAR(200) NULL DEFAULT NULL ,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`EmailID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contact_email`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact_email` ;

CREATE  TABLE IF NOT EXISTS `contact_email` (
  `ContactEmailID` INT(11) NOT NULL ,
  `ContactID` INT(11) NOT NULL ,
  `EmailID` INT(11) NOT NULL ,
  PRIMARY KEY (`ContactEmailID`) ,
  INDEX `fk_ContactEmail_ContactID` (`ContactID` ASC) ,
  INDEX `fk_ContactEmail_EmailID` (`EmailID` ASC) ,
  CONSTRAINT `fk_ContactEmail_ContactID`
    FOREIGN KEY (`ContactID` )
    REFERENCES `contact` (`ContactID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ContactEmail_EmailID`
    FOREIGN KEY (`EmailID` )
    REFERENCES `email` (`EmailID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cgroup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cgroup` ;

CREATE  TABLE IF NOT EXISTS `cgroup` (
  `cGroupID` INT(11) NOT NULL COMMENT 'not accidentally mispelled' ,
  `GroupName` VARCHAR(200) NULL DEFAULT NULL ,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`cGroupID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contact_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact_group` ;

CREATE  TABLE IF NOT EXISTS `contact_group` (
  `ContactID` INT(11) NOT NULL ,
  `GroupID` INT(11) NOT NULL ,
  PRIMARY KEY (`ContactID`, `GroupID`) ,
  INDEX `fk_ContactGroup_Contact` (`ContactID` ASC) ,
  INDEX `fk_ContactGroup_Group` (`GroupID` ASC) ,
  INDEX `fk_contact-group_contact` (`ContactID` ASC) ,
  INDEX `fk_contact-group_cgroup` (`GroupID` ASC) ,
  CONSTRAINT `fk_contact-group_contact`
    FOREIGN KEY (`ContactID` )
    REFERENCES `contact` (`ContactID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_contact-group_cgroup`
    FOREIGN KEY (`GroupID` )
    REFERENCES `cgroup` (`cGroupID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `telephone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `telephone` ;

CREATE  TABLE IF NOT EXISTS `telephone` (
  `TelephoneID` INT(11) NOT NULL ,
  `TelephoneNumber` VARCHAR(45) NULL DEFAULT NULL ,
  `Comments` VARCHAR(200) NULL DEFAULT NULL ,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`TelephoneID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contact_telephone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact_telephone` ;

CREATE  TABLE IF NOT EXISTS `contact_telephone` (
  `ContactTelephoneID` INT(11) NOT NULL ,
  `ContactID` INT(11) NOT NULL ,
  `TelephoneID` INT(11) NOT NULL ,
  PRIMARY KEY (`ContactTelephoneID`) ,
  INDEX `fk_ContactTelephone_ContactID` (`ContactID` ASC) ,
  INDEX `fk_ContactTelephone_TelephoneID` (`TelephoneID` ASC) ,
  CONSTRAINT `fk_ContactTelephone_ContactID`
    FOREIGN KEY (`ContactID` )
    REFERENCES `contact` (`ContactID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ContactTelephone_TelephoneID`
    FOREIGN KEY (`TelephoneID` )
    REFERENCES `telephone` (`TelephoneID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `category` ;

CREATE  TABLE IF NOT EXISTS `category` (
  `CategoryID` INT(11) NOT NULL ,
  `Description` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`CategoryID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `day`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `day` ;

CREATE  TABLE IF NOT EXISTS `day` (
  `DayID` INT(11) NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(20) NOT NULL ,
  `Date` DATE NOT NULL ,
  `Summary` TEXT NULL DEFAULT NULL ,
  `Sex` SMALLINT(2) NOT NULL DEFAULT '0' ,
  `Work` SMALLINT(2) NOT NULL DEFAULT '0' ,
  `Fun` SMALLINT(2) NOT NULL DEFAULT '0' ,
  `Special` SMALLINT(2) NOT NULL DEFAULT '0' ,
  `Alcohol` SMALLINT(2) NOT NULL DEFAULT '0' ,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`DayID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `day_contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `day_contact` ;

CREATE  TABLE IF NOT EXISTS `day_contact` (
  `DayID` INT(11) NOT NULL ,
  `ContactID` INT(11) NOT NULL ,
  PRIMARY KEY (`ContactID`, `DayID`) ,
  INDEX `fk_day-contact_day` (`DayID` ASC) ,
  INDEX `fk_day-contact_contact` (`ContactID` ASC) ,
  CONSTRAINT `fk_day-contact_day`
    FOREIGN KEY (`ContactID` )
    REFERENCES `day` (`DayID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_day-contact_contact`
    FOREIGN KEY (`ContactID` )
    REFERENCES `contact` (`ContactID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event` ;

CREATE  TABLE IF NOT EXISTS `event` (
  `EventID` INT(11) NOT NULL AUTO_INCREMENT ,
  `Description` TEXT NULL DEFAULT NULL ,
  `Time` TIME NULL DEFAULT NULL ,
  `_dateModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`EventID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `day_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `day_event` ;

CREATE  TABLE IF NOT EXISTS `day_event` (
  `DayID` INT(11) NOT NULL ,
  `EventID` INT(11) NOT NULL ,
  PRIMARY KEY (`DayID`, `EventID`) ,
  INDEX `fk_day-event_event` (`EventID` ASC) ,
  INDEX `fk_day-event_day` (`DayID` ASC) ,
  CONSTRAINT `fk_day-event_event`
    FOREIGN KEY (`EventID` )
    REFERENCES `event` (`EventID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_day-event_day`
    FOREIGN KEY (`DayID` )
    REFERENCES `day` (`DayID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `event_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event_category` ;

CREATE  TABLE IF NOT EXISTS `event_category` (
  `EventID` INT(11) NOT NULL ,
  `CategoryID` INT(11) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`CategoryID`, `EventID`) ,
  INDEX `fk_event-category_event` (`EventID` ASC) ,
  INDEX `fk_event-category_category` (`CategoryID` ASC) ,
  CONSTRAINT `fk_event-category_event`
    FOREIGN KEY (`EventID` )
    REFERENCES `event` (`EventID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event-category_category`
    FOREIGN KEY (`CategoryID` )
    REFERENCES `category` (`CategoryID` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meta_translation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meta_translation` ;

CREATE  TABLE IF NOT EXISTS `meta_translation` (
  `DB` VARCHAR(30) NOT NULL ,
  `English` VARCHAR(60) NULL ,
  `Greek` VARCHAR(60) NULL ,
  PRIMARY KEY (`DB`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
