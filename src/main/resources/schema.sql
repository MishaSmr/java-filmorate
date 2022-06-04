CREATE TABLE PUBLIC.FILM (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(50) NOT NULL,
	DESCRIPTION CHARACTER VARYING(200),
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER,
	RATING_ID INTEGER NOT NULL,
	CONSTRAINT FILM_PK PRIMARY KEY (ID),
	CONSTRAINT FILM_FK FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.RATING(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FILM_FK_INDEX_2 ON PUBLIC.FILM (RATING_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_2 ON PUBLIC.FILM (ID);
