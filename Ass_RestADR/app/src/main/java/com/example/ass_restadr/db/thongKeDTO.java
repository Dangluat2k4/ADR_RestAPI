package com.example.ass_restadr.db;

import com.google.gson.annotations.SerializedName;

public class thongKeDTO {

    @SerializedName("tongTien")
    private  int tongTien;

    public thongKeDTO() {
    }

    public thongKeDTO(int tongTien) {
        this.tongTien = tongTien;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }
}
