package com.findit.chamada.api;

import com.findit.bd.connector.PostgresConnector;
import com.findit.dota2.api.ApiDota2;
import com.findit.joog.tables.User;
import com.findit.joog.tables.records.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import okhttp3.Connection;
import okhttp3.ResponseBody;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.findit.joog.Tables.USER;

@Path("/api/chamada/Users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class Users {

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
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<UserRecord> result = select.selectFrom(USER).fetch();
            String responseGson = "ok!";
            return Response.ok(result.formatJSON(),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }
}
