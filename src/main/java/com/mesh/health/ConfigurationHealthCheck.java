package com.mesh.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by G on 26/11/15.
 */
public class ConfigurationHealthCheck extends HealthCheck {
    private final String dbPath;

    public ConfigurationHealthCheck(String template) {
        this.dbPath = template;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(dbPath, "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
