package com.findit.chamada.api;

import com.findit.bd.connector.PostgresConnector;
import com.findit.dota2.api.ApiDota2;
import com.google.gson.Gson;
import okhttp3.Connection;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/chamada/Users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ChamadaApi {

    /**
     * GET /api/chamada/Users/ Get all users
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            String responseGson = "ok!";
            return Response.ok(responseGson,MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }
}
