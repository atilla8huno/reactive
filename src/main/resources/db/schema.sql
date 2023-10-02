CREATE TABLE IF NOT EXISTS teams
(
    id         SERIAL,
    name       VARCHAR(255),
    founded_at DATE,
    CONSTRAINT pk_teams_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS players
(
    id      SERIAL,
    name    VARCHAR(255),
    number  INT,
    team_id INT,
    CONSTRAINT pk_players_id PRIMARY KEY (id),
    CONSTRAINT fk_players_team_id FOREIGN KEY (team_id) REFERENCES teams (id)
);
