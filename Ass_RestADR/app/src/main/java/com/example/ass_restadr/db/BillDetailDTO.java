package com.example.ass_restadr.db;

public class BillDetailDTO {
    String _id,BillID,ProductID;
    int Quantity;



    @Override
    public String toString() {
        return  "ID: " + _id + '\n' +
                "Bill ID: " + BillID + '\n' +
                "Product ID: " + ProductID + '\n' +
                "Số lượng :: " + Quantity + '\n';
    }

    public BillDetailDTO() {
    }

    public BillDetailDTO(String _id, String billID, String productID, int quantity) {
        this._id = _id;
        BillID = billID;
        ProductID = productID;
        Quantity = quantity;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBillID() {
        return BillID;
    }

    public void setBillID(String billID) {
        BillID = billID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
