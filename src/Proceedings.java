import java.util.ArrayList;
import java.util.List;

public class Proceedings {
    private String key;
    private List<String> editors = new ArrayList<>();
    private String title;
    private String booktitle;
    private String publisher;
    private String volume;
    private String pages;

    public void setKey(String key) {
        this.key = key;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getKey() {
        return key;
    }

    public void setId(String id) {
        this.key = id;
    }

    public List<String> getEditors() {
        return editors;
    }

    public void addEditor(String author) {
        this.editors.add(author);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    @Override
    public String toString() {
        return "Conference Proceedings{" +
                "key='" + key + '\'' +
                ", editors=" + editors +
                ", title='" + title + '\'' +
                ", booktitle='" + booktitle + '\'' +
                ", publisher='" + publisher + '\'' +
                ", volume='" + volume + '\'' +
                ", pages='" + pages + '\'' +
                '}';
    }
}
