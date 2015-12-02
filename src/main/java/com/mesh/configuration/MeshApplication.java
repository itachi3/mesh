package com.mesh.configuration;

import com.mesh.health.ConfigurationHealthCheck;
import com.mesh.controller.UniversalController;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by G on 26/11/15.
 */
public class MeshApplication extends Application<MeshConfiguration> {
    public static void main(String[] args) throws Exception {
        new MeshApplication().run(args);
    }

    @Override
    public String getName() {
        return "MeshApplication";
    }

    @Override
    public void initialize(Bootstrap<MeshConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(MeshConfiguration configuration, Environment environment) {
        // Health check for the configs
        final ConfigurationHealthCheck healthCheck = new ConfigurationHealthCheck(configuration.getDbPath());
        environment.healthChecks().register("template", healthCheck);

        // Initialize graphDb and hook resource to jersey
        GraphDatabaseService graphDbService = new GraphDatabaseFactory().newEmbeddedDatabase( new File(configuration.getDbPath()) );
        final UniversalController resource = new UniversalController(GraphDb.getInstance(graphDbService));
        environment.jersey().register(resource);
    }
}
