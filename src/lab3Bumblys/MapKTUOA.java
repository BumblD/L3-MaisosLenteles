package lab3Bumblys;

import java.lang.reflect.Array;
import java.util.Arrays;
import laborai.studijosktu.MapADT;
import laborai.studijosktu.MapKTU;

public class MapKTUOA<K, V> extends MapKTU<K, V> implements MapADT<K, V> {
    
    // Maišos lentelė
    protected Node<K, V>[] table;
    // Lentelėje esančių raktas-reikšmė porų kiekis
    protected int size = 0;
    // Einamas poros indeksas maišos lentelėje
    protected int index = 0;
    
    public MapKTUOA(int tSize) {
        size = 0;
        this.table = new Node[tSize];        
    }

    //Collisions are solved by quadratic probing in this structure!!!
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
    }

    @Override
    public String[][] toArray() {
        String[][] string = new String[2][table.length];
        for (int i = 0; i < table.length; i++){
            if (table[i] == null){
                string[0][i] = Integer.toString(i);
                string[1][i] = " ";
            } else {
                string[0][i] = Integer.toString(i) + " " + table[i].key.toString();
                string[1][i] = table[i].value.toString();
            }
        }
        return string;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(Key key, Value value)");
        }
        
        index = hash(key, DEFAULT_HASH_TYPE);
        int i = 0;
        int index0 = index;
        
        if(table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return value;
        } else {
            i++;
            index = (index0 + i*i) % table.length;
            while (index < table.length) {
                if (table[index] == null) {
                    table[index] = new Node<>(key, value);
                    size++;
                    return value;
                }
                i++;
                index = (index0 + i*i) % table.length;
            }
        }
        return value;
    }

    @Override
    public V get(K key) {
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
