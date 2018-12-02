package lab3Bumblys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import laborai.studijosktu.Ks;
import laborai.studijosktu.MapADT;
import laborai.studijosktu.MapADTx;
import laborai.studijosktu.MapKTU;

public class MapKTUOA<K, V> extends MapKTU<K, V> implements MapADT<K, V>, MapADTx<K, V> {
    
    // Maišos lentelė
    protected Node<K, V>[] table;
    // Lentelėje esančių raktas-reikšmė porų kiekis
    protected int size = 0;
    // Einamas poros indeksas maišos lentelėje
    protected int index = 0;
    
    // Naudojamas kvadratinis dėstymas
    
    /**
     * Konstruktorius
     * 
     * @param tSize - dydis
     */
    public MapKTUOA(int tSize) {
        size = 0;
        this.table = new Node[tSize];        
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Išvalo lentelę 
     */
    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
    }

    /**
     * Sudeda duomenis į masyvą
     * 
     * @return masyvas su duomenimis
     */
    @Override
    public String[][] toArray() {
        String[][] string = new String[table.length][2];
        for (int i = 0; i < table.length; i++){
            if (table[i] == null){
                string[i][0] = null;
                string[i][1] = null;
            } else {
                string[i][0] = table[i].key.toString() + "=" + table[i].value;
                string[i][1] = table[i].value.toString();
            }
        }
        return string;
    }

    /**
     * Įdeda elementą į lentelę
     * 
     * @param key - raktas
     * @param value - reikšmė
     * @return įdėto elemento reikšmė
     */
    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(Key key, Value value)");
        }
        maxChainSize = 1; // Spausdinimui į GUI
        
        index = hash(key, DEFAULT_HASH_TYPE);
        int i = 0;
        int index0 = index;
        
        if(table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;      
            lastUpdatedChain = index; // Spausdinimui į GUI            
            return value;
        } else {
            i++;
            index = (index0 + i*i) % table.length;
            while (index < table.length) {
                if (table[index] == null) {
                    table[index] = new Node<>(key, value);
                    size++;
                    lastUpdatedChain = index; // Spausdinimui į GUI                    
                    return value;
                }
                i++;
                index = (index0 + i*i) % table.length;
            }
        }
        return value;
    }

    /**
     * Paima elementą iš lentelės
     * 
     * @param key - raktas
     * @return ieškomo elemento reikšmė
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(K key)");
        }
        index = hash(key, DEFAULT_HASH_TYPE);
        int index0 = index;
        int i = 0;
        
        while (index < table.length) {
            if(table[index] != null && table[index].key.equals(key)) {
                return table[index].value;
            }
            i++;
            index = (index0 + i*i) % table.length;
        }
        return null;
    }

    /**
     * Pašalina elementą iš lentelės
     * 
     * @param key - raktas
     * @return pašalinto elemento reikšmė
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }        
        
        V removed = null;
        index = hash(key, DEFAULT_HASH_TYPE);
        int index0 = index;
        int i = 0;
        
        while (index < table.length) {
            if(table[index] != null && table[index].key.equals(key)) {
                removed = table[index].value;
                table[index] = null;
                size--;
                return removed;
            }
            i++;
            index = (index0 + i*i) % table.length;
        }
        return removed;
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    @Override
    public V put(String dataString) {
        throw new UnsupportedOperationException("Not supported yet"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load(String filePath) {
        throw new UnsupportedOperationException("Not supported yet"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(String filePath) {
        throw new UnsupportedOperationException("Not supported yet"); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Spausdina į konsolę
     */
    @Override
    public void println() {
        if (size == 0) {
            Ks.oun("Atvaizdis yra tuščias");
        } else {
            String[][] data = getModelList("=");
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    String format = (j == 0 | j % 2 == 1) ? "%7s" : "%15s";
                    Object value = data[i][j];
                    Ks.ouf(format, (value == null ? "" : value));
                }
                Ks.oufln("");
            }
        }
    }
    
    @Override
    public void println(String title) {
        Ks.ounn("========" + title + "=======");
        println();
        Ks.ounn("======== Atvaizdžio pabaiga =======");
    }

    /**
     * Sudeda viską į masyvą
     * 
     * @param delimiter - teksto kirtiklis
     * @return masyvas su duomenimis
     */
    @Override
    public String[][] getModelList(String delimiter) {
        String[][] result = new String[table.length][2];
        int count = 0;
        for (Node<K, V> n : table) {
            List<String> list = new ArrayList();
            list.add( "[ " + count + " ]");
            if(n == null) {
                list.add("");
                list.add("");
            } else {
                list.add("-->");
                list.add(n.toString());
                list.add(split(n.toString(), delimiter));
            }
            result[count] = list.toArray(new String[0]);
            count++;
        }
        return result;
    }
    
    /**
     * Atskiria string
     * 
     * @param s - paduodamas string
     * @param delimiter - teksto kirtiklis
     * @return atkirstą string
     */
    private String split(String s, String delimiter) {
        int k = s.indexOf(delimiter);
        if (k <= 0) {
            return s;
        }
        return s.substring(0, k);
    }
    
    protected class Node<K, V> {

        // Raktas        
        protected K key;
        // Reikšmė
        protected V value;

        protected Node() {
        }

        protected Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
