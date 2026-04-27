-- =========================================
-- VideoGamesShop: reset and seed script
-- =========================================
-- This script:
-- 1. clears all project data
-- 2. resets identity sequences
-- 3. inserts a larger data set with rich relations
--
-- Created objects:
-- - developers: 19
-- - publishers: 15
-- - categories: 20
-- - games: 44
-- - users: 16
-- - game_category links: 187
-- - user_game links: 128
--
-- Relation density:
-- - every game has a developer and publisher
-- - every category is linked with multiple games
-- - every user has 8 games in library
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
-- Developers: 19
-- -----------------------------------------
INSERT INTO developers (id, name, country, founded_date) VALUES
    (1, 'CD Projekt Red', 'Poland', '1994-05-01'),
    (2, 'Larian Studios', 'Belgium', '1996-10-01'),
    (3, 'Santa Monica Studio', 'United States', '1999-01-01'),
    (4, 'Remedy Entertainment', 'Finland', '1995-08-18'),
    (5, 'FromSoftware', 'Japan', '1986-11-01'),
    (6, 'Guerrilla Games', 'Netherlands', '2000-01-01'),
    (7, 'Mojang Studios', 'Sweden', '2009-05-17'),
    (8, 'Playground Games', 'United Kingdom', '2010-02-01'),
    (9, 'id Software', 'United States', '1991-02-01'),
    (10, 'Capcom', 'Japan', '1979-05-30'),
    (11, 'Supergiant Games', 'United States', '2009-09-01'),
    (12, 'ConcernedApe', 'United States', '2011-01-01'),
    (13, 'Ubisoft Montreal', 'Canada', '1997-04-25'),
    (14, 'EA Vancouver', 'Canada', '1983-01-01'),
    (15, 'Bandai Namco Studios', 'Japan', '2012-04-02'),
    (16, 'NetherRealm Studios', 'United States', '2010-04-20'),
    (17, 'Valve', 'United States', '1996-08-24'),
    (18, 'Hazelight Studios', 'Sweden', '2014-01-01'),
    (19, 'Relic Entertainment', 'Canada', '1997-06-01');

-- -----------------------------------------
-- Publishers: 15
-- -----------------------------------------
INSERT INTO publishers (id, name, country, founded_date) VALUES
    (1, 'CD Projekt', 'Poland', '1994-05-01'),
    (2, 'Sony Interactive Entertainment', 'Japan', '1993-11-16'),
    (3, '505 Games', 'Italy', '2006-01-01'),
    (4, 'Bandai Namco Entertainment', 'Japan', '1955-06-01'),
    (5, 'Xbox Game Studios', 'United States', '2000-03-01'),
    (6, 'Bethesda Softworks', 'United States', '1986-06-28'),
    (7, 'Capcom', 'Japan', '1979-05-30'),
    (8, 'Electronic Arts', 'United States', '1982-05-27'),
    (9, 'Ubisoft', 'France', '1986-03-28'),
    (10, 'Devolver Digital', 'United States', '2009-06-25'),
    (11, 'Supergiant Games', 'United States', '2009-09-01'),
    (12, 'ConcernedApe', 'United States', '2011-01-01'),
    (13, 'Warner Bros. Games', 'United States', '1993-01-14'),
    (14, 'Sega', 'Japan', '1960-06-03'),
    (15, 'Valve', 'United States', '1996-08-24');

-- -----------------------------------------
-- Categories: 20
-- -----------------------------------------
INSERT INTO categories (id, name) VALUES
    (1, 'RPG'),
    (2, 'Action'),
    (3, 'Adventure'),
    (4, 'Open World'),
    (5, 'Fantasy'),
    (6, 'Story Rich'),
    (7, 'Shooter'),
    (8, 'Mythology'),
    (9, 'Strategy'),
    (10, 'Simulation'),
    (11, 'Sports'),
    (12, 'Racing'),
    (13, 'Horror'),
    (14, 'Survival'),
    (15, 'Indie'),
    (16, 'Puzzle'),
    (17, 'Platformer'),
    (18, 'Fighting'),
    (19, 'Roguelike'),
    (20, 'Stealth');

-- -----------------------------------------
-- Games: 44
-- -----------------------------------------
INSERT INTO games (id, title, price, release_date, description, developer_id, publisher_id) VALUES
    (1, 'The Witcher 3: Wild Hunt', 39.99, '2015-05-19', 'Story-driven open world RPG about Geralt of Rivia.', 1, 1),
    (2, 'Cyberpunk 2077', 49.99, '2020-12-10', 'Futuristic action RPG set in Night City.', 1, 1),
    (3, 'Thronebreaker: The Witcher Tales', 19.99, '2018-10-23', 'Narrative card RPG set in The Witcher universe.', 1, 1),
    (4, 'Baldur''s Gate 3', 59.99, '2023-08-03', 'Party-based RPG inspired by Dungeons and Dragons.', 2, 4),
    (5, 'Divinity: Original Sin 2', 44.99, '2017-09-14', 'Tactical fantasy RPG with deep party mechanics.', 2, 4),
    (6, 'God of War', 39.99, '2018-04-20', 'Action adventure about Kratos and Atreus.', 3, 2),
    (7, 'God of War Ragnarok', 59.99, '2022-11-09', 'Continuation of Kratos journey through the Norse realms.', 3, 2),
    (8, 'Control', 29.99, '2019-08-27', 'Supernatural action adventure inside the Federal Bureau of Control.', 4, 3),
    (9, 'Alan Wake 2', 49.99, '2023-10-27', 'Psychological survival horror with dual protagonists.', 4, 3),
    (10, 'Quantum Break', 24.99, '2016-04-05', 'Action game built around time manipulation.', 4, 5),
    (11, 'Elden Ring', 59.99, '2022-02-25', 'Open world fantasy action RPG in the Lands Between.', 5, 4),
    (12, 'Sekiro: Shadows Die Twice', 39.99, '2019-03-22', 'Shinobi action adventure focused on precise combat.', 5, 6),
    (13, 'Dark Souls III', 39.99, '2016-03-24', 'Dark fantasy action RPG with challenging encounters.', 5, 4),
    (14, 'Horizon Zero Dawn', 39.99, '2017-02-28', 'Open world adventure with machines and ancient mysteries.', 6, 2),
    (15, 'Horizon Forbidden West', 59.99, '2022-02-18', 'Aloy explores the Forbidden West to uncover a new threat.', 6, 2),
    (16, 'Minecraft', 29.99, '2011-11-18', 'Creative survival sandbox about building and exploration.', 7, 5),
    (17, 'Minecraft Dungeons', 19.99, '2020-05-26', 'Accessible dungeon crawler set in the Minecraft universe.', 7, 5),
    (18, 'Forza Horizon 5', 59.99, '2021-11-09', 'Open world racing festival across Mexico.', 8, 5),
    (19, 'Forza Motorsport', 69.99, '2023-10-10', 'Track-focused racing simulation with detailed car tuning.', 8, 5),
    (20, 'DOOM Eternal', 39.99, '2020-03-20', 'Fast arena shooter about ripping through demonic armies.', 9, 6),
    (21, 'DOOM', 19.99, '2016-05-13', 'Modern reboot of the classic demon-slaying shooter.', 9, 6),
    (22, 'Resident Evil 4', 59.99, '2023-03-24', 'Survival horror remake about Leon Kennedy in rural Europe.', 10, 7),
    (23, 'Monster Hunter: World', 29.99, '2018-01-26', 'Cooperative action RPG about tracking giant monsters.', 10, 7),
    (24, 'Devil May Cry 5', 29.99, '2019-03-08', 'Stylish action game with multiple playable demon hunters.', 10, 7),
    (25, 'Hades', 24.99, '2020-09-17', 'Roguelike action RPG about escaping the underworld.', 11, 11),
    (26, 'Hades II', 29.99, '2024-05-06', 'Roguelike action sequel focused on Melinoe and witchcraft.', 11, 11),
    (27, 'Bastion', 14.99, '2011-07-20', 'Narrated action RPG in a broken fantasy world.', 11, 11),
    (28, 'Stardew Valley', 14.99, '2016-02-26', 'Farming life simulation about restoring a countryside home.', 12, 12),
    (29, 'Assassin''s Creed Valhalla', 59.99, '2020-11-10', 'Open world Viking saga blending action and stealth.', 13, 9),
    (30, 'Far Cry 6', 49.99, '2021-10-07', 'Open world shooter about rebellion on the island of Yara.', 13, 9),
    (31, 'Rainbow Six Siege', 19.99, '2015-12-01', 'Tactical competitive shooter built around destructible spaces.', 13, 9),
    (32, 'Prince of Persia: The Lost Crown', 39.99, '2024-01-18', 'Action platformer with time powers and metroidvania structure.', 13, 9),
    (33, 'EA Sports FC 24', 69.99, '2023-09-29', 'Football sports simulation with clubs and national teams.', 14, 8),
    (34, 'NHL 24', 69.99, '2023-10-06', 'Ice hockey sports simulation focused on pressure systems.', 14, 8),
    (35, 'Tekken 8', 69.99, '2024-01-26', '3D fighting game continuing the Mishima saga.', 15, 4),
    (36, 'Tales of Arise', 39.99, '2021-09-10', 'Anime-styled action RPG about liberation and identity.', 15, 4),
    (37, 'Mortal Kombat 1', 69.99, '2023-09-19', 'Fighting game reboot with brutal cinematic battles.', 16, 13),
    (38, 'Injustice 2', 19.99, '2017-05-16', 'Superhero fighting game with gear-based progression.', 16, 13),
    (39, 'Portal 2', 9.99, '2011-04-18', 'Puzzle adventure about portals, test chambers, and sharp humor.', 17, 15),
    (40, 'Half-Life 2', 9.99, '2004-11-16', 'Story-rich sci-fi shooter about resistance and survival.', 17, 15),
    (41, 'It Takes Two', 39.99, '2021-03-26', 'Cooperative adventure platformer about a fractured family.', 18, 8),
    (42, 'Split Fiction', 49.99, '2025-03-06', 'Cooperative action adventure across fantasy and sci-fi stories.', 18, 8),
    (43, 'Age of Empires IV', 39.99, '2021-10-28', 'Historical real-time strategy about medieval civilizations.', 19, 5),
    (44, 'Company of Heroes 3', 49.99, '2023-02-23', 'World War II strategy game with dynamic tactical battles.', 19, 14);

-- -----------------------------------------
-- Game-category links: 187
-- -----------------------------------------
INSERT INTO game_category (game_id, category_id) VALUES
    (1, 1), (1, 3), (1, 4), (1, 5), (1, 6),
    (2, 1), (2, 2), (2, 4), (2, 6), (2, 7), (2, 20),
    (3, 1), (3, 3), (3, 5), (3, 6), (3, 9),
    (4, 1), (4, 3), (4, 5), (4, 6), (4, 9),
    (5, 1), (5, 3), (5, 5), (5, 6), (5, 9),
    (6, 2), (6, 3), (6, 6), (6, 8),
    (7, 2), (7, 3), (7, 6), (7, 8),
    (8, 2), (8, 3), (8, 6), (8, 7), (8, 16),
    (9, 3), (9, 6), (9, 13), (9, 14),
    (10, 2), (10, 6), (10, 7), (10, 16),
    (11, 1), (11, 2), (11, 3), (11, 4), (11, 5),
    (12, 2), (12, 3), (12, 5), (12, 8), (12, 20),
    (13, 1), (13, 2), (13, 3), (13, 5),
    (14, 2), (14, 3), (14, 4), (14, 6), (14, 7),
    (15, 2), (15, 3), (15, 4), (15, 6), (15, 7),
    (16, 3), (16, 4), (16, 10), (16, 14), (16, 15),
    (17, 1), (17, 2), (17, 3), (17, 5), (17, 15),
    (18, 4), (18, 10), (18, 11), (18, 12),
    (19, 10), (19, 11), (19, 12),
    (20, 2), (20, 7), (20, 13),
    (21, 2), (21, 7), (21, 13),
    (22, 2), (22, 3), (22, 13), (22, 14),
    (23, 1), (23, 2), (23, 3), (23, 5), (23, 14),
    (24, 2), (24, 3), (24, 5),
    (25, 1), (25, 2), (25, 5), (25, 15), (25, 19),
    (26, 1), (26, 2), (26, 5), (26, 15), (26, 19),
    (27, 1), (27, 2), (27, 3), (27, 5), (27, 15),
    (28, 1), (28, 10), (28, 14), (28, 15),
    (29, 1), (29, 2), (29, 3), (29, 4), (29, 8), (29, 20),
    (30, 2), (30, 3), (30, 4), (30, 7), (30, 20),
    (31, 2), (31, 7), (31, 9), (31, 20),
    (32, 2), (32, 3), (32, 5), (32, 16), (32, 17),
    (33, 10), (33, 11),
    (34, 10), (34, 11),
    (35, 2), (35, 6), (35, 18),
    (36, 1), (36, 2), (36, 3), (36, 5), (36, 6),
    (37, 2), (37, 6), (37, 18),
    (38, 2), (38, 6), (38, 18),
    (39, 3), (39, 6), (39, 15), (39, 16),
    (40, 2), (40, 3), (40, 6), (40, 7),
    (41, 3), (41, 6), (41, 15), (41, 16), (41, 17),
    (42, 2), (42, 3), (42, 6), (42, 16), (42, 17),
    (43, 6), (43, 9), (43, 10),
    (44, 6), (44, 9), (44, 10);

-- -----------------------------------------
-- Users: 16
-- -----------------------------------------
INSERT INTO users (id, username) VALUES
    (1, 'gerald_player'),
    (2, 'night_city_runner'),
    (3, 'norse_saga_fan'),
    (4, 'tactical_mage'),
    (5, 'story_hunter'),
    (6, 'souls_veteran'),
    (7, 'racing_ace'),
    (8, 'coop_builder'),
    (9, 'horror_reader'),
    (10, 'arena_fighter'),
    (11, 'strategy_captain'),
    (12, 'indie_farmer'),
    (13, 'open_world_nomad'),
    (14, 'sports_manager'),
    (15, 'puzzle_portalist'),
    (16, 'stealth_operator');

-- -----------------------------------------
-- User libraries: 128
-- -----------------------------------------
INSERT INTO user_game (user_id, game_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 11), (1, 13), (1, 25), (1, 28), (1, 36),
    (2, 2), (2, 8), (2, 10), (2, 14), (2, 15), (2, 30), (2, 31), (2, 40),
    (3, 6), (3, 7), (3, 11), (3, 12), (3, 23), (3, 29), (3, 35), (3, 37),
    (4, 3), (4, 4), (4, 5), (4, 17), (4, 25), (4, 26), (4, 36), (4, 43),
    (5, 1), (5, 4), (5, 8), (5, 9), (5, 14), (5, 22), (5, 39), (5, 41),
    (6, 11), (6, 12), (6, 13), (6, 20), (6, 21), (6, 23), (6, 24), (6, 25),
    (7, 18), (7, 19), (7, 30), (7, 33), (7, 34), (7, 35), (7, 38), (7, 42),
    (8, 16), (8, 17), (8, 28), (8, 39), (8, 41), (8, 42), (8, 43), (8, 44),
    (9, 8), (9, 9), (9, 20), (9, 21), (9, 22), (9, 30), (9, 31), (9, 40),
    (10, 24), (10, 32), (10, 35), (10, 36), (10, 37), (10, 38), (10, 41), (10, 42),
    (11, 3), (11, 4), (11, 5), (11, 31), (11, 33), (11, 34), (11, 43), (11, 44),
    (12, 16), (12, 25), (12, 26), (12, 27), (12, 28), (12, 39), (12, 41), (12, 42),
    (13, 1), (13, 2), (13, 11), (13, 14), (13, 15), (13, 18), (13, 29), (13, 30),
    (14, 18), (14, 19), (14, 23), (14, 31), (14, 33), (14, 34), (14, 35), (14, 38),
    (15, 8), (15, 10), (15, 32), (15, 39), (15, 40), (15, 41), (15, 42), (15, 43),
    (16, 2), (16, 12), (16, 29), (16, 30), (16, 31), (16, 40), (16, 41), (16, 44);

-- Sync PostgreSQL sequences with actual max(id) in tables.
-- Safe for both non-empty and empty tables.
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
