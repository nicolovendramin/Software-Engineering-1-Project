package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProduct;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductBonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPermit;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOSellable;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class ProductPane extends JPanel{

	/**
	 * This container stores the element to display
	 */
	private JLayeredPane myLayeredPane = new JLayeredPane();
	/**
	 * The label containing the price and the image of the coin
	 */
	private JLabel priceLabel = new JLabel();
	/**
	 * The label in which is shown the name of the seller
	 */
	private JLabel ownerLabel = new JLabel();
	/**
	 * The Jpanel in which are inserted the elements contained in the product
	 */
	private JPanel cardPanel = new JPanel();
	
	/**
	 * Constructor
	 * @param product is the product that the ProductPane should represent in graphics
	 * @param font is the font to be used for all the texts
	 */
	public ProductPane(DTOProduct<? extends DTOSellable> product,Font font){
		this.setBounds(0,0,145,100);
		this.setLayout(null);
		this.add(myLayeredPane);
		myLayeredPane.setLayout(null);;
		myLayeredPane.setBounds(0,0,145,100);
		ownerLabel.setText("Sold by : ".concat(product.getOwner().getName()));
		ownerLabel.setFont(new Font(font.getFontName(),Font.BOLD,15));
		ownerLabel.setOpaque(false);
		ownerLabel.setBounds(10,70,145,20);
		ownerLabel.setOpaque(false);
		priceLabel.setOpaque(false);
		this.myLayeredPane.add(ownerLabel);
		this.setOpaque(false);
		myLayeredPane.setOpaque(false);
		myLayeredPane.add(priceLabel);
		priceLabel.setText(Integer.toString(product.getPrice()));
		priceLabel.setForeground(Color.BLACK);
		priceLabel.setHorizontalTextPosition(SwingUtilities.CENTER);
		priceLabel.setVerticalTextPosition(SwingUtilities.TOP);
		priceLabel.setToolTipText(product.toString());
		priceLabel.setFont(new Font(font.getFontName(),Font.BOLD,15));
		priceLabel.setBounds(115, 5, 30, 55);
		try {
			priceLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/coinIcon.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cardPanel.setLayout(null);
		cardPanel.setOpaque(false);
		cardPanel.setBounds(0, 0, 50, 50);
		JScrollPane scroll = new JScrollPane(cardPanel);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(0, 0, 110, 70);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		this.myLayeredPane.add(scroll);
		if(product instanceof DTOProductBonus){
			DTOProductBonus bonusProd = (DTOProductBonus)product;
			cardPanel.add(new BonusPanel(bonusProd.getItemsToSell().get(0),font));
		}
		if(product instanceof DTOProductPoliticsCard){
			DTOProductPoliticsCard bonusProd = (DTOProductPoliticsCard)product;
			int offset = 0;
			for(DTOPoliticsCard c : bonusProd.getItemsToSell()){
				PoliticsCardLabel label = new PoliticsCardLabel(c, 30, 42);
				cardPanel.add(label);
				label.setBounds(offset,3,label.getWidth(),label.getHeight());
				offset= offset + label.getWidth()+5;
				cardPanel.setPreferredSize(new Dimension(offset,cardPanel.getHeight()));
			}
		}
		if(product instanceof DTOProductPermit){
			DTOProductPermit bonusProd = (DTOProductPermit)product;
			int offset = 0;
			for(DTOPermitCard c : bonusProd.getItemsToSell()){
				PermitPane label = new PermitPane(font,c);
				cardPanel.add(label);
				label.setBounds(offset,3,label.getWidth(),label.getHeight());
				offset= offset + label.getWidth()+5;
				cardPanel.setPreferredSize(new Dimension(offset,cardPanel.getHeight()));
			}
		}
	}

}
