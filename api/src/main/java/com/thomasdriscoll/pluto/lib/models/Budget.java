package com.thomasdriscoll.pluto.lib.models;

import lombok.Data;

import java.util.List;

@Data
public class Budget {
    public Budget(List<Category> categories){
        this.categories = categories;
    }
    List<Category> categories;
}
