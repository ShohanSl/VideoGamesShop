-- =========================================
-- VideoGamesShop: reset and seed script
-- =========================================
-- This script:
-- 1. clears all project data
-- 2. resets identity sequences
-- 3. inserts a fresh data set with rich relations
--
-- Created objects:
-- - developers: 4
-- - publishers: 4
-- - categories: 8
-- - games: 10
-- - users: 5
-- - game_category links: 24
-- - user_game links: 15
--
-- Relation density:
-- - every developer owns multiple games
-- - every publisher is linked with multiple games
-- - every category is linked with multiple games
-- - every user has multiple games in library
-- - most games belong to several categories

BEGIN;

-- -----------------------------------------
-- Cleanup
-- -----------------------------------------
DELETE FROM user_game;
DELETE FROM game_category;
DELETE FROM users;
DELETE FROM games;
DELETE FROM categories;
DELETE FROM publishers;
DELETE FROM developers;

ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE games_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;
ALTER SEQUENCE publishers_id_seq RESTART WITH 1;
ALTER SEQUENCE developers_id_seq RESTART WITH 1;

-- -----------------------------------------
-- Developers: 4
-- -----------------------------------------
INSERT INTO developers (id, name, country, founded_date) VALUES
    (1, 'CD Projekt Red', 'Poland', '1994-05-01'),
    (2, 'Larian Studios', 'Belgium', '1996-10-01'),
    (3, 'Santa Monica Studio', 'United States', '1999-01-01'),
    (4, 'Remedy Entertainment', 'Finland', '1995-08-18');

-- -----------------------------------------
-- Publishers: 4
-- -----------------------------------------
INSERT INTO publishers (id, name, country, founded_date) VALUES
    (1, 'CD Projekt', 'Poland', '1994-05-01'),
    (2, 'Sony Interactive Entertainment', 'Japan', '1993-11-16'),
    (3, '505 Games', 'Italy', '2006-01-01'),
    (4, 'Bandai Namco Entertainment', 'Japan', '1955-06-01');

-- -----------------------------------------
-- Categories: 8
-- -----------------------------------------
INSERT INTO categories (id, name) VALUES
    (1, 'RPG'),
    (2, 'Action'),
    (3, 'Adventure'),
    (4, 'Open World'),
    (5, 'Fantasy'),
    (6, 'Story Rich'),
    (7, 'Shooter'),
    (8, 'Mythology');

-- -----------------------------------------
-- Games: 10
-- -----------------------------------------
INSERT INTO games (id, title, price, release_date, description, developer_id, publisher_id) VALUES
    (1, 'The Witcher 3: Wild Hunt', 39.99, '2015-05-19',
        'Story-driven open world RPG about Geralt of Rivia.', 1, 1),
    (2, 'Cyberpunk 2077', 49.99, '2020-12-10',
        'Futuristic action RPG set in Night City.', 1, 1),
    (3, 'Baldur''s Gate 3', 59.99, '2023-08-03',
        'Party-based RPG inspired by Dungeons and Dragons.', 2, 4),
    (4, 'Divinity: Original Sin 2', 44.99, '2017-09-14',
        'Tactical fantasy RPG with deep party mechanics.', 2, 4),
    (5, 'God of War', 39.99, '2018-04-20',
        'Action adventure about Kratos and Atreus.', 3, 2),
    (6, 'God of War Ragnarok', 59.99, '2022-11-09',
        'Continuation of Kratos journey through the Norse realms.', 3, 2),
    (7, 'Control', 29.99, '2019-08-27',
        'Supernatural action adventure inside the Federal Bureau of Control.', 4, 3),
    (8, 'Alan Wake 2', 49.99, '2023-10-27',
        'Psychological survival horror with dual protagonists.', 4, 3),
    (9, 'Quantum Break', 24.99, '2016-04-05',
        'Action game built around time manipulation.', 4, 3),
    (10, 'Thronebreaker: The Witcher Tales', 19.99, '2018-10-23',
        'Narrative RPG card-based adventure in The Witcher universe.', 1, 1);

-- -----------------------------------------
-- Game-category links: 24
-- -----------------------------------------
INSERT INTO game_category (game_id, category_id) VALUES
    (1, 1), (1, 3), (1, 4), (1, 5), (1, 6),
    (2, 1), (2, 2), (2, 4), (2, 6),
    (3, 1), (3, 3), (3, 5), (3, 6),
    (4, 1), (4, 3), (4, 5),
    (5, 2), (5, 3), (5, 8),
    (6, 2), (6, 3), (6, 8),
    (7, 2), (7, 6), (7, 7),
    (8, 3), (8, 6), (8, 7),
    (9, 2), (9, 6), (9, 7),
    (10, 1), (10, 3), (10, 5), (10, 6);

-- -----------------------------------------
-- Users: 5
-- -----------------------------------------
INSERT INTO users (id, username) VALUES
    (1, 'gerald_player'),
    (2, 'night_city_runner'),
    (3, 'norse_saga_fan'),
    (4, 'tactical_mage'),
    (5, 'story_hunter');

-- -----------------------------------------
-- User libraries: 15
-- -----------------------------------------
INSERT INTO user_game (user_id, game_id) VALUES
    (1, 1), (1, 10), (1, 3),
    (2, 2), (2, 7), (2, 9),
    (3, 5), (3, 6), (3, 1),
    (4, 3), (4, 4), (4, 10),
    (5, 1), (5, 7), (5, 8);

-- Sync PostgreSQL sequences with actual max(id) in tables
-- Safe for both non-empty and empty tables

SELECT setval(
               pg_get_serial_sequence('developers', 'id'),
               COALESCE((SELECT MAX(id) FROM developers), 1),
               (SELECT MAX(id) IS NOT NULL FROM developers)
       );

SELECT setval(
               pg_get_serial_sequence('publishers', 'id'),
               COALESCE((SELECT MAX(id) FROM publishers), 1),
               (SELECT MAX(id) IS NOT NULL FROM publishers)
       );

SELECT setval(
               pg_get_serial_sequence('categories', 'id'),
               COALESCE((SELECT MAX(id) FROM categories), 1),
               (SELECT MAX(id) IS NOT NULL FROM categories)
       );

SELECT setval(
               pg_get_serial_sequence('games', 'id'),
               COALESCE((SELECT MAX(id) FROM games), 1),
               (SELECT MAX(id) IS NOT NULL FROM games)
       );

SELECT setval(
               pg_get_serial_sequence('users', 'id'),
               COALESCE((SELECT MAX(id) FROM users), 1),
               (SELECT MAX(id) IS NOT NULL FROM users)
       );


COMMIT;
