package com.example.fypcommunicationtool;

public class TestSectionModel {

    String sectionName;
    int sectionPassMark;
    int noOfQuestions;
    long dateAdded, dateModified;

    TestSectionModel() {}

    public TestSectionModel(String sectionName, int sectionPassMark, int noOfQuestions, long dateAdded, long dateModified) {
        this.sectionName = sectionName;
        this.sectionPassMark = sectionPassMark;
        this.noOfQuestions = noOfQuestions;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionPassMark() {
        return sectionPassMark;
    }

    public void setSectionPassMark(int sectionPassMark) {
        this.sectionPassMark = sectionPassMark;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }
}
