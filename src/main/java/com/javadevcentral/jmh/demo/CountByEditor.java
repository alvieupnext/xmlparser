package com.javadevcentral.jmh.demo;

public class CountByEditor {
    private String editor;
    private int count;

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CountByEditor{" +
                "editor='" + editor + '\'' +
                ", count=" + count +
                '}';
    }
}
