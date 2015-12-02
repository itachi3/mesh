package com.mesh.configuration;

import com.mesh.utils.Constants;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.UniqueFactory;

import java.util.Map;

/**
 * Created by G on 27/11/15.
 */
public final class GraphDb {
    private GraphDatabaseService dbService;
    private UniqueFactory.UniqueNodeFactory nodeFactory;
    private final Logger log = Logger.getLogger(GraphDb.class);
    private static GraphDb dbInstance = null;

    public static GraphDb getInstance(GraphDatabaseService service) {
        if (dbInstance == null) {
            dbInstance = new GraphDb(service);
            return dbInstance;
        }
        return dbInstance;
    }

    private GraphDb(GraphDatabaseService service) {
        dbService = service;
        registerShutdownHook();
        nodeFactory = UniqueNodeConstraint();
    }

    private UniqueFactory.UniqueNodeFactory UniqueNodeConstraint() {
        try (Transaction tx = dbService.beginTx()) {
            UniqueFactory.UniqueNodeFactory result = new UniqueFactory.UniqueNodeFactory(dbService, "Users") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.addLabel(DynamicLabel.label("Node"));
                    created.setProperty(Constants.NodeConstants.ID, properties.get(Constants.NodeConstants.ID));
                }
            };

            tx.success();
            return result;
        } catch (Exception e) {
            log.error("Error initializing unique constraint :", e);
        }
        return null;
    }

    public void CreateUniqueRelationShip(Node node1, Node node2, RelationshipType type) throws QueryExecutionException {
        String query = String.format("MATCH (node1 {%s:%s}), (node2 {%s:%s}) WHERE NOT (()-[:%s]->node2) CREATE node1-[:%s]->node2",
                Constants.NodeConstants.ID,
                node1.getProperty(Constants.NodeConstants.ID),
                Constants.NodeConstants.ID,
                node2.getProperty(Constants.NodeConstants.ID),
                type,
                type);
        dbService.execute(query);
    }

    public Node CreateUniqueNode(String idValue) {
        return nodeFactory.getOrCreate(Constants.NodeConstants.ID, idValue.hashCode());
    }

    private void registerShutdownHook() {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                dbService.shutdown();
            }
        });
    }

    public GraphDatabaseService getDbService() {
        return dbService;
    }
}
