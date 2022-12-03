public class CountByAuthorAndConference  {
    private String editor;
    private int count;
    private String booktitle;

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
        return "CountByAuthorAndConference{" +
                "editor='" + editor + '\'' +
                ", count=" + count +
                ", booktitle='" + booktitle + '\'' +
                '}';
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }
}
