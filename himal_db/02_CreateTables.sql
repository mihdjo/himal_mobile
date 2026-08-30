USE himal_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS moj_plan;
DROP TABLE IF EXISTS sacuvana_ekspedicija;
DROP TABLE IF EXISTS ekspedicija_oprema;
DROP TABLE IF EXISTS oprema;
DROP TABLE IF EXISTS ekspedicija;
DROP TABLE IF EXISTS tip_ekspedicije;
DROP TABLE IF EXISTS korisnik;

SET FOREIGN_KEY_CHECKS = 1;


/* ============================================================
   KORISNIK
   ============================================================ */

CREATE TABLE korisnik (
    id_korisnika BIGINT AUTO_INCREMENT PRIMARY KEY,

    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,

    email VARCHAR(150) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    datum_rodjenja DATE NOT NULL,
    datum_kreiranja DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_korisnik_email
        UNIQUE (email),

    CONSTRAINT uq_korisnik_username
        UNIQUE (username),

    CONSTRAINT chk_korisnik_ime
        CHECK (CHAR_LENGTH(TRIM(ime)) > 0),

    CONSTRAINT chk_korisnik_prezime
        CHECK (CHAR_LENGTH(TRIM(prezime)) > 0),

    CONSTRAINT chk_korisnik_email
        CHECK (CHAR_LENGTH(TRIM(email)) > 0),

    CONSTRAINT chk_korisnik_username
        CHECK (CHAR_LENGTH(TRIM(username)) > 0)
);


/* ============================================================
   TIP EKSPEDICIJE
   ============================================================ */

CREATE TABLE tip_ekspedicije (
    id_tip_ekspedicije BIGINT AUTO_INCREMENT PRIMARY KEY,

    tip VARCHAR(50) NOT NULL,

    CONSTRAINT uq_tip_ekspedicije_tip
        UNIQUE (tip),

    CONSTRAINT chk_tip_ekspedicije_tip
        CHECK (CHAR_LENGTH(TRIM(tip)) > 0)
);


/* ============================================================
   EKSPEDICIJA
   ============================================================ */

CREATE TABLE ekspedicija (
    id_ekspedicije BIGINT AUTO_INCREMENT PRIMARY KEY,

    naziv VARCHAR(120) NOT NULL,
    opis TEXT NOT NULL,

    datum_polaska DATE NOT NULL,

    lokacija VARCHAR(150) NOT NULL,

    tezina VARCHAR(20) NOT NULL,

    trajanje_min INT NOT NULL,
    duzina_km DECIMAL(7,2) NOT NULL,

    external_url VARCHAR(500) NULL,

    datum_kreiranja DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    id_tip_ekspedicije BIGINT NOT NULL,
    id_korisnika BIGINT NOT NULL,

    CONSTRAINT fk_ekspedicija_tip
        FOREIGN KEY (id_tip_ekspedicije)
        REFERENCES tip_ekspedicije(id_tip_ekspedicije),

    CONSTRAINT fk_ekspedicija_korisnik
        FOREIGN KEY (id_korisnika)
        REFERENCES korisnik(id_korisnika)
        ON DELETE CASCADE,

    CONSTRAINT chk_ekspedicija_naziv
        CHECK (CHAR_LENGTH(TRIM(naziv)) > 0),

    CONSTRAINT chk_ekspedicija_lokacija
        CHECK (CHAR_LENGTH(TRIM(lokacija)) > 0),

    CONSTRAINT chk_ekspedicija_tezina
        CHECK (tezina IN ('EASY', 'MEDIUM', 'HARD')),

    CONSTRAINT chk_ekspedicija_trajanje
        CHECK (trajanje_min > 0),

    CONSTRAINT chk_ekspedicija_duzina
        CHECK (duzina_km > 0)
);


/* ============================================================
   OPREMA
   ============================================================ */

CREATE TABLE oprema (
    id_opreme BIGINT AUTO_INCREMENT PRIMARY KEY,

    naziv VARCHAR(100) NOT NULL,
    opis VARCHAR(255) NULL,

    CONSTRAINT uq_oprema_naziv
        UNIQUE (naziv),

    CONSTRAINT chk_oprema_naziv
        CHECK (CHAR_LENGTH(TRIM(naziv)) > 0)
);


/* ============================================================
   EKSPEDICIJA - OPREMA
   ============================================================ */

CREATE TABLE ekspedicija_oprema (
    id_ekspedicije BIGINT NOT NULL,
    id_opreme BIGINT NOT NULL,

    obavezna BOOLEAN NOT NULL DEFAULT TRUE,
    kolicina INT NOT NULL DEFAULT 1,
    napomena VARCHAR(255) NULL,

    PRIMARY KEY (id_ekspedicije, id_opreme),

    CONSTRAINT fk_ekspedicija_oprema_ekspedicija
        FOREIGN KEY (id_ekspedicije)
        REFERENCES ekspedicija(id_ekspedicije)
        ON DELETE CASCADE,

    CONSTRAINT fk_ekspedicija_oprema_oprema
        FOREIGN KEY (id_opreme)
        REFERENCES oprema(id_opreme)
        ON DELETE CASCADE,

    CONSTRAINT chk_ekspedicija_oprema_kolicina
        CHECK (kolicina > 0)
);


/* ============================================================
   SACUVANA EKSPEDICIJA
   ============================================================ */

CREATE TABLE sacuvana_ekspedicija (
    id_korisnika BIGINT NOT NULL,
    id_ekspedicije BIGINT NOT NULL,

    datum_cuvanja DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_korisnika, id_ekspedicije),

    CONSTRAINT fk_sacuvana_korisnik
        FOREIGN KEY (id_korisnika)
        REFERENCES korisnik(id_korisnika)
        ON DELETE CASCADE,

    CONSTRAINT fk_sacuvana_ekspedicija
        FOREIGN KEY (id_ekspedicije)
        REFERENCES ekspedicija(id_ekspedicije)
        ON DELETE CASCADE
);


/* ============================================================
   MOJ PLAN
   ============================================================ */

CREATE TABLE moj_plan (
    id_korisnika BIGINT NOT NULL,
    id_ekspedicije BIGINT NOT NULL,

    status BOOLEAN NOT NULL DEFAULT FALSE,

    datum_dodavanja DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_korisnika, id_ekspedicije),

    CONSTRAINT fk_moj_plan_korisnik
        FOREIGN KEY (id_korisnika)
        REFERENCES korisnik(id_korisnika)
        ON DELETE CASCADE,

    CONSTRAINT fk_moj_plan_ekspedicija
        FOREIGN KEY (id_ekspedicije)
        REFERENCES ekspedicija(id_ekspedicije)
        ON DELETE CASCADE
);


/* ============================================================
   INDEXI
   ============================================================ */

CREATE INDEX idx_ekspedicija_korisnik
    ON ekspedicija(id_korisnika);

CREATE INDEX idx_ekspedicija_tip
    ON ekspedicija(id_tip_ekspedicije);

CREATE INDEX idx_ekspedicija_tezina
    ON ekspedicija(tezina);

CREATE INDEX idx_ekspedicija_lokacija
    ON ekspedicija(lokacija);

CREATE INDEX idx_ekspedicija_naziv
    ON ekspedicija(naziv);

CREATE INDEX idx_ekspedicija_datum_polaska
    ON ekspedicija(datum_polaska);