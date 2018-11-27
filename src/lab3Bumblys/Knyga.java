package lab3Bumblys;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import laborai.studijosktu.*;

public final class Knyga implements KTUable{
    
    final static private int esamiMetai  = LocalDate.now().getYear();
    final static private double minKaina =     0.01;
    final static private double maxKaina =    100.0;
    final static private String id = "ID";
    static private int nr = 0;
    
    // kiekvienos knygos individualūs duomenys
    private String pavadinimas;
    private String autorius;
    private String leidykla;
    private int leidimoMetai;
    private double kaina;    
    
    /**
     * Klasės konstruktorius
     * 
     * @param pavadinimas
     * @param autorius
     * @param leidykla
     * @param leidimoMetai
     * @param kaina 
     */
    public Knyga (String pavadinimas, String autorius, String leidykla, int leidimoMetai, double kaina) {
        this.pavadinimas = pavadinimas;
        this.autorius = autorius;
        this.leidykla = leidykla;
        this.leidimoMetai = leidimoMetai;
        this.kaina = kaina;
        validate();
    }
    
    /**
     * Tuščias klasės konstruktorius
     */
    public Knyga() {  
    }
    
    /**
     * Klasės konstruktorius iš String elemento
     * 
     * @param dataString duomenys apie knygą
     */
    public Knyga(String dataString) {
        this.parse(dataString);
    }
    
    /**
     * Knygos konstruktorius iš builder klasės
     * 
     * @param builder knygų gaminimo klasė
     */
    public Knyga(Builder builder) {
        this.pavadinimas = builder.pavadinimas;
        this.autorius = builder.autorius;
        this.leidykla = builder.leidykla;
        this.leidimoMetai = builder.leidimoMetai;
        this.kaina = builder.kaina;
        validate();
    }

    /**
     * Sukuria knygą iš String
     * 
     * @param dataString - duomenys apie knygą
     * @return knyga
     */
    @Override
    public Knyga create(String dataString) {
        Knyga k = new Knyga();
        k.parse(dataString);
        return k;
    }

    /**
     * Patikrina ar knyga tinkama
     * 
     * @return klaida arba ne
     */
    @Override
    public String validate() {
        String klaidosTipas = "";
        if (leidimoMetai > esamiMetai || leidimoMetai < 0) {
            klaidosTipas = "Netinkami leidimo metai, turi būti [ 0:" + esamiMetai + "]";
        }
        if (kaina < minKaina || kaina > maxKaina) {
            klaidosTipas += " Kaina už leistinų ribų [" + minKaina + ":" + maxKaina  + "]";
        }
        return klaidosTipas;
    }

    /**
     * Sukuria knygą iš String
     * 
     * @param dataString - duomenys apie knygą
     */
    @Override
    public final void parse(String dataString) {
        try {       // sc - duomenys atskirti ";" simboliu
            Scanner sc = new Scanner(dataString);
            sc.useDelimiter(";");
            pavadinimas = sc.next().replaceAll("^\\s+", "").replaceAll("\\s+$", "");    // replaceAll metodai nutrina
            autorius = sc.next().replaceAll("^\\s+", "").replaceAll("\\s+$", "");       // tarpus žodžio priekyje ir 
            leidykla = sc.next().replaceAll("^\\s+", "").replaceAll("\\s+$", "");       // gale
            leidimoMetai = Integer.parseInt(sc.next().replaceAll(" ", ""));
            kaina = Double.parseDouble(sc.next().replaceAll(" ", "").replaceAll(",", ".")); // double skaitymui pakeičiamas
        } catch (InputMismatchException  e) {                                               // "," į "."
            Ks.ern("Blogas duomenų formatas apie knygą -> " + dataString);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų apie knygą -> " + dataString);
        }
    }
    
    @Override
    public String toString(){
        return String.format("%s; %s; %s; %s; %s; %s", pavadinimas, autorius, leidykla, leidimoMetai, NumberFormat.getInstance(Locale.GERMANY).format(kaina), validate());
    }
    
    public String getPavadinimas() {
        return pavadinimas;
    }
    
    public String getAutorius() {
        return autorius;
    }
    
    public String getLeidykla() {
        return leidykla;
    }
    
    public int getLeidimoMetai() {
        return leidimoMetai;
    }
    
    public double getKaina() {
        return kaina;
    }
    
    public void setKaina(double kaina) {
        this.kaina = kaina;
    }    
    
    @Override
    public int hashCode() {
        return Objects.hash(pavadinimas, autorius, leidykla, leidimoMetai, kaina);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Knyga other = (Knyga) obj;
        if (!Objects.equals(this.pavadinimas, other.pavadinimas)) {
            return false;
        }
        if (!Objects.equals(this.autorius, other.autorius)) {
            return false;
        }
        if (this.leidykla != other.leidykla) {
            return false;
        }
        if (this.leidimoMetai != other.leidimoMetai) {
            return false;
        }
        if (Double.doubleToLongBits(this.kaina) != Double.doubleToLongBits(other.kaina)) {
            return false;
        }

        return true;
    }
/*
    // palyginimas pagal kainą
    public int compareTo(Knyga e) {
        double kitaKaina = e.kaina;
        if (kaina < kitaKaina) return -1;
        if (kaina > kitaKaina) return +1;
        return 0;
    }
    
    // palyginimas pagal pavadinimą
    public final static Comparator<Knyga> pagalPavadinima =
              new Comparator<Knyga>() {
       @Override
       public int compare(Knyga k1, Knyga k2) {
          return k1.getPavadinimas().compareTo(k2.getPavadinimas());
       }
    };
    
    // palyginimas pagal autorių
    public final static Comparator<Knyga> pagalAutoriu =
              new Comparator<Knyga>() {
       @Override
       public int compare(Knyga k1, Knyga k2) {
          return k1.getAutorius().compareTo(k2.getAutorius());
       }
    };
    
    // palyginimas pagal pavadinimą ir autorių
    public final static Comparator<Knyga> pagalPavadinimaAutoriu =
              new Comparator<Knyga>() {
       @Override
       public int compare(Knyga k1, Knyga k2) {
          int cmp = k1.getPavadinimas().compareTo(k2.getPavadinimas());
          if(cmp != 0) return cmp;
          return k1.getAutorius().compareTo(k2.getAutorius());
       }
    };
    
    // palyginimas pagal kainą
    public final static Comparator pagalKainą = new Comparator() {
       // sarankiškai priderinkite prie generic interfeiso ir Lambda funkcijų
       @Override
       public int compare(Object o1, Object o2) {
          double k1 = ((Knyga) o1).getKaina();
          double k2 = ((Knyga) o2).getKaina();
          // didėjanti tvarka, pradedant nuo mažiausios
          if(k1<k2) return -1;
          if(k1>k2) return 1;
          return 0;
       }
    };
   
    public static void main(String... args){
        // suvienodiname skaičių formatus pagal LT lokalę (10-ainis kablelis)
        Locale.setDefault(new Locale("LT")); 
        Knyga k1 = new Knyga("Kaip pasiekti (beveik) viską", "Anders Ericsson", "BALTO leidybos namai", 2018, 9.99);
        Knyga k2 = new Knyga("Vilko kelias", "Jordan Belfort", "BALTO leidybos namai", 2018, 12.99);
        Knyga k3 = new Knyga("iSteve", "George Beahm", "Baltos lankos", 2012, 6.79);
        Knyga k4 = new Knyga();//("Dievas visada keliauja incognito", "Laurent Gounelle", "Alma littera", 2012, 12.49);
        //k3.parse("Motyvuoti paprasta;Lina Preikšienė;Tyto alba;2017;6,81");
        //k4.parse("Boso valanda;Saulius Jovaišas;Baltos lankos;2015;10,03");
        Ks.oun(k1);
        Ks.oun(k2);
        Ks.oun(k3);
        Ks.oun(k4);
    }
 */   
    /**
     * Knygos gamybos klasė
     */
    public static class Builder {
        
        private final static Random rnd = new Random();
        private final static String[] pav = {"Java", "Programavimas", "Miegas", "Keliauk ten", "Ar verta Mokytis?"};  
        private final static String[] aut = {"Jurgis Mazalis", "Pranas Petrarka", "Nobertas Marauskas", "Kazlas Kazlauskas"};
        private final static String[] leid = {"Baltos lankos", "Alma Littera", "VeniVidiVici"};
        private final static int[] metai = {1999, 1998, 1997, 1996, 2000, 2002, 2003, 2004, 2016, 2015, 2017, 2018};
        
        private String pavadinimas = "";
        private String autorius = "";
        private String leidykla = "";
        private int leidimoMetai = 0;
        private double kaina = 0;
        
        public Knyga build() {
            return new Knyga(this);
        }
        
        /**
         * Pagamina knygą iš atsitiktinai paimtų elementų.
         * 
         * @return knyga
         */
        public Knyga buildRandom() {
            return new Knyga(pav[rnd.nextInt(pav.length)], aut[rnd.nextInt(aut.length)],
                leid[rnd.nextInt(leid.length)], metai[rnd.nextInt(metai.length)], rnd.nextInt(100));
        }
        
        public Builder pavadinimas(String pava) {
            this.pavadinimas = pava;
            return this;
        }
        
        public Builder autorius(String au) {
            this.autorius = au;
            return this;
        }
        
        public Builder leidykla(String le) {
            this.leidykla = le;
            return this;
        }
        public Builder leidimoMetai(int yyyy) {
            this.leidimoMetai = yyyy;
            return this;
        }
        public Builder kaina(double ka) {
            this.kaina = ka;
            return this;
        }
    }
}