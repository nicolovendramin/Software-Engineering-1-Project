package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class ConnectionChoser extends JDialog {

	/**
	 * The panel with the 2 radio buttons
	 */
	private final JPanel rmiSocketPanel;
	/**
	 * the list of radio buttons
	 */
	private final List<JRadioButton> radios = new ArrayList<>();
	/**
	 * The gui that has opened this Dialog
	 */
	private final CoFGui gui;
	/**
	 * The port number selected
	 */
	private int portNumber;
	/**
	 * The label displaying the currently selected ip
	 */
	private JLabel ip;
	/**
	 * The ip inserted by the user
	 */
	private String ipAddress;
	/**
	 * The connection type in string
	 */
	private String connType;
	/**
	 * The JLabel displaying the port number that is currently selected
	 */
	private JLabel port;
	
	/**
	 * Constructor
	 * @param gui is the gui that has called this JDialog
	 */
	public ConnectionChoser(CoFGui gui){
		this.gui = gui;
		this.portNumber = gui.getPort();
		this.ipAddress = gui.getIp();
		this.connType = gui.getConnectionType();
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(null);
		this.setModal(true);
		this.setTitle("Connection Settings");
		this.getContentPane().setLayout(null);
		this.setBounds(100, 100, 240, 195);
		this.setResizable(false);
		this.rmiSocketPanel = new JPanel();
		rmiSocketPanel.setLayout(new BoxLayout(rmiSocketPanel, BoxLayout.LINE_AXIS));
		this.rmiSocketPanel.setBounds(5, 5, 225, 60);
		JRadioButton socket = new JRadioButton();
		radios.add(socket);
		socket.setText("Socket");
		socket.setSelected(!gui.getSocketRmi());
		socket.addActionListener(new radioActionListener(socket));
		JPanel socketPanel = new JPanel();
		socketPanel.add(socket);
		rmiSocketPanel.add(socketPanel);
		JRadioButton rmi = new JRadioButton();
		radios.add(rmi);
		rmi.setText("Rmi");
		rmi.setSelected(gui.getSocketRmi());
		rmi.addActionListener(new radioActionListener(rmi));
		JPanel rmiPanel = new JPanel();
		rmiPanel.add(rmi);
		rmiSocketPanel.add(rmiPanel);
		LineBorder line = new LineBorder(Color.BLACK, 1);
		TitledBorder border = new TitledBorder(line, "Connection Parameters");
		rmiSocketPanel.setBorder(border);
		this.add(rmiSocketPanel);
		ip = new JLabel();
		ip.setText("IP address:  "+ (gui.getIp()==null ? "not set" : gui.getIp()));
		port = new JLabel("Port Number: "+ (gui.getPort()==0 ? "not set" : Integer.toString(gui.getPort())));
		ip.setBounds(10,70, 135, 20);
		port.setBounds(10,95,135,20);
		JButton portButton = new JButton();
		JButton ipButton = new JButton();
		portButton.setText("Set Port");
		ipButton.setText("Set IP");
		portButton.addActionListener(new changeActionListener("Insert the PORT number:"));
		ipButton.addActionListener(new changeActionListener("Insert the Ip address:"));
		ipButton.setBounds(155, 70, 75, 20);
		portButton.setBounds(155, 95, 75, 20);
		JButton closeAndSave = new JButton();
		closeAndSave.setText("Save");
		closeAndSave.addActionListener(new saveActionListener(this));
		closeAndSave.setBounds(155, 135, 75, 20);
		this.add(closeAndSave);
		this.add(ipButton);
		this.add(portButton);
		this.add(ip);
		this.add(port);
		closeAndSave.getRootPane().setDefaultButton(closeAndSave);
	}
	
	/**
	 * Updates the graphical elements to make them consistents to the latest choices of the user
	 */
	private void refresh(){
		port.setText("Port Number: "+ (portNumber==0 ? "not set" : Integer.toString(portNumber)));
		ip.setText("IP address:  "+ (ipAddress==null ? "not set" : ipAddress));
	}
	
	/**
	 * 
	 * @author nicolo
	 *
	 */
	private class radioActionListener implements ActionListener{
		/**
		 * The String that represent the "meaning" of the radio button
		 */
		String selector;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for(JRadioButton button : radios)
			{
				if(!selector.equals(button.getText()))
					button.setSelected(false);
				else
					button.setSelected(true);
			}
			connType = selector;
		}
		
		private radioActionListener(JRadioButton radio){
			if(radio.getText()==null)
				selector = "empty";
			else 
				selector = radio.getText();
		}
	}
	
	/**
	 * 
	 * @author nicolo
	 *
	 */
	private class saveActionListener implements ActionListener{
		
		ConnectionChoser choser;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(gui.getIp() != ipAddress)
				gui.setIp(ipAddress);
			if(gui.getPort() != portNumber)
				gui.setPort(portNumber);
			if(gui.getConnectionType()!=connType)
				gui.setConnectionType(connType);
			if(gui.getConnectionType()==null)
				gui.setConnectionType("Socket");
			choser.setVisible(false);
		}
		
		private saveActionListener(ConnectionChoser choser){
			this.choser = choser;
		}
	}
	
	/**
	 * 
	 * @author nicolo
	 *
	 */
	private class changeActionListener implements ActionListener{
		
		String selector;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String s = JOptionPane.showInputDialog(null, selector, "Connection Customization", JOptionPane.PLAIN_MESSAGE); 
			try{
			if(selector.equals("Insert the PORT number:"))
			{
				try{
					portNumber = Integer.parseInt(s);
					JOptionPane.showMessageDialog(null,"Port number correctly updated to ".concat(s).concat("   \nClick -save- to update"),"Well Done",JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception e2){
					throw new IllegalArgumentException(s.concat(" is not a valid port number"));
				}
				
			}
			if(selector.equals("Insert the Ip address:"))
			{
				String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
			   if(s.matches(PATTERN))
			   {
				   ipAddress = s;
				   JOptionPane.showMessageDialog(null,"IP address correctly updated to ".concat(s).concat("   \nClick -save- to update"),"Well Done",JOptionPane.INFORMATION_MESSAGE);
			   }
			   else
				   throw new IllegalArgumentException(s.concat(" is not a valid ip"));
			}
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null,ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);
			}
			refresh();
		}
		
		private changeActionListener(String selector){
			if(selector==null)
				this.selector = "empty";
			else 
				this.selector = selector;
		}
	}

}
