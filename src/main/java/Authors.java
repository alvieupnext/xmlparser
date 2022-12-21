import java.util.ArrayList;
import java.util.List;

public class Authors {
    private List<String> authors = new ArrayList<>();

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Authors{" +
                "authors=" + authors +
                '}';
    }
}
