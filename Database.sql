drop database s193956;
create database s193956;
use s193956;

-- -----------------------------------------------------
-- Table `s193956`.`Bruker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Bruker` (
  `Personnummer` CHAR(11) NOT NULL,
  `Navn` VARCHAR(45) NULL,
  `Adresse` VARCHAR(45) NULL,
  `Email` VARCHAR(45) NULL,
  `Telefon` CHAR(8) NULL,
  `Postnummer` CHAR(4) NULL,
  `Opprettet` DATE NULL,
  PRIMARY KEY (`Personnummer`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Utleier`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Utleier` (
  `Bruker_Personnummer` CHAR(11) NOT NULL,
  `Firma` VARCHAR(45) NULL,
  PRIMARY KEY (`Bruker_Personnummer`),
  INDEX `fk_Utleier_Bruker1_idx` (`Bruker_Personnummer` ASC),
  CONSTRAINT `fk_Utleier_Bruker1`
    FOREIGN KEY (`Bruker_Personnummer`)
    REFERENCES `s193956`.`Bruker` (`Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Boligsøker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Boligsøker` (
  `Bruker_Personnummer` CHAR(11) NOT NULL,
  PRIMARY KEY (`Bruker_Personnummer`),
  INDEX `fk_Boligsøker_Bruker1_idx` (`Bruker_Personnummer` ASC),
  CONSTRAINT `fk_Boligsøker_Bruker1`
    FOREIGN KEY (`Bruker_Personnummer`)
    REFERENCES `s193956`.`Bruker` (`Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`SøkerInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`SøkerInfo` (
  `Boligsøker_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Antall_personer` INT NULL,
  `Sivilstatus` VARCHAR(45) NULL,
  `Yrke` VARCHAR(45) NULL,
  `Røyker` TINYINT(1) NULL,
  `Husdyr` TINYINT(1) NULL,
  PRIMARY KEY (`Boligsøker_Bruker_Personnummer`),
  CONSTRAINT `fk_SøkerInfo_Boligsøker1`
    FOREIGN KEY (`Boligsøker_Bruker_Personnummer`)
    REFERENCES `s193956`.`Boligsøker` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`SøkerKrav`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`SøkerKrav` (
  `Boligsøker_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Min_Areal` INT NULL,
  `Max_Areal` INT NULL,
  `Min_Soverom` INT NULL,
  `Min_Byggår` INT NULL,
  `Min_Pris` INT NULL,
  `Max_Pris` INT NULL,
  `Peis` TINYINT(1) NULL,
  `Parkering` TINYINT(1) NULL,
  PRIMARY KEY (`Boligsøker_Bruker_Personnummer`),
  CONSTRAINT `fk_SøkerKrav_Boligsøker1`
    FOREIGN KEY (`Boligsøker_Bruker_Personnummer`)
    REFERENCES `s193956`.`Boligsøker` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Bolig`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Bolig` (
  `BoligID` INT NOT NULL AUTO_INCREMENT,
  `Utleier_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Adresse` VARCHAR(45) NULL,
  `Boareal` INT NULL,
  `Antall_rom` INT NULL,
  `Byggår` INT NULL,
  `Beskrivelse` VARCHAR(150) NULL,
  `Utleie_pris` INT NULL,
  `Avertert` DATE NULL,
  `Postnummer` CHAR(4) NULL,
  PRIMARY KEY (`BoligID`, `Utleier_Bruker_Personnummer`),
  INDEX `fk_Bolig_Utleier1_idx` (`Utleier_Bruker_Personnummer` ASC),
  CONSTRAINT `fk_Bolig_Utleier1`
    FOREIGN KEY (`Utleier_Bruker_Personnummer`)
    REFERENCES `s193956`.`Utleier` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Kundebehandler`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Kundebehandler` (
  `Bruker_Personnummer` CHAR(11) NOT NULL,
  PRIMARY KEY (`Bruker_Personnummer`),
  CONSTRAINT `fk_Kundebehandler_Bruker1`
    FOREIGN KEY (`Bruker_Personnummer`)
    REFERENCES `s193956`.`Bruker` (`Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Leiekontrakt`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Leiekontrakt` (
  `Bolig_BoligID` INT NOT NULL,
  `Utleier_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Boligsøker_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Kundebehandler_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Leiepris_pr_måned` INT NULL,
  `Avtale_start` DATE NULL,
  `Avtale_slutt` DATE NULL,
  INDEX `fk_Leiekontrakt_Bolig1_idx` (`Bolig_BoligID` ASC),
  PRIMARY KEY (`Bolig_BoligID`, `Utleier_Bruker_Personnummer`, `Boligsøker_Bruker_Personnummer`, `Kundebehandler_Bruker_Personnummer`),
  INDEX `fk_Leiekontrakt_Utleier1_idx` (`Utleier_Bruker_Personnummer` ASC),
  INDEX `fk_Leiekontrakt_Boligsøker1_idx` (`Boligsøker_Bruker_Personnummer` ASC),
  INDEX `fk_Leiekontrakt_Kundebehandler1_idx` (`Kundebehandler_Bruker_Personnummer` ASC),
  CONSTRAINT `fk_Leiekontrakt_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Leiekontrakt_Utleier1`
    FOREIGN KEY (`Utleier_Bruker_Personnummer`)
    REFERENCES `s193956`.`Utleier` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Leiekontrakt_Boligsøker1`
    FOREIGN KEY (`Boligsøker_Bruker_Personnummer`)
    REFERENCES `s193956`.`Boligsøker` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Leiekontrakt_Kundebehandler1`
    FOREIGN KEY (`Kundebehandler_Bruker_Personnummer`)
    REFERENCES `s193956`.`Kundebehandler` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Enebolig_og_rekkehus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Enebolig_og_rekkehus` (
  `Bolig_BoligID` INT NOT NULL,
  `Antall_etasjer` INT NULL,
  `Kjeller` TINYINT(1) NULL,
  `Tomt_areal` INT NULL,
  PRIMARY KEY (`Bolig_BoligID`),
  INDEX `fk_Enebolig_og_rekkehus_Bolig1_idx` (`Bolig_BoligID` ASC),
  CONSTRAINT `fk_Enebolig_og_rekkehus_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Leilighet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Leilighet` (
  `Bolig_BoligID` INT NOT NULL,
  `Etasje` INT NULL,
  `Heis` TINYINT(1) NULL,
  `Balkong` TINYINT(1) NULL,
  `Garasje` TINYINT(1) NULL,
  `Fellesvaskeri` TINYINT(1) NULL,
  PRIMARY KEY (`Bolig_BoligID`),
  INDEX `fk_Leilighet_Bolig1_idx` (`Bolig_BoligID` ASC),
  CONSTRAINT `fk_Leilighet_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Bruker_PassordRegister`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Bruker_PassordRegister` (
  `Bruker_Personnummer` CHAR(11) NOT NULL,
  `Passord` VARCHAR(45) NULL,
  PRIMARY KEY (`Bruker_Personnummer`),
  INDEX `fk_Bruker_PassordRegister_Bruker1_idx` (`Bruker_Personnummer` ASC),
  CONSTRAINT `fk_Bruker_PassordRegister_Bruker1`
    FOREIGN KEY (`Bruker_Personnummer`)
    REFERENCES `s193956`.`Bruker` (`Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`SendtEmail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`SendtEmail` (
  `Bolig_BoligID` INT NOT NULL,
  `Bolig_Utleier_Personnummer` CHAR(11) NOT NULL,
  PRIMARY KEY (`Bolig_BoligID`, `Bolig_Utleier_Personnummer`),
  INDEX `fk_SendtEmail_Bolig1_idx` (`Bolig_BoligID` ASC, `Bolig_Utleier_Personnummer` ASC),
  CONSTRAINT `fk_SendtEmail_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Bolig_bilde`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Bolig_bilde` (
  `BildeID` INT NOT NULL AUTO_INCREMENT,
  `Bolig_BoligID` INT NOT NULL,
  `Bilde` LONGBLOB NULL,
  `Digest` VARCHAR(255) NULL,
  INDEX `fk_Bolig_bilde_Bolig1_idx` (`Bolig_BoligID` ASC),
  PRIMARY KEY (`BildeID`),
  CONSTRAINT `fk_Bolig_bilde_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Leiekontrakt_forespørsel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Leiekontrakt_forespørsel` (
  `Boligsøker_Bruker_Personnummer` CHAR(11) NOT NULL,
  `Bolig_BoligID` INT NOT NULL,
  `Opprettet_Dato` DATE NOT NULL,
  `Kundebehandler_Bruker_Personnummer` CHAR(11) NULL,
  `Påtatt` TINYINT(1) NULL,
  INDEX `fk_Leiekontrakt_forespørsel_Bolig1_idx` (`Bolig_BoligID` ASC),
  PRIMARY KEY (`Boligsøker_Bruker_Personnummer`, `Bolig_BoligID`, `Opprettet_Dato`),
  INDEX `fk_Leiekontrakt_forespørsel_Kundebehandler1_idx` (`Kundebehandler_Bruker_Personnummer` ASC),
  CONSTRAINT `fk_Leiekontrakt_forespørsel_Boligsøker1`
    FOREIGN KEY (`Boligsøker_Bruker_Personnummer`)
    REFERENCES `s193956`.`Boligsøker` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Leiekontrakt_forespørsel_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Leiekontrakt_forespørsel_Kundebehandler1`
    FOREIGN KEY (`Kundebehandler_Bruker_Personnummer`)
    REFERENCES `s193956`.`Kundebehandler` (`Bruker_Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s193956`.`Viste_Boliger`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s193956`.`Viste_Boliger` (
  `Bolig_BoligID` INT NOT NULL,
  `Bruker_Personnummer` CHAR(11) NOT NULL,
  PRIMARY KEY (`Bolig_BoligID`, `Bruker_Personnummer`),
  INDEX `fk_Bolig_has_Bruker_Bruker1_idx` (`Bruker_Personnummer` ASC),
  INDEX `fk_Bolig_has_Bruker_Bolig1_idx` (`Bolig_BoligID` ASC),
  CONSTRAINT `fk_Bolig_has_Bruker_Bolig1`
    FOREIGN KEY (`Bolig_BoligID`)
    REFERENCES `s193956`.`Bolig` (`BoligID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Bolig_has_Bruker_Bruker1`
    FOREIGN KEY (`Bruker_Personnummer`)
    REFERENCES `s193956`.`Bruker` (`Personnummer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

insert into Bruker (Personnummer, Navn, Adresse, Email, Telefon, Postnummer, Opprettet) VALUES('12345678912', 'Arlen', 'Testveien 1', 'norge@norge.no', '12345678', '1234', curdate());
insert into Kundebehandler (Bruker_Personnummer) VALUES ('12345678912');
insert into Bruker_PassordRegister (Bruker_Personnummer, Passord) VALUES(12345678912, password(123));

insert into Bruker (Personnummer, Navn, Adresse, Email, Telefon, Postnummer, Opprettet) VALUES('12345678923', 'Alex', 'Testveien 1', 'norge@norge.no', '12345678', '1234', curdate());
insert into Kundebehandler (Bruker_Personnummer) VALUES ('12345678923');
 insert into Bruker_PassordRegister (Bruker_Personnummer, Passord) VALUES(12345678923, password(123));

insert into Bruker (Personnummer, Navn, Adresse, Email, Telefon, Postnummer, Opprettet) VALUES('12345678934', 'Petter', 'Testveien 1', 'norge@norge.no', '12345678', '1234', curdate());
insert into Kundebehandler (Bruker_Personnummer) VALUES ('12345678934');
 insert into Bruker_PassordRegister (Bruker_Personnummer, Passord) VALUES(12345678934, password(123));