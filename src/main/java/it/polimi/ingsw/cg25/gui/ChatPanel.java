package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import it.polimi.ingsw.cg25.gui.guilisteners.ChatSendButtonListener;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class ChatPanel extends JLayeredPane {

	/**
	 * The area where the text is displayed
	 */
	private final JTextArea displayArea = new JTextArea();
	/**
	 * The area where the text to be sent has to be typed
	 */
	private final JTextArea inputBox = new JTextArea(this.getSize().toString());
	/**
	 * The send button for the chat
	 */
	private final JButton send = new JButton("Send");
	/**
	 * The button to send a pm
	 */
	private final JButton sendTo = new JButton("Send to...");
	/**
	 * The gui that has called this Dialog
	 */
	private final CoFGui gui;
	/**
	 * The image used as background
	 */
	private Image paperVector;
	
	/**
	 * ChatPanel class constructor
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param gui a reference to the gui main class
	 */
	public ChatPanel(int x, int y, int w, int h, CoFGui gui) {
		try {
			loadResources();
		} catch (IOException e) {
			System.out.println("Unable to load Chat resources!");
		}
		
		this.setBounds(x, y, w, h);
		this.gui = gui;
		this.setLayout(null);
		
		Image scaledPVector = paperVector.getScaledInstance(w, 240, Image.SCALE_SMOOTH);
		JLabel paperBack = new JLabel(new ImageIcon(scaledPVector));
		paperBack.setBounds(0, 0, paperBack.getIcon().getIconWidth(), paperBack.getIcon().getIconHeight());
		this.add(paperBack);
		this.setLayer(paperBack, 0);
		send.setFont(new Font(gui.getFont().getFontName(),Font.PLAIN,13));
		sendTo.setFont(new Font(gui.getFont().getFontName(),Font.PLAIN,13));
		displayArea.setText("Here is the chat\n");
		displayArea.setEditable(false);
		displayArea.setOpaque(false);
		displayArea.setLineWrap(true);
		displayArea.setAutoscrolls(true);
		displayArea.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 15));
		JScrollPane displayAreaPane = new JScrollPane(displayArea);
		displayAreaPane.setBorder(null);
		displayAreaPane.setVisible(true);
		displayAreaPane.setOpaque(false);
		displayAreaPane.getViewport().setOpaque(false);
		displayAreaPane.setBounds(15, 30, 215, 180);
		this.add(displayAreaPane);
		this.setLayer(displayAreaPane, 1);
		
		inputBox.setText("Type here your message");
		inputBox.setOpaque(false);
		inputBox.setAutoscrolls(true);
		inputBox.setLineWrap(true);
		inputBox.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 15));
		JScrollPane inputBoxPane = new JScrollPane(inputBox);
		inputBoxPane.setVisible(true);
		inputBoxPane.setBounds(15, 250, 215, 40);
		inputBoxPane.setOpaque(false);
		inputBoxPane.getViewport().setOpaque(false);
		inputBoxPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(inputBoxPane);
		this.setLayer(inputBoxPane, 1);
		
		send.setBounds(54, 295, 86, 25);
		send.setText("Send");
		send.addActionListener(new ChatSendButtonListener(this, false));
		send.setMnemonic('q');
		this.add(send);
		this.setLayer(send, 1);
		
		sendTo.setBounds(144, 295, 86, 25);
		sendTo.setText("Send To...");
		sendTo.setMnemonic('w');
		sendTo.addActionListener(new ChatSendButtonListener(this, true));
		this.add(sendTo);
		this.setLayer(sendTo, 1);
	}
	
	/**
	 * This method loads all the graphics resources
	 * @throws IOException if some resources are missing
	 */
	private void loadResources() throws IOException {
		paperVector = ImageIO.read(new File("src/main/resources/paperVectorPL.png"));
	}
	
	/**
	 * This method appends a new chat message in the chat panel
	 * @param message the message to append
	 */
	public void chat(String message) {
		if(message == null)
			return;
		this.displayArea.append(message.concat("\n"));
	}

	/**
	 * @return a reference to the JTextArea input box
	 */
	public JTextArea getInputBox() {
		return inputBox;
	}
	
	/**
	 * @return the gui reference which hosts this chat panel
	 */
	public CoFGui getGui() {
		return gui;
	}

}
