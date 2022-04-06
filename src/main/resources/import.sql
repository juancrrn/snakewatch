-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES 
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'admin','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'user1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'user2','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'user3','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO Friendship (id, user1_id, user2_id, status) 
VALUES 
    (NEXT VALUE FOR friendship_id_seq, 1, 2, 0), 
    (NEXT VALUE FOR friendship_id_seq, 3, 4, 1), 
    (NEXT VALUE FOR friendship_id_seq, 4, 3, 1),
    (NEXT VALUE FOR friendship_id_seq, 1, 3, 1), 
    (NEXT VALUE FOR friendship_id_seq, 3, 1, 1), 
    (NEXT VALUE FOR friendship_id_seq, 2, 4, 1),
    (NEXT VALUE FOR friendship_id_seq, 4, 2, 1);

INSERT INTO Level (id, representation) 
VALUES (NEXT VALUE FOR level_id_seq, 'tmp');

INSERT INTO Room (id, visibility, max_users) 
VALUES 
    (NEXT VALUE FOR room_id_seq, 0, 10),
    (NEXT VALUE FOR room_id_seq, 0, 5),
    (NEXT VALUE FOR room_id_seq, 1, 5);

/*INSERT INTO Room_User (id, room_id, user_id, admin) 
VALUES 
    (NEXT VALUE FOR roomuser_id_seq, 1, 1, true),
    (NEXT VALUE FOR roomuser_id_seq, 1, 2, false),
    (NEXT VALUE FOR roomuser_id_seq, 1, 3, false),
    (NEXT VALUE FOR roomuser_id_seq, 1, 4, false),
    (NEXT VALUE FOR roomuser_id_seq, 2, 1, false),
    (NEXT VALUE FOR roomuser_id_seq, 2, 2, false),
    (NEXT VALUE FOR roomuser_id_seq, 2, 3, true),
    (NEXT VALUE FOR roomuser_id_seq, 2, 4, false),
    (NEXT VALUE FOR roomuser_id_seq, 3, 1, false),
    (NEXT VALUE FOR roomuser_id_seq, 3, 2, true),
    (NEXT VALUE FOR roomuser_id_seq, 3, 3, false),
    (NEXT VALUE FOR roomuser_id_seq, 3, 4, false);
*/
INSERT INTO Match (id, room_id, level_id, status, max_players, date) 
VALUES 
    (NEXT VALUE FOR match_id_seq, 1, 1, 3, 5, CURDATE()),
    (NEXT VALUE FOR match_id_seq, 1, 1, 3, 5, CURDATE()),
    (NEXT VALUE FOR match_id_seq, 1, 1, 3, 5, CURDATE()),
    (NEXT VALUE FOR match_id_seq, 1, 1, 3, 5, CURDATE());

INSERT INTO Match_player (id, player_id, match_id, position) 
VALUES 
    (NEXT VALUE FOR matchplayer_id_seq, 1, 1, 1),
    (NEXT VALUE FOR matchplayer_id_seq, 2, 1, 2),
    (NEXT VALUE FOR matchplayer_id_seq, 3, 1, 3),
    (NEXT VALUE FOR matchplayer_id_seq, 4, 1, 4);

INSERT INTO Match_player (id, player_id, match_id, position) 
VALUES 
    (NEXT VALUE FOR matchplayer_id_seq, 1, 2, 1),
    (NEXT VALUE FOR matchplayer_id_seq, 2, 2, 2),
    (NEXT VALUE FOR matchplayer_id_seq, 3, 2, 3),
    (NEXT VALUE FOR matchplayer_id_seq, 4, 2, 4);

INSERT INTO Match_player (id, player_id, match_id, position) 
VALUES 
    (NEXT VALUE FOR matchplayer_id_seq, 1, 3, 2),
    (NEXT VALUE FOR matchplayer_id_seq, 2, 3, 1),
    (NEXT VALUE FOR matchplayer_id_seq, 3, 3, 4),
    (NEXT VALUE FOR matchplayer_id_seq, 4, 3, 3);

INSERT INTO Match_player (id, player_id, match_id, position) 
VALUES 
    (NEXT VALUE FOR matchplayer_id_seq, 1, 4, 3),
    (NEXT VALUE FOR matchplayer_id_seq, 2, 4, 4),
    (NEXT VALUE FOR matchplayer_id_seq, 3, 4, 1),
    (NEXT VALUE FOR matchplayer_id_seq, 4, 4, 2);

INSERT INTO User_report (id, reasons, status, moderator_user_id, reported_user_id, reporting_user_id) 
VALUES 
    (NEXT VALUE FOR userreport_id_seq, 'Cheater', 0, NULL, 2, 3),
    (NEXT VALUE FOR userreport_id_seq, 'Cheater', 1, 1, 3, 4),
    (NEXT VALUE FOR userreport_id_seq, 'Cheater', 2, 1, 4, 2),
    (NEXT VALUE FOR userreport_id_seq, 'Cheater', 0, 1, 2, 3);
