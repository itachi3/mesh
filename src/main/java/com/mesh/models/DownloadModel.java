package com.mesh.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by G on 01/12/15.
 */
public class DownloadModel {

    private String path;

    private List children;

    private Map<String, Object> data;

    public DownloadModel() {

    }

    public DownloadModel(String path, List children, Map<String, Object> value) {
        this.path = path;
        this.children = children;
        this.data = value;
    }

    @JsonProperty
    public String getPath() {
        return path;
    }

    @JsonProperty
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty
    public List getChildren() {
        return children;
    }

    @JsonProperty
    public void setChildren(List children) {
        this.children = children;
    }

    @JsonProperty
    public Map<String, Object> getData() {
        return data;
    }

    @JsonProperty
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
