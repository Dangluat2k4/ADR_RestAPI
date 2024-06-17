package com.example.ass_restadr.db;

public class ProductDTO {
    String _id, ProductName, Description, CateID;
    int Price;


    @Override
    public String toString() {
        return "ID: " + _id + '\n' +
                "ProductName: " + ProductName + '\n' +
                "Description: " + Description + '\n' +
                "Price: " + Price + '\n' +
                "Cate ID: " + CateID + '\n';
    }

    public ProductDTO() {
    }

    public ProductDTO(String productName, String description, String cateID, int price) {
        ProductName = productName;
        Description = description;
        CateID = cateID;
        Price = price;
    }

    public String getProductName() {
        return ProductName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCateID() {
        return CateID;
    }

    public void setCateID(String cateID) {
        CateID = cateID;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
