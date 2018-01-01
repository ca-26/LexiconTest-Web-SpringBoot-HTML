
-- -----------------------------------------------------
-- Schema lexicontest
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS "lexicontest";
USE "lexicontest";

-- -----------------------------------------------------
-- Table "lexicontest"."user"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."user";
CREATE TABLE "lexicontest"."user" (
  "id" INT NOT NULL AUTO_INCREMENT,
  "email" VARCHAR(255) NOT NULL,
  "password" VARCHAR(100) NOT NULL,
  "first_name" VARCHAR(100) NULL,
  "last_name" VARCHAR(100) NULL,
  "active" TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY ("id", "email"),
  UNIQUE INDEX "email_UNIQUE" ("email" ASC),
  UNIQUE INDEX "id_UNIQUE" ("id" ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table "lexicontest"."lexicon"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."lexicon";
CREATE TABLE "lexicontest"."lexicon" (
  "id" INT NOT NULL,
  "user_id" INT NOT NULL,
  "name" VARCHAR(100) NOT NULL,
  "languages" VARCHAR(7) NOT NULL,
  PRIMARY KEY ("id"),
  UNIQUE INDEX "idLexicon_UNIQUE" ("id" ASC),
  CONSTRAINT "userLogin"
    FOREIGN KEY ("user_id")
    REFERENCES "lexicontest"."user" ("id")
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table "lexicontest"."scores"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."scores";
CREATE TABLE "lexicontest"."scores" (
  "id" INT NOT NULL AUTO_INCREMENT,
  "user_id" INT NOT NULL,
  "lex_name" VARCHAR(100) NOT NULL,
  "lex_languages" VARCHAR(7) NOT NULL,
  "date_time" DATETIME NOT NULL,
  "duration" INT NOT NULL,
  "score" SMALLINT(2) NOT NULL,
  "nb_words" INT NOT NULL,
  "nb_correct" INT NOT NULL,
  "nb_wrong" INT NOT NULL,
  "nb_skipped" INT NOT NULL,
  PRIMARY KEY ("id"),
  UNIQUE INDEX "idResults_UNIQUE" ("id" ASC),
  CONSTRAINT "userId"
    FOREIGN KEY ("user_id")
    REFERENCES "lexicontest"."user" ("id")
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
;


-- -----------------------------------------------------
-- Table "lexicontest"."words"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."words";
CREATE TABLE "lexicontest"."words" (
  "id" INT NOT NULL AUTO_INCREMENT,
  "lexicon_id" INT NOT NULL,
  "word_1" VARCHAR(255) NOT NULL,
  "word_2" VARCHAR(255) NOT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "idLexicon"
    FOREIGN KEY ("lexicon_id")
    REFERENCES "lexicontest"."lexicon" ("id")
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
;


-- -----------------------------------------------------
-- Table "lexicontest"."role"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."role";
CREATE TABLE "lexicontest"."role" (
  "id" INT NOT NULL AUTO_INCREMENT,
  "name" VARCHAR(45) NOT NULL,
  PRIMARY KEY ("id"),
  UNIQUE INDEX "idRole_UNIQUE" ("id" ASC),
  UNIQUE INDEX "nameRole_UNIQUE" ("name" ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table "lexicontest"."user_role"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."user_role";
CREATE TABLE "lexicontest"."user_role" (
  "user_id" INT NOT NULL,
  "role_id" INT NOT NULL,
  CONSTRAINT "user_id"
    FOREIGN KEY ("user_id")
    REFERENCES "lexicontest"."user" ("id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT "role_id"
    FOREIGN KEY ("role_id")
    REFERENCES "lexicontest"."role" ("id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table "lexicontest"."persistent_logins"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "lexicontest"."persistent_logins";
CREATE TABLE "lexicontest"."persistent_logins" (
  "username" VARCHAR(100) NOT NULL,
  "series" VARCHAR(64) NOT NULL,
  "token" VARCHAR(64) NOT NULL,
  "last_used" TIMESTAMP NOT NULL,
  PRIMARY KEY ("series"))
ENGINE = InnoDB;

