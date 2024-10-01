package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
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
            .limit(pageRequest.getSize())
            .orderBy(playlistItem.orderingNum.asc(), playlistItem.id.desc());
//            .orderBy(pageRequest.isDesc() ? playlistItem.id.desc() : playlistItem.id.asc());

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(playlistItem.orderingNum.gt(cursorLong));
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
            .limit(pageRequest.getSize())
            .orderBy(playlistItem.orderingNum.asc(), playlistItem.id.desc());
//            .orderBy(pageRequest.isDesc() ? playlistItem.id.desc() : playlistItem.id.asc());

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(playlistItem.orderingNum.gt(cursorLong));
        }

        return resultQuery.fetch();
    }

    @Override
    public Long countAllMyPlaylistItems(Long userId) {
        return queryFactory.select(playlistItem.count())
            .from(playlistItem)
            .where(playlistItem.user.id.eq(userId))
            .where(playlistItem.playlist.isNull())
            .fetchOne();
    }

    @Override
    public Long lastOrderingNumInPlaylist(Long playlistId) {
        return queryFactory.select(playlistItem.orderingNum)
            .from(playlistItem)
            .where(playlistItem.playlist.id.eq(playlistId))
            .orderBy(playlistItem.orderingNum.desc())
            .fetchFirst();
    }

    @Override
    public Long lastOrderingNumInMyMusicList(Long userId) {
        return queryFactory.select(playlistItem.orderingNum)
            .from(playlistItem)
            .where(playlistItem.user.id.eq(userId))
            .where(playlistItem.playlist.isNull())
            .orderBy(playlistItem.orderingNum.desc())
            .fetchFirst();
    }

    @Override
    public boolean isMusicItemInMyMusicList(Long userId, Long musicId) {
        Long count = queryFactory.select(playlistItem.count())
            .from(playlistItem)
            .leftJoin(playlistItem.playlist)
            .where(playlistItem.playlist.isNull())
            .where(playlistItem.user.id.eq(userId))
            .where(playlistItem.musicContent.id.eq(musicId))
            .fetchOne();
        return count != null && count > 0;
    }
}
