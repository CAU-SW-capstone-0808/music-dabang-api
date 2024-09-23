package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import com.anarchyadventure.music_dabang_api.entity.music.QPlaylistItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.anarchyadventure.music_dabang_api.entity.music.QMusicContent.musicContent;
import static com.anarchyadventure.music_dabang_api.entity.music.QPlaylistItem.*;

@Repository
@RequiredArgsConstructor
public class PlaylistItemCustomRepositoryImpl implements PlaylistItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaylistItem> paginateMyPlaylistItems(Long userId, PageRequest pageRequest) {
        JPAQuery<PlaylistItem> resultQuery = queryFactory.selectFrom(playlistItem)
            .join(playlistItem.musicContent, musicContent).fetchJoin()
            .join(musicContent.artist).fetchJoin()
            .where(playlistItem.user.id.eq(userId))
            .where(playlistItem.playlist.isNull())
            .limit(pageRequest.getSize());

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(playlistItem.id.lt(cursorLong));
        }

        return resultQuery.fetch();
    }

    @Override
    public List<PlaylistItem> paginatePlaylistItems(Long userId, Long playlistId, PageRequest pageRequest) {
        JPAQuery<PlaylistItem> resultQuery = queryFactory.selectFrom(playlistItem)
            .join(playlistItem.playlist)
            .join(playlistItem.musicContent, musicContent).fetchJoin()
            .join(musicContent.artist).fetchJoin()
            .where(playlistItem.playlist.id.eq(playlistId).and(
                playlistItem.user.id.eq(userId).or(playlistItem.playlist.usedForSystem.isTrue())
            ))
            .limit(pageRequest.getSize());

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(playlistItem.id.lt(cursorLong));
        }

        return resultQuery.fetch();
    }
}
