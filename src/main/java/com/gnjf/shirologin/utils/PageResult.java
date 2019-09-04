package com.gnjf.shirologin.utils;

import java.util.List;

public class PageResult<T> {
    private long total;
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
