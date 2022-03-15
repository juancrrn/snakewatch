-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES 
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'admin','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user2','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user3','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO Friendship (user1_id, user2_id, status) 
VALUES 
    (1, 2, 1), 
    (3, 4, 1), 
    (1, 3, 1), 
    (2, 4, 1);

INSERT INTO Level (id, representation) 
VALUES (NEXT VALUE FOR level_id_seq, 'tmp');

INSERT INTO Room (id, visibility, max_users) 
VALUES (NEXT VALUE FOR room_id_seq, 0, 10);

INSERT INTO Room_User (room_id, user_id, admin) 
VALUES 
    (1, 1, true),
    (1, 2, false),
    (1, 3, false),
    (1, 4, false);

INSERT INTO Match (id, room_id, level_id, status, max_players) 
VALUES 
    (NEXT VALUE FOR match_id_seq, 1, 1, 3, 5),
    (NEXT VALUE FOR match_id_seq, 1, 1, 3, 5);

INSERT INTO Match_player (player_id, match_id, position) 
VALUES 
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
    (4, 1, 4);

INSERT INTO Match_player (player_id, match_id, position) 
VALUES 
    (1, 2, 1),
    (2, 2, 2),
    (3, 2, 3),
    (4, 2, 4);
