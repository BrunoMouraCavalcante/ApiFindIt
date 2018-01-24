package com.findit.chamada.api;

import com.findit.bd.connector.PostgresConnector;
import com.findit.joog.tables.records.MeetingsRecord;
import com.findit.joog.tables.records.UserRecord;
import okhttp3.ResponseBody;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.findit.joog.Tables.MEETINGS;

@Path("/api/chamada/Meetings")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class Meetings {

    /**
     * GET /api/chamada/Meetings/ Get all meetings
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeetings() {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<MeetingsRecord> result = select.selectFrom(MEETINGS).fetch();
            String responseGson = "ok!";
            return Response.ok(result.formatJSON(),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }
}
