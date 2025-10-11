package com.jobmatching.searchservice.dto;

import jakarta.validation.constraints.*;

public class JobSearchRequest {
    @Size(max = 200) private String keyword;
    private String location;
    private String type;
    @Min(0)  private Integer page = 0;
    @Min(1) @Max(100) private Integer size = 10;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
