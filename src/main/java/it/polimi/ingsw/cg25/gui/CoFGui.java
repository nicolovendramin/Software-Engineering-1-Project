package it.polimi.ingsw.cg25.gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.GraphicsEnvironment;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.clients.GuiSimpleClient;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.model.dto.GameStatus;
import it.polimi.ingsw.cg25.gui.guilisteners.AboutAction;
import it.polimi.ingsw.cg25.gui.guilisteners.ConnectionChoserActionListener;
import it.polimi.ingsw.cg25.gui.guilisteners.QuitAction;

/**
 * 
 * @author nicolo
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class CoFGui extends JFrame implements Updatable<GameStatus> {

	/**
	 * The frame width
	 */
	private static final int W_WIDTH = 1240;
	/**
	 * The frame height
	 */
	private static final int W_HEIGHT = 718;
	/**
	 * The space between balconies
	 */
	private static final int BAL_SPACING = 244;
	/**
	 * The client associated with this interface
	 */
	private GuiSimpleClient client;
	/**
	 * The menu of the frame
	 */
	private JMenuBar menuBar;
	/**
	 * The port chosen by the user for the connection
	 */
	private int port;
	/**
	 * The ip chosen by the user for the connection
	 */
	private String ip;
	/**
	 * The connection tyoe chosen by the user for the connection
	 */
	private String connectionType;
	/**
	 * The main font used by the gui
	 */
	private Font font;
	/**
	 * The icon of the window
	 */
	private ImageIcon frameIcon;
	/**
	 * The main content pane
	 */
	private JLayeredPane pane;
	/**
	 * The label that show the notification when there is a pending action interaction
	 */
	private JLabel notificationLabel;
	/**
	 * The background image for the window
	 */
	private Image background;
	/**
	 * The image of the king crown
	 */
	private Image crown;
	/**
	 * The flag to know if the action window is asked to be persistant or not
	 */
	private boolean persistanceFlag = false;
	/**
	 * The label used to cover the whole window content pane when disabled
	 */
	private JLabel siparium;
	/**
	 * The JDialog used to display the choices for the action interaction
	 */
	private final ActionWindow choiceFrame;
	/**
	 * The list of panels that represent the balconies
	 */
	private final List<BalconyPanel> balconies;
	/**
	 * The panel used to display the nobility Track
	 */
	private NobilityTrackPanel nobTrackPanel;
	/**
	 * The panel used to display the informations related to the players 
	 * associated to this client
	 */
	private PlayerPanel player;
	/**
	 * The panel for the informations related to the king
	 */
	private KingRewardPanel kPanel;
	/**
	 * The panel used to display the informations related to the other players
	 */
	private OtherPlayersPanel otherPlayers;
	/**
	 * The panel offering chat functionalities
	 */
	private ChatPanel chatPanel;
	/**
	 * The panel with the map
	 */
	private MapPanel mapPanel;
	/**
	 * The text to display in the JOptionPane for user name change
	 */
	private String changeUsername = "The name you type will be both your player' and chat' username";
	
	/**
	 * CoFGui class constructor
	 * @param client a reference to a gui client
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IOException
	 */
	public CoFGui(GuiSimpleClient client) throws ClassNotFoundException, InstantiationException, 
		IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		this.client = client;
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     try {
		    	 InputStream fontFile = new BufferedInputStream(
		    			 new FileInputStream("src/main/resources/Fonts/GoudyMediaevalDemiBold.ttf"));
		    	 font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
				ge.registerFont(font);
			} catch (IOException | FontFormatException e) {
				font = new Font("SansSerif",Font.BOLD,20);
				System.out.println("Failed while parsing font!");
			}
		balconies = new ArrayList<>();
		choiceFrame = new ActionWindow(this);
		//GUI initialising
		initGUI();
	}
	
	/**
	 * This method is used by the class constructor to 
	 * initialize the main JFrame which hosts the graphics interface
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IOException
	 */
	private void initGUI() throws ClassNotFoundException, InstantiationException, 
		IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		//Setta le dimensioni
		setSize(W_WIDTH, W_HEIGHT);
		//Posiziona la finestra al centro dello schermo
		setLocationRelativeTo(null);
		//Setta il titolo della finestra
		setTitle("The Council of Four");
		//Non ridimensionabile
		setResizable(false);
		//Disconnetti e chiudi quando clicco sulla X
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				client.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>("quit"), "Quit", 0, 0));
				System.exit(0);
			}
		});
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set system LookAndFeel
		setLookAndFeel();
		createMenu();
		//Absolute layout
		this.setLayout(null);
		
		loadResources();
		
		//Set JFrame icon
		this.setIconImage(frameIcon.getImage());
		//Set a background JLayeredPane
		pane = new JLayeredPane();
		pane.setBounds(0, 0, this.getWidth(), this.getHeight());
		setContentPane(pane);
		
		//Add background scaled image
		Image scaledBackground = background.getScaledInstance(pane.getWidth(), pane.getHeight(), Image.SCALE_SMOOTH);
		JLabel back = new JLabel(new ImageIcon(scaledBackground));
		back.setBounds(pane.getX(), pane.getY(), pane.getWidth(), pane.getHeight());
		pane.add(back);
		pane.setLayer(back, 0);
		
		//MAP
		mapPanel = new MapPanel(254, 93, 732, 486, font);
		pane.add(mapPanel);
		pane.setLayer(mapPanel, 1);
		
		//CURRENT PLAYER
		player = new PlayerPanel(5, 5, 244, 327, getFont(), this);
		pane.add(player);
		pane.setLayer(player, 1);
		
		//CHAT
		chatPanel = new ChatPanel(5, 337, 244, 327, this);
		pane.add(chatPanel);
		pane.setLayer(chatPanel, 1);
		
		//OTHER PLAYERS
		otherPlayers = new OtherPlayersPanel(991, 5, 240, 516, font);
		pane.add(otherPlayers);
		pane.setLayer(otherPlayers, 1);
		
		//NOBILITY
		nobTrackPanel = new NobilityTrackPanel(254, 5, 732, 86, font);
		pane.add(nobTrackPanel);
		pane.setLayer(nobTrackPanel, 1);
		
		//BALCONIES
		for(int i = 0; i < 4; i++) {
			BalconyPanel balcony = new BalconyPanel(264 + i*BAL_SPACING, 588, 224, 75);
			balconies.add(balcony);
			pane.add(balcony);
			pane.setLayer(balcony, 1);
		}
		
		//KING LOGO
		Image crownIcon = crown.getScaledInstance(40, 30, Image.SCALE_SMOOTH);
		JLabel kingCrown = new JLabel(new ImageIcon(crownIcon));
		kingCrown.setBounds(0, 0, 40, 30);
		balconies.get(3).add(kingCrown);
		
		//REWARD
		kPanel = new KingRewardPanel(1066, 525, 90, 51,font);
		pane.add(kPanel);
		pane.setLayer(kPanel, 1);
		try {
			notificationLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/notifications.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			notificationLabel.setBounds(215, 215, 20, 20);
			notificationLabel.setVisible(false);
			pane.add(notificationLabel);
			pane.setLayer(notificationLabel, 3);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//SPLASH SCREEN
		try{
			siparium = new JLabel();
			siparium.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/councilOfFour.jpg")).
					getScaledInstance(this.getContentPane().getWidth(), this.getContentPane().getHeight(), Image.SCALE_SMOOTH)));
			siparium.setBounds(0,0,this.getContentPane().getWidth(), this.getContentPane().getHeight());
			pane.add(siparium);
			pane.setLayer(siparium, 50);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.repaint();
	}
	
	/**
	 * Set the look and feel - Windows stile if you're using Windows as OS
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	private void setLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}


	@Override
	public void dispose(){
		new QuitAction().actionPerformed(null);
		super.dispose();
	}

	/**
	 * Creates the frame menu
	 */
	private void createMenu() {
		//Crea una barra menu
		menuBar = new JMenuBar();
		//Crea un menu "Game"
		JMenu gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		
		//Aggiungi sottomenu di Game
		JMenuItem menuStart = new JMenuItem("Start");
		menuStart.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectionType!=null && ip!=null && port!=0)
					if(client.start(port, ip, connectionType))
					{
						menuStart.setEnabled(false);
						gameMenu.getItem(3).setEnabled(true);
					}
				else
					JOptionPane.showMessageDialog(null,"You must setup your connection before  \nstarting the game. \nGo to Settings/Connection Settings.","Error",JOptionPane.ERROR_MESSAGE);
			}
			
		});
		menuStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
		gameMenu.add(menuStart);
		JMenuItem menuDisconnect = new JMenuItem("Disconnect");
		menuDisconnect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				client.disconnect(JOptionPane.showInputDialog(null, "Type your disconnection message:", "Disconnection Message", JOptionPane.PLAIN_MESSAGE));
			}
			
		});
		menuDisconnect.setEnabled(false);
		JMenuItem menuChangeUsername = new JMenuItem("Change Username");
		menuChangeUsername.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String returned = JOptionPane.showInputDialog(null, changeUsername, "Change your Username", JOptionPane.PLAIN_MESSAGE);
				if(returned != null)
					client.changeUsername(returned);
			}
			
		});
		menuChangeUsername.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK));
		gameMenu.add(menuChangeUsername);
		JMenuItem menuConnect = new JMenuItem("Connect");
		menuConnect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				client.connect(JOptionPane.showInputDialog(null, "Type your reconnection message:", "Reconnection Message", JOptionPane.PLAIN_MESSAGE));
			}
			
		});
		menuConnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));
		gameMenu.add(menuConnect);
		menuDisconnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_MASK));
		gameMenu.add(menuDisconnect);
		JMenuItem menuQuit = new JMenuItem("Quit");
		menuQuit.addActionListener(new QuitAction(this.getGuiClient()));
		menuQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));
		gameMenu.add(menuQuit);
		
		//Crea menu "Options"
		JMenu optionsMenu = new JMenu("Settings");
		menuBar.add(optionsMenu);
		JCheckBoxMenuItem persistance = new JCheckBoxMenuItem("Set automatic opening for Action Window");
		persistance.setToolTipText("If this is checked every time an action is received the action panel is automatically opened");
		persistance.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				persistanceFlag = persistance.isSelected();
			}
			
			
		});
		optionsMenu.add(persistance);
		JMenuItem connectionSettings = new JMenuItem("Connection Settings");
		connectionSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_MASK));
		connectionSettings.addActionListener(new ConnectionChoserActionListener(this));
		optionsMenu.add(connectionSettings);
		
		//Crea menu '?'
		JMenu helpMenu = new JMenu("?");
		menuBar.add(helpMenu);
		
		//Aggiungi sottomenu di ?
		JMenuItem menuAbout = new JMenuItem("About");
		menuAbout.addActionListener(new AboutAction(font));
		helpMenu.add(menuAbout);
		this.setJMenuBar(menuBar);
		notificationLabel = new JLabel();
	}
	
	/**
	 * Set the port to establish a connection
	 * @param port the number of the port to set
	 */
	protected void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Set the IP address of the server
	 * @param ip the IP address of the server to connect to
	 */
	protected void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * @return the set port of the connection
	 */
	protected int getPort() {
		return this.port;
	}
	
	/**
	 * @return the set IP to establish the connection
	 */
	protected String getIp() {
		return this.ip;
	}
	
	/**
	 * Set the connection type - Socket or RMI
	 * @param connectionType a string which represents the chosen connection type
	 */
	protected void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * @return the connection type as a string
	 */
	protected String getConnectionType() {
		return this.connectionType;
	}
	
	/**
	 * @return the connection type. True is Rmi
	 */
	protected boolean getSocketRmi() {
		return "Rmi".equals(this.connectionType);
	}
	
	/**
	 * Displays a message in the interface
	 * @param message the text message to be displayed
	 * @param error is a flag to decide if it is an error message or not. True = error message
	 */
	public void displayMessage(String message, boolean error) {
		 if(!error)
			 JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);
		 else 
			 JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Prints the received message in the chat panel of the interface
	 * @param printOptions is the message to display
	 */
	public void chat(String message) {
		this.chatPanel.chat(message);
	}
	
	/**
	 * @return the GuiSimpleClient client related to this object
	 */
	public GuiSimpleClient getGuiClient() {
		return this.client;
	}
	
	/**
	 * Adds or removes the notification icon over the action button 
	 * @param valueToNotify
	 */
	public void notify(boolean valueToNotify)
	{
		this.notificationLabel.setVisible(valueToNotify);
	}
	
	/**
	 * @return the font used in the gui
	 */
	public Font getFont(){
		return this.font;
	}
	
	@Override
	public void update(GameStatus object) {
		if(siparium!=null)
		{
			this.changeUsername = "As the game has already started, \nthe name you type will be your chat's username.\n Your Player's username will remain \nthe same";
			pane.remove(siparium);
			siparium = null;
			pane.repaint();
		}
		
		for(int i = 0; i < balconies.size()-1; i++)
			balconies.get(i).update(object.getBoard().getRegions().get(i).getCouncil());
		balconies.get(balconies.size()-1).update(object.getBoard().getKingCouncil());
		mapPanel.update(object);
		nobTrackPanel.update(object);
		player.update(object.getRequestingPlayer());
		kPanel.update(object.getBoard().getKingReward());
		otherPlayers.update(object);
		
		if(!object.isRunning())
		{
			final StringBuilder c = new StringBuilder("The game is over! Here is the rank:\n");
			object.getHighScorers().stream().forEachOrdered(t -> c.append(t.getName().concat(" : ").concat(Integer.toString(
					t.getPocket().getVictoryPoints()).concat("\n"))));
			JOptionPane.showMessageDialog(null, c.toString(),
					((object.getHighScorers().indexOf(object.getRequestingPlayer())==0 || object.getHighScorers().get(0).
					getPocket().getVictoryPoints()==object.getRequestingPlayer().getPocket().getVictoryPoints()) ? "Winner!" : "Loser"), 
					JOptionPane.INFORMATION_MESSAGE);
		}
		
		this.repaint();
	}
	
	/**
	 * Method used by the constructor to load gui resources
	 * @throws IOException if one or more resources are unreachable
	 */
	private void loadResources() throws IOException {
		frameIcon = new ImageIcon("src/main/resources/nobIcon.png");
		background = ImageIO.read(new File("src/main/resources/Old_paper1.png"));
		crown = ImageIO.read(new File("src/main/resources/crown.png"));
	}

	public void updateAction(Interaction actionInteraction) {
		this.choiceFrame.setActionInteraction(actionInteraction);
		this.notify(true);
		if(this.persistanceFlag)
			this.choiceFrame.display();
	}
	
	/**
	 * @return a reference to the action frame
	 */
	public ActionWindow getChoiceFrame() {
		return this.choiceFrame;
	}
	
	/**
	 * Send to the client the filled action interaction
	 * @param actionInteraction is the filled action interaction
	 */
	public void replyAction(Interaction actionInteraction) {
		this.client.sendAction(actionInteraction);
	}
	
	/**
	 * This method provides the disconnection function
	 * @param message disconnection message will appear while disconnetting
	 */
	public void disconnect(String message){
		this.menuBar.getMenu(0).getItem(3).setEnabled(false);
		if(message!=null && !"".equals(message.replace(" ", "")))
			JOptionPane.showMessageDialog(null, message, "Disconnection", JOptionPane.INFORMATION_MESSAGE);
		this.siparium = new JLabel(){
		    protected void paintComponent(Graphics g)
		    {
		        g.setColor( getBackground() );
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		siparium.setText("<html>You are currently Disconnected.<br>Go to Game/Connect (CTRL + C) to reconnect.</html>");
		siparium.setHorizontalTextPosition(SwingUtilities.CENTER);
		siparium.setVerticalTextPosition(SwingUtilities.CENTER);
		siparium.setFont(new Font(this.getFont().getFontName(),Font.PLAIN,35));
		siparium.setOpaque(false);
		siparium.setBackground( new Color(0, 0, 0, 60) );
	siparium.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "You are currently disconnected",
						"Disconnected", 
						JOptionPane.ERROR_MESSAGE);
				
			}
		});
		siparium.setBounds(0, 0, this.getContentPane().getWidth(), this.getContentPane().getHeight());
		this.pane.add(siparium);
		this.pane.setLayer(siparium, 52);
	}
	
	/**
	 * This method provides connection feature
	 * @param message the connection message
	 */
	public void connect(String message){
		this.menuBar.getMenu(0).getItem(3).setEnabled(true);
		if(message!=null && !"".equals(message.replace(" ", "")))
			JOptionPane.showMessageDialog(null, message, "Connection", JOptionPane.INFORMATION_MESSAGE);
		client.refresh("");
	}
	
}
