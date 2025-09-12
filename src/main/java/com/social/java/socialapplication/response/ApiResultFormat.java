package com.social.java.socialapplication.response;

import java.util.List;

public class ApiResultFormat<T> {
    private List<T> data;
    private long totalData;

    public ApiResultFormat(List<T> data, long totalData) {
        this.data = data;
        this.totalData = totalData;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }
}
