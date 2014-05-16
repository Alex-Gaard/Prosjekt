package boligformidling;

//Petter S.W Gjerstad
//SubPanel_KundeProfil
//Siste versjon kl 11:37 16.05.2014

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Klassen har til hensikt å representere en brukers profil.
 * <p>
 * Brukeren vil måtte ha evnen til å modifisere sine egne data, kanskje til og
 * med si opp sin konto. Til dette har vi SubPanel_KundeProfil.
 * <p>
 * Brukeren har evnen til å endre på relevante data, samt utvide sin konto til
 * utleier/boligsøker om så ønskes.
 * <p>
 * Brukeren for og forslag til boliger lagt ut for leie i et forenklet
 * grensesnitt, som hvis man trykker sender deg til SubPanel_Boliger.
 * <p>
 * Klassen har følgende metoder:
 * erboligsøker,slettblibliboligsøkerknappogbliboligsøker,populatesøkerinfo,
 * populatesøkerkravinfo,erutleier,slettbliutleierknappoglastutleier,
 * VisBoligVarsler, updatevisteboliger, switchtab, lagre.
 * <p>
 * Nærmere beskrivelse for hver metode finnes som kommentar over hver metode.
 * Bemerk at kommentarene er skrevet med javadoc syntax.
 */
public class SubPanel_KundeProfil extends SubPanel
{
    
    //diverse paneler
    private final JPanel brukerpanel;
    private final JPanel søkerinfopanel;
    private final JPanel søkerkravpanel;
    private final JPanel utleierpanel;
    private final JPanel knapperpanel;
    private final JPanel tablepanel;
    private JPanel samling;
    
    private final JPanel upperpanel;
    private final JPanel centerpanel;
    private final JPanel lowerpanel;
    private final JPanel upperpanelsub;
    private final JPanel centerpanelsub;
    
    //JLabels vil ha navn som ender på L, dette er å skille på variabelnavn tilhørende labels og textfields som logisk hører sammen.
    //Informasjon om brukerkonto
    private JLabel BrukerinfoL;
    private JLabel PersonnummerL;
    private JLabel NavnL;
    private JLabel AdresseL;
    private JLabel EmailL;
    private JLabel TelefonL;
    
    private JTextField Personnummer;
    private JTextField Navn;
    private JTextField Adresse;
    private JTextField Email;
    private JTextField Telefon;
    
    //Boligsøker felter (hvis tilgjengelig)
    private JLabel søkerinfoL;
    private JLabel Antall_personerL;
    private JLabel SivilstatusL;
    private JLabel YrkeL;
    private JLabel RøykerL;
    private JLabel HusdyrL;
    
    private JTextField antall_personer;
    private JTextField sivilstatus;
    private JTextField yrke;
    private JCheckBox røyker;
    private JTextField husdyr;
    
    //boligkrav felter(hvis tilgjengelig)
    private JLabel kravinfoL;
    private JLabel minBoarealL;
    private JLabel maxBoarealL;
    private JLabel Antall_soveromL;
    private JLabel ByggårL;
    private JLabel minPrisL;
    private JLabel maxPrisL;
    private JLabel PeisL;
    private JLabel ParkeringL;
    
    private JTextField minBoareal;
    private JTextField maxBoareal;
    private JTextField Antall_soverom;
    private JTextField Byggår;
    private JTextField minPris;
    private JTextField maxPris;
    private JComboBox Peis;
    private JComboBox Parkering;
    
    //Utleier felter(hvis tilgjengelig)
    private JLabel firmaL;
    private JTextField firma;
    
    //diverse andre labels.
    private final JLabel brukerdataL;
    private JLabel utleierinfoL;
    private final JLabel forslagsidensistL;
    private JLabel bliutleierL;
    
    //Knapper, alle lowerpanel
    private final JButton lagreknapp;
    private JButton bliutleierknapp;
    private final JButton resettførknapp;
    private final JButton slettprofilknapp;
    private JButton bliboligsøkerknapp;
    
    //tilbakemeldingsvindu, del av lowerpanl
    private final JLabel tilbakemeldingerL;
    private final JTextArea tilbakemeldinger;
    private int linjeteller;

    //tabell relaterte variabler.
    private JTable vistreff;
    private ResultSet søkerinfors;
    private ResultSet boligvarslerrs;
    private ResultSet søkerkravrs;
    private DefaultTableModel model;
    
    //diverse hjelpevariabler.
    private Component[] comp;
    private String personnummer;
    private final Data_Boliger database;
    private boolean boligsøkertest;
    private boolean utleiertest;
    private Data_Bruker brukerdb;
    
    /**
     * 
     * @param parent
     * @param id
     * Konstruktør. Setter opp mesteparten av GUIet.
     * <P>Grensesnittet består av en rekke JComponents pakket inni hverandre for å tegne grensesnittet.
     * Grovt forklart, har klassen tre "løytnant" paneler kjent som upper,center og lowerpanel, arrangert i en borderlayout(nord,senter og sør). Disse igjen tjener som kontainere
     * for andre paneler igjen(gjerne kjent som subs, wrappers) så så til slutt holder mer elementære JComponents, som f.eks JButtons og JTextField.
     * <p>
     * Siden brukeren skal kunne utvide sin konto om nødvendig, må man ha et system som sjekker under oppstart hvilke subtyper den innloggede brukeren er(utleier/boligsøker).
     * og tegne grensesnittet deretter.
     */
    public SubPanel_KundeProfil(MainPanel parent, String id)
    {
        super(parent);
        personnummer = id;
        
        database = new Data_Boliger();
        
        setLayout(new BorderLayout());
        
        upperpanel = new JPanel();
        upperpanel.setLayout(new BorderLayout());
        centerpanel = new JPanel();
        centerpanel.setLayout(new BorderLayout());
        lowerpanel = new JPanel();
        lowerpanel.setLayout(new BorderLayout());
        
        add(upperpanel,BorderLayout.NORTH);
        add(centerpanel,BorderLayout.CENTER);
        add(lowerpanel,BorderLayout.SOUTH);
        
        brukerdataL = new JLabel("Dine brukerdata:");
        upperpanel.add(brukerdataL,BorderLayout.CENTER);
        upperpanelsub = new JPanel();
        upperpanelsub.setLayout(new GridLayout());
        upperpanelsub.setBorder(BorderFactory.createBevelBorder(1));
        upperpanel.add(upperpanelsub,BorderLayout.SOUTH);
        
        brukerpanel = new JPanel();
        brukerpanel.setLayout(new BoxLayout(brukerpanel,BoxLayout.Y_AXIS));
        søkerinfopanel = new JPanel();
        søkerinfopanel.setLayout(new BoxLayout(søkerinfopanel,BoxLayout.Y_AXIS));
        søkerkravpanel = new JPanel();
        søkerkravpanel.setLayout(new BoxLayout(søkerkravpanel,BoxLayout.Y_AXIS));
        utleierpanel = new JPanel();
        utleierpanel.setLayout(new BoxLayout(utleierpanel,BoxLayout.Y_AXIS));
        knapperpanel = new JPanel();
        tablepanel = new JPanel();
        
        JPanel brukerpanelwrapper = new JPanel();
        brukerpanelwrapper.setBorder(BorderFactory.createEtchedBorder());
        brukerpanelwrapper.add(brukerpanel);
        
        JPanel søkerinfopanelwrapper = new JPanel();
        søkerinfopanelwrapper.setBorder(BorderFactory.createEtchedBorder());
        søkerinfopanelwrapper.add(søkerinfopanel);
        
        JPanel søkerkravpanelwrapper = new JPanel();
        søkerkravpanelwrapper.setBorder(BorderFactory.createEtchedBorder());
        søkerkravpanelwrapper.add(søkerkravpanel);
        
        JPanel utleierpanelwrapper = new JPanel();
        utleierpanelwrapper.setBorder(BorderFactory.createEtchedBorder());
        utleierpanelwrapper.add(utleierpanel);

        //dummies
        
        //legger informasjons panelene (i wrappers) til suben.
        upperpanelsub.add(brukerpanelwrapper);
        upperpanelsub.add(søkerinfopanelwrapper);
        upperpanelsub.add(søkerkravpanelwrapper);
        upperpanelsub.add(utleierpanelwrapper);
        
        centerpanelsub = new JPanel(new BorderLayout());
        forslagsidensistL=new JLabel("Boligforslag siden siste innlogging:");
        centerpanel.add(forslagsidensistL,BorderLayout.NORTH);
        centerpanelsub.setBorder(BorderFactory.createBevelBorder(1));
        centerpanelsub.add(tablepanel,BorderLayout.CENTER);
        centerpanel.add(centerpanelsub);
        
        //legger et tilbakemeldingsvindu til lowerpane
        linjeteller = 0;
        tilbakemeldinger = new JTextArea(4,1);
        tilbakemeldinger.setEditable(false);
        tilbakemeldinger.setBorder(BorderFactory.createEtchedBorder());
        tilbakemeldingerL = new JLabel("Tilbakemeldinger:");
        lowerpanel.add(tilbakemeldingerL,BorderLayout.NORTH);
        lowerpanel.add(new JScrollPane(tilbakemeldinger),BorderLayout.CENTER);
        lowerpanel.add(knapperpanel,BorderLayout.SOUTH);

        //Last brukerdata, blir gjort uavhengig av eventuelle brukersubtyper.
        String sql = "SELECT * from Bruker where Personnummer=" + personnummer + ";";
        try
        {
            ResultSet rs = Data_Bruker.execQuery(sql);
            rs.next();

            BrukerinfoL = new JLabel("Informasjon om din brukerkonto: ");
            PersonnummerL = new JLabel("Personnummer:");
            NavnL = new JLabel("Navn:");
            AdresseL = new JLabel("Adresse:");
            EmailL = new JLabel("Email:");
            TelefonL = new JLabel("Telefon:");

            Personnummer = new JTextField(10);
            Navn = new JTextField(10);
            Adresse = new JTextField(10);
            Email = new JTextField(10);
            Telefon = new JTextField(10);

            Personnummer.setText(rs.getString("Personnummer"));
            Personnummer.setEditable(false);
            Navn.setText(rs.getString("Navn"));
            Adresse.setText(rs.getString("Adresse"));
            Email.setText(rs.getString("Email"));
            Telefon.setText(rs.getString("Telefon"));

            brukerpanel.add(Box.createVerticalStrut(5));
            brukerpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            brukerpanel.add(Box.createVerticalStrut(5) );
            brukerpanel.add(BrukerinfoL);
            brukerpanel.add(Box.createVerticalStrut(5));
            brukerpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            brukerpanel.add(Box.createVerticalStrut(5) );
            brukerpanel.add(PersonnummerL);
            brukerpanel.add(Personnummer);
            brukerpanel.add(NavnL);
            brukerpanel.add(Navn);
            brukerpanel.add(AdresseL);
            brukerpanel.add(Adresse);
            brukerpanel.add(EmailL);
            brukerpanel.add(Email);
            brukerpanel.add(TelefonL);
            brukerpanel.add(Telefon);
            //ferdig med bruker
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
        lagreknapp = new JButton("Lagre endringer");
        lagreknapp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                lagre();
            }
        });        
        knapperpanel.add(lagreknapp);

        slettprofilknapp = new JButton("Slett Profil");
        slettprofilknapp.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String advarsel = "Advarsel! Svarer du ja vil din profil bli fullstendig slettet fra vårt system.";
                int svar = JOptionPane.showConfirmDialog(null, advarsel);
                tilbakemeldinger.append("["+linjeteller++ + "] "+advarsel+"(svar = "+svar+")\n");

                if (svar == 0)
                {
                    if (utleiertest)
                    {
                        try
                        {
                            Data_utleier.delete(personnummer);
                        } 
                        catch (SQLException ex)
                        {
                            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (boligsøkertest)
                    {
                        try {
                            Data_boligsøker.deletespecfromBoligsøker(personnummer);
                            Data_boligsøker.deletespecfromSøkerKrav(personnummer);
                            Data_boligsøker.deletespecfromdSøkerInfo(personnummer);
                            Data_Bruker.deleteBruker(personnummer);
                        } 
                        catch (SQLException ex) 
                        {
                            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
            }
        });
        knapperpanel.add(slettprofilknapp);
        
        resettførknapp = new JButton("nullstill boligforslagsdata");
        resettførknapp.addActionListener(new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String reset = "Boligforslagsdata nullstilt.";
                tilbakemeldinger.append("["+linjeteller++ + "] "+reset+"\n");
                try
                {
                    Data_Viste_Boliger.deletespecfromViste_Boliger(personnummer);
                    visBoligVarsler();
                } 
                catch (SQLException ex)
                {
                    Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        knapperpanel.add(resettførknapp);
        
        //Sjekker så om brukeren også er utleiere og/eller boligsøkere
        erboligsøker();
        erutleier();
    }
    
    /**
     * Hjelpemetode, sjekker om brukeren er en boligsøker, og handler deretter.
     * 
     * <p>
     * Metoden gjør først en skjapp sjekk mot databasen for å finne ut om brukeren er en boligsøker.
     * Hvis ja, kalles populatesøkerinfo, som har til hensikt med å oppdatere grensesnittet med søkerinfo og søkerkrav.
     * <p>Hvis ikke, må brukeren ha evnen til å bli en boligsøker, altså utvide sin konto.
     * Da legges likesågjerne til en ny knapp til lowerpanel som gjør brukeren istand til å utvide om ønskelig.
     * 
     */
    private void erboligsøker()
    {
        try
        {
            ResultSet rs = Data_boligsøker.selectspecRSfromboligsøker(personnummer);
            rs.first();
            boligsøkertest = rs.first(); // if true: brukers personnummer er representert i boligs�ker
            if (boligsøkertest) //populerer, dvs oppretter og adder jcomponenter av ulike slag hvis boligs�ker.
                //men hvis boligsøker først nylig er opprettet, må dette fanges opp ipopulateboligsøkerinfo og da sette feltene til "Ikke registrert".
                populatesøkerinfo();
            else  //ikke boligsøker, må da kunne bli boligsøker
            {
                bliboligsøkerknapp =  new JButton("Bli boligsøker");
                knapperpanel.add(bliboligsøkerknapp);
                bliboligsøkerknapp.addActionListener(new ActionListener() 
                {
                    
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        //Execute when button is pressed
                        samling = new JPanel();
                        BoxLayout box=new BoxLayout(samling,BoxLayout.Y_AXIS);
                        String advarsel = "Aksepterer du vil du bli underlagt norsk lov om boligsøker virksomhet.";
                        samling.add(new JLabel(advarsel) );
                        int bekreft = JOptionPane.showConfirmDialog(null,samling,"er du sikker på at du vil bli utleier?",2);
                        tilbakemeldinger.append("["+linjeteller++ + "] "+advarsel+"(svar = "+bekreft+")\n");
                        if (bekreft == 0)
                        {
                            slettblibliboligsøkerknappogbliboligsøker();
                        }
                    }
                });
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Hjelpemetode, brukes når utleier utvider sin konto.
     * 
     * <p> Metoden har kun til funksjon å oppdatere databasen.
     * Når det er gjort, kalles populatesøkerinfo.
     */
    private void slettblibliboligsøkerknappogbliboligsøker()
    {
        knapperpanel.remove(bliboligsøkerknapp);
        try
        {
            Data_boligsøker.insertspecintoboligsøker(personnummer);
            populatesøkerinfo();
            getRootPane().revalidate();
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Populerer grensesnittet med felter relatert til søkerinfo.
     * 
     * <p>Metoden kalles enten som del av oppstartsprosedyren når klassen finner ut at brukeren er en boligsøker, eller senere når en utleier velger å utvide sin konto.
     * <p>Metoden sjekker også om subentiteten til boligsøker(kjent som søkerinfo) tabellen faktisk har data for gjeldende bruker, og populerer feltene deretter.
     * <p>Metoden har også ansvar for å gjøre kall på populatesøkerkrav og VisBoligVarsler.
     */
    private void populatesøkerinfo()
    {
        try
        {
            søkerinfors = Data_boligsøker.selectspecRSfromsøkerinfo(personnummer);
            
            String noReg= "Ingen registrerte data.";
            
            søkerinfoL = new JLabel("Informasjon om deg som boligsøker:");
            Antall_personerL = new JLabel("Antall personer:");
            SivilstatusL = new JLabel("Sivilstatus:");
            YrkeL = new JLabel("Yrke:");
            RøykerL = new JLabel("Røyker:");
            HusdyrL = new JLabel("Husdyr:");
            antall_personer = new JTextField(7);
            sivilstatus = new JTextField(7);
            yrke = new JTextField(7);
            røyker = new JCheckBox();
            husdyr = new JTextField(1);
            
            if (søkerinfors.next() == false) //dvs bruker er boligsøker, men har ikke ennå registrert noen preferanser.
            {
                antall_personer.setText(noReg);
                sivilstatus.setText(noReg );
                yrke.setText(noReg);
                røyker.setSelected(false); //erstatt med int(valgt, ikke valgt og don't care)
                husdyr.setText(noReg);
            }
            else
            {
                
                antall_personer.setText(søkerinfors.getString("antall_personer"));
                sivilstatus.setText(søkerinfors.getString("Sivilstatus"));
                yrke.setText(søkerinfors.getString("Yrke"));
                
                int røykertemp = søkerinfors.getInt("Røyker");
                if (røykertemp == 1)
                {
                    røyker.setSelected(true);
                }
                husdyr.setText(søkerinfors.getString("Husdyr"));
            }
            søkerinfopanel.add(Box.createVerticalStrut(5));
            søkerinfopanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            søkerinfopanel.add(Box.createVerticalStrut(5) );
            søkerinfopanel.add(søkerinfoL);
            søkerinfopanel.add(Box.createVerticalStrut(5));
            søkerinfopanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            søkerinfopanel.add(Box.createVerticalStrut(5) );
            søkerinfopanel.add(Antall_personerL);
            søkerinfopanel.add(antall_personer);
            søkerinfopanel.add(SivilstatusL);
            søkerinfopanel.add(sivilstatus);
            søkerinfopanel.add(YrkeL);
            søkerinfopanel.add(yrke);
            søkerinfopanel.add(RøykerL);
            søkerinfopanel.add(røyker);
            søkerinfopanel.add(HusdyrL);
            søkerinfopanel.add(husdyr);
            
            populatesøkerkravinfo();
            visBoligVarsler();
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Populerer grensesnittet med felter relatert til subentiteten søkerkrav.
     * <p>Metoden kalles enten som del av oppstartsprosedyren når klassen finner ut at brukeren er en boligsøker,
     * eller senere når en utleier velger å utvide sin konto.
     * <p>Metoden sjekker også om subentiteten til boligsøkeren(kjent som søkerkrav) tabellen faktisk har data for gjeldende bruker, og populerer feltene deretter. 
     */
    private void populatesøkerkravinfo()
    {
        try
        {
            søkerkravrs = Data_boligsøker.selectspecRSfromSøkerkrav(personnummer);
            søkerkravrs.first();
            
            kravinfoL = new JLabel("Informasjon om dine boligkrav:");
            minBoarealL  = new JLabel("Minimum Boareal:");
            maxBoarealL  = new JLabel("Maximum Boareal:");
            Antall_soveromL = new JLabel("Minimum Antall soverom:");
            ByggårL = new JLabel("Eldste ønsket Byggår:");
            minPrisL = new JLabel("Minimum Pris:");
            maxPrisL = new JLabel("Maximum Pris:");
            PeisL = new JLabel("Har Peis:");
            ParkeringL = new JLabel("Har Parkering:");
            
            minBoareal = new JTextField(7);
            maxBoareal = new JTextField(7);
            Antall_soverom = new JTextField(7);
            Byggår = new JTextField(7);
            minPris = new JTextField(7);
            maxPris = new JTextField(7);
            String[] peismuligheter = {"Ingen preferanser","Vil ha Peis","Vil ikke ha peis"};
            Peis = new JComboBox(peismuligheter);
            String[] parkeringsmuligheter = {"Ingen preferanser","Vil ha Parkering","Vil ikke ha parkering"};
            Parkering = new JComboBox(parkeringsmuligheter);
            
            if (søkerkravrs.first() == false) //dvs er boligsøker men krav er ikke registrert enda.
            {
                String noReg= "Ingen registrerte data.";
                minBoareal.setText(noReg);
                maxBoareal.setText(noReg);
                Antall_soverom.setText(noReg);
                Byggår.setText(noReg);
                minPris.setText(noReg);
                maxPris.setText(noReg);
                Peis.setSelectedIndex(0);
                Parkering.setSelectedIndex(0);
            }
            else
            {
                minBoareal.setText(søkerkravrs.getString("Min_Areal"));
                maxBoareal.setText(søkerkravrs.getString("Max_Areal"));
                Antall_soverom.setText(søkerkravrs.getString("Min_Soverom"));
                Byggår.setText(søkerkravrs.getString("Min_Byggår"));
                minPris.setText(søkerkravrs.getString("Min_Pris"));
                maxPris.setText(søkerkravrs.getString("Max_Pris"));
                Peis.setSelectedIndex(søkerkravrs.getInt("Peis"));
                Parkering.setSelectedIndex(søkerkravrs.getInt("Parkering"));
            }
            
            søkerkravpanel.add(Box.createVerticalStrut(5));
            søkerkravpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            søkerkravpanel.add(Box.createVerticalStrut(5) );
            søkerkravpanel.add(kravinfoL);
            søkerkravpanel.add(Box.createVerticalStrut(5));
            søkerkravpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            søkerkravpanel.add(Box.createVerticalStrut(5) );
            søkerkravpanel.add(minBoarealL);
            søkerkravpanel.add(minBoareal);
            søkerkravpanel.add(maxBoarealL);
            søkerkravpanel.add(maxBoareal);
            søkerkravpanel.add(Antall_soveromL);
            søkerkravpanel.add(Antall_soverom);
            søkerkravpanel.add(ByggårL);
            søkerkravpanel.add(Byggår);
            søkerkravpanel.add(minPrisL);
            søkerkravpanel.add(minPris);
            søkerkravpanel.add(maxPrisL);
            søkerkravpanel.add(maxPris);
            søkerkravpanel.add(PeisL);
            søkerkravpanel.add(Peis);
            søkerkravpanel.add(ParkeringL);
            søkerkravpanel.add(Parkering);
            invalidate();
            repaint();
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Hjelpemetode, sjekker om brukeren er en utleier, og handler deretter.
     * 
     * <p>
     * Metoden gjør først en kjapp sjekk mot databasen for å finne ut om brukeren er en utleier.
     * Hvis ja, kalles populateutleierdata, som har til hensikt med å oppdatere grensesnittet med utleierens navn og firma.
     * <p>Hvis ikke, må brukeren ha evnen til å bli en utleier, altså utvide sin konto.
     * Da legges likesågjerne til en ny knapp til lowerpanel som gjør brukeren istand til å utvide om ønskelig.
     * 
     */
    private void erutleier()
    {
        try
        {
            ResultSet rs = Data_utleier.selectspecRSfromUtleier(personnummer);
            utleiertest = rs.first(); // if true: brukers personnummer er representert i boligs�ker, ogs� til � sjekke om utleierdata skal oppdateres.
 
            if (utleiertest) //populerer utleierdata(firma tilh�righet)
                populateutleierdata();
            else  //ikke utleier, m� da kunne bli utleier
            {
                bliutleierknapp =  new JButton("Bli utleier");
                knapperpanel.add(bliutleierknapp);
                bliutleierknapp.addActionListener(new ActionListener() 
                {
                    //ber brukeren bekrefte ønske om utvidelse, for så å oppdatere databasen.
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        samling = new JPanel();
                        BoxLayout box=new BoxLayout(samling,BoxLayout.Y_AXIS);
                        String advarsel = "Aksepterer du vil du bli underlagt norsk lov om utleievirksomhet. Videre vil du nå få mulighet til å legge ut boliger for leie."
                                + "Husk å taste inn ditt firmanavn.";
                        JTextField firmanavn = new JTextField(10);
                        samling.add(new JLabel(advarsel) );
                        samling.add(firmanavn);
                        int bekreft = JOptionPane.showConfirmDialog(null,samling,"er du sikker på at du vil bli utleier?",2);
                        tilbakemeldinger.append("["+linjeteller++ + "] "+advarsel+"(svar = "+bekreft+")\n");
                        
                        if (bekreft == 0)
                        {
                            String firmanavnString = firmanavn.getText();
                            slettbliutleierknappoglastutleier(firmanavnString);
                        }
                    }
                });
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Kalles som ledd av en boligsøkers ønske om å utvide sin konto.
     * <p>Metoden har kun til hensikt å oppdatere utleier med brukerens personnummer, og dens firmanavn.
     * <p>Etter dette sendes programmet videre til populateutleierdata, som er metoden med ansvaret for å populere feltene
     * @param firmanavn 
     */
    private void slettbliutleierknappoglastutleier(String firmanavn)
    {
        knapperpanel.remove(bliutleierknapp);
        try 
        {
            if (firmanavn.equals(""))
                firmanavn ="Ingen registrerte data.";
            Data_utleier.insertspecintoutleier(personnummer, firmanavn);
            populateutleierdata();
            getRootPane().revalidate();
        } catch (SQLException ex) 
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Populerer grensesnittet med felter relatert til entiten utleier.
     * <p>Metoden kalles enten som del av oppstartsprosedyren når klassen finner ut at brukeren er en utleier,
     * eller senere når en boligsøker velger å utvide sin konto.
     * <p>Metoden holder også styr på hvorvidt brukeren har satt et firmanavn eller ikke, og handler deretter.
     * @throws
     * 
     */
    private void populateutleierdata()
    {
        try
        {
            ResultSet utleierrs = Data_utleier.selectspecRSfromUtleier(personnummer);
            
            utleierinfoL = new JLabel("Informasjon om deg som utleier:");
            
            utleierpanel.add(Box.createVerticalStrut(5));
            utleierpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            utleierpanel.add(Box.createVerticalStrut(5) );
            
            utleierpanel.add(utleierinfoL);
            
            utleierpanel.add(Box.createVerticalStrut(5));
            utleierpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            utleierpanel.add(Box.createVerticalStrut(5) );
            
            firmaL = new JLabel("Firmaets navn:");
            firma = new JTextField(10);
            utleierrs.first();
            if (utleierrs.next() == false) //dvs er utleier men ingen data er registrert.
            {
                String noReg= "Ingen registrerte data.";
                firma.setText(noReg);
            }
            else firma.setText(utleierrs.getString("firma"));
            
            utleierpanel.add(firmaL);
            utleierpanel.add(firma);
            repaint();
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * Viser boliger som matcher søkerens ønsker i et scrollbart JTable.
 * 
 * <p>Metoden populerer JTable med samtlige passende boliger, men ikke hvis brukeren tidligere har blitt anvist til den boligen.
 * Disse uønskede resultatene blir fjernet fra tabellen, ved at det returnerte Bolig arrayet blir gjennomgått og oppdatert.
 * <p>Informasjonen som holder styr på akkurat hvilke boliger som allerede er sett på av hver individuelle bruker holdes i databasens "Viste_Boliger" tabell.
 * @throws SQLException 
 */
    private void visBoligVarsler() //viser en JTable med enkel informasjon om boliger som matcher s�kekriteriene siden sist du logget inn. vises automatisk hvis du er en boligs�ker
    {
        try
        {

            int[] areal =
            {
                Integer.parseInt(minBoareal.getText()), Integer.MAX_VALUE
            };
            int[] antallrom =
            {
                Integer.parseInt(Antall_soverom.getText()), Integer.MAX_VALUE
            };
            Calendar nå = new GregorianCalendar();
            int[] byggår =
            {
                Integer.parseInt(Byggår.getText()), nå.get(Calendar.YEAR)
            };
            long[] pris =
            {
                Long.parseLong(minPris.getText()), Long.MAX_VALUE
            };
            Bolig[] array = Data_Boliger.getBoliger(null, null, areal, null, antallrom, byggår, null, pris, " order by Avertert desc ", 0);

            //fjern det som matcher boligID OG brukerID
            ResultSet sett = Data_Viste_Boliger.selectspecRSfromViste_Boliger(personnummer);
            List liste = new ArrayList();
            while (sett.next())
            {
                liste.add(sett.getString("bolig_BoligID"));
            }

            for (int i = 0; i < array.length; i++)
            {
                if (liste.contains("" + array[i].getId()))
                {
                    array[i] = null;
                }
            }
            Data_Boliger boliger = new Data_Boliger();
            //Fortsett med populering av modelen.
            String[] kolnavn =
            {
                "Bolig ID", "Adresse", "Bo areal", "Antall Rom", "Byggår", "Beskrivelse av bolig", "Utleie pris", "Averterings dato"
            };
            model = new DefaultTableModel(null, kolnavn);
            Object[] object = new Object[8];
            for (Bolig array1 : array)
            {
                if (array1 != null)
                {
                    if (array1.getId() != -1)
                    {
                        object[0] = array1.getId();
                        object[1] = array1.getAdresse();
                        object[2] = array1.getAreal();
                        object[3] = array1.getAntRom();
                        object[4] = array1.getByggår();
                        object[5] = array1.getBesk();
                        object[6] = array1.getPris();
                        object[7] = array1.getAvertert();
                        model.addRow(object);
                    }
                }
            }
            if (vistreff == null)
            {
                vistreff = new JTable(6, 10);

                vistreff.setModel(model);
                vistreff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                vistreff.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        int rad = vistreff.getSelectedRow();
                        int BoligID = (int) model.getValueAt(rad, 0);
                        tilbakemeldinger.append("[" + linjeteller++ + "] " + "Bolig med ID:" + BoligID + " Er nå merket som sett før." + "\n");
                        //psudo update visteboligerTabell i db.
                        model.removeRow(rad);
                        updatevisteboliger(BoligID);
                        //switch to visbolig(boligindeks).
                        switchtab(BoligID);
                    }
                });

                tablepanel.setLayout(new BorderLayout());
                tablepanel.add(vistreff.getTableHeader(), BorderLayout.PAGE_START);
                tablepanel.add(new JScrollPane(vistreff), BorderLayout.CENTER);
                
            }
            else
            {
                vistreff.setModel(model);
            }
            
            repaint();
            revalidate();
        }
        //fanges denne opp betyr det at informasjonen i relevante tekstfelt ikke kan parses til ints, 
        //da skal denne metoden ikke gjøre noe.
        //Istedet vises en label som sier at data ikke er tilgjengelig.
        catch (NumberFormatException e) 
        {
            tablepanel.setLayout(new BorderLayout());
            tablepanel.add(new JLabel("Data ikke tilgjengelig."));
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Hjelpemetode, oppdaterer tabellen Viste_Boliger.
     * <p>Metoden brukes av metoden VisBoligVarsler så fort brukeren har trykket på en anvist bolig i vistreff.
     * <p>Kun to felt er nødvendig, brukerens personnummer, og BoligIDen brukeren ville se på.
     * @param BoligID 
     */
    private void updatevisteboliger(int BoligID)
    {
        try
        {
            Data_Viste_Boliger.insertSpecintoViste_Boliger(personnummer, BoligID);
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Skifter SubPanel_Boliger tabben, og kaller denne klassens visBolig metode.
     * <p>Når brukeren ønsker å se på en bolig, vil man måtte finne ut hva SubPanel_Boliger har til indeks i JTabbedPane.
     * Når dette er gjort, blir JTabbedPanen klassen selv ligger i bedt programatisk om å skifte indeks, altså hvilket pane som er i fokus.
     * @param BoligID 
     */
    private void switchtab(int BoligID) //Skal switche til tab av typen SubPanel_Boliger
    {
        
        final JTabbedPane tp = (JTabbedPane)SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
        Component[] c = tp.getComponents();
        //fungerer veldig rett fram, finner ut hvilken komponent TPs component liste som er en instans av VisBolig, 
        //for så sette fokus til dette objektet og kjøre en metode i det objektet som viser bolig med medsendt BoligID
        for (int i = 0; i < c.length; i++)
        {
            
            if (c[i] instanceof SubPanel_Boliger)
            {
                tp.setSelectedIndex(i);
                SubPanel_Boliger vis = (SubPanel_Boliger)tp.getSelectedComponent();
                Bolig[] bolig = Data_Boliger.getBoliger(""+BoligID,null,null,null,null,null,null,null,null,0);
                vis.visBolig(bolig[0]);
                return;
            }
            
        }
        String error = "Feil!! Intet SubPanel_Boliger objekt er lagt til JTabbedPane!";
        tilbakemeldinger.append("["+linjeteller++ + "] "+error + "\n");
        JOptionPane.showMessageDialog(null, error+"\n");
        
    }
    
    /**
     * Lagrer felter
     * <p>Metoden sjekker om relevante felt har ulovlige verdier, og gir brukeren beskjed om dette i tilbakemeldingspanelet.
     * <p> Til slutt sjekkes det om det var problemer meed lagringen, og gir beskjed om dette.
     */
    private void lagre() //lagrer profil endringer i database tabellene
    {
        boolean errors = false;
        String navnstring = Navn.getText();
        String adressestring = Adresse.getText();
        String emailstring = Email.getText();
        String tlfstring = Telefon.getText();
        String sql;
        
        try
        {
            Data_Bruker.updateBruker(personnummer, navnstring, adressestring, emailstring, tlfstring);
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (boligsøkertest)
        {
            //deklarasjoner
            //søkerinfo
            int ant_persint;
            String sivilstatusstring;
            String yrkestring;
            int røykerint;
            int husdyrint;

            //søkerkrav
            int minarealint;
            int maxarealint;
            int antsoveromint;
            int byggårint;
            int minprisint;
            int maxprisint;
            int peisint;
            int parkeringint;

            //inputtesting
            try
            {
                //søkerinfo
                ant_persint = Integer.parseInt(antall_personer.getText());
                sivilstatusstring = sivilstatus.getText();
                yrkestring = yrke.getText();
                if (røyker.isSelected()) røykerint = 1; else røykerint = 0;
                husdyrint = Integer.parseInt(husdyr.getText());

                //fortsatt ingen garanti for at personnummeret er registrert i søkerinfo og søkerkrav, må derfor sjekke om kunden har spesifisert noe enda.
                try
                {

                    ResultSet testrs = Data_boligsøker.selectspecRSfromsøkerinfo(personnummer);
                    
                    if (testrs.next()) //dvs personnummer er representert i søkerinfo;
                    {
                        Data_boligsøker.updatesøkerinfo(ant_persint, sivilstatusstring, yrkestring, røykerint, husdyrint, personnummer);
                    }
                    else//personnummeret er ikke representert i søkerinfo, da må vi legge til framfor å oppdatere.
                    {
                        Data_boligsøker.insertintosøkerinfo(personnummer, ant_persint, sivilstatusstring, yrkestring, røykerint, husdyrint);
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            catch (NumberFormatException e) //ints må nesten vå være ints.
            {
                String error = "Feltene relatert til brukerens søkerinformasjon er angitt med ulovlige tegn, disse feltene ble ikke lagret.";
                tilbakemeldinger.append("["+linjeteller++ + "] "+error+"\n");
                errors = true;
            }
                    
            try
            {//inputtesting
                //søkerkrav
                minarealint = Integer.parseInt(minBoareal.getText());
                maxarealint = Integer.parseInt(maxBoareal.getText());
                antsoveromint = Integer.parseInt(Antall_soverom.getText());
                byggårint = Integer.parseInt(Byggår.getText());
                minprisint = Integer.parseInt(minPris.getText());
                maxprisint = Integer.parseInt(maxPris.getText());
                peisint = Peis.getSelectedIndex();
                parkeringint = Parkering.getSelectedIndex();
                try //igjen må vi sjekke om søkerkrav allerede er angitt, eller ikke
                {

                    ResultSet testrs = Data_boligsøker.selectspecRSfromSøkerkrav(personnummer);
                    if (testrs.next())
                    {
                        Data_boligsøker.updatesøkerkrav(minarealint, maxarealint, antsoveromint, minprisint, maxprisint, peisint, parkeringint, personnummer, byggårint);
                    }
                    else
                    {
                        Data_boligsøker.insertintosøkerkrav(personnummer, minarealint, maxarealint, antsoveromint, byggårint, minprisint, maxprisint, peisint, parkeringint);
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            catch (NumberFormatException e)
            {
                String error = "Feltene relatert til brukerens boligkrav er angitt med ulovlige tegn, disse feltene ble ikke lagret.";
                tilbakemeldinger.append("["+linjeteller++ + "] "+error + "\n");
                errors = true;
            }
 
        }
        
        if (utleiertest)
        {
            try 
            {
                String firmastring = firma.getText();
                Data_utleier.updateutleier(firmastring, personnummer);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String sluttmelding;
        if (errors == false)//ingen feil
        {
            sluttmelding = "Operasjonen var vellykket, alle felt ble lagret.";
        }
        else
        {
            sluttmelding = "Operasjonen var ikke helt vellykket, noen eller ingen felt ble lagret.";
        }
        tilbakemeldinger.append("["+linjeteller++ + "] "+sluttmelding + "\n");
        //til slutt må vi oppdattere vistreff
        visBoligVarsler();
        repaint();
    }
}
