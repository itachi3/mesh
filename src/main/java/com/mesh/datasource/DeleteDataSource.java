package com.mesh.datasource;

import com.mesh.configuration.GraphDb;
import com.mesh.utils.Constants;
import com.mesh.utils.ListUtils;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by G on 01/12/15.
 */
public class DeleteDataSource {
    private final static Logger log = Logger.getLogger(DownloadDataSource.class);
    private static GraphDb graphDb;
    private String path;
    private List<String> keys;

    public DeleteDataSource(GraphDb graphDbInstance, String path, List properties) {
        this.path = path;
        this.keys = properties;
        graphDb = graphDbInstance;
    }

    public Response delete() {
        if (ListUtils.isEmptyorNull(keys)) {
            log.error("Empty request keys");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try (Transaction deleted = graphDb.getDbService().beginTx()) {
            String properties = "n." + keys.stream().collect(Collectors.joining(", n."));
            String query = String.format("MATCH (n {%s:%s}) REMOVE %s RETURN n", Constants.NodeConstants.ID, path.hashCode(), properties);
            Result result = graphDb.getDbService().execute(query);
            deleted.success();
            if (!result.hasNext()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Couldn't delete preferences (Key, exception) : " + path, e);
            throw e;
        }
        return Response.status(Response.Status.OK).build();
    }
}
