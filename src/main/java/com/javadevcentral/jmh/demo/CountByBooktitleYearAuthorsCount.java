package com.javadevcentral.jmh.demo;

public class CountByBooktitleYearAuthorsCount {
    private String booktitle;
    private int year;
    private int authorscount;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAuthorscount() {
        return authorscount;
    }

    public void setAuthorscount(int authorscount) {
        this.authorscount = authorscount;
    }

    @Override
    public String toString() {
        return "CountByBooktitleYearAuthorsCount{" +
                "booktitle='" + booktitle + '\'' +
                ", year=" + year +
                ", authorscount=" + authorscount +
                ", count=" + count +
                '}';
    }
}
