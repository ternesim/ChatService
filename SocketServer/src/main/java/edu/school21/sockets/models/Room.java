package edu.school21.sockets.models;

import java.util.List;

public class Room {
    Long id;
    String name;
    Long creator_id;
    List<Message> messageList;

    public Room(Long id, String name, Long creator_id) {
        this.id = id;
        this.name = name;
        this.creator_id = creator_id;
    }

    public Long getCreatorId() {
        return creator_id;
    }

    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getLastMessages() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}