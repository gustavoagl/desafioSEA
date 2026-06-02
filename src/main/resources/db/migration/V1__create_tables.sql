CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE solicitations (
                               id BIGSERIAL PRIMARY KEY,
                               client_id BIGINT NOT NULL,
                               status VARCHAR(30) NOT NULL,
                               current_step INTEGER NOT NULL DEFAULT 1,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               submitted_at TIMESTAMP NULL,
                               analyzed_at TIMESTAMP NULL,
                               analyzed_by BIGINT NULL,
                               analysis_comment VARCHAR(1000) NULL,

                               service_type VARCHAR(30) NULL,
                               title VARCHAR(80) NULL,
                               description VARCHAR(1000) NULL,

                               cep VARCHAR(8) NULL,
                               number VARCHAR(20) NULL,
                               complement VARCHAR(100) NULL,
                               street VARCHAR(150) NULL,
                               neighborhood VARCHAR(150) NULL,
                               city VARCHAR(150) NULL,
                               state VARCHAR(2) NULL,

                               priority VARCHAR(20) NULL,
                               preferred_date DATE NULL,
                               estimated_value NUMERIC(12, 2) NULL,
                               terms_accepted BOOLEAN NULL,

                               CONSTRAINT fk_solicitations_client
                                   FOREIGN KEY (client_id)
                                       REFERENCES users (id),

                               CONSTRAINT fk_solicitations_analyzed_by
                                   FOREIGN KEY (analyzed_by)
                                       REFERENCES users (id)
);

CREATE INDEX idx_solicitations_client_id ON solicitations (client_id);
CREATE INDEX idx_solicitations_status ON solicitations (status);
CREATE INDEX idx_solicitations_state ON solicitations (state);
CREATE INDEX idx_solicitations_submitted_at ON solicitations (submitted_at);

CREATE TABLE analyst_coverage (
                                  id BIGSERIAL PRIMARY KEY,
                                  user_id BIGINT NOT NULL,
                                  state VARCHAR(2) NOT NULL,

                                  CONSTRAINT fk_analyst_coverage_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES users (id),

                                  CONSTRAINT uk_analyst_coverage_user_state
                                      UNIQUE (user_id, state)
);

CREATE INDEX idx_analyst_coverage_user_id ON analyst_coverage (user_id);
CREATE INDEX idx_analyst_coverage_state ON analyst_coverage (state);