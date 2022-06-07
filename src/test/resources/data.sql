INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,RATING_ID,RATE) VALUES
	 ('Film first','New film update decription','1989-04-17',190,5,4),
	 ('New film','New film about friends','1999-04-30',120,3,4);
INSERT INTO PUBLIC.FR_USER (EMAIL,LOGIN,NAME,BIRTHDAY) VALUES
	 ('mail@yandex.ru','dolore','est adipisicing','1976-09-20'),
	 ('friend@mail.ru','friend','friend adipisicing','1976-08-20'),
	 ('friend@common.ru','common','common','2000-08-20');
INSERT INTO PUBLIC.FRIENDS (USER_ID,FRIEND_ID,STATUS) VALUES
	 (1,3,1),
	 (2,3,1);
INSERT INTO PUBLIC.RATING (NAME) VALUES
	 ('G'),
	 ('PG'),
	 ('PG-13'),
	 ('R'),
	 ('NC-17');
