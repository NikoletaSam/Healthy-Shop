package com.example.healthyshop.service;

import com.example.healthyshop.model.Comment;
import com.example.healthyshop.model.dtos.CommentCreationDto;
import com.example.healthyshop.model.views.CommentView;
import com.example.healthyshop.repository.CommentRepository;
import com.example.healthyshop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private ModelMapper mapper;
    private List<String> uncensoredWords;

    @Autowired
    public CommentService(UserRepository userRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.mapper = new ModelMapper();
        this.uncensoredWords = List.of("kill", "die", "killing", "dying", "harm", "kick", "fight", "shoot", "murder", "death");
    }

    public Comment addComment(CommentCreationDto commentDto, String username){
        Comment comment = mapper.map(commentDto, Comment.class);

        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(userRepository.findUserByUsername(username).get());

        return commentRepository.save(comment);
    }

    public List<CommentView> getAllComments(){
        List<Comment> comments = commentRepository
                .findAll()
                .stream()
                .toList();
        List<CommentView> views = new ArrayList<>();

        for (Comment currentComment : comments){
            CommentView view = mapper.map(currentComment, CommentView.class);
            views.add(view);
        }

        return views;
    }

    public List<Comment> censorComments(){
        List<Comment> uncensored = commentRepository
                .findAll()
                .stream()
                .filter(this::isCommentMadeToday)
                .filter(this::commentIsUncensored)
                .toList();

        commentRepository.deleteAll(uncensored);
        return uncensored;
    }

    public boolean isCommentMadeToday(Comment comment){
        LocalDateTime creationDate = comment.getCreated();
        LocalDateTime currentDateTime = LocalDateTime.now();

        return creationDate.getYear() == currentDateTime.getYear() && creationDate.getMonth() == currentDateTime.getMonth()
                && creationDate.getDayOfMonth() == currentDateTime.getDayOfMonth();
    }

    public boolean commentIsUncensored(Comment comment){
        for (String word : this.uncensoredWords){
            if (comment.getText().contains(word)){
                return true;
            }
        }

        return false;
    }
}
