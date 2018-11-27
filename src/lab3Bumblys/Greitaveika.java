package lab3Bumblys;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import laborai.demo.Timekeeper;
import laborai.gui.MyException;
import laborai.studijosktu.HashType;
import laborai.studijosktu.Ks;
import laborai.studijosktu.MapKTU;
import static laborai.studijosktu.MapKTU.DEFAULT_HASH_TYPE;
import laborai.studijosktu.MapKTUx;

public class Greitaveika {
    public static final String FINISH_COMMAND = "finishCommand";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    private final BlockingQueue resultsLogger = new SynchronousQueue();
    private final Semaphore semaphore = new Semaphore(-1);
    private final Timekeeper tk;

    private final String[] TYRIMU_VARDAI = {"put-KTUOA", "put-KTU", "remove-KTUOA", "remove-KTU"};
    private final int[] TIRIAMI_KIEKIAI = {80368};

    private static final File file = new File("data/zodynas.txt");

    public Greitaveika() {
        semaphore.release();
        tk = new Timekeeper(TIRIAMI_KIEKIAI, resultsLogger, semaphore);
    }
    
    public static void main(String[] args) {
        MapKTUOA<String, String> list = new MapKTUOA<String, String>(10);
        list.put("a", " -> A");
        list.put("b", " -> B");
        list.put("c", " -> C");
        list.put("d", " -> D");
        list.put("e", " -> E");
        list.put("d", " -> D");
        list.put("0", " -> 0");
        list.put("", " -> ");
        
        for (int i = 0; i < list.table.length; i++) {
            if (list.table[i] != null)
                System.out.println(i + "=" +list.table[i].key + list.table[i].value);
            else
                System.out.println(i + "=");
        }
        System.out.println("");
        System.out.println(list.remove("d"));
        
        /*HashSet<String> map3 = skaitytiDuomenisHash();
        Iterator it = map3.iterator();
        int c = 0;
        while(it.hasNext()) {
            System.out.println(it.next());
            c++;
        }
        System.out.println(c);*/
        
        /*List<String> str = skaitytiDuomenisList();
        for (int i = 0; i < str.size(); i++) {
            System.out.println(str.get(i).toString());
        }
        System.out.println(str.size());*/
    }
    
    private static HashSet<String> skaitytiDuomenisHash() {
        HashSet<String> map = new HashSet<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                map.add(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    private static List<String> skaitytiDuomenisList() {
        List<String> strings = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                strings.add(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public void pradetiTyrima() {
        try {
            SisteminisTyrimas();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void SisteminisTyrimas() throws InterruptedException {
        try {
            MapKTUOA<String, String> mapKTUOA = new MapKTUOA<>(160_000);
            MapKTU<String, String> mapKTU = new MapKTU<>(160_000, DEFAULT_HASH_TYPE);
            HashSet<String> mapHash = skaitytiDuomenisHash();
            List<String> data = skaitytiDuomenisList();
            tk.start();

            for (int i = 0; i < data.size(); i++) {
                mapKTUOA.put(data.get(i), data.get(i));
            }
            tk.finish(TYRIMU_VARDAI[0]);

            for (int i = 0; i < data.size(); i++) {
                mapKTU.put(data.get(i), data.get(i));
            }
            tk.finish(TYRIMU_VARDAI[1]);

            for (int i = 0; i < data.size(); i++) {
                mapKTUOA.remove(data.get(i));
            }
            tk.finish(TYRIMU_VARDAI[2]);

            for (int i = 0; i < data.size(); i++) {
                mapHash.remove(data.get(i));
            }
            tk.finish(TYRIMU_VARDAI[3]);
            tk.seriesFinish();

            StringBuilder sb = new StringBuilder();
            tk.logResult(sb.toString());
            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            tk.logResult(e.getMessage());
        }
    }

    public BlockingQueue<String> getResultsLogger() {
        return resultsLogger;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
