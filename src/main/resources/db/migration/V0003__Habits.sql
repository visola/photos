CREATE TABLE habit (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  user_id BIGINT NOT NULL REFERENCES user(id),
  type VARCHAR(50) NOT NULL,

  created BIGINT NOT NULL,
  updated BIGINT NOT NULL,

  `interval` INT NOT NULL,
  interval_unit VARCHAR(50) NOT NULL,

  frequency INT NOT NULL,
  frequency_type VARCHAR(50) NOT NULL,
  frequency_unit VARCHAR(50)
);

CREATE TABLE manual_practice (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  happened_at BIGINT NOT NULL,
  habit_id BIGINT NOT NULL REFERENCES habit(id),
  description VARCHAR(255)
);
