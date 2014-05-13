package testprosjekt;

//Petter
//SubPanel_KundeProfil

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

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
    private final JLabel BrukerinfoL;
    private final JLabel PersonnummerL;
    private final JLabel NavnL;
    private final JLabel AdresseL;
    private final JLabel EmailL;
    private final JLabel TelefonL;
    
    private final JTextField Personnummer;
    private final JTextField Navn;
    private final JTextField Adresse;
    private final JTextField Email;
    private final JTextField Telefon;
    
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
    
    public SubPanel_KundeProfil(MainPanel parent, String id) throws SQLException
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
       
        lowerpanel.add(knapperpanel);
        
        //Last brukerdata, blir gjort uavhengig av eventuelle brukersubtyper.
        String sql = "SELECT * from bruker where Personnummer=" + personnummer + ";";
        ResultSet rs = Data_Boliger.execQuery(sql);
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
                int svar = JOptionPane.showConfirmDialog(null, "Advarsel! Svarer du ja vil din profil bli fullstendig slettet fra vårt system.");
                if (svar == 0)
                {
                    if (utleiertest)
                    {
                        String sql = "delete from utleier where Bruker_Personnummer="+personnummer+";";
                        try
                        {
                            Data_Boliger.execUpdate(sql);
                        } 
                        catch (SQLException ex)
                        {
                            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (boligsøkertest)
                    {
                        try {
                            String sql = "delete from søkerinfo where Boligsøker_Bruker_Personnummer="+personnummer+";";
                            Data_Boliger.execUpdate(sql);
                            sql = "delete from søkerkrav where Boligsøker_Bruker_Personnummer="+personnummer+";";
                            Data_Boliger.execUpdate(sql);
                            sql = "delete from boligsøker where Bruker_Personnummer="+personnummer+";";
                            Data_Boliger.execUpdate(sql);
                            sql = "delete from bruker where Personnummer="+personnummer+";";
                            Data_Boliger.execUpdate(sql);
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
        
        resettførknapp = new JButton("reset boligforslagsdata");
        resettførknapp.addActionListener(new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "OBS: Omstart er nødvendig for at endringene skal ta effekt.");
                try
                {
                    Data_Boliger.execUpdate("delete from viste_boliger where bruker_Personnummer="+personnummer+";");
                } 
                catch (SQLException ex)
                {
                    Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        knapperpanel.add(resettførknapp);
        
        erboligsøker();
        erutleier();
    }
    /**
     * 
     * @throws SQLException 
     */
    private void erboligsøker() throws SQLException
    {
        String sql = "select * from boligsøker where Bruker_Personnummer="+personnummer+";";
        ResultSet rs = Data_Boliger.execQuery(sql);
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
                    if (bekreft == 0)
                    {
                        slettblibliboligsøkerknappogbliboligsøker();
                    }
                }
            });
        }
    }
    private void slettblibliboligsøkerknappogbliboligsøker()
    {
        knapperpanel.remove(bliboligsøkerknapp);
        try
        {
            String sql = "insert into boligsøker VALUES('"+personnummer+"');";
            Data_Boliger.execUpdate(sql);
            populatesøkerinfo();
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void populatesøkerinfo() throws SQLException
    {
        String sql = "select * from søkerinfo where Boligsøker_Bruker_Personnummer='"+personnummer+"';";
        søkerinfors = Data_Boliger.execQuery(sql);
        
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
    }
    
    private void populatesøkerkravinfo() throws SQLException
    {
        String sql = "select * from søkerkrav where Boligsøker_Bruker_Personnummer='"+personnummer+"';";
        søkerkravrs = Data_Boliger.execQuery(sql);
        søkerkravrs.first();
        
        kravinfoL = new JLabel("Informasjon om dine boligkrav:");
        minBoarealL  = new JLabel("Minimum Boareal:");
        maxBoarealL  = new JLabel("Maximum Boareal:");
        Antall_soveromL = new JLabel("Antall soverom:");
        ByggårL = new JLabel("Byggår:");
        minPrisL = new JLabel("Minimum Pris:");
        maxPrisL = new JLabel("Maximum Pris:");
        PeisL = new JLabel("Peis:");
        ParkeringL = new JLabel("Parkering:");
        
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
        repaint();

    }

    private void erutleier() throws SQLException 
    {
        String sql = "select * from utleier where Bruker_Personnummer="+personnummer+";";
        ResultSet rs = Data_Boliger.execQuery(sql);
        utleiertest = rs.first(); // if true: brukers personnummer er representert i boligs�ker, ogs� til � sjekke om utleierdata skal oppdateres.
        
        if (utleiertest) //populerer utleierdata(firma tilh�righet)
            populateutleierdata();
        else  //ikke utleier, m� da kunne bli utleier
        {
            bliutleierknapp =  new JButton("Bli utleier");
            knapperpanel.add(bliutleierknapp);
            bliutleierknapp.addActionListener(new ActionListener() 
            {

                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    //Execute when button is pressed
                    samling = new JPanel();
                    BoxLayout box=new BoxLayout(samling,BoxLayout.Y_AXIS);
                    String advarsel = "Aksepterer du vil du bli underlagt norsk lov om utleievirksomhet.\nVidere vil du nå få mulighet til å legge ut boliger for leie."
                            + "\nHusk å taste inn ditt firmanavn.\n";
                    JTextField firmanavn = new JTextField(10);
                    samling.add(new JLabel(advarsel) );
                    samling.add(firmanavn);
                    int bekreft = JOptionPane.showConfirmDialog(null,samling,"er du sikker på at du vil bli utleier?",2);
                    
                    if (bekreft == 0)
                    {
                        String firmanavnString = firmanavn.getText();
                        slettbliutleierknappoglastutleier(firmanavnString);
                    }
                }
            });
        }
    }
    
    private void slettbliutleierknappoglastutleier(String firmanavn)
    {
        knapperpanel.remove(bliutleierknapp);
        try 
        {
            String sql = "Insert into utleier VALUES('"+personnummer+"','"+firmanavn+"'); ";
            Data_Boliger.execUpdate(sql);
            populateutleierdata();
        } catch (SQLException ex) 
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void populateutleierdata() throws SQLException
    {
        String sql = "select * from utleier where Bruker_Personnummer='"+personnummer+"';";
        ResultSet utleierrs = Data_Boliger.execQuery(sql);
        
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
        if (utleierrs.next() == false) //dvs er utleier men ingen data er registret.
        {
            String noReg= "Ingen registrerte data.";
            firma.setText(noReg);
        }
        else firma.setText(utleierrs.getString("firma"));

        utleierpanel.add(firmaL);
        utleierpanel.add(firma);
        repaint();
    }
    
    private void visBoligVarsler() throws SQLException //viser en JTable med enkel informasjon om boliger som matcher s�kekriteriene siden sist du logget inn. vises automatisk hvis du er en boligs�ker
    {
        try
        {
            vistreff = new JTable(6, 10);
            Data_Boliger boliger = new Data_Boliger();

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
            String sql = "select * from viste_boliger where bruker_Personnummer='" + personnummer + "';";
            ResultSet sett = Data_Boliger.execQuery(sql);
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

            vistreff.setModel(model);
            vistreff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            vistreff.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    int rad = vistreff.getSelectedRow();
                    int BoligID = (int) model.getValueAt(rad, 0);

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
        }
    }
    
    
    private void updatevisteboliger(int BoligID)
    {
        String sql = "Insert into viste_boliger VALUES('"+personnummer+"','"+BoligID+"'); ";
        try
        {
            Data_Boliger.execUpdate(sql);
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void switchtab(int BoligID) //Skal switche til klassen visBolig
    {
        
        final JTabbedPane tp = (JTabbedPane)SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
        Component[] c = tp.getComponents();
        //fungerer veldig rett fram, finner ut hvilken komponent TPs component liste som er en instans av VisBolig, 
        //for s� sette fokus til dette objektet og kj�re en metode i den klassen som viser bolig med medsendt BoligID
        for (int i = 0; i < c.length; i++)
        {
            
            if (c[i] instanceof SubPanel_Boliger)
            {
                tp.setSelectedIndex(i);
                SubPanel_Boliger vis = (SubPanel_Boliger)tp.getSelectedComponent();
                Bolig[] bolig = Data_Boliger.getBoliger(""+BoligID,null,null,null,null,null,null,null,null,0);
                vis.visBolig(bolig[0]); //eksakt metodenavn ukjent p� n�v�rende tidspunkt. 
                return;
            }
            
        }
        JOptionPane.showMessageDialog(null, "Feil, intet visbolig objekt er lagt til JTabbedPane.");
        
    }
    
    private void lagre() //lagrer profil endringer i database tabellene
    {
        String navnstring = Navn.getText();
        String adressestring = Adresse.getText();
        String emailstring = Email.getText();
        String tlfstring = Telefon.getText();

        String sql = "UPDATE bruker "
                + "SET "
                + "Navn='" + navnstring + "', Adresse='" + adressestring + "', Email='" + emailstring + "', Telefon='" + tlfstring + "' "
                + "WHERE Personnummer='" + personnummer + "';";
        try
        {
            Data_Boliger.execUpdate(sql);
        } catch (SQLException ex)
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

                //søkerkrav
                minarealint = Integer.parseInt(minBoareal.getText());
                maxarealint = Integer.parseInt(maxBoareal.getText());
                antsoveromint = Integer.parseInt(Antall_soverom.getText());
                byggårint = Integer.parseInt(Byggår.getText());
                minprisint = Integer.parseInt(minPris.getText());
                maxprisint = Integer.parseInt(maxPris.getText());
                peisint = Peis.getSelectedIndex();
                parkeringint = Parkering.getSelectedIndex();
                
                
            } catch (NumberFormatException e) //ints må nesten vå være ints. bemerk at mysqls tinyints er bogus, 
                                               //de blir behandlet akkurat som vanlige ints du kan fint sette en tinyint(1) til 30.
            {
                JOptionPane.showMessageDialog(null, "Feltene har ikke gyldige verdier, ingenting vil bli lagret.");
                return;
            }

            //fortsatt ingen garanti for at personnummeret er registrert i søkerinfo og søkerkrav, må derfor sjekke om kunden har spesifisert noe enda.
            try
            {

                sql = "select * from søkerinfo WHERE Boligsøker_Bruker_Personnummer='" + personnummer + "';";
                ResultSet testrs = Data_Boliger.execQuery(sql); //brukes for å finne ut om personnummer er representert i søkerinfo;

                if (testrs.next()) //dvs personnummer er representert i søkerinfo;
                {
                    sql = "UPDATE søkerinfo "
                            + "SET "
                            + "Antall_personer='" + ant_persint + "', Sivilstatus='" + sivilstatusstring + "', Yrke='" + yrkestring + "'"
                            + ",Røyker='" + røykerint + "', Husdyr='" + husdyrint + "' "
                            + "WHERE Boligsøker_Bruker_Personnummer='" + personnummer + "';";
                }
                else
                {
                    sql = "Insert into søkerinfo "
                            + "VALUES('" + personnummer + "','" + ant_persint + "','" + 
                            sivilstatusstring + "','" + yrkestring + "'," + røykerint + ",'" + husdyrint + "');";
                }
                Data_Boliger.execUpdate(sql);
            } catch (SQLException ex)
            {
                Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try //kan egentlig lagt sammen denne try-catch blokken med den over, gjort for å få lettere debugging.
            {
                
                sql = "select * from søkerkrav where Boligsøker_Bruker_Personnummer='" + personnummer + "';";
                ResultSet testrs = Data_Boliger.execQuery(sql); //brukes for å finne ut om personnummer er representert i søkerkrav;
                if (testrs.next())
                {
                    sql = "UPDATE søkerkrav "
                        + "SET "
                        + "Min_Areal='" + minarealint + "',Max_Areal='" + maxarealint + "',Min_Soverom='" + antsoveromint + "',"
                        + "Min_Byggår='" + byggårint + "',Min_Pris='" + minprisint + "',Max_Pris='" 
                            + maxprisint + "',Peis='" + peisint + "',Parkering='" + parkeringint + "' "
                        + "WHERE Boligsøker_Bruker_Personnummer='" + personnummer + "';";
                }
                else
                {
                    sql = "Insert into søkerkrav "
                    + "VALUES('" + personnummer + "',"+minarealint+","+maxarealint+","+antsoveromint+","
                    + byggårint+","+0+","+minprisint+","+maxprisint+","+peisint+","+parkeringint+"); ";
                     
                }
                Data_Boliger.execUpdate(sql);
            } catch (SQLException ex)
            {
                Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (utleiertest)
        {
            String firmastring = firma.getText();
            try
            {
                sql = "UPDATE utleier "
                        + "SET "
                        + "Firma='" + firmastring + "' "
                        + "WHERE Bruker_Personnummer=" + personnummer + ";";

                Data_Boliger.execUpdate(sql);
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(SubPanel_KundeProfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
