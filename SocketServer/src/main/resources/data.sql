INSERT INTO "user" (login, password) VALUES ('user1', 'password1');
INSERT INTO "user" (login, password) VALUES ('user2', 'password2');
INSERT INTO "user" (login, password) VALUES ('user3', 'password3');
INSERT INTO "user" (login, password) VALUES ('a', 'a');

INSERT INTO Rooms (name, user_id) VALUES ('room1', 1);
INSERT INTO Rooms (name, user_id) VALUES ('room2', 2);
INSERT INTO Rooms (name, user_id) VALUES ('room3', 3);

INSERT INTO Messages (sender, room_id, text) VALUES ('user1', 1, 'message1');
INSERT INTO Messages (sender, room_id, text) VALUES ('user1', 1, 'message2');
INSERT INTO Messages (sender, room_id, text) VALUES ('user1', 1, 'message3');
INSERT INTO Messages (sender, room_id, text) VALUES ('user2', 2, 'Hello, room2!');
INSERT INTO Messages (sender, room_id, text) VALUES ('user3', 3, 'Hello, room3!');