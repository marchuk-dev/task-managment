CREATE TABLE task
(
    id          BIGSERIAL NOT NULL,
    title       VARCHAR(50),
    description VARCHAR(255),
    status      VARCHAR(255),
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);
