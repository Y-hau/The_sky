package com.yhau.core.async;

import java.util.List;

/**
 * 事件处理者  接口
 */
public interface EventHandler {
    void doHandler(EventModel model);

    List<EventType> getSupportEventTypes();
}
