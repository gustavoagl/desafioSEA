CREATE TABLE analyst_coverage (
                                  id BIGSERIAL PRIMARY KEY,
                                  analyst_id BIGINT NOT NULL,
                                  state VARCHAR(2) NOT NULL,
                                  CONSTRAINT fk_analyst_coverage_user
                                      FOREIGN KEY (analyst_id)
                                          REFERENCES users(id)
);