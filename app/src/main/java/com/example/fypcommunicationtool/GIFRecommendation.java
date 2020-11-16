package com.example.fypcommunicationtool;

import java.util.List;

public class GIFRecommendation {

    String categoryTitle;
    List<GIF> recommendationList;

    public GIFRecommendation(String categoryTitle, List<GIF> recommendationList) {
        this.categoryTitle = categoryTitle;
        this.recommendationList = recommendationList;
    }

    public List<GIF> getCategoryItemList() {
        return recommendationList;
    }

    public void setCategoryItemList(List<GIF> recommendationList) {
        this.recommendationList = recommendationList;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}

