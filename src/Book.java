import java.util.ArrayList;
import java.util.List;

public class Book {

    public int id;
    public int score;
    public List<Library> libraries;

    public Book(int id, int score) {
        this.id = id;
        this.score = score;
        this.libraries = new ArrayList<>();
    }

}
