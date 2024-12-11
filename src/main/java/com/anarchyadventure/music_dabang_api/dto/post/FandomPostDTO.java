package com.anarchyadventure.music_dabang_api.dto.post;

import com.anarchyadventure.music_dabang_api.dto.user.UserDTO;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPost;
import com.anarchyadventure.music_dabang_api.entity.post.FandomPostComment;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public class FandomPostDTO {

    @Getter
    @RequiredArgsConstructor
    public static class Main {
        private final Long id;
        private final UserDTO user;
        private final Long artistId;
        private final String title;
        private final String content;
        private final int likes;
        private final List<Comment> comments;
        private final LocalDateTime createdAt;
        private final LocalDateTime editedAt;

        public static Main from(FandomPost post) {
            return new Main(
                    post.getId(),
                    UserDTO.from(post.getUser()),
                    post.getArtist().getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getLikes(),
                    post.getComments().stream().map(Comment::from).toList(),
                    post.getCreatedAt(),
                    post.getEditedAt()
            );
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Comment {
        private final Long id;
        private final String content;
        private final UserDTO user;
        private final Long fandomPostId;
        private final Long parentCommentId;
        private final List<Comment> replies;
        private final LocalDateTime createdAt;
        private final LocalDateTime editedAt;

        public static Comment from(FandomPostComment comment) {
            return new Comment(
                    comment.getId(),
                    comment.getContent(),
                    UserDTO.from(comment.getUser()),
                    comment.getFandomPost().getId(),
                    comment.getParent() != null ? comment.getParent().getId() : null,
                    comment.getReplies().stream().map(Comment::from).toList(),
                    comment.getCreatedAt(),
                    comment.getEditedAt()
            );
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class CreateRequest {
        @NotNull
        @Length(min = 1, max = 300)
        private final String title;
        @NotNull
        @Length(min = 1, max = 10000)
        private final String content;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CommentRequest {
        @NotNull
        @Length(min = 1, max = 1000)
        private final String content;
    }
}
