package com.findit.chamada.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findit.bd.connector.PostgresConnector;
import com.findit.joog.tables.records.MeetingsRecord;
import com.findit.joog.tables.records.StudentMeetingRecord;
import com.findit.models.chamada.ModelMeetings;
import com.findit.models.chamada.ModelStudentMeeting;
import okhttp3.ResponseBody;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.findit.joog.Tables.MEETINGS;
import static com.findit.joog.Tables.STUDENT_MEETING;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.when;

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
            conn.close();
            return Response.ok(generateResponse(1,result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @javax.ws.rs.POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveMeeting(@FormDataParam("meeting") String meeting) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ModelMeetings meet = mapper.readValue(meeting, ModelMeetings.class);
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext insert = DSL.using(conn, SQLDialect.POSTGRES);
            Result<MeetingsRecord> result = insert.insertInto(MEETINGS)
                    .columns(MEETINGS.TYPE, MEETINGS.DATE)
                    .values(meet.getType(), meet.getDate())
                    .returning(MEETINGS.MEETING_ID)
                    .fetch();
            conn.close();
            return Response.ok(generateResponse(1, result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * GET /api/chamada/Meetings/type/{id} Get meetings by type
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Path("/type/{id: [1-2]}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeetingsByType(@PathParam("id") Integer type) {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);

            Result<Record6<Integer, Integer, Timestamp, BigDecimal, BigDecimal, BigDecimal>> result = select
                    .select(MEETINGS.MEETING_ID,
                            MEETINGS.TYPE,
                            MEETINGS.DATE,
                            sum(when(STUDENT_MEETING.STATUS.eq(1),1).otherwise(0)).as("presence"),
                            sum(when(STUDENT_MEETING.STATUS.eq(2),1).otherwise(0)).as("late"),
                            sum(when(STUDENT_MEETING.STATUS.eq(3),1).otherwise(0)).as("miss")
                    ).from(MEETINGS).join(STUDENT_MEETING).on(MEETINGS.MEETING_ID.eq(STUDENT_MEETING.MEETING_ID))
                    .where(MEETINGS.TYPE.eq(type))
                    .groupBy(MEETINGS.MEETING_ID)
                    .fetch();
            conn.close();
            return Response.ok(generateResponse(1,result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    public JSONObject generateResponse(int id, String data) {
        if (data != null) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(data);
                return ResponseStatus.getValueById(id,json);
            } catch (Exception e) {
                return ResponseStatus.getValueById(id,null);
            }
        }
        return ResponseStatus.getValueById(id,null);
    }

    public enum ResponseStatus {
        STU_001(1,"Sucess", true),
        STU_000(0,"Fail", false);

        private String value;
        private int id;
        private boolean succcess;

        public String getValue() { return this.value; }

        public static JSONObject getValueById(int id, JSONObject data) {
            for(ResponseStatus rs : ResponseStatus.values()){
                if (rs.id == id) {
                    JSONObject json = new JSONObject();
                    JSONObject jsonS = new JSONObject();
                    JSONObject jsonF = new JSONObject();
                    JSONObject jsonD = new JSONObject();
                    if(data == null) {
                        jsonD.put("value",rs.getValue());
                        data = jsonD;
                    }
                    if (rs.succcess) {
                        jsonS.put("code",rs.name());
                        jsonS.put("data",data);
                    } else {
                        jsonF.put("code",rs.name());
                        jsonF.put("extras",data);
                    }
                    json.put("success",jsonS);
                    json.put("error", jsonF);
                    return  json;
                }
            }
            return null;
        }

        ResponseStatus(int result, String value, boolean success) {
            this.id = result;
            this.value = value;
            this.succcess = success;
        }
    }
}
