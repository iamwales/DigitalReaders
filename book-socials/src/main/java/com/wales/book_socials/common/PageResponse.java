package com.wales.book_socials.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> hydraMember;
    private int page;
    private int size;
    private long hydraTotal;
    private int totalPages;
    private boolean first;
    private boolean last;
}
