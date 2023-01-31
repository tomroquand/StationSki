package ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import solveur.*;
import parseur.*;


/**
 * Prototype d'interface graphique de l'application EasySKi
 * - charge une image du plan de la station .jpg en parametre du main)
 * - lui superpose une grille (X,Y) en vue du reperage de lieux de la station par leurs coordonnees pour en faire un
 * plan "cliquable"
 * - ou permet de saisir un lieu directement par son nom.
 * Ce prototype ne fait qu'afficher les coordonnees cliquees ou le nom d'un lieu saisi dans une
 * zone de texte.
 * 
 * Usage : java ihm.EasySkiProto <plan jpg>
 * 
 * @author Bernard.Carre -at- polytech-lille.fr
 * 
 */

@SuppressWarnings("serial")
public class EasySki extends JFrame 
{

	/**
	 * Precision de la grille de reperage.
	 * 
	 */
	protected static final int DELTA = 20;
	/**
	 * Taille du plan.
	 */
	protected int hauteurPlan, largeurPlan;

	/**
	 * ImageCanvas scrollable
	 * NE PAS MODIFIER
	 * Plan "cliquable" = image + grille.	 * 
	 */
	protected ImageCanvas canvas = new ImageCanvas();
	/**
	 * Vue "scrollable" du plan.
	 */
	protected ScrollPane planView = new ScrollPane();
	
	/**
	 * InteractionPanel
	 * Panel d'interaction avec l'utilisateur
	 */	
	protected InteractionPanel interact = new InteractionPanel();

	/**
	 * Charge une image de plan et construit l'interface graphique.
	 * 
	 * @param fichierImage
	 *            Nom du fichier image de plan
	 * @throws java.io.IOException
	 *             Erreur d'acces au fichier
	 */
	public EasySki(String fichierImage) throws java.io.IOException,PathNonExistant
	{

		// Chargement de l'image
		Image im = new ImageIcon(fichierImage).getImage();
		hauteurPlan = im.getHeight(this);
		largeurPlan = im.getWidth(this);

		// Preparation de la vue scrollable de l'image
		canvas.setImage(im); // image a afficher dans le Canvas
		canvas.addMouseListener(interact.getSelectionPanel()); // notification de clic sur la grille
		planView.setSize(hauteurPlan + DELTA, largeurPlan + DELTA);
		planView.add(canvas); // apposition de la vue scrollable sur l'ImageCanvas

		// Construction de l'ensemble
		setLayout(new BorderLayout());
		add(planView, BorderLayout.CENTER);
		add(interact, BorderLayout.SOUTH);

		// Evenement de fermeture de la fenetre : quitter l'application
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent evt) 
			{
				System.exit(0);
			}
		});
	}

	/**
	 * 
	 * Classe utilitaire interne (sous-classe de java.awt.Canvas = plan (jpg) + grille de coordonnees cliquable
	 * NE PAS MODIFIER
	 */
	class ImageCanvas extends Canvas 
	{
		Image image;

		void setImage(Image img) 
		{
			image = img;
			setSize(largeurPlan, hauteurPlan);
			repaint();
		}

		/**
		 * Affiche l'image + la grille.
		 * 
		 */
		public void paint(Graphics g) 
		{
			if (image != null)
				g.drawImage(image, DELTA, DELTA, this);

			// Grille de repérage apposée
			int lignes = hauteurPlan / DELTA;
			int colonnes = largeurPlan / DELTA;
			g.setColor(Color.gray);
			for (int i = 1; i <= lignes; i++) 
			{
				g.drawString("" + i, 0, (i + 1) * DELTA);
				g.drawLine(DELTA, i * DELTA, DELTA + largeurPlan, i * DELTA);
			}
			g.drawLine(DELTA, (lignes + 1) * DELTA, DELTA + largeurPlan, (lignes + 1) * DELTA);
			for (int i = 1; i <= colonnes; i++) 
			{
				g.drawString("" + i, i * DELTA, DELTA / 2);
				g.drawLine(i * DELTA, DELTA, i * DELTA, DELTA + hauteurPlan);
			}
			g.drawLine((colonnes + 1) * DELTA, DELTA, (colonnes + 1) * DELTA, DELTA + hauteurPlan);
		}
	}

	/**
	 * InteractionPanel
	 * Panel d'interaction avec l'utilisateur composee :
	 * - d'un panel de selection utilisateur "SelectionSubPanel"
	 * - une zone de texte "resultTextArea" pour afficher les resultats 
	 * 
	 */
	class InteractionPanel extends JPanel
	{
 		SelectionSubPanel selectionPanel = new SelectionSubPanel();
		JTextArea resultTextArea = new JTextArea(5, 30);

		InteractionPanel()
		{
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(selectionPanel);
			resultTextArea.setEditable(false);
			add(resultTextArea);
		}

		/**
		 * SelectionSubPanel
		 * Sous-panel de selection utilisateur :
		 * - a l'ecoute (implements MouseListener) de clics utilisateur sur le plan:
		 * enregistre les coordonnees (x,y) et les affiche dans les label "X=...", "Y=..."
		 * - permet de saisir un nom de lieu dans le JTextField "lieu"
		 * - bouton : "GO!" : reporte les saisies dans la zone "resultTextArea"		 * 
		 */
		class SelectionSubPanel extends JPanel implements MouseListener
		{
			
			
			int x, y;
			StatParser P;
			Station S;
			Dijkstra Dij;
			List<Point> Lpoint = new ArrayList<Point>();
			List<PointCoord> LpointCoord = new ArrayList<PointCoord>();
			
			
			/**
			 * Affichage de X,Y cliquees
			 */
			JLabel xLabel = new JLabel("X");
			JTextField xCoord = new JTextField(40); 
			
			/**
			 * Affichage de X,Y cliquees
			 */
			JLabel yLabel = new JLabel("Y");
			JTextField yCoord = new JTextField(40); 
			
			/**
			 * Champ de saisie d'un nom de lieu
			 */
			JTextField lieu = new JTextField(20);
			
			/**
			 * Reporte les saisies dans la zone "resultTextArea"
			 */
			
			JButton setDepart = new JButton("SET DEPART");
			JTextField departure = new JTextField(40); 
			JButton setArrivee = new JButton("SET ARRIVEE");
			JTextField arrival = new JTextField(40); 
			JCheckBox tpsReel = new JCheckBox("TEMPS REEL");
			
			/**
			 * Reporte les saisies dans la zone "resultTextArea"
			 */
			JButton go = new JButton("GO!"); // reporte les saisies dans la zone "resultTextArea"

			int time = 0;
			Point Departure = null;
		    Point Arrival = null;
		    
			// Construction et ecouteur du bouton "GO!"
			SelectionSubPanel()
			{
				
				try 
				{
					P = new StatParser();
					Station S = P.parser();
					Lpoint = S.Lpoint;
					for (Point p : Lpoint)           //Creating List of PointCoord
					{
						if( p instanceof PointCoord)
						{
							LpointCoord.add((PointCoord)p);
						}
					}
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(xLabel);
				add(xCoord);
				add(yLabel);
				add(yCoord);
				add(new JLabel("NOM LIEU"));
				add(lieu);
				
				add(setDepart); 
				add(departure);
				add(setArrivee);
				add(arrival);
				add(tpsReel);
				add(go);
				
				
				setDepart.addActionListener(evt ->
				{              
					departure.setText(lieu.getText());          // Adding name of departure point 
					lieu.setText("");                           // Reset other boxes
					xCoord.setText("");
					yCoord.setText("");
					
				});
				
				setArrivee.addActionListener(evt ->
				{
					arrival.setText(lieu.getText());            // Adding name of arrival point
					lieu.setText("");                           // Reset other boxes
					xCoord.setText("");
					yCoord.setText("");
				});
				
				tpsReel.addActionListener(evt ->                // Var time is modified according to the type of time selected
				{ 
					time = 1;
				});
				
				
				go.addActionListener(evt ->
				{
				    
					
					//Departure = LpointCoord.stream().filter(p -> p.x == x && p.y == y).findFirst().orElse(null);
					
					
					Departure = Lpoint.stream().filter(p-> p.name.equals(departure.getText())).findFirst().orElse(null);  //Select the Point corresponding to the name departure
					Arrival = Lpoint.stream().filter(p-> p.name.equals(arrival.getText())).findFirst().orElse(null);      //Select the Point corresponding to the name arrival
					
					
					// Test to ensure the selection of the Point is correct
					
					/*
					resultTextArea.setText("Resultats:\n" + "- vous avez cliqué sur:\n" + xLabel.getText() + "\n"
							+ "d'indice :" + Departure.getID() + "\n" + "- vous avez saisi le lieu:\n" + lieu.getText() + "\n"
									+ "d'indice :" + Arrival.getID());*/
					
					
					try
					{
						P = new StatParser();
						Station S = P.parser();
						Dij = new Dijkstra (S, Departure.getID(),Arrival.getID(),time);
						resultTextArea.setText("Le plus court chemin est:\n" + Dij.toString());  //Display the shortestPath between the two points
						
					} catch (PointInexistantException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					time=0;                      // Reset time to 0 by default
					tpsReel.setSelected(false);  // Reset the tpsReel boxes
					
				});
				
			}

			/**
			 * 
			 * Reactions au clic utilisateur sur la grille
			 * - methode "void mouseReleased(MouseEvent e)" : selection de coordonnees
			 * - autres méthodes sans effet
			 * 
			 */
			public void mouseReleased(MouseEvent e)
			{
				x = e.getX() / DELTA;
				y = e.getY() / DELTA;
				xCoord.setText(String.valueOf(x));    // Add the value of x in x box
				yCoord.setText(String.valueOf(y));    // Add the value of y in y box
				lieu.setText("");
				
				
				for (PointCoord p: LpointCoord)       // Loop on List of PointCoord to search the corresponding name of these coordinates
				{
					if (p.x == x && p.y == y)
					{
						lieu.setText(p.name);         // Add the name of this point in the box
					}
				}

			}

			/**
			 * NOP
			 */
			public void mousePressed(MouseEvent e)
			{
			}

			/**
			 * NOP
			 */
			public void mouseClicked(MouseEvent e)
			{
			}

			/**
			 * NOP
			 */
			public void mouseEntered(MouseEvent e)
			{
			}

			/**
			 * NOP
			 */
			public void mouseExited(MouseEvent e)
			{
			}
		}

		SelectionSubPanel getSelectionPanel()
		{
			return selectionPanel;
		}

	}

	/**
	 * 
	 * Usage : java ihm.EasySkiProto <plan jpg>
	 * 
	 * @param argv[0]
	 *            plan jpg
	 */
	public static void main(String[] argv) throws Exception,PathNonExistant
	{
		if (argv.length != 1)
			System.err.println("usage : java ihm.EasySkiProto <plan jpg>");
		else 
		{
			EasySki window = new EasySki(argv[0]);
			window.setTitle("EasySkiProto");
			window.setSize(600, 640);
			window.setVisible(true);	
		}
	}
}