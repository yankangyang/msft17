package webviewtry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class MissingPart {
    private String productName, supplierName;
    private int ProductID,BusinessID, Min, Max;
    private float Price;
    
    public MissingPart(int ProductID, String productName, int BusinessID, String supplierName, float Price, int Min, int Max){
        this.productName = productName;
        this.supplierName = supplierName;
        this.ProductID = ProductID;
        this.Min = Min;
        this.Max = Max;
        this.Price = Price;
        this.BusinessID = BusinessID;
    }

    public int getBusinessID() {
        return BusinessID;
    }

    public void setBusinessID(int BusinessID) {
        this.BusinessID = BusinessID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public int getMin() {
        return Min;
    }

    public void setMin(int Min) {
        this.Min = Min;
    }

    public int getMax() {
        return Max;
    }

    public void setMax(int Max) {
        this.Max = Max;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float Price) {
        this.Price = Price;
    }
    
    
}
