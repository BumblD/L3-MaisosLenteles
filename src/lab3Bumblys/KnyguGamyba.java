package lab3Bumblys;

import laborai.gui.MyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class KnyguGamyba {
    
    private static final String id = "ID";
    private static int serNr = 1000;
    
    private Knyga[] knygos;
    private String[] raktai;
    private static int kiekis = 0, idKiekis = 0;
    
    public static Knyga[] gamintiKnygas(int kiekis) {
        Knyga[] knygos = IntStream.range(0, kiekis)
                .mapToObj(i -> new Knyga.Builder().buildRandom())
                .toArray(Knyga[]::new);
        Collections.shuffle(Arrays.asList(knygos));
        return knygos;
    }

    public static String[] gamintiKnyguIds(int kiekis) {
        String[] raktai = IntStream.range(0, kiekis)
                .mapToObj(i -> id + (serNr++))
                .toArray(String[]::new);
        Collections.shuffle(Arrays.asList(raktai));
        return raktai;
    }

    public Knyga[] gamintiIrParduotiKnygas(int aibesDydis,
            int aibesImtis) throws MyException {
        if (aibesImtis > aibesDydis) {
            aibesImtis = aibesDydis;
        }
        knygos = gamintiKnygas(aibesDydis);
        raktai = gamintiKnyguIds(aibesDydis);
        this.kiekis = aibesImtis;
        return Arrays.copyOf(knygos, aibesImtis);
    }

    // Imamas po vienas elementas iš sugeneruoto masyvo. Kai elementai baigiasi sugeneruojama
    // nuosava situacija ir išmetamas pranešimas.
    public Knyga parduotiKnyga() {
        if (knygos == null) {
            throw new MyException("booksNotGenerated");
        }
        if (kiekis < knygos.length) {
            return knygos[kiekis++];
        } else {
            throw new MyException("allSetStoredToMap");
        }
    }

    public String gautiIsBazesKnyguId() {
        if (raktai == null) {
            throw new MyException("booksIdsNotGenerated");
        }
        if (idKiekis >= raktai.length) {
            idKiekis = 0;
        }
        return raktai[idKiekis++];
    }
}
