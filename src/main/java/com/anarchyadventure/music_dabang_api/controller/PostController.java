package com.anarchyadventure.music_dabang_api.controller;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import com.anarchyadventure.music_dabang_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fandom")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/{artistId}/posts")
    public ResponseEntity<FandomPost> createPost(
            @PathVariable Long artistId,
            @RequestParam String title,
            @RequestParam String content) {
        FandomPost post = postService.createPost(artistId, title, content);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{artistId}/posts")
    public ResponseEntity<List<FandomPost>> getPostsByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        List<FandomPost> posts = postService.getPostsByArtist(artistId, sortBy);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<FandomPost> getPostById(@PathVariable Long postId) {
        FandomPost post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<FandomPostComment> addComment(
            @PathVariable Long postId,
            @RequestParam String content){
        FandomPostComment comment = postService.addComment(postId, content);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/posts/{parentId}/replies")
    public ResponseEntity<FandomPostComment> addReply(
            @PathVariable Long parentId,
            @RequestParam String content) {
        FandomPostComment reply = postService.addReply(parentId, content);
        return ResponseEntity.ok(reply);
    }

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok().build();
    }
}
