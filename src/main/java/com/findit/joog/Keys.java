/*
 * This file is generated by jOOQ.
*/
package com.findit.joog;


import com.findit.joog.tables.Meetings;
import com.findit.joog.tables.StudentMeeting;
import com.findit.joog.tables.Students;
import com.findit.joog.tables.records.MeetingsRecord;
import com.findit.joog.tables.records.StudentMeetingRecord;
import com.findit.joog.tables.records.StudentsRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<MeetingsRecord, Integer> IDENTITY_MEETINGS = Identities0.IDENTITY_MEETINGS;
    public static final Identity<StudentMeetingRecord, Integer> IDENTITY_STUDENT_MEETING = Identities0.IDENTITY_STUDENT_MEETING;
    public static final Identity<StudentsRecord, Integer> IDENTITY_STUDENTS = Identities0.IDENTITY_STUDENTS;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<MeetingsRecord> MEETINGS_PKEY = UniqueKeys0.MEETINGS_PKEY;
    public static final UniqueKey<StudentMeetingRecord> STUDENT_MEETING_PKEY = UniqueKeys0.STUDENT_MEETING_PKEY;
    public static final UniqueKey<StudentsRecord> STUDENTS_PKEY = UniqueKeys0.STUDENTS_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<StudentMeetingRecord, StudentsRecord> STUDENT_MEETING__FK_STUDENT = ForeignKeys0.STUDENT_MEETING__FK_STUDENT;
    public static final ForeignKey<StudentMeetingRecord, MeetingsRecord> STUDENT_MEETING__FK_MEETING = ForeignKeys0.STUDENT_MEETING__FK_MEETING;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<MeetingsRecord, Integer> IDENTITY_MEETINGS = createIdentity(Meetings.MEETINGS, Meetings.MEETINGS.MEETING_ID);
        public static Identity<StudentMeetingRecord, Integer> IDENTITY_STUDENT_MEETING = createIdentity(StudentMeeting.STUDENT_MEETING, StudentMeeting.STUDENT_MEETING.ID);
        public static Identity<StudentsRecord, Integer> IDENTITY_STUDENTS = createIdentity(Students.STUDENTS, Students.STUDENTS.STUDENT_ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<MeetingsRecord> MEETINGS_PKEY = createUniqueKey(Meetings.MEETINGS, "Meetings_pkey", Meetings.MEETINGS.MEETING_ID);
        public static final UniqueKey<StudentMeetingRecord> STUDENT_MEETING_PKEY = createUniqueKey(StudentMeeting.STUDENT_MEETING, "Student_Meeting_pkey", StudentMeeting.STUDENT_MEETING.ID);
        public static final UniqueKey<StudentsRecord> STUDENTS_PKEY = createUniqueKey(Students.STUDENTS, "Students_pkey", Students.STUDENTS.STUDENT_ID);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<StudentMeetingRecord, StudentsRecord> STUDENT_MEETING__FK_STUDENT = createForeignKey(com.findit.joog.Keys.STUDENTS_PKEY, StudentMeeting.STUDENT_MEETING, "Student_Meeting__fk_student", StudentMeeting.STUDENT_MEETING.STUDENT_ID);
        public static final ForeignKey<StudentMeetingRecord, MeetingsRecord> STUDENT_MEETING__FK_MEETING = createForeignKey(com.findit.joog.Keys.MEETINGS_PKEY, StudentMeeting.STUDENT_MEETING, "Student_Meeting__fk_meeting", StudentMeeting.STUDENT_MEETING.MEETING_ID);
    }
}