USE himal_db;


/* ============================================================
   KORISNICI
   ============================================================ */

INSERT INTO korisnik (
    ime,
    prezime,
    email,
    username,
    password_hash,
    datum_rodjenja
)
VALUES
(
    'Marko',
    'Markovic',
    'marko@example.com',
    'marko',
    '123',
    '1998-05-12'
),
(
    'Jovana',
    'Jovanovic',
    'jovana@example.com',
    'jovana',
    '123',
    '2000-09-21'
),
(
    'Nikola',
    'Nikolic',
    'nikola@example.com',
    'nikola',
    '123',
    '1997-02-17'
);


/* ============================================================
   TIPOVI EKSPEDICIJA
   ============================================================ */

INSERT INTO tip_ekspedicije (tip)
VALUES
    ('HIKING'),
    ('TREKKING'),
    ('ALPINISM'),
    ('VIA_FERRATA');


/* ============================================================
   EKSPEDICIJE
   ============================================================ */

INSERT INTO ekspedicija (
    naziv,
    opis,
    datum_polaska,
    lokacija,
    tezina,
    trajanje_min,
    duzina_km,
    external_url,
    id_tip_ekspedicije,
    id_korisnika
)
VALUES
(
    'Rtanj - Siljak',
    'Uspon na vrh Siljak klasicnom planinarskom stazom.',
    '2026-09-12',
    'Rtanj, Srbija',
    'HARD',
    300,
    14.20,
    'https://www.google.com/maps/search/?api=1&query=Rtanj+Siljak',
    1,
    1
),
(
    'Fruska gora - Zmajevac',
    'Lagana ruta pogodna za jednodnevni planinarski izlet.',
    '2026-09-20',
    'Fruska gora, Srbija',
    'EASY',
    150,
    7.50,
    'https://www.google.com/maps/search/?api=1&query=Zmajevac+Fruska+Gora',
    1,
    2
),
(
    'Tara - Banjska stena',
    'Ruta kroz Taru sa pogledom na kanjon Drine.',
    '2026-10-03',
    'Tara, Srbija',
    'MEDIUM',
    240,
    11.00,
    'https://www.google.com/maps/search/?api=1&query=Banjska+Stena+Tara',
    2,
    2
),
(
    'Stara planina - Midzor',
    'Duža planinarska ruta ka jednom od najvisih vrhova Srbije.',
    '2026-10-17',
    'Stara planina, Srbija',
    'HARD',
    360,
    16.80,
    'https://www.google.com/maps/search/?api=1&query=Midzor+Stara+Planina',
    2,
    3
);


/* ============================================================
   OPREMA
   ============================================================ */

INSERT INTO oprema (naziv, opis)
VALUES
(
    'Planinarske cipele',
    'Obuca prilagodjena zahtevnijem planinarskom terenu.'
),
(
    'Ranac',
    'Planinarski ranac za opremu, hranu i vodu.'
),
(
    'Kisna jakna',
    'Vodootporna zastita od kise i vetra.'
),
(
    'Prva pomoc',
    'Osnovni komplet prve pomoci.'
),
(
    'Ceona lampa',
    'Lampa za kretanje pri slaboj vidljivosti.'
),
(
    'Planinarski stapovi',
    'Stapovi za dodatnu stabilnost tokom uspona i spusta.'
),
(
    'Voda',
    'Voda potrebna za trajanje ekspedicije.'
),
(
    'Hrana',
    'Energetski obrok za vreme ekspedicije.'
);


/* ============================================================
   RTANJ - OPREMA
   ============================================================ */

INSERT INTO ekspedicija_oprema (
    id_ekspedicije,
    id_opreme,
    obavezna,
    kolicina,
    napomena
)
VALUES
    (1, 1, TRUE, 1, 'Preporucena obuća sa dobrim djonom.'),
    (1, 2, TRUE, 1, NULL),
    (1, 3, TRUE, 1, 'Vreme na vrhu moze brzo da se promeni.'),
    (1, 4, TRUE, 1, NULL),
    (1, 6, FALSE, 1, 'Preporuceni tokom strmijih delova uspona.'),
    (1, 7, TRUE, 2, 'Najmanje dve boce vode.'),
    (1, 8, TRUE, 2, 'Dovoljno hrane za celodnevnu turu.');


/* ============================================================
   FRUSKA GORA - OPREMA
   ============================================================ */

INSERT INTO ekspedicija_oprema (
    id_ekspedicije,
    id_opreme,
    obavezna,
    kolicina,
    napomena
)
VALUES
    (2, 1, TRUE, 1, NULL),
    (2, 2, TRUE, 1, NULL),
    (2, 7, TRUE, 1, 'Jedna boca vode je dovoljna za kracu rutu.');


/* ============================================================
   TARA - OPREMA
   ============================================================ */

INSERT INTO ekspedicija_oprema (
    id_ekspedicije,
    id_opreme,
    obavezna,
    kolicina,
    napomena
)
VALUES
    (3, 1, TRUE, 1, NULL),
    (3, 2, TRUE, 1, NULL),
    (3, 3, TRUE, 1, NULL),
    (3, 4, TRUE, 1, NULL),
    (3, 7, TRUE, 2, NULL),
    (3, 8, TRUE, 1, NULL);


/* ============================================================
   MIDZOR - OPREMA
   ============================================================ */

INSERT INTO ekspedicija_oprema (
    id_ekspedicije,
    id_opreme,
    obavezna,
    kolicina,
    napomena
)
VALUES
    (4, 1, TRUE, 1, NULL),
    (4, 2, TRUE, 1, NULL),
    (4, 3, TRUE, 1, NULL),
    (4, 4, TRUE, 1, NULL),
    (4, 5, TRUE, 1, 'Poneti zbog duzine ekspedicije.'),
    (4, 6, FALSE, 1, NULL),
    (4, 7, TRUE, 3, 'Potrebna veca kolicina vode.'),
    (4, 8, TRUE, 2, NULL);


/* ============================================================
   SACUVANE EKSPEDICIJE
   ============================================================ */

INSERT INTO sacuvana_ekspedicija (
    id_korisnika,
    id_ekspedicije
)
VALUES
    (1, 3),
    (1, 4),
    (2, 1);


/* ============================================================
   MOJ PLAN
   ============================================================ */

INSERT INTO moj_plan (
    id_korisnika,
    id_ekspedicije,
    status
)
VALUES
    (1, 1, FALSE),
    (1, 3, FALSE);

INSERT INTO moj_plan (
    id_korisnika,
    id_ekspedicije,
    status
)
VALUES
    (2, 4, TRUE);