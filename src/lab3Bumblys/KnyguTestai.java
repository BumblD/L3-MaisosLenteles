package lab3Bumblys;

import java.util.Locale;
import laborai.studijosktu.HashType;
import laborai.studijosktu.Ks;
import laborai.studijosktu.MapKTUx;


public class KnyguTestai {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // suvienodiname skaičių formatus
        //atvaizdzioTestas();
        greitaveikosTestas();
    }
    
    public static void atvaizdzioTestas() { 
        Knyga k1 = new Knyga("Kaip pasiekti (beveik) viską", "Anders Ericsson", "BALTO leidybos namai", 2018, 9.99);
        Knyga k2 = new Knyga("Vilko kelias", "Jordan Belfort", "BALTO leidybos namai", 2018, 12.99);
        Knyga k3 = new Knyga("iSteve", "George Beahm", "Baltos lankos", 2012, 6.79);
        Knyga k4 = new Knyga("Dievas visada keliauja incognito", "Laurent Gounelle", "Alma littera", 2012, 12.49);
        Knyga k5 = new Knyga();
        Knyga k6 = new Knyga("Never settle", "One plus", "Alma littera", 2012, 8.01);
        //Knyga k0 = new Knyga();
        k5.parse("Programavimas;Vardenis Pavardenis;KTU;2010;99,99");
        
        // Raktų masyvas
        String[] autoId = {"ID56", "ID12", "ID18", "ID11", "ID15", "ID16", "ID17"};
        int id = 0;
        
        MapKTUx<String, Knyga> atvaizdis = new MapKTUx(new String(), new Knyga(), HashType.DIVISION);
        // Reikšmių masyvas
        Knyga[] knygos = {k1, k2, k3, k4, k5, k6};
        for (Knyga k : knygos) {
            atvaizdis.put(autoId[id++], k);
        }
        
        /*atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Ar egzistuoja pora atvaizdyje?");
        Ks.oun(atvaizdis.contains(autoId[4]));
        Ks.oun(atvaizdis.contains(autoId[5]));
        Ks.oun("Pašalinamos poros iš atvaizdžio:");
        Ks.oun(atvaizdis.remove(autoId[0]));
        Ks.oun(atvaizdis.remove(autoId[5]));
        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Atliekame porų paiešką atvaizdyje:");
        Ks.oun(atvaizdis.get(autoId[1]));
        Ks.oun(atvaizdis.get(autoId[5]));
        Ks.oun("Išspausdiname atvaizdžio poras String eilute:");*/
        Ks.oun(atvaizdis);
        System.out.println();
        //System.out.println(atvaizdis.containsValue(atvaizdis.get("TA105")));
        //System.out.println(atvaizdis.numberOfEmpties());
        //Ks.oun(atvaizdis.containsValue(null));
    }
        
        //Konsoliniame režime
    private static void greitaveikosTestas() {
        System.out.println("Greitaveikos tyrimas:\n");
        Greitaveika gt = new Greitaveika();
        //Šioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.pradetiTyrima(),
                "Greitaveikos_tyrimo_gija").start();
        try {
            String result;
            while (!(result = gt.getResultsLogger().take())
                    .equals(Greitaveika.FINISH_COMMAND)) {
                System.out.println(result);
                gt.getSemaphore().release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        gt.getSemaphore().release();
    }
}
