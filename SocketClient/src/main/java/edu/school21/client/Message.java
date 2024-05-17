package edu.school21.client;

import java.sql.Timestamp;

public class Message {
    Long id;
    String sender;
    String text;
    Timestamp timestamp;
    Long roomId;

    public Message() {}

    public Message(Long id, String sender, String text, Long roomId, Timestamp timestamp) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.roomId = roomId;
        this.timestamp = timestamp;
    }
    public Message(String sender, String text, Long roomId) {
        this.sender = sender;
        this.text = text;
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
