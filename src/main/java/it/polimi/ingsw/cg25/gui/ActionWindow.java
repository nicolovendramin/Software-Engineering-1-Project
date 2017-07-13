package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import it.polimi.ingsw.cg25.actions.AskQuantityInteraction;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.actions.MultipleChoiceInteraction;
import it.polimi.ingsw.cg25.actions.SingleChoiceInteraction;
import it.polimi.ingsw.cg25.model.dto.DTOAction;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.model.dto.DTOCouncelor;
import it.polimi.ingsw.cg25.model.dto.DTOCouncil;
import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.dto.DTORegion;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductBonus;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPermit;
import it.polimi.ingsw.cg25.model.dto.dtoproduct.DTOProductPoliticsCard;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class ActionWindow extends JDialog {

	/**
	 * The set represents the number of elements selected by the user
	 */
	private final Set<Integer> reply = new HashSet<>();
	/**
	 * This containers contains the elements that can be selected
	 */
	private final JLayeredPane choicesBody = new JLayeredPane();
	/**
	 * The list of checkbox, ordered in the same order of the element
	 * to choose.
	 */
	private final List<JCheckBox> checkBoxes = new ArrayList<>();
	/**
	 * The actionInteraction to be filled, if null is because it's not the turn of the player
	 */
	private Interaction actionInteraction = null;
	/**
	 * The gui interface from which this Dialog is opened
	 */
	private CoFGui gui;
	/**
	 * The Label to display the title of the choice
	 */
	private JTextArea title = new JTextArea();
	/**
	 * The scrollPane containing the layeredPane with the choices
	 */
	private JScrollPane choices;
	/**
	 * Constants that define the side distance from the borders
	 */
	private final int sideDistance = 5;
	/**
	 * Constants that define the distance between 2 elements of the choices
	 */
	private final int inBetweenDistance = 10;
	/**
	 * The button to confirm the selection
	 */
	private JButton okButton = new JButton();
	/**
	 * The button to abort the current action
	 */
	private JButton abortButton = new JButton("Abort");
	/**
	 * The graphic representation of the elemment to choose
	 */
	private List<Component> options = new ArrayList<>();
	/**
	 * The upperBound for the number of items to select
	 */
	private int lowerBound;
	/**
	 * The lower Bound for the number of items to select
	 */
	private int upperBound;
	
	/**
	 * 
	 * @author nicolo
	 *
	 */
	private class elementChecker extends JCheckBox {
		
		/**
		 * The Constructor for this inner class
		 * @param value is the number that corresponds to the checkbox. 
		 * given in positional order. i-th checkbox has value i and stands for the
		 * i-th element that can be chosen
		 */
		public elementChecker(int value){
			super("", false);
			//this.value = new Integer(value);
			this.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(!isSelected())
					{	
						if(upperBound>0 && upperBound==reply.size())
							for(JCheckBox c: checkBoxes)
								if(!c.isEnabled())
									c.setEnabled(true);
						reply.remove(value);
					}
					else
					{
						reply.add(value);
						if(upperBound>0 && upperBound==reply.size())
							for(JCheckBox c : checkBoxes)
								if(!c.isSelected())
									c.setEnabled(false);
							
					}
					okButton.setEnabled((reply.size()>=lowerBound || lowerBound<0) && (reply.size()<=upperBound || upperBound<0));
				}
				
			});
		}
		
	}
	
	/**
	 * Constructor for the actionWindow
	 * @param gui is the CoFGui that has opened this window
	 */
	public ActionWindow(CoFGui gui) {
		this.setSize(345, 450);
		this.choices = new JScrollPane(this.choicesBody);
		this.gui = gui;
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		JLayeredPane pane = new JLayeredPane();
		pane.setLayout(null);
		this.setContentPane(pane);
		
		JLabel backGround = new JLabel();
		try {
			backGround.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/Old_paper1.png")).
					getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH)));
			backGround.setBounds(0, 0, backGround.getIcon().getIconWidth(), backGround.getIcon().getIconHeight());
			pane.add(backGround);
			pane.setLayer(backGround, 0);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		this.setTitle("Action Window");
		this.title.setEditable(false);
		this.title.setOpaque(false);
		this.title.setBounds(sideDistance, sideDistance, 330, 35);
		pane.add(title);
		this.okButton.setText("Ok");
		
		//Setting choices JScrollPanel
		this.choices.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.choices.setBounds(sideDistance, 45, 330, 330);
		this.choices.setOpaque(false);
		this.choices.getViewport().setOpaque(false);
		this.choices.setBorder(null);
		this.abortButton.setEnabled(true);
		this.abortButton.setVisible(true);
		this.abortButton.setToolTipText("Aborts the current actions and restart from the choice of available actions");
		this.okButton.setBounds(255, 380, 75, 30);
		this.abortButton.setBounds(170, 380, 75, 30);
		this.choicesBody.setBounds(0, 0, 330, 330);
		pane.add(choices);
		pane.add(abortButton);
		pane.add(okButton);
		okButton.getRootPane().setDefaultButton(okButton);
		pane.setLayer(choices, 10);
		pane.setLayer(title, 10);
		pane.setLayer(okButton, 10);
		pane.setLayer(abortButton, 10);
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					ArrayList<Integer> toArray = new ArrayList<>(reply);
					if((lowerBound>=0 && toArray.size()<lowerBound) || (upperBound>0 && toArray.size()>upperBound))
						throw new IllegalArgumentException("Invalid selection");
					String writtenReply = "";
					writtenReply = Integer.toString(toArray.get(0));
					for(int i=1;i<reply.size();i++){
						writtenReply = writtenReply.concat(",").concat(Integer.toString(toArray.get(i)));
					}
					actionInteraction.registerReply(writtenReply);
					gui.replyAction(actionInteraction);
					actionInteraction = null;
					gui.notify(false);
					setVisible(false);
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		
		abortButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Interaction temp = new DisplayInteraction<String>("[[Abort]]","[[Abort]]");
				gui.replyAction(temp);
				actionInteraction = null;
				gui.notify(false);
				setVisible(false);
			}
			
		});
	}
	
	/**
	 * This function displays the action window basing on the action interaction stored above
	 */
	public void display(){
		this.reply.clear();
		if(this.actionInteraction==null){
			JOptionPane.showMessageDialog(null, "It's not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			if(actionInteraction.getQuestion().equals(""))
				title.setText("  Choose");
			else
				title.setText("  ".concat(actionInteraction.getQuestion()));
			title.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 13));
			title.setForeground(Color.BLACK);
		this.okButton.setEnabled(false);
		for(JCheckBox c :this.checkBoxes)
		{
			c.setSelected(false);
			c.setEnabled(true);
		}
		if(actionInteraction.getClass()==MultipleChoiceInteraction.class)
		{
			this.repaint();
			this.setVisible(true);
		}
		else if(actionInteraction.getClass()==SingleChoiceInteraction.class)
		{
			this.repaint();
			this.setVisible(true);
		}
		else if(actionInteraction.getClass()==AskQuantityInteraction.class)
		{
			try{
				String writtenReply = JOptionPane.showInputDialog(null, actionInteraction.printOptions(), "Insert the quantity", JOptionPane.PLAIN_MESSAGE);
				this.actionInteraction.registerReply(writtenReply);
				this.gui.replyAction(actionInteraction);
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(actionInteraction.getClass()==DisplayInteraction.class)
		{
			try{
				JOptionPane.showMessageDialog(null, actionInteraction.printOptions(), "Error", JOptionPane.ERROR_MESSAGE);
				this.actionInteraction.registerReply("ok");
				this.gui.replyAction(actionInteraction);
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		}
	}	
	
	/**
	 * Setter for the value of the field action interaction
	 * @param actionInteraction
	 */
	public void setActionInteraction(Interaction actionInteraction){
		this.actionInteraction = actionInteraction;
		for(JCheckBox c : checkBoxes)
			this.choicesBody.remove(c);
		this.checkBoxes.clear();
		for(Component l : options)
			this.choicesBody.remove(l);
		this.options.clear();
		if(actionInteraction.getClass()==MultipleChoiceInteraction.class)
		{
			MultipleChoiceInteraction<? extends Object> act = (MultipleChoiceInteraction<?>)actionInteraction;
			this.lowerBound = act.getLowerBound();
			this.upperBound = act.getUpperBound();
			this.panellizer(act.getChoices());
		}
		else if(actionInteraction.getClass()==SingleChoiceInteraction.class)
		{
			SingleChoiceInteraction<? extends Object> act = (SingleChoiceInteraction<?>)actionInteraction;
			this.lowerBound = 1;
			this.upperBound = 1;
			this.panellizer(act.getChoices());
		}
	}

	/**
	 * this method takes all the items of the choices and turns them into components 
	 * @param objects
	 */
	private void panellizer(List<?> objects) {
		for(Object o : objects){
			if(o instanceof DTOAction)
				options.add(new ActionLabel((DTOAction)o,gui.getFont()));
			if(o instanceof String)
			{
				JLabel label = new JLabel((String)o);
				label.setBounds(0,0,150,48);
				options.add(label);
			}
			if(o instanceof DTOCouncelor){
				options.add(new Councelor((DTOCouncelor)o,2));
			}
			if(o instanceof DTOProductBonus){
				options.add(new ProductPane((DTOProductBonus)o,gui.getFont()));
			}
			if(o instanceof DTOProductPoliticsCard){
				options.add(new ProductPane((DTOProductPoliticsCard)o,gui.getFont()));
			}
			if(o instanceof DTOProductPermit){
				options.add(new ProductPane((DTOProductPermit)o,gui.getFont()));
			}
			if(o instanceof DTOCouncil){
				BalconyPanel lab;
				try {
					lab = new BalconyPanel(0,0,150,65,2,((DTOCouncil)o).getLocation(),gui.getFont());
					lab.update((DTOCouncil)o);
					options.add(lab);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(o instanceof DTOCity){
				JLabel lab = new CityLabel(0,0,(DTOCity)o,this.gui.getFont(), Color.BLACK);
				options.add(lab);
			}
			if(o instanceof DTOPoliticsCard){
				JLabel lab = new PoliticsCardLabel((DTOPoliticsCard)o);
				options.add(lab);
			}
			if(o instanceof DTOPermitCard){
				PermitPane lab = new PermitPane(0,0,this.gui.getFont());
				lab.update((DTOPermitCard)o);
				options.add(lab);
			}
			if(o instanceof DTORegion){
				JLabel lab = new JLabel(((DTORegion)o).getName());
				lab.setBounds(0,0,150,48);
				options.add(lab);
			}
		}
		int currentWidth = 5;
		int currentHeight = 5;
		for(int i=0;i<options.size();i++)
		{
			if(currentWidth+options.get(i).getWidth()>choicesBody.getWidth()-5)
			{
				currentWidth = 5;
				currentHeight+=options.get(i).getHeight()+5;
			}
			JCheckBox box = new elementChecker(i);
			options.get(i).setBounds(currentWidth,currentHeight,options.get(i).getWidth(),options.get(i).getHeight());
			box.setBounds(currentWidth+options.get(i).getWidth()-25, currentHeight+options.get(i).getHeight()-25, 20, 20);
			currentWidth+=options.get(i).getWidth()+inBetweenDistance;
			choicesBody.add(options.get(i));
			choicesBody.add(box);
			this.choicesBody.setLayer(options.get(i), 3);
			checkBoxes.add(box);
			this.choicesBody.setLayer(box, 10);
			this.choicesBody.setPreferredSize(new Dimension(330,currentHeight+options.get(0).getHeight()+20));
			choicesBody.repaint();
		}
	}
	
}
