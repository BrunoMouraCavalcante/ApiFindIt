package com.findit.chamada.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findit.bd.connector.PostgresConnector;
import com.findit.jooq.tables.records.MeetingsRecord;
import com.findit.jooq.tables.records.StudentMeetingRecord;
import com.findit.jooq.tables.records.StudentsRecord;
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

import java.text.SimpleDateFormat;
import java.util.List;

import static com.findit.jooq.Tables.MEETINGS;
import static com.findit.jooq.Tables.STUDENT_MEETING;
import static com.findit.jooq.tables.Students.STUDENTS;

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
            conn.close();
            return Response.ok(generateResponse(1,result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * GET /api/chamada/StudentMeeting/ Get all Student_Meeting by student
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Path("/student/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeetingsUserByStudent(@PathParam("id") Integer student_id) {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<StudentMeetingRecord> result = select.selectFrom(STUDENT_MEETING)
                    .where(STUDENT_MEETING.STUDENT_ID
                    .eq(student_id)).fetch();
            conn.close();
            return Response.ok(generateResponse(1,result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * GET /api/chamada/StudentMeeting/ Get all Student_Meeting by meeting
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Path("/meeting/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeetingsUserByMeeting(@PathParam("id") Integer meeting_id) {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<Record7<Integer, Integer, Integer, Integer, String, String, String>> result = select.select(STUDENT_MEETING.ID,
                    STUDENT_MEETING.MEETING_ID,
                    STUDENT_MEETING.STATUS,
                    STUDENT_MEETING.STUDENT_ID,
                    STUDENTS.EMAIL,
                    STUDENTS.FIRST_NAME,
                    STUDENTS.LAST_NAME)
                    .from(STUDENTS)
                    .join(STUDENT_MEETING)
                    .on(STUDENTS.STUDENT_ID.eq(STUDENT_MEETING.STUDENT_ID))
                    .where(STUDENT_MEETING.MEETING_ID.eq(meeting_id))
                    .fetch();
            conn.close();
            return Response.ok(generateResponse(1,result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @javax.ws.rs.POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveStudentMeetings(@FormDataParam("students")String students) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ModelStudentMeeting> list = mapper.readValue(students, mapper.getTypeFactory().constructCollectionType(List.class, ModelStudentMeeting.class));
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext insert = DSL.using(conn, SQLDialect.POSTGRES);
            InsertValuesStep3<StudentMeetingRecord, Integer, Integer, Integer> result = insert.insertInto(
                    STUDENT_MEETING)
                    .columns(STUDENT_MEETING.STUDENT_ID,
                            STUDENT_MEETING.MEETING_ID,
                            STUDENT_MEETING.STATUS);

            for (ModelStudentMeeting student : list) {
                if (student.getStatus() != 1) {
                    prepareEmail(student.getStudent_id(), student.getMeeting_id(), student.getStatus());
                }
                result = result.values(student.getStudent_id(),student.getMeeting_id(),student.getStatus());
            }
            int output = result.execute();
            conn.close();
            return Response.ok(generateResponse(1, null),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    public void prepareEmail(Integer student_id, Integer meeting_id, int status) {
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<StudentsRecord> student = select.selectFrom(STUDENTS)
                    .where(STUDENTS.STUDENT_ID
                            .eq(student_id)).fetch();

            Result<MeetingsRecord> meeting = select.selectFrom(MEETINGS)
                    .where(MEETINGS.MEETING_ID
                            .eq(meeting_id)).fetch();
            conn.close();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date  = dateFormat.format(meeting.get(0).getDate());
            String statusValue = status == 2 ? "o atraso" : "a falta";
            String email = student.get(0).getEmail();
            String name = student.get(0).getFirstName()+" "+student.get(0).getLastName();
            String kind = meeting.get(0).getType() == 1 ? "reunião" : "sede";

            String message = "Prezado "+name+" gostariamos de alertar sobre "+statusValue+" ocorrida na "
                    +kind+" no dia "+date;
            System.err.println("im here - prepare email to :: "+email);
            SendEmail.send("",email, "Aviso sobre "+statusValue,message);

        } catch (Exception e) { }
    }

    public void updateStudent(int studentId, int status) {
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext update = DSL.using(conn, SQLDialect.POSTGRES);
            Result<StudentsRecord> student =  update.selectFrom(STUDENTS)
                  .where(STUDENTS.STUDENT_ID.eq(studentId))
                  .fetch();

            UpdateSetFirstStep<StudentsRecord> result = update.update(STUDENTS);
            if (status == 2) {
                if (student.get(0).getLate() >= 3) {
                    result.set(STUDENTS.MISSING,STUDENTS.MISSING.add(1))
                        .set(STUDENTS.LATE, STUDENTS.LATE.sub(3))
                        .where(STUDENTS.STUDENT_ID.eq(studentId))
                        .execute();
                } else {
                    result.set(STUDENTS.LATE, STUDENTS.LATE.add(1))
                        .where(STUDENTS.STUDENT_ID.eq(studentId))
                        .execute();
                }
            } else if (status == 3) {
                result.set(STUDENTS.MISSING,STUDENTS.MISSING.add(1))
                    .where(STUDENTS.STUDENT_ID.eq(studentId))
                    .execute();
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Fail on update student late-miss ::  "+e.getMessage());
        }
    }

    @javax.ws.rs.POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveMeetingAndStudents(@FormDataParam("meeting") String meeting,
                                           @FormDataParam("students")String students) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ModelMeetings meet = mapper.readValue(meeting, ModelMeetings.class);
            List<ModelStudentMeeting> list = mapper.readValue(students, mapper.getTypeFactory().constructCollectionType(List.class, ModelStudentMeeting.class));
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext insert = DSL.using(conn, SQLDialect.POSTGRES);

            Result<MeetingsRecord> result = insert.insertInto(MEETINGS)
                    .columns(MEETINGS.TYPE, MEETINGS.DATE)
                    .values(meet.getType(), meet.getDate())
                    .returning(MEETINGS.MEETING_ID)
                    .fetch();
            InsertValuesStep3<StudentMeetingRecord, Integer, Integer, Integer> resultS = insert.insertInto(
                    STUDENT_MEETING)
                    .columns(STUDENT_MEETING.STUDENT_ID,
                            STUDENT_MEETING.MEETING_ID,
                            STUDENT_MEETING.STATUS);


            for (ModelStudentMeeting student : list) {
                if (student.getStatus() != 1) {
                    prepareEmail(student.getStudent_id(), result.get(0).getMeetingId(), student.getStatus());
                    updateStudent(student.getStudent_id(), student.getStatus());
                }
                resultS = resultS.values(student.getStudent_id(),result.get(0).getMeetingId(),student.getStatus());
            }
            int output = resultS.execute();
            conn.close();
            return Response.ok(generateResponse(1, null),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @javax.ws.rs.POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudentMeetings(@FormDataParam("students")String students) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ModelStudentMeeting> list = mapper.readValue(students, mapper.getTypeFactory().constructCollectionType(List.class, ModelStudentMeeting.class));
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext update = DSL.using(conn, SQLDialect.POSTGRES);

            for (ModelStudentMeeting student : list) {
                 update.update(STUDENT_MEETING)
                        .set(STUDENT_MEETING.STATUS,student.getStatus())
                        .where(STUDENT_MEETING.ID.eq(student.getId()))
                        .execute();
            }
            conn.close();
            return Response.ok(generateResponse(1, null),MediaType.APPLICATION_JSON).build();
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
