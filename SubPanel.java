package boligformidling;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
/**
 * Abstrakt GUI klasse som kan bli arvet.
 * Innehar flere funksjoner som gjør seg nyttig i sine arvinger.
 * @
 */
abstract class SubPanel extends JPanel {
    
    MainPanel parent;
    
    final static int MINIMUM_IMAGES = 1;
    final static int MAXIMUM_IMAGES = 9;
    final static int BOLIG_IMAGE_WIDTH = 300;
    final static int BOLIG_IMAGE_HEIGHT = 200;
    final static int BOLIG_THUMBNAIL_WIDTH = 60;
    final static int BOLIG_THUMBNAIL_HEIGHT = 60;
    
    public SubPanel(MainPanel Parent) {
        parent = Parent;
    }
    /**
     * Returnerer Vindu_Main objektet
     * @return  
     */
    Vindu_Main getRoot() {
        return parent.getRoot();
    }
    /**
     * Returnerer MainPanelet som holder dette SubPanelet
     * @
     */
    MainPanel getMainPanel() {
        return parent;
    }
    /**
     * Returnerer brukeren som bruker SubPanelet og MainPanelet
     * @return 
     */
    Bruker getBruker() {
        return getMainPanel().getBruker();
    }
    /**
     * GUI dropdown box med tallene ifra 'start' til 'slutt'
     * @param start Minste tall i dropdown boxen
     * @param slutt Høyeste tall i dropdown boxen
     * @return 
     */
    protected JComboBox getComboBox(int start, int slutt) {
        if (slutt <= start) {
            return null;
        }
        String[] array = new String[slutt - start];
        for (int i = start; i < slutt; i++) {
            array[i] = String.valueOf(i);
        }
        JComboBox jcb = new JComboBox(array);
        return jcb;
    }
    /**
     * GUI Element som innehar alle elementene for å kunne velge en dato. Innehar get funksjonalitet
     */
    class dateChooser extends JPanel {
        private JCheckBox enabledBox;
        private boolean enabled;
    
        private JComboBox JCBdStart, JCBmStart, JCByStart;
        private JComboBox JCBdEnd, JCBmEnd, JCByEnd;
        //Får antall dager i månedene mht skuddår
        private int[] DayMonthRelation = getDaysForEachMonth(Calendar.getInstance().get(Calendar.YEAR));

        public dateChooser(String tittel) {
            Calendar nå = Calendar.getInstance();
            JCBdStart = new JComboBox(getNumberArray(1, DayMonthRelation[nå.get(Calendar.MONTH)]));
            JCBmStart = new JComboBox(getNumberArray(1, 12));
            JCByStart = new JComboBox(getNumberArray(2014, 2064));
            //Lytter til måneds boxen, forandrer dags boksen til riktig antall dager/måned 
            JCBmStart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCBdStart.removeAllItems();
                    int days = DayMonthRelation[JCBmStart.getSelectedIndex()];
                    for(int i=0;i<days;i++){
                        JCBdStart.addItem(i+1+"");
                    }
                    revalidate();
                }
            });
            //Lytter til års boxen, forandrer dags boksen til riktig antall dager/måned
            JCByStart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DayMonthRelation = getDaysForEachMonth(getNumberFromJCB(JCByStart));
                    JCBdStart.removeAllItems();
                    int days = DayMonthRelation[JCBmStart.getSelectedIndex()];
                    for(int i=0;i<days;i++){
                        JCBdStart.addItem(i+1+"");
                    }
                    revalidate();
                }
            });
            JCBdEnd = new JComboBox(getNumberArray(1, DayMonthRelation[nå.get(Calendar.MONTH)]));
            JCBmEnd = new JComboBox(getNumberArray(1, 12));
            JCByEnd = new JComboBox(getNumberArray(2014, 2064));
            //Lytter til måneds boxen, forandrer dags boksen til riktig antall dager/måned
            JCBmEnd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCBdEnd.removeAllItems();
                    int days = DayMonthRelation[JCBmEnd.getSelectedIndex()];
                    for(int i=0;i<days;i++){
                        JCBdEnd.addItem(i+1+"");
                    }
                    revalidate();
                }
            });
            //Lytter til års boxen, forandrer dags boksen til riktig antall dager/måned
            JCByEnd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DayMonthRelation = getDaysForEachMonth(getNumberFromJCB(JCByEnd));
                    JCBdEnd.removeAllItems();
                    int days = DayMonthRelation[JCBmEnd.getSelectedIndex()];
                    for(int i=0;i<days;i++){
                        JCBdEnd.addItem(i+1+"");
                    }
                    revalidate();
                }
            });
            enabled(false);
            enabledBox = new JCheckBox("", false);
            //Har muligheten til å 'disable' hele elementet
            enabledBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   enabled(enabledBox.isSelected());
                }
            });
            
            add(new JLabel(tittel));

            add(enabledBox);
            
            add(JCBdStart);
            add(JCBmStart);
            add(JCByStart);

            add(new JLabel(" - "));

            add(JCBdEnd);
            add(JCBmEnd);
            add(JCByEnd);
        }
        /**
         * Brukes av checkboxen som avgjør om elementet er brukbart eller ikke
         * @param bool setter elementet på eller av. True: på, false: av
         */
        private void enabled(boolean bool){
            JCBdStart.setEnabled(bool);
            JCBmStart.setEnabled(bool);
            JCByStart.setEnabled(bool);
            JCBdEnd.setEnabled(bool);
            JCBmEnd.setEnabled(bool);
            JCByEnd.setEnabled(bool);
            enabled = bool;
        }
        /**
         * Privat funksjon. Brukes for å hente ut tall ifra combo boxene innad
         * @param box
         * @return 
         */
        private int getNumberFromJCB(JComboBox box) {
            return Integer.parseInt(box.getSelectedItem().toString());
        }
        /**
         * Få tilbake et dato dato spenn i formatet dd-MM-yyyy.
         * @return [0] inneholder første angitt dato, [1] inneholder andre angitt dato
         */
        String[] getArray() {
            if(!enabled){return null;}
            String startDate = getNumberFromJCB(JCBdStart) + "-" + getNumberFromJCB(JCBmStart) + "-" + getNumberFromJCB(JCByStart);
            String endDate = getNumberFromJCB(JCBdEnd) + "-" + getNumberFromJCB(JCBmEnd) + "-" + getNumberFromJCB(JCByEnd);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String[] output = new String[2];
            try {
                sdf.setLenient(false);
                output[0] = sdf.parse(startDate).toString();
                output[1] = sdf.parse(endDate).toString();
            } catch (ParseException pe) {
                return null;
            }
            return output;
        }
        /**
         * Henter datoen ifra første dato element
         * @return dd-MM-yyyy
         */
        String getStartDate() {
            return getNumberFromJCB(JCBdStart) + "-" + getNumberFromJCB(JCBmStart) + "-" + getNumberFromJCB(JCByStart);
        }
        /**
         * Henter datoen ifra siste dato element
         * @return dd-MM-yyyy
         */
        String getEndDate() {
            return getNumberFromJCB(JCBdEnd) + "-" + getNumberFromJCB(JCBmEnd) + "-" + getNumberFromJCB(JCByEnd);
        }
        /**
         * Privat funksjon brukt innad. Brukes for å lage arrays for å vise i JCombobox
         * @param start angi start tall
         * @param end angi slutt tall
         * @return array med tallene ifra start til slutt
         */
        private String[] getNumberArray(int start, int end) {
            String[] output = new String[end - start + 1];
            for(int i=0;i<output.length;i++){
                output[i] = start + "";
                start++;
            }
            return output;
        }
        /**
         * Privat funksjon brukt innad. Gir tilbake dager i månedene
         * @param year For å kunne finne ut om det er skuddår
         * @return array med alle dagene i månedene kronologisk
         */
        private int[] getDaysForEachMonth(int year) {
            int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            if (isLeapYear(year)) {
                days[1] = 29;
            }
            return days;
        }
        /**
         * Privat funksjon brukt innad. Finner ut om det er skuddår
         * @param year
         * @return true: skuddår, false = ikke skuddår
         */
        private boolean isLeapYear(int year) {
            if (year % 4 != 0) {
                return false;
            }
            if (year % 100 != 0) {
                return true;
            }
            if (year % 400 == 0) {
                return true;
            }
            return false;
        }//end of isLeapYear          
    }//end of class dateChooser
    //GUI komponent: som viser 2 inputs med en ' - ' imellom dem
    /**
     * GUI element som viser 2 tall inputs med angitt lengde. Har get metoder.
     */
    class userInputRange extends JPanel {
        private JLabel title;
        private JTextField inputLow, inputHigh;
        private JLabel seperator;

        public userInputRange(int width, String Title){
            title = new JLabel(Title);
            inputLow = new JTextField(width);
            inputHigh = new JTextField(width);
            seperator = new JLabel(" - ");
            
            JPanel ranger = new JPanel();
            ranger.add(inputLow);
            ranger.add(seperator);
            ranger.add(inputHigh);
            
            add(title);
            add(ranger);
        }
        /**
         * Gir tilbake angitt tall i venstre input
         * @return 
         */
        int getRangeLow() {
            return parseInput(inputLow);
        }
        /**
         * Gir tilbake angitt tall i høyre input
         * @return 
         */
        int getRangeHigh() {
            return parseInput(inputHigh);
        }
        /**
         * Gir tilbake et array med angitte tall i begge feltene
         * @return [0] er høyre input, [1] er venstre input
         */
        int[] getArray() {
            if (checkField(inputLow) && checkField(inputHigh)) {
                return new int[]{getRangeLow(), getRangeHigh()};
            } else {
                return null;
            }
        }
        /**
         * Gir tilbake et array med angitte tall i begge feltene
         * @return [0] er høyre input, [1] er venstre input
         */
        long[] getLongArray(){
            if (checkField(inputLow) && checkField(inputHigh)) {
                long[] output = new long[2];
                output[0] = Long.parseLong(inputLow.getText());
                output[1] = Long.parseLong(inputHigh.getText());
                return output;
            } else {
                return null;
            }
        }
        /**
         * Privat funksjon brukt innad for å sjekke et felt om det er tomt
         * @param field Feltet som skal testes
         * @return true of feltet ikke er tom, false hvis det er tomt
         */
        private boolean checkField(JTextField field) {
            if (field.getText().trim().length() < 1) {
                return false;
            } else {
                return true;
            }
        }
        /**
         * Privat funksjon brukt innad, henter tallet ut av feltet
         * @param field feltet som skal bli hentet ut ifra
         * @return returnerer tallet i feltet, ellers null hvis ikke er et tall
         */
        private int parseInput(JTextField field) {
            if (field.getText().trim().length() < 1) {
                return 0;
            }
            try {
                return Integer.parseInt(field.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Vennligst se over at alle tall er gydlige tall ved kun bruk av 0-9 tegn");
                return 0;
            }
        }
    }
    /**
     * GUI komponent som gir brukeren i praksis tre valg mulige valg med checkbokser.
     * 1. ignorer, 2. false, 3. true. Nyttig for bruk i søke muligheter for brukereren
     */
    class userInputAlternativeCheckbox extends JPanel{
        JCheckBox enabled, value;
        String title;
        public userInputAlternativeCheckbox(String Title) {
            title = Title;
            enabled = new JCheckBox("", false);
            value = new JCheckBox(Title, false);

            value.setEnabled(false);

            enabled.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    value.setEnabled(enabled.isSelected());
                    updateTitle();
                }
            });
            value.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateTitle();
                }
            });
            add(enabled);
            add(value);
        }
        private void updateTitle(){
            if(!enabled.isSelected()){
                value.setText(title);
            }else if(value.isSelected()){
                value.setText(title);
            }else{
                value.setText("Ikke " + title);
            }
        }
        /**
         * Sender tilbake et tall mellom 0 og 2.
         * @return 0 for ignorer, 1 for false, 2 for true
         */
        int getIntBoolean() {
            if (enabled.isSelected()) {
                if (value.isSelected()) {
                    return 2;
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        }
        /**
         * Setter verdien komponenten skal ha
         * @param state 0 til 2, 0 ignorer, 1 false, 2 true
         */
        void setState(int state){
            if(state == 0){
                enabled.setSelected(false);
                value.setSelected(false);
            }else if(state == 1){
                enabled.setSelected(true);
                value.setSelected(false);
            }else if(state == 2){
                enabled.setSelected(true);
                enabled.setSelected(true);
            }
        }
    }
    /**
     * Sjekker om et felt er tomt eller ikke
     * @param fields Et hvilket som helst JTextComponent i array
     * @return false hvis noen var tomme, true hvis ikke tom
     */
    boolean userInputCheckFieldsNotEmpty(JTextComponent[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().trim().length() < 1) {
                return false;
            }
        }
        return true;
    }
    /**
     * Sjekker om et felt har et gyldig tall
     * @param fields Hvilket som helst JTextComponent i array
     * @return true hvis alle hadde gyldige tall, false ellers
     */
    boolean userInputCheckFieldNumber(JTextComponent[] fields) {
        for (int i = 0; i < fields.length; i++) {
            try {
                Integer.parseInt(fields[i].getText());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
    /**
     * GUI komponent for å vise Bolig objekter i en JTable. 
     */
    class BoligTabellModel extends AbstractTableModel{
        private String[] kolloner;
        private Bolig[] boliger;

        public BoligTabellModel(Bolig[] Boliger, String[] Kolloner) {
            kolloner = Kolloner;
            boliger = Boliger;
        }
        public Bolig getBolig(int row) {
            return boliger[row];
        }
        @Override
        public int getColumnCount(){
            return kolloner.length;
        }
        @Override
        public int getRowCount() {
            return boliger.length;
        }
        @Override
        public String getColumnName(int col) {
            return kolloner[col];
        }
        @Override
        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return boliger[row].getAdresse();
                case 1:
                    return boliger[row].getPris();
                case 2:
                    return boliger[row].getAreal();
                case 3:
                    return boliger[row].getPostadresse();
                case 4:
                    return boliger[row].getBesk();
                case 5:
                    return boliger[row].getByggår();
                default:
                    return null;
            }
        }
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }
    /**
     * Metode som fjerner text ifra JTextComponents arrayet
     * @param list fjerner teksts ifra disse
     */
    void resetInputFields(JTextComponent[] list){
        for(int i=0;i<list.length;i++){
            list[i].setText("");
        }
    }
    /**
     * Metode som får et tall utifra et input felt
     * @param field JTextField
     * @return et tall eller 0 hvis det ikke var et tall
     */
    int getNum(JTextField field) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
    /**
     * Metode som gir tilbake den nåværende datoen i yyyy-MM-dd
     * @return yyyy-MM-dd formatert dato
     */
    String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(cal.getTime());
    }
    protected void displayMessage(String message){
    	JOptionPane.showMessageDialog(null, message);
    	
    }//end of displayMessage
    
    protected boolean isNum(String s){
        try{
            Long.parseLong(s);
            return true;
        }catch(NumberFormatException nfe){
            System.out.println("Feil i isNum: "+ nfe);
            return false;
        }
			
    }//end of isNUm
    /**
     * Klassen fungerer som et bilde editerings panel. 
     * Kan vise bilder som er valgt, man kan fjerne de og få tilbake den endelige listen med bilder
     */
    class EditImages extends JPanel{
        private Bolig bolig;
        
        private ArrayList<Image> bildeListe;
        private JPanel imagePanel;
        private JButton addImageButton;
        
        public EditImages(Bolig Bolig){
            bolig = Bolig;
            bildeListe = new ArrayList();
            
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            
            imagePanel = new JPanel();
            imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
            if(bolig != null){
                for(int i=0;i<bolig.getAntallBilder();i++){
                    addImage(bolig.getBilde(i));
                }
            }
            addImageButton = new JButton("Legg til bilde (jpg)");
            addImageButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setCurrentDirectory(new File("."));
                    int resultat = jfc.showOpenDialog(null);
                    if (resultat == JFileChooser.APPROVE_OPTION) {
                        File valgtFil = jfc.getSelectedFile();
                        if(checkFilJpg(valgtFil)){
                            try{
                                Image img = ImageIO.read(valgtFil);
                                img = img.getScaledInstance(BOLIG_IMAGE_WIDTH, BOLIG_IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                                BufferedImage buffImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                                Graphics2D buffImageGraphics = buffImage.createGraphics();
                                buffImageGraphics.drawImage(img, null, null);
                                RenderedImage rImage = (RenderedImage) buffImage;
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(rImage, "jpeg", baos);
                                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                                addImage(ImageIO.read(is));
                                baos.close();
                                is.close();
                            }catch(IOException ioe){
                                System.out.println(ioe);
                            }
                        }
                    }
                }
            });
            add(addImageButton);
            add(imagePanel);
        }
        /**
         * Legger angitt bilde til for visning i GUI og i array listen. 
         * Bilder som blir dobbelt klikket kan bli fjernet. 
         * @param Bilde Bilde som skal bli lagt til i GUI og liste
         */
        private void addImage(Image Bilde){
            final Image bilde = Bilde;
            if(bilde != null){
                final JLabel imageIcon = new JLabel(new ImageIcon(bilde.getScaledInstance(BOLIG_THUMBNAIL_WIDTH, BOLIG_THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH)));
                imageIcon.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if (e.getClickCount() == 2) {
                            if(JOptionPane.showConfirmDialog(null, "Er du sikkert på at du vil slette bilde?", "Slett Bilde", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                if(bildeListe.size() == MINIMUM_IMAGES){
                                    JOptionPane.showMessageDialog(null, "Du må ha minst 1 bilde");
                                }else{
                                    bildeListe.remove(bilde);
                                    imagePanel.remove(imageIcon);
                                    imagePanel.revalidate();
                                }
                            }
                        }
                    }
                });
                if(bildeListe.size() == MAXIMUM_IMAGES){
                    addImageButton.setEnabled(false);
                }
                bildeListe.add(bilde);
                imagePanel.add(imageIcon);
                imagePanel.revalidate();
            }
        }
        private boolean checkFilJpg(File valgtFil) {
            if (valgtFil == null) {
                return false;
            }
            String name = valgtFil.getName();
            if (name.endsWith("jpg") || name.endsWith("jpeg")) {
                return true;
            } else {
                return false;
            }
        }
        Image[] getImages(){
            return bildeListe.toArray(new Image[bildeListe.size()]);
        }
    }//End class EditImages
}//End class SubPanel