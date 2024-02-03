package com.myblog9.service.impl;

import com.myblog9.entity.Post;
import com.myblog9.exception.ResourceNotFound;
import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.repository.PostRepository;
import com.myblog9.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper){
        this.postRepository=postRepository;
        this.modelMapper=modelMapper;
    }



    @Override
    public PostDto savePost(PostDto postDto) {
        Post post = mapToEntity(postDto);


        Post savedPost = postRepository.save(post);

        PostDto dto = mapToDto(savedPost);

        return dto;
    }

    @Override
    public void deletePost(long id) {

        postRepository.deleteById(id);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post =postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Post not found:"+id)
        );
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updatePost = postRepository.save(post);
        PostDto dto = mapToDto(updatePost);
        return dto;


    }

    @Override
    public PostDto getPostById(long id) {

        Post post =postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("post not found"+id)
        );
        PostDto dto = mapToDto(post);
        return dto;
    }

    @Override
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();


        Pageable pageable= PageRequest.of(pageNo, pageSize,sort );
        Page<Post> pagePosts = postRepository.findAll(pageable);
        List<Post> posts = pagePosts.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLast(pagePosts.isLast());
        postResponse.setTotalPages(pagePosts.getTotalPages());

        return postResponse;


    }

    PostDto mapToDto(Post post){
        //model mapper we will use by this we can reduce the lines of code
        PostDto dto=modelMapper.map(post, PostDto.class);
        // PostDto dto= new PostDto();
        //dto.setId(post.getId());
        //dto.setTitle(post.getTitle());
        //dto.setDescription(post.getDescription());
        // dto.setContent(post.getContent());
        return dto;
    }
    Post mapToEntity(PostDto postDto){
        //use of modelmapper
        Post post=modelMapper.map(postDto, Post.class);

        // Post post =new Post();
        // post.setTitle(postDto.getTitle());
        //post.setDescription(postDto.getDescription());
        //post.setContent(postDto.getContent());
        return post;
    }
}