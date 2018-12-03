package lab3Bumblys;

import java.text.NumberFormat;
import java.time.LocalDate;
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
    
    /**
     * Sukuria hash objektui
     * 
     * @return hash
     */
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