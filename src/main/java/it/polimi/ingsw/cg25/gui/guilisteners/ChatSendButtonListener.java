package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.gui.ChatPanel;
/**
 * 
 * @author nicolo
 *
 */
public class ChatSendButtonListener implements ActionListener {

	/**
	 * This attribute is true if the listener has been registered on
	 * the sendTo button
	 */
	private final boolean sendTo;
	/**
	 * A reference to the chat panel
	 */
	private final ChatPanel chat;
	
	/**
	 * This is the class constructor
	 * @param chat a reference to the chat panel
	 * @param sendTo boolean argument to identify the sentTo button
	 */
	public ChatSendButtonListener(ChatPanel chat, boolean sendTo) {
		this.sendTo = sendTo;
		this.chat = chat;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String destination = "";
		if(sendTo) {
			destination = JOptionPane.showInputDialog(null, 
					"Type the chat username of the player you want\nto send your message to:", "Pm", JOptionPane.PLAIN_MESSAGE);
			if(destination == null)
				destination = "";
		}

		if(chat.getInputBox().getText() != null && !chat.getInputBox().getText().equals("")) {
			Interaction inter = new DisplayInteraction<String>((sendTo ? 
				chat.getInputBox().getText().concat("@@").concat(destination) : chat.getInputBox().getText()));
			
			if(sendTo)
				chat.getGui().getGuiClient().notifyObservers(new VectorPacket<Interaction>(inter, "Pm", 0, 0));
			else
				chat.getGui().getGuiClient().notifyObservers(new VectorPacket<Interaction>(inter, "Chat", 0, 0));
		}
		//Clear the input box
		chat.getInputBox().setText("");
	}

}
