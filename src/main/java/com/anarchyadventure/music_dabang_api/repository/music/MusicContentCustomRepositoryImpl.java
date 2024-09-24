package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContentType;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.anarchyadventure.music_dabang_api.entity.music.QMusicContent.musicContent;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class MusicContentCustomRepositoryImpl implements MusicContentCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MusicContent> search(String keyword, MusicContentType musicContentType, PageRequest pageRequest) {
        JPAQuery<MusicContent> resultQuery = queryFactory.selectFrom(musicContent)
            .limit(pageRequest.getSize())
            .orderBy(pageRequest.isDesc() ? musicContent.id.desc() : musicContent.id.asc());

        if (keyword != null) {
            resultQuery = resultQuery.where(
                musicContent.title.contains(keyword)
                    .or(musicContent.artist.name.contains(keyword))
            );
        }

        if (musicContentType != null) {
            resultQuery = resultQuery.where(
                musicContent.musicContentType.eq(musicContentType)
            );
        }

        Long cursorLong = pageRequest.parseCursorLong();
        if (cursorLong != null) {
            resultQuery = resultQuery.where(musicContent.id.lt(cursorLong));
        }

        return resultQuery.fetch();
    }

    @Override
    public List<String> autoComplete(String keyword, Integer limit) {
        // Combine priority conditions into a single expression
        NumberExpression<Integer> priority = new CaseBuilder()
            .when(musicContent.title.startsWithIgnoreCase(keyword)
                .or(musicContent.artist.name.startsWithIgnoreCase(keyword))).then(2)
            .when(musicContent.title.containsIgnoreCase(keyword)
                .or(musicContent.artist.name.containsIgnoreCase(keyword))).then(1)
            .otherwise(0);

        JPAQuery<MusicContent> resultQuery = queryFactory.selectFrom(musicContent)
            .join(musicContent.artist).fetchJoin()
            .where(priority.gt(0)) // Only include results with priority > 0
            .orderBy(priority.desc(), musicContent.title.length().asc(), musicContent.id.desc())
            .limit(limit);

        List<MusicContent> results = resultQuery.fetch();
        return results.stream()
            .map(mc -> {
//                if (mc.getArtist().getName().contains(keyword)) {
//                    return mc.getArtist().getName();
//                } else {
//                    return mc.getTitle();
//                }
                return mc.getTitle();
            })
            .toList();
    }
}
