package com.mesh.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by G on 27/11/15.
 */
public class UploadModel {
    @NotEmpty
    @JsonProperty("path")
    private String nodePath;

    @NotNull
    @JsonProperty("data")
    private Map<String, Object> data;

    public UploadModel(){

    }

    @JsonProperty
    public String getNodePath() {
        return nodePath;
    }

    @JsonProperty
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    @JsonProperty
    public Map<String, Object> getData() {
        return data;
    }

    @JsonProperty
    public void setData(Map<String, Object> properties) {
        this.data = properties;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("NodePath :" + nodePath)
                .append("Properties : " + data)
                .toString();
    }
}
