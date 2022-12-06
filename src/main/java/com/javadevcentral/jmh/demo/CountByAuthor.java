package com.javadevcentral.jmh.demo;

public class CountByAuthor  {
    private String author;
    private int count;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "CountByAuthor{" +
                "author='" + author + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public void setCount(int count) {
        this.count = count;
    }

}
