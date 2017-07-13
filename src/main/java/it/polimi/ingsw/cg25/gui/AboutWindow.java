package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class AboutWindow extends JFrame {

	/**
	 * The icon of the window
	 */
	private ImageIcon frameIcon;
	/**
	 * The background image
	 */
	private Image paperVector;
	/**
	 * The font to be used for any writing
	 */
	private Font font;
	
	/**
	 * AboutWindow class constructor.
	 * This class extends JFrame
	 */
	public AboutWindow(Font font) {
		super("About");
		this.font = font;
		startWindow();
	}
	
	/**
	 * Initializes the new window
	 */
	private void startWindow() {
		setSize(400, 200);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frameIcon = new ImageIcon("src/main/resources/nobIcon.png");
		this.setIconImage(frameIcon.getImage());
		
		try {
			paperVector = ImageIO.read(new File("src/main/resources/Old_paper1.png"));
		} catch (IOException e) {
			System.out.println("An error occured while loading the background image of About Window!");
			e.printStackTrace();
		}
		
		Image background = paperVector.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
		
		String labelStr = createInfoString();
		JLabel label = new JLabel();
		label.setFont(new Font(font.getName(), Font.PLAIN, 15));
		label.setIcon(new ImageIcon(background));
		label.setText(labelStr);
		label.setHorizontalTextPosition(JLabel.CENTER);
		add(label);
	}
	
	/**
	 * This method uses html tags in order to create a custom string
	 * for the label in startWindow method
	 * @return a custom string with game information
	 */
	private String createInfoString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>THE COUNCIL OF FOUR<br>");
		sb.append("<br>");
		sb.append("A game by Cranio Creations<br>");
		sb.append("<br>");
		sb.append("Developers:<br>");
		sb.append("Davide Piantella - Giovanni Scotti - Nicolò Vendramin</html>");
		
		return sb.toString();
	}

}
