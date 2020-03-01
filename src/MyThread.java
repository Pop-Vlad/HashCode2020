import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyThread extends Thread {

    public boolean interupted;
    List<Library> libraries;
    int localLoss;
    int day;
    List<Library> path;

    public MyThread(List<Library> libraries, int localLoss, int day, List<Library> path){
        this.libraries = libraries;
        this.localLoss = localLoss;
        this.day = day;
        this.path = path;
    }

    @Override
    public void run(){
        this.backtrack(libraries, localLoss, day, path);
    }

    public void backtrack(List<Library> libraries, int localLoss, int day, List<Library> path){
        if(localLoss >= Main.minLoss || this.interupted) {
            return;
        }
        if(libraries.isEmpty()){
            Main.minLoss = localLoss;
            Main.bestOrder = path;
            return;
        }
        libraries.sort(Comparator.comparingInt(l -> l.processTime));
        for(Library l : libraries){
            if(day + l.processTime >= Main.noDays){
                Main.minLoss = localLoss;
                Main.bestOrder = path;
            }
            else {
                List<Library> list = new ArrayList<>(libraries);
                list.remove(l);
                List<Library> newPath = new ArrayList<>(path);
                newPath.add(l);
                this.backtrack(list, localLoss + Main.penalties[l.id][day + l.processTime], day + l.processTime, newPath);
            }
        }
    }

}
