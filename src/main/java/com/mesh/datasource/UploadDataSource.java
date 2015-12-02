package com.mesh.datasource;

import com.mesh.configuration.GraphDb;
import com.mesh.models.UploadModel;
import com.mesh.utils.Constants;
import com.mesh.utils.MapUtils;
import com.mesh.utils.RelTypes;
import com.mesh.utils.StringUtils;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by G on 26/11/15.
 */
public class UploadDataSource {
    private final static Logger log = Logger.getLogger(UploadDataSource.class);
    private static GraphDb graphDb;
    private UploadModel preferences;

    public UploadDataSource(GraphDb graphDbInstance, UploadModel preferences) {
        this.preferences = preferences;
        graphDb = graphDbInstance;
    }

    public Response upload() throws Exception {
        String[] chunks = preferences.getNodePath().split("/");

        if (StringUtils.isEmptyorNull(chunks)) {
            log.error("Empty request path");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        /*
           Each attribute in the path {attr1}/{attr2} constitutes a node
           Each node is uniquely identified by hash of the path so far for easy retrieval
           Trailing slash is removed to query directly on path for download
           Unique Node & Relationship is established by API & Cypher respectively
           Leaf node is expected to store all the data needed
         */
        try (Transaction tx = graphDb.getDbService().beginTx()) {
            Node prev = null, curr = null;
            StringBuffer buffer = new StringBuffer();
            String nodeName;
            for (int i = 0; i < chunks.length; i++) {
                nodeName = chunks[i];
                if (i == 0) {
                    buffer.append(nodeName);
                } else {
                    buffer.append("/" + nodeName);
                }
                //Creating the first node
                if (prev == null) {
                    prev = curr = graphDb.CreateUniqueNode(buffer.toString());
                    prev.setProperty(Constants.NodeConstants.NAME, nodeName);
                    continue;
                }
                //Setting up consequent relationships
                curr = graphDb.CreateUniqueNode(buffer.toString());
                curr.setProperty(Constants.NodeConstants.NAME, nodeName);
                graphDb.CreateUniqueRelationShip(prev, curr, RelTypes.HAS);
                prev = curr;
            }

            if (!MapUtils.isEmptyorNull(preferences.getData()) && curr != null) {
                Iterator it = preferences.getData().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    curr.setProperty(pair.getKey().toString(), pair.getValue());
                    it.remove();
                }
            }
            tx.success();
        } catch (Exception e) {
            log.error("Couldn't save preferences exception (Key, exception) :  " + preferences.getNodePath(), e);
            throw e;
        }

        return Response.status(Response.Status.CREATED).build();
    }

}
