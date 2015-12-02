package com.mesh.controller;

import com.codahale.metrics.annotation.Timed;
import com.mesh.configuration.GraphDb;
import com.mesh.datasource.DeleteDataSource;
import com.mesh.datasource.DownloadDataSource;
import com.mesh.datasource.UploadDataSource;
import com.mesh.models.UploadModel;
import com.mesh.utils.Constants;
import com.mesh.utils.HeaderUtils;
import com.mesh.utils.ListUtils;
import com.mesh.utils.StringUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by G on 26/11/15.
 */
@Path("v1/preferences")
@Produces(MediaType.APPLICATION_JSON)
public class UniversalController {
    private static GraphDb graphDb;
    private final static Logger log = Logger.getLogger(UniversalController.class);

    public UniversalController(GraphDb graphDB) {
        this.graphDb = graphDB;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePreference(@HeaderParam(Constants.USER_AGENT) String userAgent, UploadModel uploadPreferences) {
        if (!HeaderUtils.validate(userAgent)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (uploadPreferences == null) {
            log.error("Invalid request : " + uploadPreferences);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            return new UploadDataSource(graphDb, uploadPreferences).upload();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Timed
    public Response getPreference(@HeaderParam(Constants.USER_AGENT) String userAgent, @QueryParam("path") String path) {
        if (!HeaderUtils.validate(userAgent) || StringUtils.isEmptyorNull(path)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            return new DownloadDataSource(graphDb, path).download();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Timed
    public Response deletePreference(@HeaderParam(Constants.USER_AGENT) String userAgent, @QueryParam("path") String path, @QueryParam("keys") List<String> keys) {
        if (!HeaderUtils.validate(userAgent) || ListUtils.isEmptyorNull(keys)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            return new DeleteDataSource(graphDb, path, keys).delete();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
