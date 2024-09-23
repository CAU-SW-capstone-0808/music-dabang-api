package com.anarchyadventure.music_dabang_api.dto.common;

import lombok.Data;

/**
 * Cursor-based pagination. Used via @ModelAttribute in controller methods. It can be put in url query parameters.
 */
@Data
public class PageRequest {
    /**
     * cursor of the last item of the previous page
     */
    private String cursor;
    /**
     * default: 20
     */
    private Integer size;
    /**
     * default: "id"
     */
    private String sortBy;
    /**
     * desc or asc
     */
    private String sortOrder;

    public Long parseCursorLong() {
        if (cursor == null)
            return null;
        try {
            return Long.parseLong(getCursor());
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getSize() {
        return size == null ? 20 : size;
    }
}
