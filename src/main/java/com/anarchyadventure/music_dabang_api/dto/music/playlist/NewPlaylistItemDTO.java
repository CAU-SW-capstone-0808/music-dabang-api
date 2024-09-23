package com.anarchyadventure.music_dabang_api.dto.music.playlist;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class NewPlaylistItemDTO {
    @NotEmpty
    private List<Long> musicIds;
}
