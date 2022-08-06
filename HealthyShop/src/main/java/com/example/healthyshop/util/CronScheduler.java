package com.example.healthyshop.util;

import com.example.healthyshop.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CronScheduler {
    private CommentService commentService;
    private Logger logger;

    @Autowired
    public CronScheduler(CommentService commentService) {
        this.commentService = commentService;
        this.logger = LoggerFactory.getLogger(CronScheduler.class.getSimpleName());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteUncensoredComments(){
        commentService.censorComments();
        logger.info("Censored comments made today");
    }
}
