package com.lpk806studio.ive_st_cafe.Model;

public class Order {
    public String id,uid,status,totalName,totalPrice;

    public Order(){

    }

    public Order(String id,String uid,String status,String totalName,String totalPrice){
        this.id=id;
        this.uid=uid;
        this.status=status;
        this.totalName=totalName;
        this.totalPrice=totalPrice;
    }

    public String getUid() {
        return uid;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getTotalName() {
        return totalName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }


}
