package com.anarchyadventure.music_dabang_api.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PageResponse<T> {
    private final List<T> data;
    private final String cursor;

    public static <T> PageResponse.PageResponseBuilder<T> builder() {
        return new PageResponse.PageResponseBuilder<>();
    }

    public static class PageResponseBuilder<T> {
        private List<T> data;
        private String cursor;

        PageResponseBuilder() {
        }

        public PageResponse.PageResponseBuilder<T> data(List<T> data) {
            this.data = data;
            return this;
        }

        public PageResponse.PageResponseBuilder<T> cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        public PageResponse<T> build() {
            return new PageResponse<>(data, cursor);
        }

        public String toString() {
            return "PageResponse.PageResponseBuilder(data=" + this.data + ", cursor=" + this.cursor + ")";
        }
    }
}
