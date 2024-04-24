CREATE TYPE "task_status_type" AS ENUM ('PENDING','COMPLETED','IN_PROGRESS','TO_DO','TESTING');
CREATE TABLE task
(
    id          BIGSERIAL NOT NULL,
    title       VARCHAR(50),
    description VARCHAR(255),
    status      task_status_type,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);
