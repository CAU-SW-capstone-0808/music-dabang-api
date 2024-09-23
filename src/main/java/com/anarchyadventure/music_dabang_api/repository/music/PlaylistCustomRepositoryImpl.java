package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.anarchyadventure.music_dabang_api.entity.music.QPlaylist.playlist;

@Repository
@RequiredArgsConstructor
public class PlaylistCustomRepositoryImpl implements PlaylistCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Playlist> paginateMainPlaylist(PageRequest pageRequest) {
        JPAQuery<Playlist> resultQuery = queryFactory.selectFrom(playlist)
            .limit(pageRequest.getSize());

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(playlist.id.lt(cursorLong));
        }

        return resultQuery.fetch();
    }
}
