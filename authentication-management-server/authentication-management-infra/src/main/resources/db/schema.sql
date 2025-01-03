CREATE SCHEMA IF NOT EXISTS eventuate;

DROP Table IF Exists eventuate.saga_instance_participants;

DROP Table IF Exists eventuate.saga_instance;

DROP Table IF Exists eventuate.saga_lock_table;

DROP Table IF Exists eventuate.saga_stash_table;

DROP Table IF Exists eventuate.message;
DROP Table IF Exists eventuate.received_messages;
DROP Table IF Exists eventuate.offset_store;

CREATE TABLE eventuate.message (
  id VARCHAR(767) PRIMARY KEY,
  destination VARCHAR(1000) NOT NULL,
  headers VARCHAR(1000) NOT NULL,
  payload VARCHAR(1000) NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT,
  message_partition VARCHAR(255)
);

CREATE INDEX message_published_idx ON eventuate.message(published, id);

CREATE TABLE eventuate.received_messages (
  consumer_id VARCHAR(767),
  message_id VARCHAR(767),
  PRIMARY KEY(consumer_id, message_id),
  creation_time BIGINT
);

CREATE TABLE eventuate.offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset VARCHAR(255)
);

CREATE TABLE eventuate.saga_instance_participants (
    saga_type VARCHAR(255) NOT NULL,
    saga_id VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    PRIMARY KEY (
        saga_type,
        saga_id,
        destination,
        resource
    )
);

CREATE TABLE eventuate.saga_instance (
    saga_type VARCHAR(255) NOT NULL,
    saga_id VARCHAR(100) NOT NULL,
    state_name VARCHAR(100) NOT NULL,
    last_request_id VARCHAR(100),
    end_state BOOLEAN,
    compensating BOOLEAN,
    failed BOOLEAN,
    saga_data_type VARCHAR(1000) NOT NULL,
    saga_data_json VARCHAR(1000) NOT NULL,
    PRIMARY KEY (saga_type, saga_id)
);

create table eventuate.saga_lock_table (
    target VARCHAR(100) PRIMARY KEY,
    saga_type VARCHAR(255) NOT NULL,
    saga_Id VARCHAR(100) NOT NULL
);

create table eventuate.saga_stash_table (
    message_id VARCHAR(100) PRIMARY KEY,
    target VARCHAR(100) NOT NULL,
    saga_type VARCHAR(255) NOT NULL,
    saga_id VARCHAR(100) NOT NULL,
    message_headers VARCHAR(1000) NOT NULL,
    message_payload VARCHAR(1000) NOT NULL
);
