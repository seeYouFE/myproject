package com.test.myproject.asyn;

import java.util.List;

public interface EventHandler {
    void doHandle(EventModel eventModel);

    List<EventType> getSupportTypes();

}
