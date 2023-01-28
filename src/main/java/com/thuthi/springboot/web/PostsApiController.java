package com.thuthi.springboot.web;

import com.thuthi.springboot.service.posts.PostsService;
import com.thuthi.springboot.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
}
