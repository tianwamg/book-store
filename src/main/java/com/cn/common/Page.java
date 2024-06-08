package com.cn.common;

import java.io.Serializable;

public class Page implements Serializable {

    /**
     * 当前页
     */
    protected int current=1;

    /**
     *每页记录数
     */
    protected int pageSize=10;

    /**
     * 记录总数
     */
    protected int count;

    /**
     * 页数
     */
    protected int pages;

    /**
     * 当前页索引
     */
    protected int index;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
