package com.myblog9.controller;

import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hibernate.event.internal.PostDeleteEventListenerStandardImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import javax.validation.Valid;
import java.util.List;

@RestController//combination of controller+response anotation
@RequestMapping("/api/post")//act as base url
public class PostController {
    private PostService postservice;

    public PostController(PostService postservice)
    {
        this.postservice=postservice;
    }
    //This is an example of dependency injection, specifically constructor injection. When an instance of PostController is created, it needs to be provided with an instance of PostService. This helps in achieving loose coupling between the controller and the service, making the code more modular and easier to test.
    //http://localhost:8080/api/post



    //CREATE RECORD IN SPRINGBOOT
    @PreAuthorize("hasRole('ADMIN')")

    @PostMapping
//here we will perform saving a data and after that we will
// see spring validation in perfroms in springboot in this section

    public ResponseEntity<?> savePost(@Valid @RequestBody PostDto postDto, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postservice.savePost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
//DELETE RECORD IN
//http://localhost:8080/api/post/1
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        postservice.deletePost(id);
        return new ResponseEntity<>("Post is deleted",HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
//UPDATE RECORD IN SPRINGBOOT
    //http://localhost:8080/api/post/1
    public ResponseEntity<PostDto> updatePost(@PathVariable("id") long id,@RequestBody PostDto postDto){
        PostDto dto = postservice.updatePost(id, postDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //READ POST BY ID
    //http://localhost:8080/api/post/1
    @GetMapping("/{id}")
    public  ResponseEntity<PostDto> getPostById(@PathVariable("id") long id) {
        PostDto dto = postservice.getPostById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    // READ ALL POST USING STREAM JAVA FEATURE
    //ALSO WE LEARN PAGINATION AND SORTING IN THIS MODULE
    //url-http://localhost:8080/api/post?pageNo=0&pageSize=5&sortBy=title
    @GetMapping
    public PostResponse getPosts(
            @RequestParam(value= "pageNo",defaultValue ="0",required = false) int pageNo,
            @RequestParam(value= "pageSize",defaultValue ="5",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "id",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir



    ){
        PostResponse postResponse =postservice.getPosts(pageNo,pageSize,sortBy,sortDir);
        return  postResponse;
    }

}
