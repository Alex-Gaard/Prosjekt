package boligformidling;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
/**
 * Fungerer som en portal til databasen. Alle funksjoner knyttet direkte til manipulering av bolg tabellene er her.
 * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public class Data_Boliger extends Database {   
    /**
     * Inneholder objekter hvor de igjen inneholder bilder for hvert nedlastet bilde  for hver bolig.
     * I et forsøk på å minske belastningen på databasen, blir bildene mellomlagret i programmet. Hvis bilder blir oppdatert
     * i databasen, merker denne implementasjoen dette og laster ned det nyeste bildet.
     * Denne listen inneholder da et objekt for hver bolig som har fått lastet bildene sine ifra database.
     */
    private static HashMap BoligBildeHolderList = new HashMap();
    /**
     * Spesifiserer intervallet mellom oppdatering av digests ifra databasen
     */
    private static final int ImageUpdateDelay = 30000;
    /**
     * Representerer 1 bolig sine bilder, og laster dem ned etter behov, 
     * @author Arlen Syver Wasserman, s193956, IT 1 år
     * @version 1.0 13 Mai 2014
     */
    private static class BoligBildeHolder{
        private int boligId;
        private HashMap list;
        private ArrayList<String> DataBasedigestKeys;
        long lastUpdate;
        public BoligBildeHolder(int BoligId){
            boligId = BoligId;
            list = new HashMap();
            DataBasedigestKeys = new ArrayList();
            lastUpdate = 0;
            updateDigesetKeys();
        }
        /**
         * Oppdaterer listen med bilde digests ifra databasen
         */
        private void updateDigesetKeys(){
            if(lastUpdate + ImageUpdateDelay < System.currentTimeMillis()){
                ArrayList<String> digestKeys = new ArrayList();
                try(ResultSet rs = execQuery("SELECT " + COLUMN_DIGEST + " FROM " +  TABLE_BILDE + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + boligId)){
                    while(rs.next()){
                        digestKeys.add(rs.getString(COLUMN_DIGEST));
                    }
                }catch(SQLException e){
                    System.out.println(e);
                    return;
                }
                DataBasedigestKeys = digestKeys;
                lastUpdate = System.currentTimeMillis();
                list.keySet().retainAll(digestKeys);
            }
        }
        /**
         * This 
         * @return Returnerer boligen denne er knyttet til
         */
        int getId(){
            return boligId;
        }
        /**
         * Gir tilbake antall bilder i databasen
         * @return ett tall som representererer antall bilder i databasen
         */
        int getSize(){
            updateDigesetKeys();
            return DataBasedigestKeys.size();
        }
        /**
         * Gir tilbake 1 bilde ifra databasen med spesifisert index. (ifra boligen som er nevnt i konstruktøren)
         * @param index Hvilket bilde som skal returneres
         * @return Gir alltid tilbake et bilde uansett index
         */
        Image getImage(int index){
            updateDigesetKeys();
            if(DataBasedigestKeys.isEmpty()){
                return null;
            }
            index = Math.abs(index%getSize());
            if(list.containsKey(DataBasedigestKeys.get(index))){
                return (Image) list.get(DataBasedigestKeys.get(index));
            }else{
                try(ResultSet rs = execQuery("SELECT * FROM " +  TABLE_BILDE + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + boligId + " AND " + COLUMN_DIGEST + " = '" + DataBasedigestKeys.get(index) + "'")){
                    while(rs.next()){
                        list.put(rs.getString(COLUMN_DIGEST), ImageIO.read(rs.getBinaryStream(COLUMN_BILDE)));
                    }
                    }catch(SQLException|IOException  e){
                        System.out.println(e);
                        return null;
                    }
                return (Image) list.get(DataBasedigestKeys.get(index));
            }
        }
    }//End class BoligBildeHolder
    /**
     * Hvis en bolig trenger å vite hvor mange bilder den innehar, er dette metoden.
     * @param bolig Boligen som det skal sjekkes med.
     * @return antall bilder i databasen knyttet til boligen.
     */
    public static int getAntallBilder(Bolig bolig){
        BoligBildeHolder boligBildeHolder = (BoligBildeHolder) BoligBildeHolderList.get(bolig.getId());
        if(boligBildeHolder != null){
            return boligBildeHolder.getSize();
        }else{
            boligBildeHolder = new BoligBildeHolder(bolig.getId());
            BoligBildeHolderList.put(bolig.getId(), boligBildeHolder);
            return boligBildeHolder.getSize();
        }
    }//End getAntallBilder
    /**
     * Boliger kan hente sine bilder herifra
     * @param bolig Boligen som skal få sine bilder ut.
     * @param index Hvilket bildet skal hentes ut
     * @return returnerer et Image objekt hvis boligen har bilder. (som den skal ha)
     */
    public static Image getBilde(Bolig bolig, int index){
        BoligBildeHolder boligBildeHolder = (BoligBildeHolder) BoligBildeHolderList.get(bolig.getId());
        if(boligBildeHolder != null){
            return boligBildeHolder.getImage(index);
        }else{
            boligBildeHolder = new BoligBildeHolder(bolig.getId());
            BoligBildeHolderList.put(bolig.getId(), boligBildeHolder);
            return boligBildeHolder.getImage(index);
        }
    }//End getBilde
    /**
     * Setter inn en ny bolig. Alle felter må være fyllt til beste evne.
     * @param kunde Kunden, eller rettere sagt utleieren som skal leie ut boligen.
     * @param bolig Boligen med alle felter fyllt ut.
     * @param bilder Et array med bilder som skal knyttes til denne boligen.
     * @return true hvis det gikk fint, false ellers.
     */
    public static boolean settinnBolig(Bruker kunde, Bolig bolig, Image[] bilder) {/*Boligeier kunde*/
        try{
            if (!execQuery("SELECT * FROM " + TABLE_UTLEIER + " WHERE " + COLUMN_BRUKER_PERSONNUMMER + " = " + kunde.getId()).next()) {
                return false;
            }
        }catch (SQLException e){
            System.out.println("Feil i settinn bolig" + e);
            return false;
        }
        //Generate images and digests
        int numOfFiles = bilder.length;
        ArrayList<byte[]> images = new ArrayList<>();
        String[] digests = new String[numOfFiles];
            //Proccess
        for(int i=0;i<numOfFiles;i++){
            try{
                //Konverter bilde til byte array
                Image img = bilder[i];
                BufferedImage buffImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D buffImageGraphics = buffImage.createGraphics();
                buffImageGraphics.drawImage(img, null, null);
                RenderedImage rImage = (RenderedImage) buffImage;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(rImage, "jpeg", baos);
                images.add(baos.toByteArray());
                //Lag digest av byte arrayet
                MessageDigest digester = MessageDigest.getInstance("SHA-256");
                digester.update(baos.toByteArray());
                byte[] digest = digester.digest();
                StringBuilder hexDigest = new StringBuilder();
                for(int x=0;x<digest.length;x++) {
                    hexDigest.append(Integer.toHexString(0xFF & digest[x]));
                }
                digests[i] = hexDigest.toString();
                baos.close();
            }catch(IOException|NoSuchAlgorithmException ex){
                System.out.println("Feil is sett inn" + ex);
                return false;
            }
        }
        //Commence inserts
        //Lag første sql setninger
        int boligId;
        boolean abort = false;
        String sql1 = "INSERT INTO " + TABLE_BOLIG + " (" +
                COLUMN_UTLEIER_PERSONNUMMER + ", " + 
                COLUMN_ADRESSE + ", " + 
                COLUMN_POSTNUMMER + ", " + 
                COLUMN_BOAREAL + ", " + 
                COLUMN_ANTALL_ROM + ", " + 
                CLOUMN_BYGGÅR + ", " + 
                COLUMN_BESKRIVELSE + ", " + 
                COLUMN_AVERTERT + ", " + 
                COLUMN_UTLEIE_PRIS + ")";
        sql1 += " VALUES ('" + kunde.getId() + "' ,'" + 
                bolig.getAdresse() + "' ,'" + 
                bolig.getPostadresse() + "' ,'" + 
                bolig.getAreal() + "' ,'" + 
                bolig.getAntRom() + "' ,'" + 
                bolig.getByggår() + "' ,'" + 
                bolig.getBesk() + "' ,'" + 
                bolig.getAvertert() + "' ,'" + 
                bolig.getPris() + "')";
        Connection con = getConnection();
        try {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql1, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                boligId = rs.getInt(1);
            }else{
                System.out.println("Kunne ikke hente generert nøkkel! Avbryter");
                abort = true;
                return false;
            }
            //Sett inn leilighet eller bolig
            String sql2 = "";
            if(bolig instanceof Enebolig){
                Enebolig bolig2 = (Enebolig) bolig;
                sql2 = "INSERT INTO " + TABLE_ENEBOLIG_OG_REKKEHUS + " (" + COLUMN_BOLIG_BOLIG_ID + ", " + COLUMN_ETASJER + ", " + COLUMN_KJELLER + ", " + COLUMN_TOTAL_AREAL + ")";
                sql2 += " VALUES (" + boligId + ", " + bolig2.getEtasjer() + ", " + bolig2.getKjeller() + ", " + bolig2.getTotalAreal() + ")";
            }else if (bolig instanceof Leilighet){
                Leilighet bolig2 = (Leilighet) bolig;
                sql2 = "INSERT INTO " + TABLE_LEILIGHET + " (" + COLUMN_BOLIG_BOLIG_ID + ", " + COLUMN_ETASJE + ", " + COLUMN_HEIS + ", " + COLUMN_BALKONG + ", " + COLUMN_GARASJE + ", " + COLUMN_FELLESVASK + ")";
                sql2 += " VALUES (" + boligId + ", " + bolig2.getEtasje() + ", " + bolig2.getHeis() + ", " + bolig2.getBalkong() + ", " + bolig2.getGarasje() + ", " + bolig2.getFellesvask() + ")";
            }else{
                abort = true;
                return false;
            }
            stmt.executeUpdate(sql2);

            //Sett inn bilde(r)
            String sql3 = "INSERT INTO " + TABLE_BILDE + " (" + COLUMN_BOLIG_BOLIG_ID + ", " + COLUMN_BILDE + ", " + COLUMN_DIGEST + ") values(" + boligId + ", ?, ?)";
            for(int i=0;i<numOfFiles;i++){
                PreparedStatement stmt2 = con.prepareStatement(sql3);
                InputStream is = new ByteArrayInputStream(images.get(i));
                stmt2.setBinaryStream(1, is, images.get(i).length);
                stmt2.setString(2,digests[i]);
                stmt2.execute();
            }
            con.commit();
            return true;
        }catch (SQLException e) {
            System.out.println("Feil i sett inn bolig SQL E " + e);
            abort = true;
            return false;
        } finally{
            if (con != null) {
                try{
                    if(abort){
                        con.rollback();
                    }
                    con.setAutoCommit(true);
                } catch (SQLException xe) {
                    System.out.println(xe);
                }
            }
        }
    }//End settInnBolig
    /**
     * Henter ut boliger ifra databasen. Feltene er "nullable / 0 able" for å justere søket.
     * @param Id ID'en på boligen, kun en bolig med denne id'en blir returnert. Sett "null" for å ignorere dette feltet.
     * @param Eier Personnummer til eieren. Boliger som har denne eieren blir returnert. Sett "null" for å ignorere dette feltet.
     * @param Areal integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Postadresse integer array med postnummer. Kun boliger med postnummer i arrayen blir med. Sett "null" for å ignorere dette feltet.
     * @param Rom integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Byggår integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Avertert String array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Pris integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param modifiers om man vil legge til sql på slutten av en query er dette stedet. Sett "null" for å ignorere dette feltet.
     * @param InUse int verdi som spesifiserer om en bolig skal være ledig eller ikke. 1 for ikke opptatt, 2 for opptat. 0 for å ignorere dette feltet.
     * @return En array med Bolig objekter
     */
    public static Bolig[] getBoliger(String Id, String Eier, int[] Areal, int[] Postadresse, int[] Rom, int[] Byggår, String[] Avertert, long[] Pris, String modifiers, int InUse) {
        int counter = 0;
        String sql = "SELECT * FROM " + TABLE_BOLIG;
        if (Id != null || Eier != null || Areal != null || Postadresse != null || Rom != null || Byggår != null || Avertert != null || Pris != null) {
            sql += " WHERE ";
        }
        if (Id != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_BOLIG_ID + " = " + Id;
            counter++;
        }
        if (Eier != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_UTLEIER_PERSONNUMMER + " = " + Eier;
            counter++;
        }
        if (Areal != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_BOAREAL + sqlConverterRANGE(Areal);
            counter++;
        }
        if (Postadresse != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_POSTNUMMER + sqlConverterCONTAINS(Postadresse);
            counter++;
        }
        if (Rom != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_ANTALL_ROM + sqlConverterRANGE(Rom);
            counter++;
        }
        if (Byggår != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += CLOUMN_BYGGÅR + sqlConverterRANGE(Byggår);
            counter++;
        }
        if (Avertert != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_AVERTERT + sqlConverterRANGE(Avertert);
            counter++;
        }
        if (Pris != null) {
            if (counter > 0) {
                sql += " AND ";
            }
            sql += COLUMN_UTLEIE_PRIS + sqlConverterRANGE(Pris);
            counter++;
        }
        if (modifiers != null) {
            sql += " " + modifiers;
        }
        ArrayList<Bolig> boliger = new ArrayList();
        try {
            ResultSet r = execQuery(sql);
            while (r.next()) {
                boliger.add(new Bolig(r.getInt(COLUMN_BOLIG_ID), 
                                    r.getString(COLUMN_UTLEIER_PERSONNUMMER), 
                                    r.getString(COLUMN_ADRESSE), 
                                    r.getInt(COLUMN_POSTNUMMER), 
                                    r.getInt(COLUMN_BOAREAL), 
                                    r.getInt(COLUMN_ANTALL_ROM), 
                                    r.getInt(CLOUMN_BYGGÅR), 
                                    r.getString(COLUMN_BESKRIVELSE), 
                                    r.getString(COLUMN_AVERTERT), 
                                    r.getLong(COLUMN_UTLEIE_PRIS)));
            }
        } catch (SQLException e) {
            System.out.println("error: " + e);
            return new Bolig[0];
       }
        if(InUse == 1){           
            boliger = filterOpptattBoliger(boliger, false);
        }else if(InUse == 2){
            boliger = filterOpptattBoliger(boliger, true);
        }
        return boliger.toArray(new Bolig[boliger.size()]);
    }//End getBoliger 
    /** 
     *   Henter ut eneboliger ifra databasen. Feltene er "nullable / 0 able" for å justere søket.
     * @param Id ID'en på boligen, kun en bolig med denne id'en blir returnert. Sett "null" for å ignorere dette feltet.
     * @param Eier Personnummer til eieren. Boliger som har denne eieren blir returnert. Sett "null" for å ignorere dette feltet.
     * @param Areal integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Postadresse integer array med postnummer. Kun boliger med postnummer i arrayen blir med. Sett "null" for å ignorere dette feltet.
     * @param Rom integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Byggår integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Avertert String array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Pris integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Etasjer integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Kjeller int verdi hvor 0 er ignorer, 1 er false, og 2 er true for kjeller
     * @param Total_areal integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param modifiers om man vil legge til sql på slutten av en query er dette stedet. Sett "null" for å ignorere dette feltet.
     * @param InUse int verdi som spesifiserer om en bolig skal være ledig eller ikke. 1 for ikke opptatt, 2 for opptat. 0 for å ignorere dette feltet.
     * @return En array med Enebolig objekter
     */
    public static Enebolig[] getEneboliger(String Id, String Eier, int[] Areal, int[] Postadresse, int[] Rom, int[] Byggår, String[] Avertert, long[] Pris, int[] Etasjer, int Kjeller, int[] Total_areal, String modifiers, int InUse) {
        String sql = "SELECT " + TABLE_BOLIG + ".*, " + TABLE_ENEBOLIG_OG_REKKEHUS + ".* from " + TABLE_BOLIG + ", " + TABLE_ENEBOLIG_OG_REKKEHUS + " where " + TABLE_BOLIG + "." + COLUMN_BOLIG_BOLIG_ID + " = " + TABLE_ENEBOLIG_OG_REKKEHUS + "." + COLUMN_BOLIG_BOLIG_ID;
        if (Id != null) {
            sql += " AND ";
            sql += COLUMN_BOLIG_BOLIG_ID + " = " + Id;
        }
        if (Eier != null) {
            sql += " AND ";
            sql += COLUMN_UTLEIER_PERSONNUMMER + " = " + Eier;
        }
        if (Areal != null) {
            sql += " AND ";
            sql += COLUMN_BOAREAL + sqlConverterRANGE(Areal);
        }
        if (Postadresse != null) {
            sql += " AND ";
            sql += COLUMN_POSTNUMMER + sqlConverterCONTAINS(Postadresse);
        }
        if (Rom != null) {
            sql += " AND ";
            sql += COLUMN_ANTALL_ROM + sqlConverterRANGE(Rom);
        }
        if (Byggår != null) {
            sql += " AND ";
            sql += CLOUMN_BYGGÅR + sqlConverterRANGE(Byggår);
        }
        if (Avertert != null) {
            sql += " AND ";
            sql += COLUMN_AVERTERT + sqlConverterRANGE(Avertert);
        }
        if (Pris != null) {
            sql += " AND ";
            sql += COLUMN_UTLEIE_PRIS + sqlConverterRANGE(Pris);
        }
        if (Etasjer != null) {
            sql += " AND ";
            sql += COLUMN_ETASJER + sqlConverterRANGE(Etasjer);
        }
        if (Kjeller != 0) {
            sql += " AND ";
            sql += COLUMN_KJELLER + sqlConverterINT2BOOL(Kjeller);
        }
        if (Total_areal != null) {
            sql += " AND ";
            sql += COLUMN_TOTAL_AREAL + sqlConverterRANGE(Total_areal);
        }
        if (modifiers != null) {
            sql += " " + modifiers;
        }
        ArrayList<Bolig> eneboliger = new ArrayList();
        try {
            ResultSet r = execQuery(sql);
            while (r.next()){
                eneboliger.add(new Enebolig(r.getInt(COLUMN_BOLIG_BOLIG_ID),
                                            r.getString(COLUMN_UTLEIER_PERSONNUMMER),
                                            r.getString(COLUMN_ADRESSE),
                                            r.getInt(COLUMN_POSTNUMMER),
                                            r.getInt(COLUMN_BOAREAL),
                                            r.getInt(COLUMN_ANTALL_ROM),
                                            r.getInt(CLOUMN_BYGGÅR),
                                            r.getString(COLUMN_BESKRIVELSE),
                                            r.getString(COLUMN_AVERTERT),
                                            r.getLong(COLUMN_UTLEIE_PRIS),
                                            r.getInt(COLUMN_ETASJER),
                                            r.getBoolean(COLUMN_KJELLER),
                                            r.getInt(COLUMN_TOTAL_AREAL)));
            }
        } catch (SQLException e) {
            System.out.println("error: " + e);
            return new Enebolig[0];
        }
        if(InUse == 1){
            eneboliger = filterOpptattBoliger(eneboliger, false);
        }else if(InUse == 2){
            eneboliger = filterOpptattBoliger(eneboliger, true);
        }
        return eneboliger.toArray(new Enebolig[eneboliger.size()]);
    }//End getEneboliger
    /** 
     *   Henter ut leiligheter ifra databasen. Feltene er "nullable / 0 able" for å justere søket.
     * @param Id ID'en på boligen, kun en bolig med denne id'en blir returnert. Sett "null" for å ignorere dette feltet.
     * @param Eier Personnummer til eieren. Boliger som har denne eieren blir returnert. Sett "null" for å ignorere dette feltet.
     * @param Areal integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Postadresse integer array med postnummer. Kun boliger med postnummer i arrayen blir med. Sett "null" for å ignorere dette feltet.
     * @param Rom integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Byggår integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Avertert String array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Pris integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet.
     * @param Etasje integer array hvor første element er nedre grense, andre element er øvre grense. Sett "null" for å ignorere dette feltet. 
     * @param Heis int verdi hvor 0 er ignorer, 1 er false, og 2 er true for heis
     * @param Balkong int verdi hvor 0 er ignorer, 1 er false, og 2 er true for balkong
     * @param Garasje int verdi hvor 0 er ignorer, 1 er false, og 2 er true for garasje
     * @param Fellesvask int verdi hvor 0 er ignorer, 1 er false, og 2 er true for fellesvask
     * @param modifiers om man vil legge til sql på slutten av en query er dette stedet. Sett "null" for å ignorere dette feltet.
     * @param InUse int verdi som spesifiserer om en bolig skal være ledig eller ikke. 1 for ikke opptatt, 2 for opptat. 0 for å ignorere dette feltet.
     * @return En array med Leilighet objekter
     */
    public static Leilighet[] getLeiligheter(String Id, String Eier, int[] Areal, int[] Postadresse, int[] Rom, int[] Byggår, String[] Avertert, long[] Pris, int[] Etasje, int Heis, int Balkong, int Garasje, int Fellesvask, String modifiers, int InUse) {
        String sql = "SELECT " + TABLE_BOLIG + ".*, " + TABLE_LEILIGHET + ".* from " + TABLE_BOLIG + ", " + TABLE_LEILIGHET + " where " + TABLE_BOLIG + "." + COLUMN_BOLIG_BOLIG_ID + " = " + TABLE_LEILIGHET + "." + COLUMN_BOLIG_BOLIG_ID;
        if (Id != null) {
            sql += " AND ";
            sql += COLUMN_BOLIG_ID + " = " + Id;
        }
        if (Eier != null) {
            sql += " AND ";
            sql += COLUMN_UTLEIER_PERSONNUMMER + " = " + Eier;
        }
        if (Areal != null) {
            sql += " AND ";
            sql += COLUMN_BOAREAL + sqlConverterRANGE(Areal);
        }
        if (Postadresse != null) {
            sql += " AND ";
            sql += COLUMN_POSTNUMMER + sqlConverterCONTAINS(Postadresse);
        }
        if (Rom != null) {
            sql += " AND ";
            sql += COLUMN_ANTALL_ROM + sqlConverterRANGE(Rom);
        }
        if (Byggår != null) {
            sql += " AND ";
            sql += CLOUMN_BYGGÅR + sqlConverterRANGE(Byggår);
        }
        if (Avertert != null) {
            sql += " AND ";
            sql += COLUMN_AVERTERT + sqlConverterRANGE(Avertert);
        }
        if (Pris != null) {
            sql += " AND ";
            sql += COLUMN_UTLEIE_PRIS + sqlConverterRANGE(Pris);
        }
        if (Etasje != null) {
            sql += " AND ";
            sql += COLUMN_ETASJE + sqlConverterRANGE(Etasje);
        }
        if (Heis != 0) {
            sql += " AND ";
            sql += COLUMN_HEIS + sqlConverterINT2BOOL(Heis);
        }
        if (Balkong != 0) {
            sql += " AND ";
            sql += COLUMN_HEIS + sqlConverterINT2BOOL(Balkong);
        }
        if (Garasje != 0) {
            sql += " AND ";
            sql += COLUMN_GARASJE + sqlConverterINT2BOOL(Garasje);
        }
        if (Fellesvask != 0) {
            sql += " AND ";
            sql += COLUMN_FELLESVASK + sqlConverterINT2BOOL(Fellesvask);
        }
        if (modifiers != null) {
            sql += " " + modifiers;
        }
        ArrayList<Bolig> leiligheter = new ArrayList();
        try {
            ResultSet r = execQuery(sql);
            while (r.next()) {
                leiligheter.add(new Leilighet(r.getInt(COLUMN_BOLIG_BOLIG_ID), 
                        r.getString(COLUMN_UTLEIER_PERSONNUMMER), 
                        r.getString(COLUMN_ADRESSE), 
                        r.getInt(COLUMN_POSTNUMMER), 
                        r.getInt(COLUMN_BOAREAL), 
                        r.getInt(COLUMN_ANTALL_ROM), 
                        r.getInt(CLOUMN_BYGGÅR), 
                        r.getString(COLUMN_BESKRIVELSE), 
                        r.getString(COLUMN_AVERTERT), 
                        r.getLong(COLUMN_UTLEIE_PRIS), 
                        r.getInt(COLUMN_ETASJE), 
                        r.getBoolean(COLUMN_HEIS), 
                        r.getBoolean(COLUMN_BALKONG), 
                        r.getBoolean(COLUMN_GARASJE), 
                        r.getBoolean(COLUMN_FELLESVASK)));
            }
        } catch (SQLException e) {
            System.out.println("error: " + e);
            return new Leilighet[0];
        }
        if(InUse == 1){
            leiligheter = filterOpptattBoliger(leiligheter, false);
        }else if(InUse == 2){
            leiligheter = filterOpptattBoliger(leiligheter, true);
        }
        return leiligheter.toArray(new Leilighet[leiligheter.size()]);
    }//End getLeiligheter
    /**
     * Oppdaterer en bolig med informasjonen som er i boligen og oppdaterer bildene til bildene i Image arrayet 
     * @param b boligen med feltene sine fyllt ut til beste evne
     * @param bilder et array med bilder. Kun disse bildene vil eksistere i databasen.
     * @return true hvis det lyktes, false ellers
     */
    public static boolean updateBolig(Bolig b, Image[] bilder){
        int id = b.getId();
        String sql1 = "UPDATE " + TABLE_BOLIG + " SET " +
                    COLUMN_ADRESSE + " = '" + b.getAdresse() + "'" +
                    ", " + COLUMN_BOAREAL + " = " + b.getAreal() +
                    ", " + COLUMN_ANTALL_ROM + " = " + b.getAntRom() +
                    ", " + CLOUMN_BYGGÅR + " = " + b.getByggår() +
                    ", " + COLUMN_BESKRIVELSE + " = '" + b.getBesk() + "'" +
                    ", " + COLUMN_UTLEIE_PRIS + " = " + b.getPris() + 
                    ", " + COLUMN_POSTNUMMER + " = " + b.getPostadresse() +
                    " WHERE "+ COLUMN_BOLIG_ID +" = " + id;
        String sql2 = "";
        if (b instanceof Enebolig) {
                Enebolig eb = (Enebolig) b;
                sql2 = "UPDATE "+ TABLE_ENEBOLIG_OG_REKKEHUS + " SET " + 
                        COLUMN_ETASJER + " = " + eb.getEtasjer()+ 
                        ", " + COLUMN_KJELLER + " = " + eb.getKjeller() + 
                        ", " + COLUMN_TOTAL_AREAL + " = " + eb.getTotalAreal() +
                        " WHERE "+ COLUMN_BOLIG_BOLIG_ID +" = " + id;
            }else if (b instanceof Leilighet) {
                Leilighet lb = (Leilighet) b;
                sql2 = "UPDATE " + TABLE_LEILIGHET + " SET " +
                        COLUMN_ETASJE + " = " + lb.getEtasje() +
                        ", " + COLUMN_HEIS + " = " + lb.getHeis() + 
                        ", " + COLUMN_BALKONG + " = " + lb.getBalkong() + 
                        ", " + COLUMN_GARASJE + " = " + lb.getGarasje() + 
                        ", " + COLUMN_FELLESVASK + " = " + lb.getFellesvask() + 
                        " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + id;
            }
        //Generate images and digests
        int numOfFiles = bilder.length;
        Map<String, byte[]> bildeListe = new HashMap();
            //Proccess
        for(int i=0;i<numOfFiles;i++){
            try{
                //Konverter bilde til byte array
                Image img = bilder[i];
                BufferedImage buffImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D buffImageGraphics = buffImage.createGraphics();
                buffImageGraphics.drawImage(img, null, null);
                RenderedImage rImage = (RenderedImage) buffImage;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(rImage, "jpeg", baos);
                //Lag digest av byte arrayet
                MessageDigest digester = MessageDigest.getInstance("SHA-256");
                digester.update(baos.toByteArray());
                byte[] digest = digester.digest();
                StringBuilder hexDigest = new StringBuilder();
                for(int x=0;x<digest.length;x++) {
                    hexDigest.append(Integer.toHexString(0xFF & digest[x]));
                }                    
                bildeListe.put(hexDigest.toString(), baos.toByteArray());
                baos.close();
            }catch(IOException|NoSuchAlgorithmException ex){
                System.out.println("Feil i update bolig" + ex);
                return false;
            }
        }
        ArrayList<String> DBdigests = new ArrayList();
        try{
            ResultSet rs = execQuery("SELECT " + COLUMN_DIGEST + " FROM " + TABLE_BILDE + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + b.getId());
            while(rs.next()){
                DBdigests.add(rs.getString(COLUMN_DIGEST));
            }
        }catch(SQLException e){
            System.out.println(e);
            return false;
        }
        Connection con = getConnection();
        try {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            //Handle images
            for(String DBdigest : DBdigests){
                if(bildeListe.containsKey(DBdigest)){
                    bildeListe.remove(DBdigest);
                }else{
                    stmt.executeUpdate("DELETE FROM " + TABLE_BILDE + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + b.getId() + " AND " + COLUMN_DIGEST + " = '" + DBdigest + "'");
                }
            }
            for(String digest : bildeListe.keySet()){
                String sql = "INSERT INTO " + TABLE_BILDE + " (" + COLUMN_BOLIG_BOLIG_ID + ", " + COLUMN_BILDE + ", " + COLUMN_DIGEST + ") values(" + b.getId() + ", ?, '" + digest + "')";
                PreparedStatement pstmt = con.prepareStatement(sql);
                InputStream is = new ByteArrayInputStream(bildeListe.get(digest));
                pstmt.setBinaryStream(1, is, bildeListe.get(digest).length);
                pstmt.execute();
            }
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            con.commit();
            return true;
        }catch (SQLException e) {
            System.out.println("SQL error i udpate bolig: " + e);
            if(con != null){
                try{
                    con.rollback();
                }catch(SQLException ex){
                    System.out.println("SQL error i udpate bolif: " + ex);
                }
            }
            return false;
        } finally{
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("SQL error i udpate bolif: " + e);
                }
            }
        }
    }//End updateBolig
    /**
     * Fjerne boligen ifra databasen
     * @param bolig Boligen som skal bli fjernet
     * @return true hvis boligen ble fjernet, false ellers.
     */
    static boolean removeBolig(Bolig bolig){
        if((Data_Kontrakter.aktivKontraktExists(bolig.getId()+"") || Data_Kontrakter.forespørselExists(bolig.getId()))){
            return false;
        }else{
            String sql1 = "DELETE FROM " + TABLE_BOLIG + " WHERE " + COLUMN_BOLIG_ID + " = " + bolig.getId();
            String sql2 = "";
            if(bolig instanceof Enebolig){
                sql2 = "DELETE FROM " + TABLE_ENEBOLIG_OG_REKKEHUS + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + bolig.getId();
            }else if(bolig instanceof Leilighet){
                sql2 = "DELETE FROM " + TABLE_LEILIGHET + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + bolig.getId();
            }else{
                return false;
            }
            String sql3 = "DELETE FROM " + TABLE_BILDE + " WHERE " + COLUMN_BOLIG_BOLIG_ID + " = " + bolig.getId();
            Connection con = getConnection();
            try{
                con.setAutoCommit(false);
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql3);
                stmt.executeUpdate(sql2);
                stmt.executeUpdate(sql1);
                con.commit();
                return true;
            }catch(SQLException e){
                System.out.println("Error in removeBolig" + e);
                if(con != null){
                    try{
                        con.rollback();
                    }catch(SQLException e2){
                        System.out.println("Error in removeBolig unable to rollback" + e2);
                    }
                }
                return false;
            }finally{
                if(con != null){
                    try{
                        con.setAutoCommit(true);
                    }catch(SQLException e3){
                        System.out.println("Error in removeBolig unable to set autocommit true" + e3);
                    }
                }
            }
        }
    }//End removeBolig
    /**
     * Konverterer int array med nedre og øvre grenser til sql.
     * @param array Tar imot en int array hvor
     * @return  returnerer sql: "where ? between int[0] and int[1]"
     */
    static String sqlConverterRANGE(int[] array) {
        int low = array[0];
        int high = array[0];
        for (int i = 1; i < array.length; i++) {
            if (low > array[i]) {
                low = array[i];
            }
            if (high < array[i]) {
                high = array[i];
            }
        }
        return " BETWEEN " + low + " AND " + high;
    }//End sqlConverterRANGE
    /**
     * Konverterer long array med nedre og øvre grenser til sql.
     * @param array Tar imot en int array hvor
     * @return  returnerer sql: "where ? between long[0] and long[1]"
     */
    static String sqlConverterRANGE(long[] array) {
        long low = array[0];
        long high = array[0];
        for (int i = 1; i < array.length; i++) {
            if (low > array[i]) {
                low = array[i];
            }
            if (high < array[i]) {
                high = array[i];
            }
        }
        return " BETWEEN " + low + " AND " + high;
    }//End sqlConverterRANGE
    /**
     * Konverterer String array med nedre og øvre grenser til sql.
     * @param array Tar imot en int array hvor
     * @return  returnerer sql: "where ? between String[0] and String[1]"
     */
    static String sqlConverterRANGE(String[] array) {
        return " BETWEEN " + array[0] + " AND " + array[1];
    }//End sqlConverterRANGE
    /**
     * Konverterer int array til "IN" type sql
     * @param array
     * @return Returnerer sql: " IN (int[0], int[x]...)
     */
    static String sqlConverterCONTAINS(int[] array) {
        String output = " IN (";
        int length = array.length;
        for (int i = 0; i < length; i++) {
            output += array[i];
            if (i < length) {
                output += ", ";
            }
            if (i == length) {
                output += ")";
            }
        }
        return output;
    }//End sqlConverterCONTAINS
    /**
     * Konverterer en int verdi til en sql boolean
     * @param n
     * @return Returnerer sql: 1 for "false", 2 for "true" 
     */ 
    static String sqlConverterINT2BOOL(int n) {
        if (n == 1) {
            return " = false";
        }
        if (n == 2) {
            return " = true";
        }
        return "WasNot1or2";
    }//End sqlConverterINT2BOOL
    /**
     * Kan fjerne opptatte eller ledige boliger ifra en ArrayList med Bolig objekter
     * @param liste Tar imot en ArrayList med Bolig objekter
     * @param isOccupied Hvis true: fjerner ledige boliger. False: fjerner opptatte/forespurte boliger
     * @return gir tilbake det filtrerte arrayet.
     */
    static private ArrayList<Bolig> filterOpptattBoliger(ArrayList<Bolig> liste, boolean isOccupied){
        for(Iterator i=liste.listIterator();i.hasNext();){
            Bolig bolig = (Bolig) i.next();
            if(isOccupied){
                if(!Data_Kontrakter.aktivKontraktExists(bolig.getId()+"") && !Data_Kontrakter.forespørselExists(bolig.getId())){
                    i.remove();
                }
            }else{
                if(Data_Kontrakter.aktivKontraktExists(bolig.getId()+"") || Data_Kontrakter.forespørselExists(bolig.getId())){
                    i.remove();
                }
            }

         }
        return liste;
    }//End filterOpptattBoliger
}//End class Data_Boliger
