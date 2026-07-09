package com.ecommerce.orderservice.dto;

import java.util.List;

public class PagedResponse<T> {
    
    private List<T> data;
    private Meta meta;

    public PagedResponse() {}

    public PagedResponse(List<T> data, long total, int page, int size) {
        this.data = data;
        this.meta = new Meta(total, page, size);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Meta {
        private long total;
        private int page;
        private int size;

        public Meta() {}

        public Meta(long total, int page, int size) {
            this.total = total;
            this.page = page;
            this.size = size;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
