package com.nexusretail.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMeta {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private String method;
    private long totalElements;
    private int pageSize;
    private String endpoint;
}
