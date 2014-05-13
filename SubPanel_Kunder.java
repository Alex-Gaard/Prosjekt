package Boligformidling;

//Petter
//SubPanel_Kunder

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class SubPanel_Kunder extends SubPanel implements ActionListener//extends SubPanel, vær obs på at SubPanel allerede utvider JPanel, så fjern den når du tilpasser til boligformidling
{
    //Deklarasjoner
    //cards, bruker en cardlayout til bytte mellom to fullstendig forskjellige "views". Alternativet hadde vært å hele tiden opprette og slette etterhvert som man brukte programmet. 
    private final JPanel cards;
    private final CardLayout cardLayout;
    
    //card1
    
    //card1 søkefelt,brukt i card1upperpanel
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
    
    //knapper brukt i card1lowerpanel
    private final JButton card1søk;
    private final JButton card1tømFelter;
    
    //card1s tabell og tilhørende objekter
    private DefaultTableModel søkmodel;
    private JTable card1søktabell;
    private ResultSet søkrs;
    
    //diverse panelerbrukt i card1
    private final JPanel card1;
    private final JPanel card1upperpanel;
    private final JPanel card1centerpanel;
    private final JPanel card1lowerpanel;
    private final JPanel card1upperpanelsubboxpane;
    private final JPanel card1centerpanelsub;
    private final JPanel card1tablepanel;
    private final JPanel card1upperpanelwest;
    private final JPanel card1upperpaneleast;
    private final JPanel card1upperpanelsub;
    private final JPanel card1upperpanelwrapper;
    
    //card2
    //Kontaktinformasjon, brukt i card2upperpanel
    private final JLabel kontaktinfoL;
    private final JLabel PersonnummerL2;
    private final JLabel NavnL2;
    private final JLabel AdresseL2;
    private final JLabel EmailL2;
    private final JLabel TelefonL2;
    private final JTextField Personnummer2;
    private final JTextField Navn2;
    private final JTextField Adresse2;
    private final JTextField Email2;
    private final JTextField Telefon2;
    
    //søkerinformasjon
    private final JLabel infoomsøkerL;
    private final JLabel Antall_personerL;
    private final JLabel SivilstatusL;
    private final JLabel YrkeL;
    private final JLabel RøykerL;
    private final JLabel HusdyrL;
    private final JTextField antall_personer;
    private final JTextField yrke;
    private final JTextField husdyr;
    private final JCheckBox røyker;
    private final JTextField sivilstatus;
    
    //søkers boligkrav
    private final JLabel søkerskravL;
    private final JLabel MinarealL;
    private final JLabel MaxarealL;
    private final JLabel Antall_soveromL;
    private final JLabel ByggårL;
    private final JLabel MinprisL;
    private final JLabel MaxprisL;
    private final JLabel PeisL;
    private final JLabel ParkeringL;

    private final JTextField minareal;
    private final JTextField maxareal;
    private final JTextField antall_soverom;
    private final JTextField byggår;
    private final JTextField minpris;
    private final JTextField maxpris;
    private final JComboBox peis;
    private final JComboBox parkering;

    //Utleiers firma
    private final JLabel utleierL;
    private final JLabel FirmaL;
    private final JTextField firma;
    
    //card2Centerpanel label
    private final JLabel planlagtemøterL;    
    
    //card2lowerpanel
    private final JButton card2gjørendringer;
    private final JButton card2avsluttcard2;
    
    //card2s mange panels.
    private final JPanel card2;
    private final JPanel card2upperpanel;
    private final JPanel card2centerpanel;
    private final JPanel card2lowerpanel;
    
    private final JPanel card2upperpanelBrukerPanel;
    private final JPanel card2upperpanelSøkerInfoPanel;
    private final JPanel card2upperpanelSøkerKravPanel;
    private final JPanel card2upperpanelsub1;
    private final JPanel card2upperpanelsub2;
    private final JPanel card2upperpanelsub3;
    private final JPanel card2centerpanelsub;

    private final JPanel card2upperpanelgridwrapper;
    private final JPanel card2tablepanel;
    private final JPanel card2upperpanelsub1wrapper;
    private final JPanel card2upperpanelsub2wrapper;
    private final JPanel card2upperpanelsub3wrapper;
    private final JPanel card2upperpanelUtleierPanel;
    private final JPanel card2upperpanelsub4;
    private final JPanel card2upperpanelsub4wrapper;

    //card2 tabeller,modell og resultatsett
    JTable card2leiekontrakter;
    DefaultTableModel leiekontraktermodel;
    
    ResultSet leiekontraktrs;
    ResultSet søkerinfors;
    ResultSet søkerkravrs;
    ResultSet utleierrs;
    
    //others
    private int personnummer;
    private Data_Boliger database;
 
    public SubPanel_Kunder(MainPanel parent)
    {
        super(parent);
        //Database oppretting
        database = new Data_Boliger();
        
        //card1
        setLayout(new BorderLayout()); //kun til funksjon å få hele panelet til å skalere med framen.
        
        card1 = new JPanel();
        card1.setLayout(new BorderLayout());
        
        card1upperpanel = new JPanel();
        card1upperpanel.setLayout(new BorderLayout());
        card1centerpanel = new JPanel();
        card1centerpanel.setLayout(new BorderLayout());
        card1lowerpanel = new JPanel();
        
        card1søk = new JButton("Søk");
        card1søk.addActionListener(this);
        
        card1tømFelter = new JButton("Tøm felter");
        card1tømFelter.addActionListener(this);

        PersonnummerL = new JLabel("Personnummer:");
        NavnL = new JLabel("Navn:");
        AdresseL = new JLabel("Adresse:");
        EmailL = new JLabel("Email:");
        TelefonL = new JLabel("Telefon:");

        Personnummer = new JTextField(20);
        Navn = new JTextField(20);
        Adresse = new JTextField(20);
        Email = new JTextField(20);
        Telefon = new JTextField(20);
        
        card1lowerpanel.add(card1søk);
        card1lowerpanel.add(card1tømFelter);
        
        //søkpanel
        card1upperpanelsubboxpane = new JPanel();
        card1upperpanelsubboxpane.setLayout(new BoxLayout(card1upperpanelsubboxpane,BoxLayout.Y_AXIS));
        card1upperpanelsubboxpane.add(PersonnummerL);
        card1upperpanelsubboxpane.add(Personnummer);
        card1upperpanelsubboxpane.add(NavnL);
        card1upperpanelsubboxpane.add(Navn);
        card1upperpanelsubboxpane.add(AdresseL);
        card1upperpanelsubboxpane.add(Adresse);
        card1upperpanelsubboxpane.add(EmailL);
        card1upperpanelsubboxpane.add(Email);
        card1upperpanelsubboxpane.add(TelefonL);
        card1upperpanelsubboxpane.add(Telefon);
        
        card1upperpanelsub = new JPanel();
        card1upperpanelsub.setLayout(new BoxLayout(card1upperpanelsub,BoxLayout.Y_AXIS));
        
        card1upperpanelsub.add(card1upperpanelsubboxpane);
        
        //dummies
        card1upperpanelwest = new JPanel();
        card1upperpaneleast = new JPanel();
        
        //wrapper, har til funksjon å gi søkpanelet en sentral stilling, og gi det en etchedborder.
        card1upperpanelwrapper = new JPanel();
        card1upperpanelwrapper.add(card1upperpanelwest);
        card1upperpanelwrapper.add(card1upperpanelsub);
        card1upperpanelwrapper.add(card1upperpaneleast);
        card1upperpanelwrapper.setBorder(BorderFactory.createEtchedBorder());
        card1upperpanel.add(new JLabel("Søkekritterier"),BorderLayout.NORTH);
        card1upperpanel.add(card1upperpanelwrapper,BorderLayout.SOUTH);
        
        //oppretter en tabell som viser resultater etter søk
        card1søktabell = new JTable(1,0);
        card1søktabell.addMouseListener(new MouseAdapter() //binder en actionlistener slik at noe skjer når man trykker på en rad.
                        {
                            @Override
                            public void mouseClicked(MouseEvent e)
                            {
                                int rad = card1søktabell.getSelectedRow();
                                String PersNr = (String)søkmodel.getValueAt(rad, 0);
                                byttecard(PersNr);
                            }
                        });
        //wrapper panel for søktabell, har til hensikt å gi søktabell evnen til å skalere over hele centerpanel
        card1tablepanel = new JPanel();
        card1tablepanel.setLayout(new BorderLayout());
        card1tablepanel.add(card1søktabell.getTableHeader(),BorderLayout.PAGE_START);
        card1tablepanel.add(card1søktabell, BorderLayout.CENTER);
        
        //her legges omsider søktabellen inn i centerpanel, wrappet rundt, ganske heftig, som vi ser. 
        card1centerpanelsub = new JPanel(new BorderLayout());
        card1centerpanel.add(new JLabel("Søkekritterier"),BorderLayout.NORTH);
        card1centerpanelsub.add(new JScrollPane(card1tablepanel),BorderLayout.CENTER);
        card1centerpanelsub.setBorder(BorderFactory.createEtchedBorder());
        card1centerpanel.add(card1centerpanelsub);
        
        card1.add(card1upperpanel,BorderLayout.NORTH);
        card1.add(card1centerpanel,BorderLayout.CENTER);
        card1.add(card1lowerpanel,BorderLayout.SOUTH);

        
        //card2
        card2 = new JPanel();
        card2.setLayout(new BorderLayout());
        
        card2upperpanel = new JPanel();
        card2upperpanel.setBorder(BorderFactory.createEtchedBorder());
        card2centerpanel = new JPanel();
        card2centerpanel.setLayout(new BorderLayout());
        card2lowerpanel = new JPanel();
        card2.add(card2upperpanel,BorderLayout.NORTH);
        card2.add(card2centerpanel,BorderLayout.CENTER);
        card2.add(card2lowerpanel,BorderLayout.SOUTH);
        
        card2upperpanelBrukerPanel = new JPanel();
        card2upperpanelBrukerPanel.setLayout(new BoxLayout(card2upperpanelBrukerPanel,BoxLayout.Y_AXIS));
        card2upperpanelSøkerInfoPanel = new JPanel();
        card2upperpanelSøkerInfoPanel.setLayout(new BoxLayout(card2upperpanelSøkerInfoPanel,BoxLayout.Y_AXIS));
        card2upperpanelSøkerKravPanel = new JPanel();
        card2upperpanelSøkerKravPanel.setLayout(new BoxLayout(card2upperpanelSøkerKravPanel,BoxLayout.Y_AXIS));
        card2upperpanelUtleierPanel = new JPanel();
        card2upperpanelUtleierPanel.setLayout(new BoxLayout(card2upperpanelUtleierPanel,BoxLayout.Y_AXIS));

        //bruker
        PersonnummerL2 = new JLabel("Personnummer:");//tydeligvis ikke lov å bruke samme JLabel om igjen, so be it.
        NavnL2 = new JLabel("Navn:");
        AdresseL2 = new JLabel("Adresse:");
        EmailL2 = new JLabel("Email:");
        TelefonL2 = new JLabel("Telefon:");
        Personnummer2 = new JTextField(10);
        Navn2 = new JTextField(10);
        Adresse2 = new JTextField(15);
        Email2 = new JTextField(15);
        Telefon2 = new JTextField(10);
        Personnummer2.setEditable(false);

        card2upperpanelBrukerPanel.add(PersonnummerL2);
        card2upperpanelBrukerPanel.add(Personnummer2);
        card2upperpanelBrukerPanel.add(NavnL2);
        card2upperpanelBrukerPanel.add(Navn2);
        card2upperpanelBrukerPanel.add(AdresseL2);
        card2upperpanelBrukerPanel.add(Adresse2);
        card2upperpanelBrukerPanel.add(EmailL2);
        card2upperpanelBrukerPanel.add(Email2);
        card2upperpanelBrukerPanel.add(TelefonL2);
        card2upperpanelBrukerPanel.add(Telefon2);
        
        //søkerinfo
        Antall_personerL = new JLabel("Antall personer:");
        SivilstatusL = new JLabel("Sivilstatus:");
        YrkeL = new JLabel("Yrke:");
        RøykerL = new JLabel("Røyker:");
        HusdyrL = new JLabel("Husdyr:");
        antall_personer = new JTextField(7);
        sivilstatus = new JTextField(7);
        yrke = new JTextField(7);
        røyker = new JCheckBox();
        husdyr = new JTextField(3);
        
        card2upperpanelSøkerInfoPanel.add(Antall_personerL);
        card2upperpanelSøkerInfoPanel.add(antall_personer);
        card2upperpanelSøkerInfoPanel.add(SivilstatusL);
        card2upperpanelSøkerInfoPanel.add(sivilstatus);
        card2upperpanelSøkerInfoPanel.add(YrkeL);
        card2upperpanelSøkerInfoPanel.add(yrke);
        card2upperpanelSøkerInfoPanel.add(RøykerL);
        card2upperpanelSøkerInfoPanel.add(røyker);
        card2upperpanelSøkerInfoPanel.add(HusdyrL);
        card2upperpanelSøkerInfoPanel.add(husdyr);
        
        //søkerkrav
        MinarealL = new JLabel("Minimum Areal:");
        MaxarealL = new JLabel("Maximum Areal");
        Antall_soveromL = new JLabel("Minimum antall soverom:");
        ByggårL = new JLabel("Eldste ønsket byggår:");
        MinprisL = new JLabel("Minimum Pris:");
        MaxprisL = new JLabel("Maximum Pris:");
        PeisL = new JLabel("Peis:");
        ParkeringL = new JLabel("Parkering:");
        
        
        minareal = new JTextField(7);
        maxareal = new JTextField(7);
        antall_soverom = new JTextField(1);
        byggår = new JTextField(4);
        minpris = new JTextField(8);
        maxpris = new JTextField(8);
        String[] peismuligheter = {"Ingen preferanser","Vil ha Peis","Vil ikke ha peis"};
        peis = new JComboBox(peismuligheter);
        String[] parkeringsmuligheter = {"Ingen preferanser","Vil ha Parkering","Vil ikke ha parkering"};
        parkering = new JComboBox(parkeringsmuligheter);
        
        card2upperpanelSøkerKravPanel.add(MinarealL);
        card2upperpanelSøkerKravPanel.add(minareal);
        card2upperpanelSøkerKravPanel.add(MaxarealL);
        card2upperpanelSøkerKravPanel.add(maxareal);
        card2upperpanelSøkerKravPanel.add(Antall_soveromL);
        card2upperpanelSøkerKravPanel.add(antall_soverom);
        card2upperpanelSøkerKravPanel.add(ByggårL);
        card2upperpanelSøkerKravPanel.add(byggår);
        card2upperpanelSøkerKravPanel.add(MinprisL);
        card2upperpanelSøkerKravPanel.add(minpris);
        card2upperpanelSøkerKravPanel.add(MaxprisL);
        card2upperpanelSøkerKravPanel.add(maxpris);
        card2upperpanelSøkerKravPanel.add(PeisL);
        card2upperpanelSøkerKravPanel.add(peis);
        card2upperpanelSøkerKravPanel.add(ParkeringL);
        card2upperpanelSøkerKravPanel.add(parkering);
        
        //utleier
        FirmaL = new JLabel("Firma:");
        firma = new JTextField(10);
        
        card2upperpanelUtleierPanel.add(FirmaL);
        card2upperpanelUtleierPanel.add(firma);
        //end of upperpanelsubpanels
        
        card2leiekontrakter = new JTable(5,1);
        card2leiekontrakter.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                String PersNr = Personnummer2.getText();
                int rad = card2leiekontrakter.getSelectedRow();

                String[] array = new String[4];
                array[0] = (String) leiekontraktermodel.getValueAt(rad, 0);
                array[1] = (String) leiekontraktermodel.getValueAt(rad, 1);
                array[2] = (String) leiekontraktermodel.getValueAt(rad, 2);
                array[3] = (String) leiekontraktermodel.getValueAt(rad, 3);
                JPanel panel = new JPanel();
                JLabel Bolig_ID = new JLabel("Bolig ID:");
                JLabel Dato = new JLabel("Dato:");
                JLabel Kundebehandler = new JLabel("Kundebehandler:");
                JLabel Påtatt = new JLabel("Påtatt:");
                JTextField bolig_id = new JTextField(7);
                JTextField dato = new JTextField(7);
                JTextField kundebehandler = new JTextField(7);
                JTextField påtatt = new JTextField(7);
                bolig_id.setText(array[0]);
                dato.setText(array[1]);
                kundebehandler.setText(array[2]);
                påtatt.setText(array[3]);
                panel.add(Bolig_ID);
                panel.add(bolig_id);
                panel.add(Dato);
                panel.add(dato);
                panel.add(Kundebehandler);
                panel.add(kundebehandler);
                panel.add(Påtatt);
                panel.add(påtatt);

                int svar = JOptionPane.showConfirmDialog(null, panel, "Gjør endringer", 2);
                if (svar == JOptionPane.YES_OPTION)
                {
                    try {
                        array[0] = bolig_id.getText();
                        array[1] = dato.getText();
                        array[2] = kundebehandler.getText();
                        array[3] = påtatt.getText();
                        
                        String sql = "UPDATE leiekontrakt_forespørsel "
                                + "SET "
                                + "Bolig_BoligID='" + array[0] + "',Opprettet_Dato='" + array[1]+"',Kundebehandler_Bruker_Personnummer='"
                                + array[2]+"',Påtatt='"+array[3]+ "' "
                                + "WHERE Boligsøker_Bruker_Personnummer=" + PersNr + ";";
                        database.execUpdate(sql);
                        leiekontraktermodel.removeRow(rad);
                        leiekontraktermodel.addRow(array);
                    } catch (SQLException ex) {
                        Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        card2upperpanelsub1 = new JPanel();
        card2upperpanelsub1.setLayout(new BorderLayout());
        card2upperpanelsub2 = new JPanel();
        card2upperpanelsub2.setLayout(new BorderLayout());
        card2upperpanelsub3 = new JPanel();
        card2upperpanelsub3.setLayout(new BorderLayout());
        card2upperpanelsub4 = new JPanel();
        card2upperpanelsub4.setLayout(new BorderLayout());
        
        card2upperpanelsub1wrapper = new JPanel();
        card2upperpanelsub1wrapper.setLayout(new BorderLayout());
        card2upperpanelsub1wrapper.add(card2upperpanelsub1,BorderLayout.NORTH);
        card2upperpanelsub1wrapper.setBorder(BorderFactory.createEtchedBorder());
        card2upperpanelsub2wrapper = new JPanel();
        card2upperpanelsub2wrapper.setLayout(new BorderLayout());
        card2upperpanelsub2wrapper.add(card2upperpanelsub2,BorderLayout.NORTH);
        card2upperpanelsub2wrapper.setBorder(BorderFactory.createEtchedBorder());
        card2upperpanelsub3wrapper = new JPanel();
        card2upperpanelsub3wrapper.setLayout(new BorderLayout());
        card2upperpanelsub3wrapper.add(card2upperpanelsub3,BorderLayout.NORTH);
        card2upperpanelsub3wrapper.setBorder(BorderFactory.createEtchedBorder());
        card2upperpanelsub4wrapper = new JPanel();
        card2upperpanelsub4wrapper.setLayout(new BorderLayout());
        card2upperpanelsub4wrapper.add(card2upperpanelsub4,BorderLayout.NORTH);
        card2upperpanelsub4wrapper.setBorder(BorderFactory.createEtchedBorder());
        
        kontaktinfoL =new JLabel("Brukerens kontaktinformasjon:");
        kontaktinfoL.setBorder(BorderFactory.createEtchedBorder(1));
        card2upperpanelsub1.add(kontaktinfoL,BorderLayout.NORTH);
        card2upperpanelsub1.add((card2upperpanelBrukerPanel),BorderLayout.SOUTH);
        infoomsøkerL = new JLabel("Informasjon om søker:");
        infoomsøkerL.setBorder(BorderFactory.createEtchedBorder(1));
        card2upperpanelsub2.add(infoomsøkerL,BorderLayout.NORTH);
        card2upperpanelsub2.add((card2upperpanelSøkerInfoPanel),BorderLayout.SOUTH);
        søkerskravL = new JLabel("Søkerens boligkrav:");
        søkerskravL.setBorder(BorderFactory.createEtchedBorder(1));
        card2upperpanelsub3.add(søkerskravL,BorderLayout.NORTH);
        card2upperpanelsub3.add((card2upperpanelSøkerKravPanel),BorderLayout.SOUTH);
        utleierL = new JLabel("Utleier informasjon");
        utleierL.setBorder(BorderFactory.createEtchedBorder(1));
        card2upperpanelsub4.add(utleierL,BorderLayout.NORTH);
        card2upperpanelsub4.add((card2upperpanelUtleierPanel),BorderLayout.SOUTH);
        
        
        card2upperpanelgridwrapper = new JPanel();
        card2upperpanelgridwrapper.setLayout(new GridLayout());
        card2upperpanelgridwrapper.add(card2upperpanelsub1wrapper);
        card2upperpanelgridwrapper.add(card2upperpanelsub2wrapper);
        card2upperpanelgridwrapper.add(card2upperpanelsub3wrapper);
        card2upperpanelgridwrapper.add(card2upperpanelsub4wrapper);
        
        card2upperpanel.add(card2upperpanelgridwrapper);
        
        card2tablepanel = new JPanel();
        card2tablepanel.setLayout(new BorderLayout());
        card2tablepanel.add(card2leiekontrakter.getTableHeader(),BorderLayout.PAGE_START);
        card2tablepanel.add(card2leiekontrakter, BorderLayout.CENTER);
        
        card2centerpanelsub= new JPanel();
        card2centerpanelsub.setLayout(new BorderLayout());
        planlagtemøterL = new JLabel("Planlagte møter:");
        card2centerpanel.add(planlagtemøterL,BorderLayout.NORTH);
        card2centerpanelsub.add(new JScrollPane(card2tablepanel),BorderLayout.CENTER );
        card2centerpanelsub.setBorder(BorderFactory.createEtchedBorder());
        card2centerpanel.add(card2centerpanelsub);
        
        card2gjørendringer = new JButton("gjør endringer");
        card2gjørendringer.addActionListener(this);
        card2avsluttcard2 = new JButton("returner til listevalg");
        card2avsluttcard2.addActionListener(this);
        
        card2lowerpanel.add(card2gjørendringer);
        card2lowerpanel.add(card2avsluttcard2);

        //Cards
        cards = new JPanel(new CardLayout() );
        cards.add(card1);
        cards.add(card2);
        cardLayout = (CardLayout) cards.getLayout();
        
        add(cards,BorderLayout.CENTER);
    }
    
    private void byttecard(String PersNr) //bytter til card2, samt sender med informasjonen som lå på den raden(aka personnummeret).
    {
    	BoxLayout box2 = new BoxLayout(card2,BoxLayout.Y_AXIS);
        card2.setLayout(box2);
        try
        {
            String test;
            this.
            søkrs.beforeFirst();
            while (søkrs.next())//finner den raden i resultatsettet som matcher PersNr;
            {
                test = søkrs.getString("Personnummer");
                if (test.equals(PersNr) )
                    break;
            }
            
            //Får visst ikke lov til å bruke samme tekstfelt to ganger, derfor disse.
            Personnummer2.setText(søkrs.getString("Personnummer") );
            Navn2.setText(søkrs.getString("Navn") );
            Adresse2.setText(søkrs.getString("Adresse") );
            Email2.setText(søkrs.getString("Email") );
            Telefon2.setText(søkrs.getString("Telefon") );
            søkrs.close();
            leiekontraktrs= Data_Boliger.execQuery("select * from boligsøker inner join leiekontrakt_forespørsel"
                    + " where boligsøker.Bruker_Personnummer=leiekontrakt_forespørsel.Boligsøker_Bruker_Personnummer"
                    + " and leiekontrakt_forespørsel.Boligsøker_Bruker_Personnummer="+PersNr+";");
            
            String[] leiekontraktKolonner = {"BoligID","Møtedato","Kundebehandler","Påtatt"};
            leiekontraktermodel = new DefaultTableModel(null,leiekontraktKolonner);
            card2leiekontrakter.setModel(leiekontraktermodel);
            
            while (leiekontraktrs.next())
            {
                String[] temp = new String[4]; 
                temp[0] = leiekontraktrs.getString("Bolig_BoligID");
                temp[1] = leiekontraktrs.getString("Opprettet_Dato");
                temp[2] = leiekontraktrs.getString("KundeBehandler_Bruker_Personnummer");
                temp[3] = leiekontraktrs.getString("Påtatt");
                leiekontraktermodel.addRow(temp);
            }
            
            leiekontraktrs.close();
            
            søkerinfors = Data_Boliger.execQuery("select * from boligsøker inner join søkerinfo"
                    + " where boligsøker.Bruker_Personnummer=søkerinfo.Boligsøker_Bruker_Personnummer"
                    + " and søkerinfo.Boligsøker_Bruker_Personnummer="+PersNr+";");
            
            if(søkerinfors.first() == true) //dvs er boligsøker
            {
                søkerinfors.first();
                antall_personer.setText(søkerinfors.getString("Antall_personer"));
                sivilstatus.setText(søkerinfors.getString("Sivilstatus"));
                yrke.setText(søkerinfors.getString("Yrke"));
                røyker.setSelected(søkerinfors.getBoolean("Røyker"));
                husdyr.setText(søkerinfors.getString("Husdyr"));
                
                søkerinfors.close();
                
                søkerkravrs = Data_Boliger.execQuery("select Min_Areal,Max_Areal,Min_Soverom,Min_Byggår,Min_Pris,Max_Pris,Peis,Parkering from boligsøker inner join søkerkrav"
                    + " where boligsøker.Bruker_Personnummer=søkerkrav.Boligsøker_Bruker_Personnummer"
                    + " and søkerkrav.Boligsøker_Bruker_Personnummer="+PersNr+";");
            
                søkerkravrs.first();
                minareal.setText(søkerkravrs.getString("Min_Areal"));
                maxareal.setText(søkerkravrs.getString("Max_Areal"));
                antall_soverom.setText(søkerkravrs.getString("Min_Soverom"));
                byggår.setText(søkerkravrs.getString("Min_Byggår"));
                minpris.setText(søkerkravrs.getString("Min_Pris"));
                maxpris.setText(søkerkravrs.getString("Max_Pris"));
                peis.setSelectedIndex(søkerkravrs.getInt("Peis"));
                parkering.setSelectedIndex(søkerkravrs.getInt("Parkering"));
                
                søkerkravrs.close();
            }
            else
            {
                String noReg= "Ingen registrerte data.";
                søkerinfors.first();
                antall_personer.setText(noReg);
                sivilstatus.setText(noReg);
                yrke.setText(noReg);
                røyker.setSelected(false);
                husdyr.setText(noReg);
                minareal.setText(noReg);
                maxareal.setText(noReg);
                antall_soverom.setText(noReg);
                byggår.setText(noReg);
                minpris.setText(noReg);
                maxpris.setText(noReg);
                peis.setSelectedIndex(0);
                parkering.setSelectedIndex(0);
            }
            
            utleierrs = Data_Boliger.execQuery("select * from utleier where Bruker_Personnummer='"+PersNr+"';");
            if (utleierrs.first()) 
            {
                firma.setText(utleierrs.getString("Firma"));
            }
            else //altså ikke utleier.
            {
                firma.setText("Ikke Registrert som Utleier.");
            }
            
            cardLayout.next(cards);
            card2.repaint();
            card2.revalidate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String[] lagrow(ResultSet rs) 
    {
        String[] array = null;
        try 
        {
            array = new String[5];
            
            array[0] = rs.getString("Personnummer");
            array[1] = rs.getString("Navn");
            array[2] = rs.getString("Adresse");
            array[3] = rs.getString("Email");
            array[4] = rs.getString("Telefon");
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == card1søk)
        { 
            try 
            {
                søkrs = querykonstruktør();
                String[] kolnavn = {"Personnummer","Navn","Adresse","Email","Telefon"};
                søkmodel = new DefaultTableModel(null,kolnavn);
                while (søkrs.next())
                {
                    søkmodel.addRow(lagrow(søkrs));
                }
                card1søktabell.setModel(søkmodel);
                card1søktabell.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getSource() == card1tømFelter)
        {   
            Personnummer.setText("");
            Navn.setText("");
            Adresse.setText("");
            Email.setText("");
            Telefon.setText("");
        } 
        else if (e.getSource() == card2avsluttcard2)
        {
            card1søk.doClick();

            cardLayout.next(cards);
        }
        else if (e.getSource() == card2gjørendringer) //lagrer endringer gjort av kundebehandler.
        {
            lagre();
        }
    }
    
    /**
     * Lagrer samtlige felt.
     * <p>
     * Feltene må ha representative verdier, dvs boligår må nesten være et boligår, hvis ikke får du en feilmelding via showMessageDialog.
     * @author Petter S.W Gjerstad
     */
    private void lagre()
    {
        //deklarasjoner
        String sql;
        String PersNr;
        String navn;
        String adresse;
        String email;
        String tlf;
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
        
        //utleier
        String firmastring;
        
        //Hjelpevariabler
        boolean erboligsøker = false;
        boolean erutleier = false;
        ResultSet testrs;

        try
        {
            //Innhenting av data.
            //bruker, slipper trøbbel her, det blir opp til brukeren å taste inn fornuftige data. 
            //Nonsense blir alså tolerert så lenge man ikke input er kompatibel med bakenforliggende datatyper.
            PersNr = Personnummer2.getText();
            navn = Navn2.getText();
            adresse = Adresse2.getText();
            email = Email2.getText();
            tlf = Telefon2.getText();
            //søkerinfo
            ant_persint = Integer.parseInt(antall_personer.getText());
            sivilstatusstring = sivilstatus.getText();
            yrkestring = yrke.getText();
            if (røyker.isSelected())røykerint = 1; else røykerint = 0;
            husdyrint = Integer.parseInt(husdyr.getText());
            //søkerkrav
            minarealint = Integer.parseInt(minareal.getText());
            maxarealint = Integer.parseInt(maxareal.getText());
            antsoveromint = Integer.parseInt(antall_soverom.getText());
            byggårint = Integer.parseInt(byggår.getText());
            minprisint = Integer.parseInt(minpris.getText());
            maxprisint = Integer.parseInt(maxpris.getText());
            peisint = peis.getSelectedIndex();
            parkeringint = parkering.getSelectedIndex();
            //utleier
            firmastring = firma.getText();
         
            try//brukes for å teste at brukeren virkelig er en boligsøker
            {
                testrs = Data_Boliger.execQuery("select * from boligsøker where Bruker_Personnummer='" + PersNr + "';");
                erboligsøker = testrs.next();
                testrs.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try//brukes for å teste at brukeren virkelig er en utleier.
            {
                testrs = Data_Boliger.execQuery("select * from utleier where Bruker_Personnummer='" + PersNr + "';");
                erutleier = testrs.next();
                testrs.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Feltene har ikke gyldige verdier, ingenting vil bli lagret.");
            return;
        }
        
        try
        {
            if (erboligsøker) //bruker er boligsøker; Da må boliginfo og boligkrav feltene lagres
            {
                sql = "selct * from søkerinfo where Boligsøker_Bruker_Personnummer='"+personnummer+"';";
                testrs = Data_Boliger.execQuery(sql);
                if(testrs.next()) //søkerinfo har allerede data om dette personnummeret
                {
                    sql = "UPDATE søkerinfo "
                    + "SET "
                    + "Antall_personer='" + ant_persint + "', Sivilstatus='" + sivilstatusstring + "', Yrke='" + yrkestring + "'"
                    + ",Røyker='" + røykerint + "', Husdyr='" + husdyrint + "' "
                    + "WHERE Boligsøker_Bruker_Personnummer=" + PersNr + ";";
                Data_Boliger.execUpdate(sql);

                sql = "UPDATE søkerkrav "
                        + "SET "
                        + "Min_Areal='" + minarealint + "',Max_Areal='" + maxarealint + "',Min_Soverom='" + antsoveromint + "',"
                        + "Min_Byggår='" + byggårint + "',Min_Pris='" + minprisint + "',Max_Pris='" + maxprisint + "',Peis='" + peisint + "',Parkering='" + parkeringint + "' "
                        + "WHERE Boligsøker_Bruker_Personnummer=" + PersNr + ";";

                Data_Boliger.execUpdate(sql);
                }
                else //søkerinfo(og dermed også søkerkrav) har ikke allerede info om det personnummeret
                {
                    sql ="Insert into søkerinfo VALUES("+personnummer+","+ant_persint+",'"+sivilstatusstring+"',"
                            +røykerint+","+husdyrint+");";
                    Data_Boliger.execUpdate(sql);
                    
                    sql="insert into søkerkrav VALUES("+personnummer+","+minarealint+","+maxarealint+","+antsoveromint+","
                            +byggårint+","+minprisint+","+maxprisint+","+peisint+","+parkeringint+");";
                    Data_Boliger.execUpdate(sql);
                }
                
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try //oppdaterer bruker.
        {
            sql = "UPDATE bruker "
                    + "SET Navn='" + navn + "',Adresse='" + adresse + "',Email='" + email + "',Telefon='" + tlf + "' "
                    + "WHERE Personnummer =" + PersNr + ";";
        
            Data_Boliger.execUpdate(sql);
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            if (erutleier) //Hvis bruker er utleier så må utleierdata lagres. 
                //Trenger ikke å sjekke for tilstedeværelse av om data er representert fra før i database, da firma lagres sammen med utleier
            {
                sql = "UPDATE utleier "
                        + "SET Firma='"+firmastring+"' "
                        + "Where Bruker_Personnummer="+PersNr+";";
                Data_Boliger.execUpdate(sql);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Hjelpemetode.
     * <p>
     * Konstruerer en mysql tilpasset string basert på card1s søkefelt.
     * <p>
     * Stringen blir så brukt i et kall på database klassen sin execQuery. 
     * @author Petter S.W Gjerstad
     */
    private ResultSet querykonstruktør() //Konstruerer en query når man trykker søk.
    {
        
        String PersNrT = Personnummer.getText();
        String NavnT = Navn.getText();
        String AdresseT = Adresse.getText();
        String EmailT = Email.getText();
        String TelefonT = Telefon.getText();
        
        String and = "and";
        String where = "where ";
        Boolean blank = true;
        
        String[] felt = new String[5];
        
        if (!PersNrT.equals(""))
        {
            where += "Personnummer='"+PersNrT+"'";
            blank = false;
        }
        if (!NavnT.equals(""))
        {
            if (blank)
            {
                where +="Navn='"+NavnT+"'";
                blank = false;
            }
            else where +="AND Navn='"+NavnT+"'";
        }
        if (!AdresseT.equals(""))
        {
            if (blank)
            {
                where +="Adresse='"+AdresseT+"'";
                blank = false;
            }
            else where +="AND Adresse='"+AdresseT+"'";
        }
        if (!EmailT.equals(""))
        {
            if (blank)
            {
                where +="Email='"+EmailT+"'";
                blank = false;
            }
            else where +="AND Email='"+EmailT+"'";
        }
        if (!TelefonT.equals(""))
        {
            if (blank)
            {
                where +="Telefon='"+TelefonT+"'";
                blank = false;
            }
            else where +="AND Telefon='"+TelefonT+"'";
        }
        if (blank) where ="where bruker.Personnummer not in(select * From kundebehandler)";
        else where +="AND bruker.Personnummer not in (select * from kundebehandler)";
        
        String Query = "select * from bruker "+where+" order by ABS(Personnummer)"+ ";";
        try
        {
            return Data_Boliger.execQuery(Query);
        } catch (SQLException ex)
        {
            Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
