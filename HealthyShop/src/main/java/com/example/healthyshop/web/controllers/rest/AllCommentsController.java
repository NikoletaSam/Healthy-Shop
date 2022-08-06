package com.example.healthyshop.web.controllers.rest;

import com.example.healthyshop.model.views.CommentView;
import com.example.healthyshop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AllCommentsController {
    private CommentService commentService;

    @Autowired
    public AllCommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentView>> allComments(){
        return ResponseEntity.ok(commentService.getAllComments());
    }
}
