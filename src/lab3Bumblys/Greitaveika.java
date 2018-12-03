package lab3Bumblys;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import laborai.demo.Timekeeper;
import laborai.gui.MyException;
import laborai.studijosktu.MapKTU;
import lab3Bumblys.Knyga;
import lab3Bumblys.Knyga.Builder;
import static laborai.studijosktu.MapKTU.DEFAULT_HASH_TYPE;

public class Greitaveika {
    public static final String FINISH_COMMAND = "finishCommand";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    private final BlockingQueue resultsLogger = new SynchronousQueue();
    private final Semaphore semaphore = new Semaphore(-1);
    private final Timekeeper tk;

    private final String[] TYRIMU_VARDAI = {"put-KTUOA", "put-KTU", "remove-KTUOA", "remove-Hash"};
    public int[] TIRIAMI_KIEKIAI = {10_000, 20_000, 40_000, 80_000};

    private static final File file = new File("data/zodynas.txt");

    public Greitaveika() {
        semaphore.release();
        tk = new Timekeeper(TIRIAMI_KIEKIAI, resultsLogger, semaphore);
    }
    
    /**
     * Nuskaito duomenis į HashMap
     * 
     * @return HashMap
     */
    private static HashMap<String, String> skaitytiDuomenisHash() {
        HashMap<String, String> map = new HashMap<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                map.put(data, data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    /**
     * Nuskaito duomenis į List
     * 
     * @return List
     */
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
    
    private static HashMap<String, Knyga> kurtiDuomenisHash(int kiekis) {
        HashMap<String, Knyga> map = new HashMap<>();
        Random rnd = new Random();
        Builder builder = new Builder();
        for (int i = 0; i < kiekis; i++) {
            String id = "ID" + rnd.nextInt(kiekis);
            Knyga knyga = builder.buildRandom();
            map.put(id, knyga);
        }
        return map;
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

    /*public void SisteminisTyrimas() throws InterruptedException {
        try {
            int[] TIRIAMI_KIEKIAI = {80368};
            MapKTUOA<String, String> mapKTUOA = new MapKTUOA<>(160_000);
            MapKTU<String, String> mapKTU = new MapKTU<>(160_000, DEFAULT_HASH_TYPE);
            HashMap<String, String> mapHash = skaitytiDuomenisHash();
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
    }*/
    
    public void SisteminisTyrimas() throws InterruptedException {
        try {
            int[] TIRIAMI_KIEKIAI = {10_000, 20_000, 40_000, 80_000};
            for (int k : TIRIAMI_KIEKIAI) {
                MapKTUOA<String, Knyga> mapKTUOA = new MapKTUOA<>(k*2);
                MapKTU<String, Knyga> mapKTU = new MapKTU<>(k, DEFAULT_HASH_TYPE);
                HashMap<String, Knyga> mapHash = kurtiDuomenisHash(k);
                Object[] keys = mapHash.keySet().toArray();
                tk.start();

                for (int i = 0; i < mapHash.size(); i++) {
                    mapKTUOA.put(keys[i].toString(), mapHash.get(keys[i].toString()));
                }
                tk.finish(TYRIMU_VARDAI[0]);

                for (int i = 0; i < mapHash.size(); i++) {
                    mapKTU.put(keys[i].toString(), mapHash.get(keys[i].toString()));
                }
                tk.finish(TYRIMU_VARDAI[1]);

                for (int i = 0; i < mapHash.size(); i++) {
                    mapKTUOA.remove(keys[i].toString());
                }
                tk.finish(TYRIMU_VARDAI[2]);

                for (int i = 0; i < mapHash.size(); i++) {
                    mapHash.remove(keys[i].toString());
                }
                tk.finish(TYRIMU_VARDAI[3]);
                tk.seriesFinish();
            }

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
