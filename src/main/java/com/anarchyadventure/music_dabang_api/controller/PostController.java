package com.anarchyadventure.music_dabang_api.controller;

import com.anarchyadventure.music_dabang_api.dto.post.FandomPostDTO;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import com.anarchyadventure.music_dabang_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fandom")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/{artistId}/posts")
    public ResponseEntity<FandomPostDTO.Main> createPost(
            @PathVariable Long artistId, @Validated @RequestBody FandomPostDTO.CreateRequest createRequest) {
        FandomPostDTO.Main post = postService.createPost(
                artistId,
                createRequest.getTitle(),
                createRequest.getContent()
        );
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{artistId}/posts")
    public ResponseEntity<List<FandomPostDTO.Main>> getPostsByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        var posts = postService.getPostsByArtist(artistId, sortBy);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<FandomPostDTO.Main>> getPosts(@RequestParam(defaultValue = "createdAt") String sortBy) {
        var posts = postService.getPosts(sortBy);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<FandomPostDTO.Main> getPostById(@PathVariable Long postId) {
        var post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts/{postId}/liked")
    public ResponseEntity<Boolean> getPostLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostLiked(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<FandomPostDTO.Comment> addComment(
            @PathVariable Long postId,
            @Validated @RequestBody FandomPostDTO.CommentRequest commentRequest){
        var comment = postService.addComment(postId, commentRequest.getContent());
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/reply")
    public ResponseEntity<FandomPostDTO.Comment> addReply(
            @PathVariable Long postId, @PathVariable Long commentId,
            @Validated @RequestBody FandomPostDTO.CommentRequest commentRequest) {
        var reply = postService.addReply(postId, commentId, commentRequest.getContent());
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<Boolean> getPostLike(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostLiked(postId));
    }

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> cancelLikePost(@PathVariable Long postId) {
        postService.cancelLikePost(postId);
        return ResponseEntity.ok().build();
    }
}
