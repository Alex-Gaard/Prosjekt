package boligformidling;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
/**
 * Har ansvar for å kunne registrere boliger. Vises kun til registrerte utleiere
  * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public class SubPanel_RegistrerBoliger extends SubPanel {

    private JTextField adresse, postadresse, areal, rom, byggår, etasje, etasjer, total_areal, pris;
    private JTextArea beskrivelse;
    private Checkbox kjeller, heis, balkong, garasje, fellesvask;

    private JLabel JLadresse, JLpostadresse, JLareal, JLrom, JLbyggår, JLpris, JLetasje, JLetasjer, JLbeskrivelse, JLtotal_areal;

    private JLabel enebolig, leilighet;
    private JButton eneboligKnapp, leilighetKnapp, selectBilde;

    private EditImages editImagesPanel;
    
    public SubPanel_RegistrerBoliger(MainPanel parent){
        super(parent);
        
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        adresse = new JTextField(20);
        postadresse = new JTextField(4);
        areal = new JTextField(3);
        rom = new JTextField(2);
        byggår = new JTextField(4);
        etasje = new JTextField(3);
        etasjer = new JTextField(3);
        total_areal = new JTextField(4);
        pris = new JTextField(12);

        beskrivelse = new JTextArea(10, 10);

        kjeller = new Checkbox("Kjeller");
        heis = new Checkbox("Heis");
        balkong = new Checkbox("Balkong");
        garasje = new Checkbox("Balkong");
        fellesvask = new Checkbox("Fellesvask");

        JLadresse = new JLabel("Adresse");
        JLpostadresse = new JLabel("Postadresse");
        JLareal = new JLabel("Areal i meter");
        JLrom = new JLabel("Antall rom");
        JLbyggår = new JLabel("Byggår");
        JLpris = new JLabel("Pris");
        JLetasje = new JLabel("Hvilken etasje");
        JLetasjer = new JLabel("Hvor mange etasjer");
        JLbeskrivelse = new JLabel("Beskrivelse");
        JLtotal_areal = new JLabel("Total areal");
        
        enebolig = new JLabel("--- Enebolig ---");
        leilighet = new JLabel("--- Leilighet");
        leilighetKnapp = new JButton("Sett Inn en Leilighet");
        eneboligKnapp = new JButton("Sett Inn en Enebolig");
        leilighetKnapp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(editImagesPanel.getImages().length < 1){
                    JOptionPane.showMessageDialog(null, "Man må ha minst 1 bilde");
                }
                if (!userInputCheckFieldsNotEmpty(new JTextComponent[]{adresse, postadresse, areal, rom, byggår, beskrivelse, etasje, pris}) || !userInputCheckFieldNumber(new JTextComponent[]{postadresse, areal, rom, byggår, etasje, pris})) {
                    JOptionPane.showMessageDialog(null, "Vennligst se til at alle felter er gyldige");
                } else {
                    Leilighet bolig = new Leilighet(0, null, adresse.getText(), getNum(postadresse), getNum(areal), getNum(rom), getNum(byggår), beskrivelse.getText(), getDate(), getNum(pris), getNum(etasje), heis.getState(), balkong.getState(), garasje.getState(), fellesvask.getState());
                    if (Data_Boliger.settinnBolig(getBruker(), bolig, editImagesPanel.getImages())) {
                        JOptionPane.showMessageDialog(null, "Bolig satt inn");
                        reset();
                    } else {
                        JOptionPane.showMessageDialog(null, "En feil oppsto");
                    }
                }
            }
        });
        eneboligKnapp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(editImagesPanel.getImages().length < 1){
                    JOptionPane.showMessageDialog(null, "Man må ha minst 1 bilde");
                }
                if (!userInputCheckFieldsNotEmpty(new JTextComponent[]{adresse, postadresse, areal, rom, byggår, beskrivelse, etasjer, total_areal, pris}) || !userInputCheckFieldNumber(new JTextComponent[]{postadresse, areal, rom, byggår, etasjer, total_areal, pris})) {
                    JOptionPane.showMessageDialog(null, "Vennligst se til at alle felter er gyldige");
                } else {
                    Enebolig bolig = new Enebolig(0, null, adresse.getText(), getNum(postadresse), getNum(areal), getNum(rom), getNum(byggår), beskrivelse.getText(), getDate(), getNum(pris), getNum(etasjer), kjeller.getState(), getNum(total_areal));
                    if (Data_Boliger.settinnBolig(getBruker(), bolig, editImagesPanel.getImages())) {
                        JOptionPane.showMessageDialog(null, "Bolig satt inn");
                        reset();
                    } else {
                        JOptionPane.showMessageDialog(null, "En feil oppsto");
                    }
                }
            }
        });
        JPanel boligPanel = new JPanel();
        boligPanel.setLayout(new BoxLayout(boligPanel, BoxLayout.PAGE_AXIS));
        boligPanel.setBorder(
            BorderFactory.createTitledBorder("Registrer Bolig"));        
        boligPanel.add(JLadresse);
        boligPanel.add(adresse);

        boligPanel.add(JLpostadresse);
        boligPanel.add(postadresse);

        boligPanel.add(JLareal);
        boligPanel.add(areal);

        boligPanel.add(JLrom);
        boligPanel.add(rom);

        boligPanel.add(JLbyggår);
        boligPanel.add(byggår);

        boligPanel.add(JLbeskrivelse);
        boligPanel.add(new JScrollPane(beskrivelse));

        boligPanel.add(JLpris);
        boligPanel.add(pris);
        
        //Enebolig omerade
        JPanel eneboligPanel = new JPanel();
        eneboligPanel.setLayout(new BoxLayout(eneboligPanel, BoxLayout.PAGE_AXIS));
        eneboligPanel.setBorder(
            BorderFactory.createTitledBorder("Registrer Bolig"));        
        eneboligPanel.add(enebolig);
        eneboligPanel.add(JLetasjer);
        eneboligPanel.add(etasjer);
        eneboligPanel.add(kjeller);
        eneboligPanel.add(JLtotal_areal);
        eneboligPanel.add(total_areal);
         eneboligPanel.add(eneboligKnapp);

        //Leilighet omerade
        JPanel leilighetPanel = new JPanel();
        leilighetPanel.setLayout(new BoxLayout(leilighetPanel, BoxLayout.PAGE_AXIS));
        leilighetPanel.setBorder(
            BorderFactory.createTitledBorder("Registrer Bolig"));
        leilighetPanel.add(leilighet);
        leilighetPanel.add(JLetasje);
        leilighetPanel.add(etasje);
        leilighetPanel.add(heis);
        leilighetPanel.add(balkong);
        leilighetPanel.add(garasje);
        leilighetPanel.add(fellesvask);
        leilighetPanel.add(leilighetKnapp);
        
        editImagesPanel = new EditImages(null);
        
        JPanel invisPanel1 = new JPanel();
        invisPanel1.add(editImagesPanel);
        invisPanel1.add(Box.createVerticalGlue());
        add(invisPanel1);
        
        JPanel invisPanel2 = new JPanel();
        invisPanel2.add(boligPanel);
        invisPanel2.add(Box.createVerticalGlue());
        add(invisPanel2);
        
        JPanel invisPanel3 = new JPanel();
        invisPanel3.add(eneboligPanel);
        invisPanel3.add(Box.createVerticalGlue());
        add(invisPanel3);
        
        JPanel invisPanel4 = new JPanel();
        invisPanel4.add(leilighetPanel);
        invisPanel4.add(Box.createVerticalGlue());
        add(invisPanel4);
    }//End constructor
    /**
     * Private metode brukt innad for å resette alle felt
     */
    private void reset(){
        resetInputFields(new JTextComponent[]{adresse, postadresse, areal, rom, byggår, beskrivelse, etasje, etasjer, total_areal, pris});
        Checkbox[] checkBoxList = new Checkbox[]{heis, balkong, garasje, fellesvask, kjeller};
        for(int i=0;i<checkBoxList.length;i++){
            checkBoxList[i].setState(false);
        }
        editImagesPanel = new EditImages(null);
        revalidate();
    }//End reset
}//End class SubPanel_RegistrerBoliger
