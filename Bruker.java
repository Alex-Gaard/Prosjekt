package Boligformidling;
/**
 * Representereren bruker ifra database.
 * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public class Bruker{
    private String id;
    private String navn;
    private String[] gruppe;
    private String telefon;
    private String adresse;
    private String email;
    private String postnummer;
    
    protected final static String UTLEIER = "utleier";
    protected final static String SØKER = "søker";
    protected final static String BEHANDLER = "behandler";

    protected Bruker(String id,String navn,String[] gruppe,String telefon,String adresse,String email,String postnummer){
            
            this.id = id;
            this.navn = navn;
            this.gruppe = gruppe;
            this.telefon = telefon;
            this.adresse = adresse;
            this.email = email;
            this.postnummer = postnummer;       
    }
    protected String getId(){
        return id;
    }
    protected String getNavn(){
        return navn;
    }
    protected String getTelefon(){
    	return telefon;
    }   
    protected String getAdresse(){
    	return adresse;
    }
    protected String getEmail(){
    	return email;
    }
    protected String getPostnummer(){
    	return telefon;
    }   
    protected boolean is(String bruker){
    	for(String b: gruppe){
    		if(b.equals(bruker)){
    			return true;
    		}
    	}
    	return false;
    }
}//End class Bruker
