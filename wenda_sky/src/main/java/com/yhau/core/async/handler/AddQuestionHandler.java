package com.yhau.core.async.handler;

import com.yhau.core.async.EventHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventType;
import com.yhau.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);
    @Autowired
    private SearchService searchService;

    @Override
    public void doHandler(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(), model.getExt("title"), model.getExt("content"));
        } catch (Exception e) {
            logger.error("增加题目索引失败" + e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
