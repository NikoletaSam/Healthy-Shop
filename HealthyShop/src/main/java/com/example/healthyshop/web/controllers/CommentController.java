package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.dtos.CommentCreationDto;
import com.example.healthyshop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ModelAttribute("commentDto")
    public CommentCreationDto init(){
        return new CommentCreationDto();
    }

    @GetMapping("/add/comment")
    public String addComment(){
        return "comment-add";
    }

    @PostMapping("/add/comment")
    public String addComment(@Valid CommentCreationDto commentDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("commentDto", commentDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentDto", bindingResult);

            return "redirect:/add/comment";
        }

        commentService.addComment(commentDto, userDetails.getUsername());
        return "redirect:/";
    }
}
