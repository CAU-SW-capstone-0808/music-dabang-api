package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostLike;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.entity.music.Artist;
import com.anarchyadventure.music_dabang_api.repository.music.ArtistRepository;
import com.anarchyadventure.music_dabang_api.repository.post.FandomPostRepository;
import com.anarchyadventure.music_dabang_api.repository.post.FandomPostCommentRepository;
import com.anarchyadventure.music_dabang_api.repository.post.FandomPostLikeRepository;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final FandomPostRepository fandomPostRepository;
    private final FandomPostCommentRepository fandomPostCommentRepository;
    private final FandomPostLikeRepository fandomPostLikeRepository;
    private final ArtistRepository artistRepository;

    public FandomPost createPost(Long artistId, String title, String content) {
        User user = SecurityHandler.getUserAuth();
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        FandomPost post = new FandomPost(user, artist, title, content);
        return fandomPostRepository.save(post);
    }

    public List<FandomPost> getPostsByArtist(Long artistId, String sortBy) {
        if ("createdAt".equalsIgnoreCase(sortBy)) {
            return fandomPostRepository.findByArtistIdOrderByCreatedAtDesc(artistId); // 기본값: 최신순 정렬
        } else {
            return fandomPostRepository.findByArtistIdOrderByLikesDesc(artistId);
        }
    }

    public FandomPost getPostById(Long postId) {
        FandomPost post = fandomPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<FandomPostComment> comments = fandomPostCommentRepository.findByFandomPostId(postId);
        post.getComments().addAll(comments);

        return post;
    }

    public FandomPostComment addComment(Long fandomPostId, String content) {
        User user = SecurityHandler.getUserAuth();
        FandomPost post = fandomPostRepository.findById(fandomPostId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        FandomPostComment comment = new FandomPostComment(user, post, content);
        return fandomPostCommentRepository.save(comment);
    }

    public FandomPostComment addReply(Long parentId, String content) {
        User user = SecurityHandler.getUserAuth();
        FandomPostComment parentComment = fandomPostCommentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        FandomPostComment reply = new FandomPostComment(user, parentComment, content);
        return fandomPostCommentRepository.save(reply);
    }

    public void likePost(Long postId) {
        User user = SecurityHandler.getUserAuth();
        FandomPost post = fandomPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (fandomPostLikeRepository.existsByUserAndFandomPost(user, post)) {
            throw new RuntimeException("Already liked this post");
        }

        fandomPostLikeRepository.save(new FandomPostLike(user, post));
        post.incrementLikes();
        fandomPostRepository.save(post);
    }
}
