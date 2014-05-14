package boligformidling;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
/**
 * Viser GUI hvor en utleier kan vise informasjon sine boliger, og forandre på informasjon knyttet til boligen.
 * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public class SubPanel_Boligeier extends SubPanel {

    //Visning av boliger
    private JTable tabell;
    private BoligTabellModel tabellModel;
    private JPanel editPanel;
    
    private JButton oppdater;
    
    private TableUpdateCallback callback;
    int t = 0;

    /**
     *
     * @param parent
     */
    public SubPanel_Boligeier(MainPanel parent){
        super(parent);
        
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        tabell = new JTable();        
        tabell.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editPanel.removeAll();
                    Bolig bolig = tabellModel.getBolig(tabell.getSelectedRow());
                    if (bolig instanceof Leilighet) {
                        editPanel.add(new editLeilighet((Leilighet) bolig, callback));
                    } else if (bolig instanceof Enebolig) {
                        editPanel.add(new editEnebolig((Enebolig) bolig, callback));
                    }
                    revalidate();
                    editPanel.revalidate();
                    editPanel.revalidate();
                    editPanel.repaint();
                }
            }
        });
        editPanel = new JPanel();
        oppdater = new JButton("Oppdater");
        oppdater.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateList();
            }
        });
        callback = new TableUpdateCallback(){
            public void update(){
                updateList();
            }
            public void reset(Bolig b){
                resetEditPanel(b);
            }
            public void remove(){
                clearEditPanel();
            }
        };
        updateList();
        
        JPanel invisPanel1 = new JPanel();
        invisPanel1.add(oppdater);
        invisPanel1.add(Box.createVerticalGlue());
        add(invisPanel1);
        
        JPanel invisPanel2 = new JPanel();
        invisPanel2.add(new JScrollPane(tabell));
        invisPanel2.add(Box.createVerticalGlue());
        add(invisPanel2);
        
        JPanel invisPanel3 = new JPanel();
        invisPanel3.add(editPanel);
        invisPanel3.add(Box.createVerticalGlue());
        add(invisPanel3);
    }//End constructor
    /**
     * Private metode brukt innad for å oppdatere tabellen med boliger
     */
    private void updateList(){
        Leilighet[] leiligheter = Data_Boliger.getLeiligheter(null, getBruker().getId(), null, null, null, null, null, null, null, 0, 0, 0, 0, null, 0);
        Enebolig[] eneboliger = Data_Boliger.getEneboliger(null, getBruker().getId(), null, null, null, null, null, null, null, 0, null, null, 0);
        
        Bolig[] endeligBoligListe = new Bolig[leiligheter.length + eneboliger.length];
        int loopHelper = 0;
        for(int i=0;i<endeligBoligListe.length;i++){
            if(i<leiligheter.length){
                endeligBoligListe[i] = leiligheter[i];
            }else{
                endeligBoligListe[i] = eneboliger[loopHelper++];
            }
        }
        tabellModel = new BoligTabellModel(endeligBoligListe, new String[]{"Adresse", "Pris"});
        tabell.setModel(tabellModel);
        revalidate();
    }//End updateList
    /**
     * Private metode brukt innad for å tilbakestille boligens felter i editeringen
     * @param bolig boligen som skal bli vist
     */
    private void resetEditPanel(Bolig bolig){
        editPanel.removeAll();
            if (bolig instanceof Enebolig) {
                editPanel.add(new editEnebolig((Enebolig) bolig, callback));
            } else if (bolig instanceof Leilighet) {
                editPanel.add(new editLeilighet((Leilighet) bolig, callback));
            }
            revalidate();
    }//End resetEditPanel
    /**
     * Private metode bruk innad for å fjerne editerings visningen
     */
    private void clearEditPanel(){
        System.out.println("clearEditPanel called but not working");
        editPanel.removeAll();
        editPanel.add(new JLabel(""));
        revalidate();
    }//End clearEditPanel
    /**
     * Interface mellom tabellen og editerings panelet
     */
    interface TableUpdateCallback{
        void update();
        void reset(Bolig b);
        void remove();
    }//End interface TableUpdateCallback
    /**
     * GUI komponent for å vise boligens felter, for så å kunne endre på de. Ikke brukbar i seg selv, men kan bli arvet
     */
    protected abstract class editBolig extends JPanel {
        private JTextField adresse, postadresse, areal, rom, byggår, pris;
        private JTextArea beskrivelse;
        private JLabel JLadresse, JLpostadresse, JLareal, JLrom, JLbyggår, JLbeskrivelse, JLpris;

        private Bolig bolig;
        
        private TableUpdateCallback callback;
        
        private JTable kontraktTable;
        private DefaultTableModel kontraktModell;
        private ArrayList<Leiekontrakt> kontrakter;

        private EditImages editImages;
        
        private JPanel infoPanel;
        
        /**
         *
         * @param Bolig
         * @param c
         */
        public editBolig(Bolig Bolig, TableUpdateCallback c) {
            bolig = Bolig;
            callback = c;

            editImages = new EditImages(bolig);
            
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setBorder(
            BorderFactory.createTitledBorder("Bolig"));
            
            adresse = new JTextField(bolig.getAdresse(), 20);
            postadresse = new JTextField(bolig.getPostadresse() + "", 4);
            areal = new JTextField(bolig.getAreal() + "", 3);
            rom = new JTextField(bolig.getAntRom() + "", 2);
            byggår = new JTextField(bolig.getByggår() + "", 4);
            pris = new JTextField(bolig.getPris() + "", 10);

            beskrivelse = new JTextArea(bolig.getBesk(), 10, 10);

            JLadresse = new JLabel("Adresse");
            JLpostadresse = new JLabel("Postadresse");
            JLareal = new JLabel("Areal i meter");
            JLrom = new JLabel("Antall rom");
            JLbyggår = new JLabel("Byggår");
            JLbeskrivelse = new JLabel("Beskrivelse");
            JLpris = new JLabel("Pris");

            //Populate kontrakt table
            kontraktModell = new DefaultTableModel(null, new String[]{"Period"});
            kontrakter = Data_Kontrakter.getKontrakterForBoligID(Bolig.getId()+"");
            for(int i=0;i<kontrakter.size();i++){
                String[] row = new String[]{kontrakter.get(i).displayThePeriod()};
                kontraktModell.addRow(row);
            }
            kontraktTable = new JTable(kontraktModell);
            
            editPanel = new JPanel();
            
            oppdater = new JButton("Oppdater");
            oppdater.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateList();
                    }
                });
            
            infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
            infoPanel.setBorder(
            BorderFactory.createTitledBorder("Info"));
            
            
            infoPanel.add(JLadresse);
            infoPanel.add(adresse);

            infoPanel.add(JLpostadresse);
            infoPanel.add(postadresse);

            infoPanel.add(JLareal);
            infoPanel.add(areal);

            infoPanel.add(JLrom);
            infoPanel.add(rom);

            infoPanel.add(JLbyggår);
            infoPanel.add(byggår);

            infoPanel.add(JLbeskrivelse);
            infoPanel.add(new JScrollPane(beskrivelse));
            
            infoPanel.add(JLpris);
            infoPanel.add(pris);
            
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
            imagePanel.setBorder(
            BorderFactory.createTitledBorder("Bilder"));
            
            imagePanel.add(editImages);
            
            
            //add(kontraktTable);
            add(infoPanel);
            add(imagePanel);
        }
        String getAdresse(){
            return adresse.getText();
        }
        int getPostadresse(){
            return valueOf(postadresse.getText());
        }

        /**
         *
         * @return
         */
        protected int getAreal(){
            return valueOf(areal.getText());
        }

        /**
         *
         * @return
         */
        protected int getRom(){
            return valueOf(rom.getText());
        }

        /**
         *
         * @return
         */
        protected int getByggår(){
            return valueOf(byggår.getText());
        }

        /**
         *
         * @return
         */
        protected String getBesk(){
            return beskrivelse.getText();
        }

        /**
         *
         * @return
         */
        protected int getPris(){
            return valueOf(pris.getText());
        }
        /**
         * Sjekker om feltene i denne klassen er riktige før en før en insetting/oppdatering av databasen
         * @return true hvis ingen var tomme
         */
        boolean checkBoligFields() {
            return userInputCheckFieldsNotEmpty(new JTextComponent[]{adresse, postadresse, areal, rom, byggår, beskrivelse, pris}) && userInputCheckFieldNumber(new JTextComponent[]{postadresse, areal, rom, byggår, pris});
        }
        int valueOf(String string){
            try{
                return Integer.parseInt(string);
            }catch(NumberFormatException e){
                return 0;
            }
        }
        /**
         * Gir tilbake JPanel'et som har ansvar for å vise bildene knyttet til boligen og editere dem. Bruket av sine arvinger.
         * @return gir panelet som vise bildene, til sine arviger
         */
        EditImages getEditImages(){
            return editImages; 
        }
        /**
         * Gir tilbake JPanel'et som har ansvar for å vise informasjonen knyttet til boligen. Brukt av sine arvinger
         * @return til sine arvinger, returnerer info panelet som de bruker til å legget til mer ting
         */
        JPanel getInfoPanel(){
            return infoPanel;
        }
        /**
         * Sender kommando tilbake til subscribern for å oppdatere en eventuell tabell
         */
        void updateCallback(){
            if(callback != null){
                callback.update();
            }
        }
        /**
         * Sender kommando til subscribern om at den skal resette i dette tilfellet dette panelet
         */
        void resetCallback() {
            if(callback != null){
                callback.reset(bolig);
            }
        }
        /**
         *  Sender kommando til subscribern om at den skal fjerne i dette tilfellet dette panelet
         */
        void removeCallback(){
            if(callback != null){
                callback.remove();
            }
        }
    }//End class editBolig
    /**
     * GUI komponent som viser et panel som kan vise informasjon om en enebolig, og også endre på informasjonen 
     */
    class editEnebolig extends editBolig {

        private JTextField etasjer, total_areal;
        private Checkbox kjeller;
        private JLabel JLetasjer, JLtotal_areal;

        private JButton apply, cancel, delete;

        private Enebolig bolig;
        
        public editEnebolig(Enebolig b, TableUpdateCallback callback){
            super(b, callback);
            bolig = b;
            
            etasjer = new JTextField(bolig.getEtasjer() + "", 2);
            total_areal = new JTextField(bolig.getTotalAreal() + "", 3);

            kjeller = new Checkbox("Kjeller", bolig.getKjeller());

            JLetasjer = new JLabel("Etasjer");
            JLtotal_areal = new JLabel("Total Areal");    
            
            apply = new JButton("Lagre");
            apply.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(getEditImages().getImages().length < 1){
                        JOptionPane.showMessageDialog(null, "Man må ha minst 1 bilde");
                        return;
                    }
                    if(checkBoligFields() && userInputCheckFieldsNotEmpty(new JTextComponent[]{etasjer, total_areal}) && userInputCheckFieldNumber(new JTextComponent[]{etasjer, total_areal})) {
                        Data_Boliger.updateBolig(new Enebolig(bolig.getId(), bolig.getEier(), getAdresse(), getPostadresse(), getAreal(), getRom(), getByggår(), getBesk(), null, getPris(), getEtasjer(), getKjeller(), getTotalAreal()), getEditImages().getImages());
                        updateCallback();
                        JOptionPane.showMessageDialog(null, "Oppdatering vellykket");
                    }
                }
            });
            cancel = new JButton("Angre");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetCallback();
                }
            });
            delete = new JButton("Fjern bolig");
            delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(JOptionPane.showConfirmDialog(null, "Er du sikkert på at du vil slette boligen?", "Slett Bolig", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        if(Data_Boliger.removeBolig(bolig)){
                            JOptionPane.showMessageDialog(null, "Boligen er slettet");
                            updateList();
                            removeCallback();
                        }else{
                            JOptionPane.showMessageDialog(null, "Ikke slettet, ta kontakt med en kundebehandler hvis dette fortsetter.");
                        }
                    }
                }
            });
            
            getInfoPanel().add(JLetasjer);
            getInfoPanel().add(etasjer);
            getInfoPanel().add(kjeller);
            getInfoPanel().add(JLtotal_areal);
            getInfoPanel().add(total_areal);

            getInfoPanel().add(apply);
            getInfoPanel().add(cancel);
            getInfoPanel().add(delete);
        }
        int getEtasjer(){
            return valueOf(etasjer.getText());
        }
        boolean getKjeller(){
            return kjeller.getState();
        }
        int getTotalAreal(){
            return valueOf(total_areal.getText());
        }
    }//End class editEnebolig
    /**
     * GUI komponent som viset et panel med informasjon om en leilighet og kan endre på informasjonen
     */
    class editLeilighet extends editBolig {
        private Checkbox heis, balkong, garasje, fellesvask;
        private JTextField etasje;
        private JLabel JLetasje;

        private JButton apply, cancel, delete;

        private Leilighet bolig;
        
        public editLeilighet(Leilighet b, TableUpdateCallback callback) {
            super(b, callback);
            bolig = b;
            etasje = new JTextField(bolig.getEtasje() + "", 2);
            heis = new Checkbox("Heis", bolig.getHeis());
            balkong = new Checkbox("Balkong", bolig.getBalkong());
            garasje = new Checkbox("Garasje", bolig.getGarasje());
            fellesvask = new Checkbox("Fellesvask", bolig.getFellesvask());

            JLetasje = new JLabel("Etasje");

            apply = new JButton("Lagre");
            apply.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(getEditImages().getImages().length < 1){
                        JOptionPane.showMessageDialog(null, "Man må ha minst 1 bilde");
                        return;
                    }
                    if(checkBoligFields() && userInputCheckFieldsNotEmpty(new JTextComponent[]{etasje}) && userInputCheckFieldNumber(new JTextComponent[]{etasje})) {
                        Data_Boliger.updateBolig(new Leilighet(bolig.getId(), bolig.getEier(), getAdresse(), getPostadresse(), getAreal(), getRom(), getByggår(),getBesk(), null, getPris(), getEtasje(), getHeis(), getBalkong(), getGarasje(), getFellesvask()), getEditImages().getImages());
                        updateCallback();
                        JOptionPane.showMessageDialog(null, "Oppdatering vellykket");
                    }
                }
            });
            cancel = new JButton("Angre");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetCallback();
                }
            });
            delete = new JButton("Fjern bolig");
            delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(JOptionPane.showConfirmDialog(null, "Er du sikkert på at du vil slette boligen?", "Slett Bolig", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        if(Data_Boliger.removeBolig(bolig)){
                            JOptionPane.showMessageDialog(null, "Boligen er slettet");
                            updateList();
                            removeCallback();
                        }else{
                            JOptionPane.showMessageDialog(null, "Ikke slettet, ta kontakt med en kundebehandler hvis dette fortsetter.");
                        }
                    }
                }
            });
            getInfoPanel().add(JLetasje);
            getInfoPanel().add(etasje);
            getInfoPanel().add(heis);
            getInfoPanel().add(balkong);
            getInfoPanel().add(garasje);
            getInfoPanel().add(fellesvask);

            getInfoPanel().add(apply);
            getInfoPanel().add(cancel);
            getInfoPanel().add(delete);
        }
        int getEtasje(){
            return valueOf(etasje.getText());
        }
        boolean getHeis(){
            return heis.getState();
        }
        boolean getBalkong(){
            return balkong.getState();
        }
        boolean getGarasje(){
            return garasje.getState();
        }
        boolean getFellesvask(){
            return fellesvask.getState();
        }
    }//End class editLeilighet
}//End clas Boligeier
