package com.example.ass_restadr.db;

public class AccountDTO {
    String _id, PassWord, FullName;

    public String toString() {
        return "ID: " + _id + '\n' +
                "PassWord: " + PassWord + '\n' +
                "FullName: " + FullName + '\n';
    }

    public AccountDTO() {
    }

    public AccountDTO(String _id, String passWord, String fullName) {
        this._id = _id;
        PassWord = passWord;
        FullName = fullName;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
