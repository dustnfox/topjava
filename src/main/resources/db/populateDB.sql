DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

/*
  Meals
*/
DELETE FROM meals;

INSERT INTO meals (user_id, date, calories, description) VALUES
  (100000, '2018-03-16 09:05:00' :: TIMESTAMP, 500, 'Breakfast'),
  (100000, '2018-03-16 12:11:00' :: TIMESTAMP, 1000, 'Lunch'),
  (100000, '2018-03-16 18:28:00' :: TIMESTAMP, 300, 'Dinner'),
  (100000, '2018-03-17 08:37:00' :: TIMESTAMP, 500, 'Breakfast'),
  (100000, '2018-03-17 11:48:00' :: TIMESTAMP, 1000, 'Lunch'),
  (100000, '2018-03-17 19:00:00' :: TIMESTAMP, 700, 'Dinner');
