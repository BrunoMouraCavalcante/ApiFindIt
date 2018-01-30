package com.findit.chamada.api;

import com.findit.bd.connector.PostgresConnector;
import com.findit.joog.tables.records.StudentMeetingRecord;
import okhttp3.ResponseBody;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.findit.joog.Tables.STUDENT_MEETING;

@Path("/api/chamada/StudentMeeting")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class StudentMeeting {

    /**
     * GET /api/chamada/StudentMeeting/ Get all Student_Meeting
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
            Result<StudentMeetingRecord> result = select.selectFrom(STUDENT_MEETING).fetch();
            String responseGson = "ok!";
            return Response.ok(result.formatJSON(),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }
}
