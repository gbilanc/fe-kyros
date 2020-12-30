SET AUTODDL ON;

DROP table DF_NATURAIVA;
COMMIT;

CREATE TABLE DF_NATURAIVA
(
  CODICE varchar(5) NOT NULL,
  DESCRI varchar(255),
  CONSTRAINT PK_NATURAIVA PRIMARY KEY (CODICE)
);

ALTER TABLE ALIQIVA ALTER NATURA_DF TYPE Varchar(5);

COMMIT;

UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N0', 'Imponibile Iva');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N1', 'escluse ex art. 15');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N2', 'non soggette  (codice non più valido a partire dal primo gennaio 2021)');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N2.1', 'non soggette ad IVA ai sensi degli artt. da 7 a 7-septies del DPR 633/72');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N2.2', 'non soggette - altri casi');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3', 'non imponibili  (codice non più valido a partire dal primo gennaio 2021)');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3.1', 'non imponibili - esportazioni');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3.2', 'non imponibili - cessioni intracomunitarie');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3.3', 'non imponibili - cessioni verso San Marino');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3.4', 'non imponibili - operazioni assimilate alle cessioni all''esportazione');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3.5', 'non imponibili - a seguito di dichiarazioni d''intento');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N3.6', 'non imponibili - altre operazioni che non concorrono alla formazione del plafond');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N4', 'esenti');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N5', 'regime del margine / IVA non esposta in fattura');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6', 'inversione contabile (codice non più valido a partire dal primo gennaio 2021)...');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.1', 'inversione contabile - cessione di rottami e altri materiali di recupero');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.2', 'inversione contabile - cessione di oro e argento puro');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.3', 'inversione contabile - subappalto nel settore edile');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.4', 'inversione contabile - cessione di fabbricati');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.5', 'inversione contabile - cessione di telefoni cellulari');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.6', 'inversione contabile - cessione di prodotti elettronici');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.7', 'inversione contabile - prestazioni comparto edile e settori connessi');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.8', 'inversione contabile - operazioni settore energetico');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N6.9', 'inversione contabile - altri casi');
UPDATE OR INSERT INTO DF_NATURAIVA (CODICE, DESCRI) VALUES ('N7', 'IVA assolta in altro stato UE (vendite a distanza ex art. 40 c. 3 e 4 e art. 41 c. 1 lett. b,  DL 331/93; prestazione di servizi di telecomunicazioni, tele-radiodiffusione ed elettronici ex art. 7-sexies lett. f, g, art. 74-sexies DPR 633/72)');

COMMIT;
