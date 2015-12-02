package com.mesh.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by G on 26/11/15.
 */
public class MeshConfiguration extends Configuration {
    @NotEmpty
    private String version;

    @NotEmpty
    @JsonProperty("GraphDB")
    private String dbPath;

    @JsonProperty
    public String getDbPath() {
        return dbPath;
    }

    @JsonProperty
    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    @JsonProperty
    public String getVersion() {
        return version;
    }

    @JsonProperty
    public void setVersion(String version) {
        this.version = version;
    }
}
