CREATE TABLE users
(
    id    UUID PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE meetings
(
    id   UUID PRIMARY KEY,
    time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE user_meeting
(
    user_id    UUID NOT NULL,
    meeting_id UUID NOT NULL,
    PRIMARY KEY (user_id, meeting_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (meeting_id) REFERENCES meetings (id)
);

COPY users (id, name, email)
    FROM '/docker-entrypoint-initdb.d/init_users.csv'
    DELIMITER ',' CSV HEADER;

COPY meetings (id, time)
    FROM '/docker-entrypoint-initdb.d/init_meetings.csv'
    DELIMITER ',' CSV HEADER;

COPY user_meeting (user_id, meeting_id)
    FROM '/docker-entrypoint-initdb.d/init_user_meeting.csv'
    DELIMITER ',' CSV HEADER;
