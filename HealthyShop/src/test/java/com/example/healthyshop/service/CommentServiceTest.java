package com.example.healthyshop.service;

import com.example.healthyshop.model.Comment;
import com.example.healthyshop.model.Role;
import com.example.healthyshop.model.User;
import com.example.healthyshop.model.dtos.CommentCreationDto;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.model.views.CommentView;
import com.example.healthyshop.repository.CommentRepository;
import com.example.healthyshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    private CommentService toTest;

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new CommentService(mockUserRepository, mockCommentRepository);
    }

    @Test
    void testAddCommentByUser() {
        CommentCreationDto commentCreationDto = new CommentCreationDto();
        commentCreationDto.setText("My first comment text test");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        Comment comment = new Comment();

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(mockCommentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertEquals(comment, toTest.addComment(commentCreationDto, testUser.getUsername()));
    }

    @Test
    void testGetAllComments() {
        LocalDateTime dateTime = LocalDateTime.of(2001, 9, 1, 13, 29, 13);

        User testAuthor = new User("testUser", "1234567", "test@user.com", "User Testov", 30);

        Comment firstComment = new Comment();
        firstComment.setText("My first comment text");
        firstComment.setCreated(dateTime);
        firstComment.setAuthor(testAuthor);

        Comment secondComment = new Comment();
        secondComment.setText("My second comment text");
        secondComment.setCreated(dateTime);
        secondComment.setAuthor(testAuthor);

        when(mockCommentRepository.findAll()).thenReturn(List.of(firstComment, secondComment));

        List<CommentView> allComments = toTest.getAllComments();

        Assertions.assertEquals(2, allComments.size());
        Assertions.assertEquals("My first comment text", allComments.get(0).getText());
        Assertions.assertEquals("My second comment text", allComments.get(1).getText());
    }

    @Test
    void testCensorComments() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 8, 6, 13, 29, 13);

        User testAuthor = new User("testUser", "1234567", "test@user.com", "User Testov", 30);

        Comment firstComment = new Comment();
        firstComment.setText("My first comment text");
        firstComment.setCreated(dateTime);
        firstComment.setAuthor(testAuthor);

        Comment secondComment = new Comment();
        secondComment.setText("My second comment text - kill you");
        secondComment.setCreated(dateTime);
        secondComment.setAuthor(testAuthor);

        when(mockCommentRepository.findAll()).thenReturn(List.of(firstComment, secondComment));

        List<Comment> uncensored = toTest.censorComments();

        Assertions.assertEquals(1, uncensored.size());
    }

    @Test
    void testCommentIsUncensoredReturnsTrue() {
        LocalDateTime dateTime = LocalDateTime.of(2001, 9, 1, 13, 29, 13);

        User testAuthor = new User("testUser", "1234567", "test@user.com", "User Testov", 30);

        Comment comment = new Comment();
        comment.setText("My test comment text - kill you");
        comment.setCreated(dateTime);
        comment.setAuthor(testAuthor);

        Assertions.assertTrue(toTest.commentIsUncensored(comment));
    }

    @Test
    void testCommentIsUncensoredReturnsFalse() {
        LocalDateTime dateTime = LocalDateTime.of(2001, 9, 1, 13, 29, 13);

        User testAuthor = new User("testUser", "1234567", "test@user.com", "User Testov", 30);

        Comment comment = new Comment();
        comment.setText("My test comment text - it is censored");
        comment.setCreated(dateTime);
        comment.setAuthor(testAuthor);

        Assertions.assertFalse(toTest.commentIsUncensored(comment));
    }

    @Test
    void testIsCommentMadeTodayReturnsTrue() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 8, 6, 13, 29, 13);

        User testAuthor = new User("testUser", "1234567", "test@user.com", "User Testov", 30);

        Comment comment = new Comment();
        comment.setText("My test comment text - interesting food");
        comment.setCreated(dateTime);
        comment.setAuthor(testAuthor);

        Assertions.assertTrue(toTest.isCommentMadeToday(comment));
    }

    @Test
    void testIsCommentMadeTodayReturnsFalse() {
        LocalDateTime dateTime = LocalDateTime.of(2001, 9, 1, 13, 29, 13);

        User testAuthor = new User("testUser", "1234567", "test@user.com", "User Testov", 30);

        Comment comment = new Comment();
        comment.setText("My test comment text - interesting food");
        comment.setCreated(dateTime);
        comment.setAuthor(testAuthor);

        Assertions.assertFalse(toTest.isCommentMadeToday(comment));
    }
}
