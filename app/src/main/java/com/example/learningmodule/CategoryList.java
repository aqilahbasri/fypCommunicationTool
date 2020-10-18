package com.example.learningmodule;

public class CategoryList {
    private String categoryname;
    private String categoryimage;


    public CategoryList(){

    }


    public CategoryList(String categoryname, String categoryimage) {
        this.categoryname = categoryname;
        this.categoryimage = categoryimage;

    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {

        this.categoryname = categoryname;
    }

    public String getCategoryimage() {
        return categoryimage;
    }

    public void setCategoryimage(String categoryimage) {
        this.categoryimage = categoryimage;
    }


}
