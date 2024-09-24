package com.anarchyadventure.music_dabang_api.repository.music;

import com.anarchyadventure.music_dabang_api.dto.common.PageRequest;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContent;
import com.anarchyadventure.music_dabang_api.entity.music.MusicContentType;

import java.util.List;

public interface MusicContentCustomRepository {
    List<MusicContent> search(String keyword, MusicContentType musicContentType, PageRequest pageRequest);

    List<String> autoComplete(String keyword, Integer limit);
}
