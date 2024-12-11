package com.anarchyadventure.music_dabang_api.service;

import com.anarchyadventure.music_dabang_api.dto.post.FandomPostDTO;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostLike;
import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.entity.music.Artist;
import com.anarchyadventure.music_dabang_api.exceptions.EntityAlreadyExistException;
import com.anarchyadventure.music_dabang_api.repository.music.ArtistRepository;
import com.anarchyadventure.music_dabang_api.repository.post.FandomPostRepository;
import com.anarchyadventure.music_dabang_api.repository.post.FandomPostCommentRepository;
import com.anarchyadventure.music_dabang_api.repository.post.FandomPostLikeRepository;
import com.anarchyadventure.music_dabang_api.security.SecurityHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final FandomPostRepository fandomPostRepository;
    private final FandomPostCommentRepository fandomPostCommentRepository;
    private final FandomPostLikeRepository fandomPostLikeRepository;
    private final ArtistRepository artistRepository;

    @Transactional
    public FandomPostDTO.Main createPost(Long artistId, String title, String content) {
        User user = SecurityHandler.getUserAuth();
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        FandomPost post = new FandomPost(user, artist, title, content);
        fandomPostRepository.save(post);
        return FandomPostDTO.Main.from(post);
    }

    public List<FandomPostDTO.Main> getPostsByArtist(Long artistId, String sortBy) {
        List<FandomPost> posts;
        if ("createdAt".equalsIgnoreCase(sortBy)) {
            // 기본값: 최신순 정렬
            posts = fandomPostRepository.findByArtistIdOrderByCreatedAtDesc(artistId);
        } else {
            posts = fandomPostRepository.findByArtistIdOrderByLikesDesc(artistId);
        }
        return posts.stream().map(FandomPostDTO.Main::from).toList();
    }

    public FandomPostDTO.Main getPostById(Long postId) {
        FandomPost post = fandomPostRepository.findByIdWithComments(postId)
                .orElseThrow(EntityAlreadyExistException::new);
        return FandomPostDTO.Main.from(post);
    }

    @Transactional
    public FandomPostDTO.Comment addComment(Long fandomPostId, String content) {
        User user = SecurityHandler.getUserAuth();
        FandomPost post = fandomPostRepository.findById(fandomPostId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        FandomPostComment comment = new FandomPostComment(user, post, content);
        fandomPostCommentRepository.save(comment);
        return FandomPostDTO.Comment.from(comment);
    }

    @Transactional
    public FandomPostDTO.Comment addReply(Long postId, Long commentId, String content) {
        User user = SecurityHandler.getUserAuth();
        FandomPostComment parentComment = fandomPostCommentRepository.findByPostIdAndCommentId(postId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
        FandomPostComment reply = new FandomPostComment(user, parentComment, content);
        fandomPostCommentRepository.save(reply);
        return FandomPostDTO.Comment.from(reply);
    }

    public Boolean getPostLiked(Long postId) {
        User user = SecurityHandler.getUserAuth();
        return fandomPostLikeRepository.findByPostIdAndUserId(postId, user.getId()).isPresent();
    }

    @Transactional
    public void likePost(Long postId) {
        User user = SecurityHandler.getUserAuth();
        FandomPost post = fandomPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (fandomPostLikeRepository.existsByUserAndFandomPost(user, post)) {
            throw new EntityAlreadyExistException("Already liked this post");
        }

        fandomPostLikeRepository.save(new FandomPostLike(user, post));
        post.incrementLikes();
        fandomPostRepository.save(post);
    }

    @Transactional
    public void cancelLikePost(Long postId) {
        User user = SecurityHandler.getUserAuth();
        FandomPostLike fandomPostLike = fandomPostLikeRepository.findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(EntityAlreadyExistException::new);
        fandomPostLike.getFandomPost().decrementLikes();
        fandomPostLikeRepository.delete(fandomPostLike);
    }
}
