package com.lpk806studio.ive_st_cafe.Model;

public class Category {
    private String name;

    private Category(){

    }

    private Category(String name){
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

