
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static final String inputFile = "b.txt";
    public static final String outputFile =  inputFile + "_output.txt";
    public static int noDays;
    public static int[][] penalties;
    public static int minLoss = Integer.MAX_VALUE;
    public static int maxGain = -1;
    public static List<Library> bestOrder;
    public static long startTime;
    public static long maxExecutionTime = 10;
    public static int goodEnough = 0;

    public static long computeScore(List<Library> libraries){
        long total = 0;
        int currentDay = 0;
        for(Library l : libraries){
            currentDay += l.processTime;
            if(currentDay >= noDays)
                break;
            int noBooks = Math.min((noDays-currentDay)*l.shipsPerDay, l.shipped.size());
            for(int i=0; i<noBooks; i++){
                total += l.shipped.get(i).score;
            }
        }
        return total;
    }

    public static int[][] cumulativePenalties(List<Library> libraries, List<Book> books){
        for(Library l : libraries){
            l.copyOfBooks.sort(Comparator.comparingInt(b -> -b.score));
        }
        for(Book b : books){
            if (b.libraries.size() > 0) {
                Library bestChoice = Collections.max(b.libraries, Comparator.comparingInt(l -> l.libraryPoints(b)));
                for(Library l : b.libraries){
                    if(l != bestChoice){
                        l.copyOfBooks.remove(b);
                    }
                }
            }
        }
        int[][] potentials = new int[libraries.size()][noDays];
        for(Library l : libraries){
            Arrays.fill(potentials[l.id], 0);
        }
        for(Library l : libraries){
            for(int i=0; i<l.copyOfBooks.size(); i++){
                potentials[l.id][noDays-1-i] = l.copyOfBooks.get(i).score;
            }
        }
        for(Library l : libraries){
            int sum = 0;
            for(int i=0; i<noDays; i++){
                sum += potentials[l.id][i];
                potentials[l.id][i] = sum;
            }
        }
        return potentials;
    }

    public static void runAlgorithm(List<Library> libraries) throws InterruptedException {
        List<MyThread> threads = new ArrayList<>();
        threads.add(new MyThread(libraries, 0, 0, new ArrayList<>()));
        /*for(Library l : libraries) {
            List<Library> libs = new ArrayList<>(libraries);
            libs.remove(l);
            List<Library> path = new ArrayList<>();
            path.add(l);
            threads.add(new MyThread(libs, penalties[l.id][l.processTime], l.processTime, path));
        }*/
        Thread supervisor = new Thread(() -> {
            try {
                supervisor(threads);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for(Thread t : threads)
            t.start();
        supervisor.start();
        for(Thread t : threads)
            t.join();
        supervisor.join();
    }

    public static void supervisor(List<MyThread> threads) throws InterruptedException {
        while (true) {
            if (System.currentTimeMillis() - startTime >= maxExecutionTime*1000 || minLoss <= goodEnough) {
                for(MyThread t : threads){
                    t.interupted = true;
                    return;
                }
                return;
            }
            else {
                Thread.sleep(1000);
                System.out.println(minLoss);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        // Read

        bestOrder = new ArrayList<>();

        Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File(inputFile))));

        int noBooks = scanner.nextInt();
        int noLibraries = scanner.nextInt();
        noDays = scanner.nextInt();

        List<Book> books = new ArrayList<>();
        for(int i=0; i<noBooks; i++){
            books.add(new Book(i, scanner.nextInt()));
        }

        List<Library> libraries = new ArrayList<>();
        for(int i=0; i<noLibraries; i++){
            Library l = new Library(i, scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
            libraries.add(l);
            for(int j=0; j<l.noBooks; j++){
                Book b = books.get(scanner.nextInt());
                l.books.add(b);
                l.copyOfBooks.add(b);
                b.libraries.add(l);
            }
        }

        // Part1

        List<Library> usedLibraries;
        penalties = cumulativePenalties(libraries, books);
        startTime = System.currentTimeMillis();
        runAlgorithm(libraries);
        usedLibraries = bestOrder;
        System.out.println(usedLibraries);

        //  Part2 - Completed

        books.sort((b1,b2) -> b2.score-b1.score);
        for(Book b : books){
            List<Library> libs = b.libraries
                    .stream()
                    .filter(usedLibraries::contains)
                    .collect(Collectors.toList());
            if(libs.size() > 0) {
                Library bestL = Collections.min(libs, Comparator.comparingInt(Library::getNextScore));
                for (Library l : libs) {
                    l.books.remove(b);
                }
                bestL.shipped.add(b);
            }
        }

        usedLibraries = usedLibraries.stream()
                .filter(l -> !l.shipped.isEmpty())
                .collect(Collectors.toList());

        // Write

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(outputFile)));

        bufferedWriter.write(usedLibraries.size() + "\n");
        for(Library l : usedLibraries) {
            bufferedWriter.write(l.id + " " + l.shipped.size() + "\n");
                for (Book b : l.shipped) {
                    bufferedWriter.write(b.id + " ");
                }
                bufferedWriter.write("\n");
        }

        bufferedWriter.close();

        System.out.println(computeScore(usedLibraries));

    }

}
