package com.findit.chamada.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findit.bd.connector.PostgresConnector;
import com.findit.models.chamada.ModelMeetings;
import com.findit.models.chamada.ModelStudents;
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

import static com.findit.jooq.tables.Students.STUDENTS;


@Path("/api/chamada/Students")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class Students {

    /**
     * GET /api/chamada/Users/ Get all users
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent() {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext select = DSL.using(conn, SQLDialect.POSTGRES);
            Result<Record3<Integer,String, String>> result = select.select(STUDENTS.STUDENT_ID,STUDENTS.FIRST_NAME,STUDENTS.EMAIL).from(STUDENTS).fetch();
            conn.close();
            return Response.ok(generateResponse(1,result.formatJSON()),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * POST /api/chamada/Students/ Create student
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setStudent(@FormDataParam("student")String json,
                               @FormDataParam("file") byte[] file) {
        ResponseBody response = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(json);
            ModelStudents student = new ModelStudents(
                    object.get("first_name").toString(),
                    object.get("last_name").toString(),
                    object.get("email").toString(),
                    file
            );
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext insert = DSL.using(conn, SQLDialect.POSTGRES);
            int result = insert.insertInto(
                    STUDENTS,
                    STUDENTS.FIRST_NAME,
                    STUDENTS.LAST_NAME,
                    STUDENTS.EMAIL,
                    STUDENTS.IMAGE
                ).values(
                    student.getFirst_name(),
                    student.getLast_name(),
                    student.getEmail(),
                    student.getImage()
                ).execute();
            conn.close();
            return Response.ok(generateResponse(result, null),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * PUT /api/chamada/Students/ update student
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@FormDataParam("student")String json,
                                  @FormDataParam("file") byte[] file) {
        ResponseBody response = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(json);
            ModelStudents student = new ModelStudents(
                    Integer.parseInt(object.get("student_id").toString()),
                    object.get("first_name").toString(),
                    object.get("last_name").toString(),
                    object.get("email").toString(),
                    file
            );
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext update = DSL.using(conn, SQLDialect.POSTGRES);
            int result = update.update(STUDENTS)
                    .set(STUDENTS.FIRST_NAME,student.getFirst_name())
                    .set(STUDENTS.LAST_NAME,student.getLast_name())
                    .set(STUDENTS.EMAIL,student.getEmail())
                    .set(STUDENTS.IMAGE,student.getImage())
                    .where(STUDENTS.STUDENT_ID.eq(student.getStudent_id()))
                    .execute();
            conn.close();
            return Response.ok(generateResponse(result, null),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * PUT /api/chamada/Students/update/id/removeFault update student to remove his faults
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.PUT
    @Path("update/{id: [0-9]+}/removeFault")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudentRemoveFaults(@PathParam("id") Integer student_id) {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext update = DSL.using(conn, SQLDialect.POSTGRES);
            int result = update.update(STUDENTS)
                    .set(STUDENTS.MISSING,STUDENTS.MISSING.sub(4))
                    .where(STUDENTS.STUDENT_ID.eq(student_id))
                    .execute();
            conn.close();
            return Response.ok(generateResponse(result, null),MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * DELETE /api/chamada/Students/ delete student
     *
     * @return the {@code Resource} with status 200 (OK) and body or status 404
     */
    @javax.ws.rs.DELETE
    @Path("{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("id") Integer id) {
        ResponseBody response = null;
        try {
            java.sql.Connection conn = PostgresConnector.getConnection();
            DSLContext delete = DSL.using(conn, SQLDialect.POSTGRES);
            int result = delete.delete(STUDENTS)
                    .where(STUDENTS.STUDENT_ID.eq(id))
                    .execute();
            conn.close();
            return Response.ok(generateResponse(result, null),MediaType.APPLICATION_JSON).build();
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
                    //json.add(jsonS);
                    //json.add(jsonF);
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
