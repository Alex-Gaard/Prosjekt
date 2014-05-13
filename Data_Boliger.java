    package Boligformidling;

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

        //T for table, C for collum
        //Boligsøker
        public static final String T_BS_NAME = "boligsøker";
        public static final String T_BS_C_ID = "Bruker_Personnummer";

        //Boligeier
        public static final String T_BE_NAME = "utleier";
        public static final String T_BE_C_ID = "Bruker_Personnummer";
        public static final String T_BE_C_FIRMA = "Firma";

        //Bolig Tabell Felter
        public static final String T_B_NAME = "bolig";
        public static final String T_B_C_ID = "BoligID";
        public static final String T_B_C_EIER = "Utleier_Bruker_Personnummer";
        public static final String T_B_C_ADRESSE = "Adresse";
        public static final String T_B_C_POSTADRESSE = "Postnummer";
        public static final String T_B_C_BOAREAL = "Boareal";
        public static final String T_B_C_ANTROM = "Antall_Rom";
        public static final String T_B_C_BYGGÅR = "Byggår";
        public static final String T_B_C_BESKRIVELSE = "Beskrivelse";
        public static final String T_B_C_AVERTERT = "Avertert";
        public static final String T_B_C_PRIS = "Utleie_pris";

        public static final String T_B_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + T_B_NAME
                + " ("
                + T_B_C_ID + " INT, "
                + T_B_C_EIER + " INt, "
                + T_B_C_ADRESSE + " VARCHAR(45), "
                + T_B_C_POSTADRESSE + " INT, "
                + T_B_C_BOAREAL + " INT, "
                + T_B_C_ANTROM + " INT, "
                + T_B_C_BYGGÅR + " INT, "
                + T_B_C_BESKRIVELSE + " VARHCAR(145)"
                + T_B_C_PRIS + " DATE, "
                + T_B_C_AVERTERT + " INT"
                + ")";
        //Enebolig tabell
        public static final String T_EB_NAME = "enebolig_og_rekkehus";
        public static final String T_EB_C_ID = "Bolig_BoligID";
        public static final String T_EB_C_ETASJER = "Antall_etasjer";
        public static final String T_EB_C_KJELLER = "Kjeller";
        public static final String T_EB_C_TAREAL = "Tomt_areal";
        public static final String T_EB_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + T_EB_NAME
                + " ("
                + T_EB_C_ID + " INT, "
                + T_EB_C_ETASJER + " INT, "
                + T_EB_C_KJELLER + " BOOLEAN, "
                + T_EB_C_TAREAL + " INT"
                + ")";

        //Leilighet Tabell Felter
        public static final String T_L_NAME = "leilighet";
        public static final String T_L_C_ID = "Bolig_BoligID";
        public static final String T_L_C_ETASJE = "Etasje";
        public static final String T_L_C_HEIS = "Heis";
        public static final String T_L_C_BALKONG = "Balkong";
        public static final String T_L_C_GARASJE = "Garasje";
        public static final String T_L_C_FELLESVASK = "Fellesvaskeri";
        public static final String T_L_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + T_L_NAME
                + "("
                + T_L_C_ID + " INT, "
                + T_L_C_ETASJE + " VARCHAR(255), "
                + T_L_C_HEIS + " BOOLEAN, "
                + T_L_C_BALKONG + " BOOLEAN, "
                + T_L_C_GARASJE + " BOOLEAN, "
                + T_L_C_FELLESVASK + " BOOLEAN"
                + ")";
        //Bilde Tabell Felter
        public static final String T_BILD_NAME = "bolig_bilde";
        public static final  String T_BILD_C_ID = "Bolig_BoligID";
        public static final  String T_BILD_C_BILDE = "Bilde";
        public static final  String T_BILD_C_DIGEST = "Digest";
        public static final  String T_BILD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + T_BILD_NAME
                + "("
                + T_BILD_C_ID + " INT, "
                + T_BILD_C_BILDE + "LONGBLOB, "
                + T_BILD_C_DIGEST + "VARCHAR(255)"
                + ")";

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
                    try(ResultSet rs = execQuery("SELECT " + T_BILD_C_DIGEST + " FROM " +  T_BILD_NAME + " WHERE " + T_BILD_C_ID + " = " + boligId)){
                        while(rs.next()){
                            digestKeys.add(rs.getString(T_BILD_C_DIGEST));
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
             * Gir tilbake 1 bilde ifra databasen med spesifisert index
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
                    try(ResultSet rs = execQuery("SELECT * FROM " +  T_BILD_NAME + " WHERE " + T_BILD_C_ID + " = " + boligId + " AND " + T_BILD_C_DIGEST + " = '" + DataBasedigestKeys.get(index) + "'")){
                        while(rs.next()){
                            list.put(rs.getString(T_BILD_C_DIGEST), ImageIO.read(rs.getBinaryStream(T_BILD_C_BILDE)));
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
                if (!execQuery("SELECT * FROM " + T_BE_NAME + " WHERE " + T_BE_C_ID + " = " + kunde.getId()).next()) {
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
            String sql1 = "INSERT INTO " + T_B_NAME + " (" +
                    T_B_C_EIER + ", " + 
                    T_B_C_ADRESSE + ", " + 
                    T_B_C_POSTADRESSE + ", " + 
                    T_B_C_BOAREAL + ", " + 
                    T_B_C_ANTROM + ", " + 
                    T_B_C_BYGGÅR + ", " + 
                    T_B_C_BESKRIVELSE + ", " + 
                    T_B_C_AVERTERT + ", " + 
                    T_B_C_PRIS + ")";
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
                    sql2 = "INSERT INTO " + T_EB_NAME + " (" + T_EB_C_ID + ", " + T_EB_C_ETASJER + ", " + T_EB_C_KJELLER + ", " + T_EB_C_TAREAL + ")";
                    sql2 += " VALUES (" + boligId + ", " + bolig2.getEtasjer() + ", " + bolig2.getKjeller() + ", " + bolig2.getTotalAreal() + ")";
                }else if (bolig instanceof Leilighet){
                    Leilighet bolig2 = (Leilighet) bolig;
                    sql2 = "INSERT INTO " + T_L_NAME + " (" + T_L_C_ID + ", " + T_L_C_ETASJE + ", " + T_L_C_HEIS + ", " + T_L_C_BALKONG + ", " + T_L_C_GARASJE + ", " + T_L_C_FELLESVASK + ")";
                    sql2 += " VALUES (" + boligId + ", " + bolig2.getEtasje() + ", " + bolig2.getHeis() + ", " + bolig2.getBalkong() + ", " + bolig2.getGarasje() + ", " + bolig2.getFellesvask() + ")";
                }else{
                    abort = true;
                    return false;
                }
                stmt.executeUpdate(sql2);

                //Sett inn bilde(r)
                String sql3 = "INSERT INTO " + T_BILD_NAME + " (" + T_BILD_C_ID + ", " + T_BILD_C_BILDE + ", " + T_BILD_C_DIGEST + ") values(" + boligId + ", ?, ?)";
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
            String sql = "SELECT * FROM " + T_B_NAME;
            if (Id != null || Eier != null || Areal != null || Postadresse != null || Rom != null || Byggår != null || Avertert != null || Pris != null) {
                sql += " WHERE ";
            }
            if (Id != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_ID + " = " + Id;
                counter++;
            }
            if (Eier != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_EIER + " = " + Eier;
                counter++;
            }
            if (Areal != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_BOAREAL + sqlConverterRANGE(Areal);
                counter++;
            }
            if (Postadresse != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_POSTADRESSE + sqlConverterCONTAINS(Postadresse);
                counter++;
            }
            if (Rom != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_ANTROM + sqlConverterRANGE(Rom);
                counter++;
            }
            if (Byggår != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_BYGGÅR + sqlConverterRANGE(Byggår);
                counter++;
            }
            if (Avertert != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_AVERTERT + sqlConverterRANGE(Avertert);
                counter++;
            }
            if (Pris != null) {
                if (counter > 0) {
                    sql += " AND ";
                }
                sql += T_B_C_PRIS + sqlConverterRANGE(Pris);
                counter++;
            }
            if (modifiers != null) {
                sql += " " + modifiers;
            }
            ArrayList<Bolig> boliger = new ArrayList();
            try {
                ResultSet r = execQuery(sql);
                while (r.next()) {
                    boliger.add(new Bolig(r.getInt(T_B_C_ID), 
                                        r.getString(T_B_C_EIER), 
                                        r.getString(T_B_C_ADRESSE), 
                                        r.getInt(T_B_C_POSTADRESSE), 
                                        r.getInt(T_B_C_BOAREAL), 
                                        r.getInt(T_B_C_ANTROM), 
                                        r.getInt(T_B_C_BYGGÅR), 
                                        r.getString(T_B_C_BESKRIVELSE), 
                                        r.getString(T_B_C_AVERTERT), 
                                        r.getLong(T_B_C_PRIS)));
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
            String sql = "SELECT " + T_B_NAME + ".*, " + T_EB_NAME + ".* from " + T_B_NAME + ", " + T_EB_NAME + " where " + T_B_NAME + "." + T_B_C_ID + " = " + T_EB_NAME + "." + T_EB_C_ID;
            if (Id != null) {
                sql += " AND ";
                sql += T_B_C_ID + " = " + Id;
            }
            if (Eier != null) {
                sql += " AND ";
                sql += T_B_C_EIER + " = " + Eier;
            }
            if (Areal != null) {
                sql += " AND ";
                sql += T_B_C_BOAREAL + sqlConverterRANGE(Areal);
            }
            if (Postadresse != null) {
                sql += " AND ";
                sql += T_B_C_POSTADRESSE + sqlConverterCONTAINS(Postadresse);
            }
            if (Rom != null) {
                sql += " AND ";
                sql += T_B_C_ANTROM + sqlConverterRANGE(Rom);
            }
            if (Byggår != null) {
                sql += " AND ";
                sql += T_B_C_BYGGÅR + sqlConverterRANGE(Byggår);
            }
            if (Avertert != null) {
                sql += " AND ";
                sql += T_B_C_AVERTERT + sqlConverterRANGE(Avertert);
            }
            if (Pris != null) {
                sql += " AND ";
                sql += T_B_C_PRIS + sqlConverterRANGE(Pris);
            }
            if (Etasjer != null) {
                sql += " AND ";
                sql += T_EB_C_ETASJER + sqlConverterRANGE(Etasjer);
            }
            if (Kjeller != 0) {
                sql += " AND ";
                sql += T_EB_C_KJELLER + sqlConverterINT2BOOL(Kjeller);
            }
            if (Total_areal != null) {
                sql += " AND ";
                sql += T_EB_C_TAREAL + sqlConverterRANGE(Total_areal);
            }
            if (modifiers != null) {
                sql += " " + modifiers;
            }
            ArrayList<Bolig> eneboliger = new ArrayList();
            try {
                ResultSet r = execQuery(sql);
                while (r.next()){
                    eneboliger.add(new Enebolig(r.getInt(T_B_C_ID),
                                                r.getString(T_B_C_EIER),
                                                r.getString(T_B_C_ADRESSE),
                                                r.getInt(T_B_C_POSTADRESSE),
                                                r.getInt(T_B_C_BOAREAL),
                                                r.getInt(T_B_C_ANTROM),
                                                r.getInt(T_B_C_BYGGÅR),
                                                r.getString(T_B_C_BESKRIVELSE),
                                                r.getString(T_B_C_AVERTERT),
                                                r.getLong(T_B_C_PRIS),
                                                r.getInt(T_EB_C_ETASJER),
                                                r.getBoolean(T_EB_C_KJELLER),
                                                r.getInt(T_EB_C_TAREAL)));
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
            String sql = "SELECT " + T_B_NAME + ".*, " + T_L_NAME + ".* from " + T_B_NAME + ", " + T_L_NAME + " where " + T_B_NAME + "." + T_B_C_ID + " = " + T_L_NAME + "." + T_L_C_ID;
            if (Id != null) {
                sql += " AND ";
                sql += T_B_C_ID + " = " + Id;
            }
            if (Eier != null) {
                sql += " AND ";
                sql += T_B_C_EIER + " = " + Eier;
            }
            if (Areal != null) {
                sql += " AND ";
                sql += T_B_C_BOAREAL + sqlConverterRANGE(Areal);
            }
            if (Postadresse != null) {
                sql += " AND ";
                sql += T_B_C_POSTADRESSE + sqlConverterCONTAINS(Postadresse);
            }
            if (Rom != null) {
                sql += " AND ";
                sql += T_B_C_ANTROM + sqlConverterRANGE(Rom);
            }
            if (Byggår != null) {
                sql += " AND ";
                sql += T_B_C_BYGGÅR + sqlConverterRANGE(Byggår);
            }
            if (Avertert != null) {
                sql += " AND ";
                sql += T_B_C_AVERTERT + sqlConverterRANGE(Avertert);
            }
            if (Pris != null) {
                sql += " AND ";
                sql += T_B_C_PRIS + sqlConverterRANGE(Pris);
            }
            if (Etasje != null) {
                sql += " AND ";
                sql += T_L_C_ETASJE + sqlConverterRANGE(Etasje);
            }
            if (Heis != 0) {
                sql += " AND ";
                sql += T_L_C_HEIS + sqlConverterINT2BOOL(Heis);
            }
            if (Balkong != 0) {
                sql += " AND ";
                sql += T_L_C_HEIS + sqlConverterINT2BOOL(Balkong);
            }
            if (Garasje != 0) {
                sql += " AND ";
                sql += T_L_C_GARASJE + sqlConverterINT2BOOL(Garasje);
            }
            if (Fellesvask != 0) {
                sql += " AND ";
                sql += T_L_C_FELLESVASK + sqlConverterINT2BOOL(Fellesvask);
            }
            if (modifiers != null) {
                sql += " " + modifiers;
            }
            ArrayList<Bolig> leiligheter = new ArrayList();
            try {
                ResultSet r = execQuery(sql);
                while (r.next()) {
                    leiligheter.add(new Leilighet(r.getInt(T_B_C_ID), 
                            r.getString(T_B_C_EIER), 
                            r.getString(T_B_C_ADRESSE), 
                            r.getInt(T_B_C_POSTADRESSE), 
                            r.getInt(T_B_C_BOAREAL), 
                            r.getInt(T_B_C_ANTROM), 
                            r.getInt(T_B_C_BYGGÅR), 
                            r.getString(T_B_C_BESKRIVELSE), 
                            r.getString(T_B_C_AVERTERT), 
                            r.getLong(T_B_C_PRIS), 
                            r.getInt(T_L_C_ETASJE), 
                            r.getBoolean(T_L_C_HEIS), 
                            r.getBoolean(T_L_C_BALKONG), 
                            r.getBoolean(T_L_C_GARASJE), 
                            r.getBoolean(T_L_C_FELLESVASK)));
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
            String sql1 = "UPDATE " + T_B_NAME + " SET " +
                        T_B_C_ADRESSE + " = '" + b.getAdresse() + "'" +
                        ", " + T_B_C_BOAREAL + " = " + b.getAreal() +
                        ", " + T_B_C_ANTROM + " = " + b.getAntRom() +
                        ", " + T_B_C_BYGGÅR + " = " + b.getByggår() +
                        ", " + T_B_C_BESKRIVELSE + " = '" + b.getBesk() + "'" +
                        ", " + T_B_C_PRIS + " = " + b.getPris() + 
                        ", " + T_B_C_POSTADRESSE + " = " + b.getPostadresse() +
                        " WHERE "+ T_B_C_ID +" = " + id;
            String sql2 = "";
            if (b instanceof Enebolig) {
                    Enebolig eb = (Enebolig) b;
                    sql2 = "UPDATE "+ T_EB_NAME + " SET " + 
                            T_EB_C_ETASJER + " = " + eb.getEtasjer()+ 
                            ", " + T_EB_C_KJELLER + " = " + eb.getKjeller() + 
                            ", " + T_EB_C_TAREAL + " = " + eb.getTotalAreal() +
                            " WHERE "+ T_EB_C_ID +" = " + id;
                }else if (b instanceof Leilighet) {
                    Leilighet lb = (Leilighet) b;
                    sql2 = "UPDATE " + T_L_NAME + " SET " +
                            T_L_C_ETASJE + " = " + lb.getEtasje() +
                            ", " + T_L_C_HEIS + " = " + lb.getHeis() + 
                            ", " + T_L_C_BALKONG + " = " + lb.getBalkong() + 
                            ", " + T_L_C_GARASJE + " = " + lb.getGarasje() + 
                            ", " + T_L_C_FELLESVASK + " = " + lb.getFellesvask() + 
                            " WHERE " + T_L_C_ID + " = " + id;
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
                ResultSet rs = execQuery("SELECT " + T_BILD_C_DIGEST + " FROM " + T_BILD_NAME + " WHERE " + T_BILD_C_ID + " = " + b.getId());
                while(rs.next()){
                    DBdigests.add(rs.getString(T_BILD_C_DIGEST));
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
                        stmt.executeUpdate("DELETE FROM " + T_BILD_NAME + " WHERE " + T_BILD_C_ID + " = " + b.getId() + " AND " + T_BILD_C_DIGEST + " = '" + DBdigest + "'");
                    }
                }
                for(String digest : bildeListe.keySet()){
                    String sql = "INSERT INTO " + T_BILD_NAME + " (" + T_BILD_C_ID + ", " + T_BILD_C_BILDE + ", " + T_BILD_C_DIGEST + ") values(" + b.getId() + ", ?, '" + digest + "')";
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
                String sql1 = "DELETE FROM " + T_B_NAME + " WHERE " + T_B_C_ID + " = " + bolig.getId();
                String sql2 = "";
                if(bolig instanceof Enebolig){
                    sql2 = "DELETE FROM " + T_EB_NAME + " WHERE " + T_EB_C_ID + " = " + bolig.getId();
                }else if(bolig instanceof Leilighet){
                    sql2 = "DELETE FROM " + T_L_NAME + " WHERE " + T_L_C_ID + " = " + bolig.getId();
                }else{
                    return false;
                }
                String sql3 = "DELETE FROM " + T_BILD_NAME + " WHERE " + T_BILD_C_ID + " = " + bolig.getId();
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
