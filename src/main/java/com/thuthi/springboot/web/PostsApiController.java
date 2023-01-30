package com.thuthi.springboot.web;

import com.thuthi.springboot.service.posts.PostsService;
import com.thuthi.springboot.web.dto.PostsListResponseDto;
import com.thuthi.springboot.web.dto.PostsResponseDto;
import com.thuthi.springboot.web.dto.PostsSaveRequestDto;
import com.thuthi.springboot.web.dto.PostsUpdateRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(
            @RequestBody PostsSaveRequestDto postsSaveRequestDto) {
        return postsService.save(postsSaveRequestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(
            @PathVariable Long id,
            @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(
            @PathVariable Long id) {
        return postsService.findById(id);
    }
}
