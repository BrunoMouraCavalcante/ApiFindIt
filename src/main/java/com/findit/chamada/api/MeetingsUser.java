package com.findit.chamada.api;

import com.findit.bd.connector.PostgresConnector;
import com.findit.joog.tables.records.MeetingsRecord;
import com.findit.joog.tables.records.MeetingsUserRecord;
import okhttp3.ResponseBody;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.findit.joog.Tables.MEETINGS_USER;

@Path("/api/chamada/MeetingsUser")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class MeetingsUser {

    /**
     * GET /api/chamada/MeetingsUser/ Get all meetings_user
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeetingsUser() {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<MeetingsUserRecord> result = select.selectFrom(MEETINGS_USER).fetch();
            String responseGson = "ok!";
            return Response.ok(result.formatJSON(),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }
}
