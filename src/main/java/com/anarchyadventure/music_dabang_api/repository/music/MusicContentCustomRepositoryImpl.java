package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import com.anarchyadventure.music_dabang_api.entity.music.Playlist;
import com.anarchyadventure.music_dabang_api.entity.music.PlaylistItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.anarchyadventure.music_dabang_api.entity.music.QMusicContent.musicContent;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class MusicContentCustomRepositoryImpl implements MusicContentCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MusicContent> search(String keyword, PageRequest pageRequest) {
        JPAQuery<MusicContent> resultQuery = queryFactory.selectFrom(musicContent)
            .where(
                musicContent.title.contains(keyword)
                    .or(musicContent.artist.name.contains(keyword))
            )
            .limit(pageRequest.getSize());

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(musicContent.id.lt(cursorLong));
        }

        return resultQuery.fetch();
    }
}
