package com.example.ass_restadr.db;

public class CategoryDTO {
    String _id,CateName;

    public CategoryDTO() {
    }

    public CategoryDTO(String _id, String cateName) {
        this._id = _id;
        CateName = cateName;
    }

    @Override
    public String toString() {
        return  "ID: " + _id + '\n' +
                "CateName : " + CateName + '\n';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public CategoryDTO(String cateName) {
        CateName = cateName;
    }

    public String getCateName() {
        return CateName;
    }

    public void setCateName(String cateName) {
        CateName = cateName;
    }
}
