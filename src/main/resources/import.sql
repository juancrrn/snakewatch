-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password, skin)
VALUES 
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'admin','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'white.png'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'white.png'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user2','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'white.png'),
    (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user3','{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'white.png');

INSERT INTO Friendship (id, user1_id, user2_id, status) 
VALUES 
    (NEXT VALUE FOR friendship_id_seq, 1, 2, 0), 
    (NEXT VALUE FOR friendship_id_seq, 3, 4, 1), 
    (NEXT VALUE FOR friendship_id_seq, 4, 3, 1),
    (NEXT VALUE FOR friendship_id_seq, 1, 3, 1), 
    (NEXT VALUE FOR friendship_id_seq, 3, 1, 1), 
    (NEXT VALUE FOR friendship_id_seq, 2, 4, 1),
    (NEXT VALUE FOR friendship_id_seq, 4, 2, 1);

INSERT INTO Room (id, visibility, max_users, owner_id) 
VALUES 
    (NEXT VALUE FOR room_id_seq, 0, 6, 1),
    (NEXT VALUE FOR room_id_seq, 0, 5, 2),
    (NEXT VALUE FOR room_id_seq, 1, 5, 3);

INSERT INTO Room_Users (room_id, user_id) 
VALUES 
    (1, 1),
    (2, 2),
    (3, 3);

INSERT INTO Match (id, room_id, status, date) 
VALUES 
    (NEXT VALUE FOR match_id_seq, 1, 3, CURDATE()),
    (NEXT VALUE FOR match_id_seq, 1, 3, CURDATE()),
    (NEXT VALUE FOR match_id_seq, 1, 3, CURDATE()),
    (NEXT VALUE FOR match_id_seq, 1, 3, CURDATE());

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
    (NEXT VALUE FOR userreport_id_seq, 'Cheater', 1, 1, 4, 2),
    (NEXT VALUE FOR userreport_id_seq, 'Cheater', 0, 1, 2, 3);

INSERT INTO Level (id)
VALUES 
(NEXT VALUE FOR level_id_seq),
(NEXT VALUE FOR level_id_seq),
(NEXT VALUE FOR level_id_seq);

INSERT INTO User_Level (id, player_id, level_id, highscore)
VALUES 
(NEXT VALUE FOR userlevel_id_seq, 1, 1, 4),
(NEXT VALUE FOR userlevel_id_seq, 1, 2, 1),
(NEXT VALUE FOR userlevel_id_seq, 3, 3, 10);