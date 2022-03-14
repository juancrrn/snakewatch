-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (NEXT VALUE FOR user_id_seq, TRUE, 'USER,ADMIN', 'admin',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user1',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user2',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (NEXT VALUE FOR user_id_seq, TRUE, 'USER', 'user3',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO Friendship (user1_id, user2_id, status) 
VALUES (1, 2, 1), (3, 4, 1), (1, 3, 1), (2, 4, 1);
