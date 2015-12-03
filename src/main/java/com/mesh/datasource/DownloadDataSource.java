package com.mesh.datasource;

import com.mesh.configuration.GraphDb;
import com.mesh.models.DownloadModel;
import com.mesh.utils.Constants;
import com.mesh.utils.RelTypes;
import com.mesh.utils.StringUtils;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by G on 27/11/15.
 */
public class DownloadDataSource {
    private final static Logger log = Logger.getLogger(DownloadDataSource.class);
    private static GraphDb graphDb;
    private String path;

    public DownloadDataSource(GraphDb graphDbInstance, String path) {
        this.path = path;
        graphDb = graphDbInstance;
    }

    public Response download() {
        Map<String, Object> data = new HashMap<>();
        List<Object> children = new ArrayList<>();
        String query;
        Result result;
        /*
            Fetching node matches the path and populate children details if any
         */
        try (Transaction downloaded = graphDb.getDbService().beginTx()) {
            query = String.format("MATCH (n {%s:%s}) RETURN n", Constants.NodeConstants.ID, path.hashCode());
            result = graphDb.getDbService().execute(query);
            if (result.hasNext()) {

                Map<String, Object> row = result.next();
                for (Map.Entry<String, Object> column : row.entrySet()) {
                    Node node = (Node) column.getValue();
                    for(String key : node.getPropertyKeys()) {
                        data.put(key, node.getProperty(key));
                    }
                }

                query = String.format("MATCH (n {%s:%s})-[:%s]->(childs) RETURN childs",
                        Constants.NodeConstants.ID,
                        path.hashCode(),
                        RelTypes.HAS);
                result = graphDb.getDbService().execute(query);
                while (result.hasNext()) {
                    row = result.next();
                    for (Map.Entry<String, Object> column : row.entrySet()) {
                        Node node = (Node) column.getValue();
                        children.add(node.getProperty(Constants.NodeConstants.NAME));
                    }
                }
                downloaded.success();
            } else {
                downloaded.success();
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Couldn't get preferences (Key, exception) : " + path, e);
            throw e;
        }

        DownloadModel response = new DownloadModel(path, children, data);
        return Response.status(Response.Status.OK).entity(response).build();
    }

}
