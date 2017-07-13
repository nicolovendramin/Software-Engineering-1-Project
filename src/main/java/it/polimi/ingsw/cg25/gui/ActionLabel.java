package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import it.polimi.ingsw.cg25.model.dto.DTOAction;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class ActionLabel extends JLabel{

	/**
	 * The fixed width of the action label
	 */
	public static final int width = 150;
	/**
	 * The fixed height of the action label
	 */
	public static final int height = 48;
	
	/**
	 * Constructor for the Action Label
	 * @param a is the actoin to represent as Label
	 * @param font the font to write the name of the action
	 */
	public ActionLabel(DTOAction a,Font font){
		this.setBounds(0, 0, width, height);
		String path = "";
		if(a.getAction().equals("Influence the Kings council and move the King to the city where you want to build paying 2 coins for every link used"))
			path = "buildKing.png";
		else if(a.getAction().equals("Use one of the Permits you have in your hand to build an Emporium"))
			path = "buildEmporium.png";
		else if(a.getAction().equals("Send one assistant to change the face-up permit cards of one of the regions"))
			path = "changePermit.png";
		else if(a.getAction().equals("Elect a councelor in one of the councils and get 4 coins"))
			path = "electCouncelor.png";
		else if(a.getAction().equals("Influence a Council to draw one of the face-up permits of its Region"))
			path = "buyPermit.png";
		else if(a.getAction().equals("Send 3 assistants to get an additional Main Action for the current turn"))
			path = "buyMainAction.png";
		else if(a.getAction().equals("Pay 3 coins to hire an assistant"))
			path = "buyAssistant.png";
		else if(a.getAction().equals("Pay an assistant to elect a councelor in one of the councils"))
			path = "assistantElect.png";
		else 
			path = "";
		if(path.equals(""))
		{
			this.setText(a.getAction());
			this.setFont(new Font(font.getFontName(),Font.PLAIN,13));
			this.setHorizontalAlignment(CENTER);
			this.setBounds(this.getX(), this.getY(), width, height);
		}
		else{
			Image img;
		try {
			img = ImageIO.read(new File("src/main/resources/".concat(path)));
			Image resizedImg = img.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
			this.setIcon(new ImageIcon(resizedImg));
		} catch (IOException e) {
			this.setText(a.getAction());
			this.setFont(new Font(font.getFontName(),Font.PLAIN,13));
			this.setHorizontalAlignment(CENTER);
			this.setBounds(this.getX(), this.getY(), width, height);
		}
		}
		this.setToolTipText(a.getAction());
		this.setBackground(new Color(123,123,123));
		this.setOpaque(true);
	}
	
}
