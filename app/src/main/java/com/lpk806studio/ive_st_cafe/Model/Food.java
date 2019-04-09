package com.lpk806studio.ive_st_cafe.Model;

public class Food {
    public String name,price,category;

    public Food(){

    }

    public Food(String name,String price,String category){
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
