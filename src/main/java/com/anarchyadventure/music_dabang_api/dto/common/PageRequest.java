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
    private final String cursor;
    /**
     * default: 20
     */
    private final Integer size;
    /**
     * default: "id"
     */
    private final String sortBy;
    /**
     * desc or asc
     */
    private final String sortOrder;
}
