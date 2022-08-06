package com.example.healthyshop.web.controllers.rest;

import com.example.healthyshop.model.views.CommentView;
import com.example.healthyshop.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AllCommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void getComments_CommentsReturnedAsJsonAndStatusIsOk() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2001, 9, 1, 13, 29, 13);
        when(commentService.getAllComments())
                .thenReturn(List.of(
                        new CommentView("User Userov", "comment example text", dateTime),
                        new CommentView("Admin Adminov", "second comment example text", dateTime)));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].authorFullName", is("User Userov")))
                .andExpect(jsonPath("$.[0].text", is("comment example text")))
                .andExpect(jsonPath("$.[0].created", is(dateTime.toString())))
                .andExpect(jsonPath("$.[1].authorFullName", is("Admin Adminov")))
                .andExpect(jsonPath("$.[1].text", is("second comment example text")))
                .andExpect(jsonPath("$.[1].created", is(dateTime.toString())));
    }
}
