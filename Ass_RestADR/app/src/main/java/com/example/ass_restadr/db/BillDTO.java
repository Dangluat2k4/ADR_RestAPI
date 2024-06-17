package com.example.ass_restadr.db;

public class BillDTO {
    String _id,Date,Email;


    @Override
    public String toString() {
        return  "ID: " + _id + '\n' +
                "Date : " + Date + '\n' +
                "Email : " + Email + '\n';
    }

    public BillDTO() {
    }

    public BillDTO(String _id, String date, String emaill) {
        this._id = _id;
        Date = date;
        Email = emaill;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String emaill) {
        Email = emaill;
    }
}
