import java.util.ArrayList;
import java.util.List;

public class Inproceedings {
    private String key;
    private List<String> authors = new ArrayList<>();
    private String title;
    private String pages;
    private int year;
    private String booktitle;

    public String getKey() {
        return key;
    }

    public void setId(String id) {
        this.key = id;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void addAuthor(String author) {
        this.authors.add(author);
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
        return "Conference Articles (inproceedings){" +
                "key='" + key + '\'' +
                ", authors=" + authors +
                ", title='" + title + '\'' +
                ", pages='" + pages + '\'' +
                ", year=" + year +
                ", booktitle='" + booktitle + '\'' +
                '}';
    }
}
