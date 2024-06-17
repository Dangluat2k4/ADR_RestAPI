package com.example.ass_restadr.db;

public class CartDTO {
    String _id,ProductID, Name, Description;
    int Quantity, Price;

    @Override
    public String toString() {
        return
                "id : " + _id + '\n' +
                "Product ID: " + ProductID + '\n' +
                "Name : " + Name + '\n' +
                "Email : " + Description + '\n'+
                "Quantity : " + Quantity + '\n'+
                "Price : " + Price + '\n';
    }

    public CartDTO() {
    }

    public CartDTO(String id, String productID, String name, String description, int quantity, int price) {
        this._id = id;
        ProductID = productID;
        Name = name;
        Description = description;
        Quantity = quantity;
        Price = price;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
