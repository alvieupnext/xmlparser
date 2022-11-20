import java.util.ArrayList;
import java.util.List;

public class Article {
    private String key;
    private String title;
    private String journal;
    private String volume;
    private String number;
    private List<String> authors = new ArrayList<>();

    public List<String> getAuthors() {
        return authors;
    }

    public void addAuthor(String author) {
        this.authors.add(author);
    }

    private int year;

    @Override
    public String toString() {
        return "Journal Article{" +
                "key='" + key + '\'' +
                ", author='" + authors +  '\'' +
                ", title='" + title + '\'' +
                ", journal='" + journal + '\'' +
                ", volume='" + volume + '\'' +
                ", number=" + number +
                ", year=" + year +
                '}';
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getKey() {
        return key;
    }

    public void setId(String id) {
        this.key = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
