DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
    id SERIAL,
    login VARCHAR(100),
    password VARCHAR(100)
);

DROP TABLE IF EXISTS Messages;
CREATE TABLE Messages (
    id SERIAL,
    sender VARCHAR(20),
    room_id INT,
    text TEXT,
    timestamp TIMESTAMP DEFAULT now()
);

DROP TABLE IF EXISTS Rooms;
CREATE TABLE Rooms (
    id SERIAL,
    name VARCHAR(20),
    user_id INT
)