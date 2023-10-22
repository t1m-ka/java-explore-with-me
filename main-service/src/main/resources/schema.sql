DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS participation;
DROP TABLE IF EXISTS event_compilation;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS compilation;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS ewm_user;

CREATE TABLE IF NOT EXISTS ewm_user (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,
    name VARCHAR(250) NOT NULL UNIQUE,
    CONSTRAINT pk_ewm_user PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT pk_category PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS compilation (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN DEFAULT FALSE,
    title VARCHAR(50) NOT NUll UNIQUE,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS event (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    confirmed_requests BIGINT DEFAULT 0,
    description VARCHAR(7000) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL,
    lat FLOAT NOT NUll,
    lon FLOAT NOT NUll,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INT DEFAULT 0,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT TRUE,
    state VARCHAR(20) NOT NULL,
    title VARCHAR(120) NOT NULL,
    views BIGINT DEFAULT 0,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_to_category FOREIGN KEY(category_id) REFERENCES category(id),
    CONSTRAINT fk_event_to_ewm_user FOREIGN KEY(initiator_id) REFERENCES ewm_user(id)
);
CREATE TABLE IF NOT EXISTS event_compilation (
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT event_compilation_pr_key PRIMARY KEY (event_id, compilation_id)
);
CREATE TABLE IF NOT EXISTS participation (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    request_status VARCHAR(20) NOT NULL,
    CONSTRAINT pk_participation PRIMARY KEY (id),
    CONSTRAINT fk_participation_to_event FOREIGN KEY(event_id) REFERENCES event(id),
    CONSTRAINT fk_participation_to_ewm_user FOREIGN KEY(requester_id) REFERENCES ewm_user(id)
);
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(1000) NOT NULL,
    event_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_on TIMESTAMP WITHOUT TIME ZONE,
    moderation_reason VARCHAR(25),
    moderation_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_to_event FOREIGN KEY(event_id) REFERENCES event(id),
    CONSTRAINT fk_comment_to_ewm_user FOREIGN KEY(author_id) REFERENCES ewm_user(id)
);