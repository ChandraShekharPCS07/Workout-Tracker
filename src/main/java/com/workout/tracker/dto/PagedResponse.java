package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {

    @Schema(description = "List of data items for the current page")
    private List<T> content;

    @Schema(description = "Current page number (0-based)")
    private int pageNumber;

    @Schema(description = "Number of items per page")
    private int pageSize;

    @Schema(description = "Total number of elements across all pages")
    private long totalElements;

    @Schema(description = "Total number of pages")
    private int totalPages;

    @Schema(description = "Is this the last page?")
    private boolean last;
}

