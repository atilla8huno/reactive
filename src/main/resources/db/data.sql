INSERT INTO teams(name) VALUES('Real Madrid FC');
INSERT INTO teams(name) VALUES('CR Vasco da Gama');

INSERT INTO players(name, number, team_id) VALUES('Cristiano Ronaldo', 7, (SELECT MIN(id) FROM teams));
INSERT INTO players(name, number, team_id) VALUES('Ronaldo', 9, (SELECT MIN(id) FROM teams));
INSERT INTO players(name, number, team_id) VALUES('Zinedine Zidane', 5, (SELECT MIN(id) FROM teams));
INSERT INTO players(name, number, team_id) VALUES('Roberto Dinamite', 10, (SELECT MAX(id) FROM teams));
INSERT INTO players(name, number, team_id) VALUES('Romário', 11, (SELECT MAX(id) FROM teams));
