package com.lpk806studio.ive_st_cafe.Model;

public class Cart {
    public String name,uid,price,count;
    public Cart(){

    }

    public Cart(String name,String price,String count,String uid){
        this.name=name;
        this.price=price;
        this.count=count;
        this.uid=uid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCount() {
        return count;
    }

    public String getUid() {
        return uid;
    }
}
