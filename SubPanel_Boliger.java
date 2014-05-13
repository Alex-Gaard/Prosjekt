package boligformidling;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import javax.swing.plaf.basic.BasicArrowButton;
/**
 * Har ansvar for å kunne søke på samtlige boliger i databasen. Dog kun kundebehandlere har muligheten til å se
 * forespurte eller opptatte leiligheter
 * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public class SubPanel_Boliger extends SubPanel {
    private userInputRange postadresse, areal, rom, byggår, etasje, etasjer, total_areal, pris;
    private userInputAlternativeCheckbox kjeller, heis, balkong, garasje, fellesvask, opptatt;
    private JLabel JLenebolig, JLleilighet;
    private JButton boligSok, eneboligSok, leilighetSok;
    private dateChooser avertert;

    //Visning av resultater
    BoligTabellModel tabellModel;
    private JTable tabell;
    private JPanel detaljer;
    //End Visning av resultater
    
    public SubPanel_Boliger(MainPanel parent) {
        super(parent);

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        //Søke felt
        //postadresse = new userInputRange(4, "Postadresse");
        areal = new userInputRange(3, "Areal");
        rom = new userInputRange(2, "Antall rom");
        //postadresse
        byggår = new userInputRange(4, "byggår");
        etasje = new userInputRange(3, "Hvilken etasje");
        etasjer = new userInputRange(3, "Antall etasjer");
        total_areal = new userInputRange(3, "Totalt areal");
        pris = new userInputRange(12, "Pris");

        kjeller = new userInputAlternativeCheckbox("Kjeller");
        heis = new userInputAlternativeCheckbox("Heis");
        balkong = new userInputAlternativeCheckbox("Balkong");
        garasje = new userInputAlternativeCheckbox("Balkong");
        fellesvask = new userInputAlternativeCheckbox("Fellesvask");
        //For en kundebehandler
        opptatt = new userInputAlternativeCheckbox("Forespurt eller opptatt");
        avertert = new dateChooser("Avertert Dato");

        eneboligSok = new JButton("Søk");
        eneboligSok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Enebolig[] boliger = Data_Boliger.getEneboliger(null, 
                        null, 
                        areal.getArray(), 
                        /*postadresse.getArray*/ null, 
                        rom.getArray(), 
                        byggår.getArray(),  
                        avertert.getArray(), 
                        pris.getLongArray(), 
                        etasjer.getArray(), 
                        kjeller.getIntBoolean(), 
                        total_areal.getArray(), 
                        null, 
                        opptatt.getIntBoolean());
                displayResults(boliger);
            }
        });
        leilighetSok = new JButton("Søk");
        leilighetSok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leilighet[] boliger = Data_Boliger.getLeiligheter(null, 
                        null, 
                        areal.getArray(), 
                        /*postadresse.getArray*/ null, 
                        rom.getArray(), 
                        byggår.getArray(),  
                        avertert.getArray(), 
                        pris.getLongArray(), 
                        etasje.getArray(), 
                        heis.getIntBoolean(), 
                        balkong.getIntBoolean(), 
                        garasje.getIntBoolean(), 
                        fellesvask.getIntBoolean(), 
                        null, 
                        opptatt.getIntBoolean());
                displayResults(boliger);
            }
        });
        boligSok = new JButton("Søk");
        boligSok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Bolig[] boliger = Data_Boliger.getBoliger(null, 
                                null, areal.getArray(), 
                                /*postadresse.getArray*/ null, 
                                rom.getArray(), byggår.getArray(), 
                                avertert.getArray(), 
                                pris.getLongArray(), 
                                null, 
                                opptatt.getIntBoolean());
                        displayResults(boliger);
                    }
                });
        detaljer = new JPanel();
        detaljer.setLayout(new BoxLayout(detaljer, BoxLayout.PAGE_AXIS));
        detaljer.setBorder(
            BorderFactory.createTitledBorder("Detaljer"));

        //Visning av resultater
        tabell = new JTable();
        tabell.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabell.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    detaljer.removeAll();
                    Bolig bolig = tabellModel.getBolig(tabell.getSelectedRow());
                    detaljer.add(new DisplayBolig(bolig, getBruker()));
                    revalidate();
                }
            }
        });
        
        //Begynn GUI adding
        //add(postadresse);//TODO custom dropdown med liste over fylker som gir tilbake alle postnummer i dets fylke
        JPanel boligPanel = new JPanel();
        boligPanel.setLayout(new BoxLayout(boligPanel, BoxLayout.PAGE_AXIS));
        boligPanel.setBorder(
            BorderFactory.createTitledBorder("Boliger"));
        boligPanel.add(areal);
        boligPanel.add(rom);
        boligPanel.add(byggår);
        boligPanel.add(pris);
        
        if(getBruker().is(Bruker.BEHANDLER)){
            boligPanel.add(opptatt);
            boligPanel.add(avertert);
        }else{
            opptatt.setState(1);
        }
        boligPanel.add(boligSok);
        
        JPanel eneboligPanel = new JPanel();
        eneboligPanel.setLayout(new BoxLayout(eneboligPanel, BoxLayout.PAGE_AXIS));
        eneboligPanel.setBorder(
            BorderFactory.createTitledBorder("Eneboliger"));
        eneboligPanel.add(etasjer);
        eneboligPanel.add(kjeller);
        eneboligPanel.add(total_areal);
        eneboligPanel.add(eneboligSok);
        
        
        JPanel leilighetPanel = new JPanel();
        leilighetPanel.setLayout(new BoxLayout(leilighetPanel, BoxLayout.PAGE_AXIS));
        leilighetPanel.setBorder(
            BorderFactory.createTitledBorder("Leiligheter"));
        leilighetPanel.add(etasje);
        leilighetPanel.add(heis);
        leilighetPanel.add(balkong);
        leilighetPanel.add(garasje);
        leilighetPanel.add(fellesvask);
        leilighetPanel.add(leilighetSok);
        
        //End søke felt
        boligPanel.add(eneboligPanel);
        boligPanel.add(leilighetPanel);
        
        JPanel invisPanel1 = new JPanel();
        invisPanel1.add(boligPanel);
        invisPanel1.add(Box.createVerticalGlue());
        add(invisPanel1);
        
        JPanel invisPanel2 = new JPanel();
        invisPanel2.add(new JScrollPane(tabell));
        invisPanel2.add(Box.createVerticalGlue());
        add(invisPanel2);
        
        JPanel invisPanel3 = new JPanel();
        invisPanel3.add(detaljer);
        invisPanel3.add(Box.createVerticalGlue());
        add(invisPanel3);
    }//End constructor
    /**
     * GUI komponent som viser bilde og informasjon om en bolig
     */
    class DisplayBolig extends JPanel{
        private Bolig bolig;
        private Bruker bruker;
        
        private JButton OpprettkontraktForespørsel, slett;
        
        private JPanel bilde;
        private int currentImageCounter;
        
        public DisplayBolig(Bolig b, Bruker br){
            bolig = b;
            bruker = br;
            currentImageCounter = 1;
            
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));           
            
            JPanel pictureScroller = new JPanel();
            BasicArrowButton back = new BasicArrowButton(BasicArrowButton.WEST);
            back.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        --currentImageCounter;
                        Image image = bolig.getBilde(currentImageCounter);
                        if(image != null){
                            bilde.removeAll();
                            bilde.add(new JLabel(new ImageIcon(image)));
                        }
                        revalidate();
                    }
                });
            BasicArrowButton forward = new BasicArrowButton(BasicArrowButton.EAST);
            forward.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        ++currentImageCounter;
                        Image image = bolig.getBilde(currentImageCounter);
                        if(image != null){
                            bilde.removeAll();
                            bilde.add(new JLabel(new ImageIcon(image)));
                        }
                        revalidate();
                    }
                });
            pictureScroller.add(back, BorderLayout.WEST);
            pictureScroller.add(forward , BorderLayout.EAST);
            
            bilde = new JPanel();
            Image image = bolig.getBilde(currentImageCounter);
            if(image != null){
                bilde.add(new JLabel(new ImageIcon(image)));
            }            
                
            add(pictureScroller);
            add(bilde);    
             
            add(new JLabel("Adresse: " + bolig.getAdresse()));
            add(new JLabel("Postnummer: " + bolig.getPostadresse()));
            add(new JLabel("Areal: " + bolig.getAreal()));
            add(new JLabel("Antall Rom: " + bolig.getAntRom()));
            add(new JLabel("Byggår: " + bolig.getByggår()));
            add(new JLabel("Beskrivelse: " + bolig.getBesk()));
            add(new JLabel("Pris: " + bolig.getPris()));
            
            if(bolig instanceof Enebolig){
                Enebolig enebolig = (Enebolig) bolig;
                add(new JLabel("Etasjer: " + enebolig.getEtasjer()));
                add(new JLabel("Total Areal: " + enebolig.getTotalAreal()));
                add(new JLabel("Har kjeller: " + (enebolig.getKjeller() ? "ja" : "nei")));
            }else if(bolig instanceof Leilighet){
                Leilighet leilighet = (Leilighet) bolig;
                add(new JLabel("Etasje: " + leilighet.getEtasje()));
                add(new JLabel("Har heis: " + (leilighet.getHeis() ? "ja" : "nei")));
                add(new JLabel("Har balkong: " + (leilighet.getBalkong() ? "ja" : "nei")));
                add(new JLabel("Har garasje: " + (leilighet.getGarasje() ? "ja" : "nei")));
                add(new JLabel("Har fellesvask: " + (leilighet.getFellesvask() ? "ja" : "nei")));
            }
            
            if(bruker.is(bruker.SØKER) && Data_Kontrakter.getKontraktForesørsler(bruker.getId(), 0, null, null, 0).isEmpty()){         
                OpprettkontraktForespørsel = new JButton("Forespør kontrakt opprettelse");
                OpprettkontraktForespørsel.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        if(JOptionPane.showConfirmDialog(null, "Er du sikkert på at du er intressert i å opprette kontrakt", "Kontrakt forespørsel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            if(Data_Kontrakter.nyKontraktForespørsel(bolig, bruker)){
                                OpprettkontraktForespørsel.setEnabled(false);
                            }
                        }
                    }
                });
                add(OpprettkontraktForespørsel);
            }
            if(bruker.is(Bruker.BEHANDLER)){
                slett = new JButton("SLETT BOLIG");
                slett.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        if(JOptionPane.showConfirmDialog(null, "Er du sikkert på at du vil slette boligen?", "Slett Bolig", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            if(Data_Boliger.removeBolig(bolig)){
                                JOptionPane.showMessageDialog(null, "Boligen er slettet");
                            }else{
                                JOptionPane.showMessageDialog(null, "Error");
                            }
                        }
                    }
                });
                add(slett);
            }
        }
    }//End class DisplayBolig
    /**
     * Privat metode brukt innad for å vise resultatene ifra databasen i tabellen
     * @param array en array med Boliger, Eneboliger og Leiligheter
     */
    private void displayResults(Bolig[] array) {
        detaljer.removeAll();
        tabellModel = new BoligTabellModel(array, new String[]{"Adresse", "Pris", "Areal"});
        tabell.setModel(tabellModel);
    }//End displayResults
    /**
     * Viser en bolig. Bruket av externe funksjoner.
     * @param bolig 
     */
    void visBolig(Bolig bolig){        
        detaljer.removeAll();
        detaljer.add(new DisplayBolig(bolig, getBruker()));
        revalidate();
    }//End visBolig
}//End class SubPanel_Boliger
