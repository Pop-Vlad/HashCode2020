
import java.util.*;

public class Library {

    public int id;
    public int noBooks;
    public List<Book> books;
    public int processTime;
    public int shipsPerDay;
    public int startDay;
    public List<Book> shipped;
    public List<Book> copyOfBooks;

    public Library(int id, int noBooks, int processTime, int shipsPerDay) {
        this.id = id;
        this.noBooks = noBooks;
        this.processTime = processTime;
        this.shipsPerDay = shipsPerDay;
        books = new ArrayList<>();
        copyOfBooks = new ArrayList<>();
        shipped = new ArrayList<>();
    }

    public int noBooksCapable(){
        int x = shipsPerDay*(Main.noDays - startDay);
        return Math.min(this.books.size(), Math.max(x, Integer.MAX_VALUE));
    }

    public int getNextScore(){
        this.books.sort((b1, b2) -> b2.score-b1.score);
        if(this.noBooksCapable() == this.books.size())
            return 0;
        else {
            return this.books.get(this.noBooksCapable()).score;
        }
    }

    public int libraryPoints(Book b){
        this.copyOfBooks.sort(Comparator.comparingInt(book -> -book.score));
        return -this.copyOfBooks.indexOf(b);
    }

    public int maxScore(){
        int sum = 0;
        for(Book b :books){
            sum += b.score;
        }
        return sum;
    }

    public String toString(){
        return Integer.toString(this.maxScore());
    }

}
