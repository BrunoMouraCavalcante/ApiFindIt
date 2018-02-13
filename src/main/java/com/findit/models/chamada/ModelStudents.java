package com.findit.models.chamada;

public class ModelStudents {
    private int student_id;
    private String first_name;
    private String last_name;
    private String email;
    private byte[] image;

    public ModelStudents(int student_id, String first_name, String last_name, String email, byte[] image) {
        this.student_id = student_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.image = image;
    }

    public ModelStudents() {

    }

    public ModelStudents(String first_name, String last_name, String email, byte[] image) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.image = image;
    }

    public int getStudent_id() { return student_id; }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
