/* DB Creation */
CREATE DATABASE IF NOT EXISTS db_paymybuddy;

USE db_paymybuddy;

/* Tables Creation */
CREATE TABLE IF NOT EXISTS t_user (
	id_user int NOT NULL AUTO_INCREMENT,
	user varchar(50) UNIQUE,
	email varchar(50) UNIQUE,
	password varchar(100),
	PRIMARY KEY (id_user)
);

CREATE TABLE IF NOT EXISTS t_transaction (
	id_transaction int NOT NULL AUTO_INCREMENT,
	sender int,
	receiver int,
	description varchar(50),
	amount double,
    date_transaction datetime,
	PRIMARY KEY (id_transaction)
);

CREATE TABLE IF NOT EXISTS t_connection (
	user_from int NOT NULL,
	user_to int NOT NULL,
    date_added datetime
);

ALTER TABLE t_transaction ADD FOREIGN KEY (sender) REFERENCES t_user(id_user) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE t_transaction ADD FOREIGN KEY (receiver) REFERENCES t_user(id_user) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE t_connection ADD FOREIGN KEY (user_from) REFERENCES t_user(id_user) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE t_connection ADD FOREIGN KEY (user_to) REFERENCES t_user(id_user) ON UPDATE CASCADE ON DELETE RESTRICT;

INSERT INTO t_user (user, email, password) VALUES 
("JackCurtis", "jackcurtis@gmail.com", "jackcurtis66"),
("Santana", "santana@gmail.com", "santanaBurrito"),
("Wong", "wongwong@gmail.com", "wongwongwong");

INSERT INTO t_transaction (sender, receiver, description, amount, date_transaction) VALUES 
((SELECT id_user FROM t_user WHERE user = "JackCurtis"), (SELECT id_user FROM t_user WHERE user = "Wong"), "Loyer Avril 2025", 1080, "2025-04-05 09:00:00"),
((SELECT id_user FROM t_user WHERE user = "JackCurtis"), (SELECT id_user FROM t_user WHERE user = "Santana"), "Sachet de Pop Corn", 9999.99, "2025-02-07 11:00:00");

INSERT INTO t_connection (user_from, user_to, date_added) VALUES 
((SELECT id_user FROM t_user WHERE user = "JackCurtis"), (SELECT id_user FROM t_user WHERE user = "Wong"), "2025-01-08 11:20:00"),
((SELECT id_user FROM t_user WHERE user = "Wong"), (SELECT id_user FROM t_user WHERE user = "JackCurtis"), "2025-01-08 11:25:00"),
((SELECT id_user FROM t_user WHERE user = "JackCurtis"), (SELECT id_user FROM t_user WHERE user = "Santana"), "2025-01-09 09:10:00");
