package com.test.myproject.asyn;

import java.util.HashMap;
import java.util.Map;

public class EventModel {

    private EventType eventType;
    private int entityType;
    private int entityId;
    private int actorId;
    private int getEntityOwnerId;

    private Map<String,String> exts=new HashMap<>();

    public EventModel(){

    }

    public EventModel(EventType eventType){
        this.eventType=eventType;
    }



    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getGetEntityOwnerId() {
        return getEntityOwnerId;
    }

    public EventModel setGetEntityOwnerId(int getEntityOwnerId) {
        this.getEntityOwnerId = getEntityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }
    public String getExt(String name) {
        return exts.get(name);
    }

    public EventModel setExt(String name, String value) {
        exts.put(name, value);
        return this;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
