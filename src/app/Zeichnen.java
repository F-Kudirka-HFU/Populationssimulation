package app;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import scene.Kreis;
import javax.swing.*;

public class Zeichnen extends JFrame {

	// Berechnung Population
	public Population p;

	// Grafik
	public ArrayList<Kreis> g1; // ArrayListe der Punkte von G
	public ArrayList<Kreis> h1; // ArrayListe der Punkte von H

	// UI
	private static final long serialVersionUID = 1L;
	private BufferedImage backBuffer;

	// Hier werden
	public static JTextField h = new JTextField("300", 8);
	public static JTextField g = new JTextField("3000", 8);
	public static JTextField s = new JTextField("1", 8);
	public static JTextField r = new JTextField("1", 8);
	public static JTextField delay = new JTextField("1", 8);
	public static JLabel labEnd = new JLabel("");
	public static JLabel gRest = new JLabel("");
	public static JLabel hRest = new JLabel("");
	public static JLabel zeit = new JLabel("");
	public static JLabel gfallene = new JLabel("");
	public static JLabel hfallene = new JLabel("");

	boolean drawAtAll = false; // Wird benötigt um zu überprüfen ob start
	// gedrückt wurde ==> sonst geht er immer in
	// draw rein

	Thread tLayout;

	public void setLab(String end, String RestG, String RestH, String t, String gFallene, String hFallene) {
		labEnd.setText("Endzeit: " + end);
		gRest.setText("gRest: " + RestG);
		hRest.setText("hRest: " + RestH);
		if (t.length() >= 3) {
			zeit.setText("T: " + t.substring(0, 3));
		} else {
			zeit.setText("T: " + t);
		}

		gfallene.setText("Gefallene G: "+ gFallene);
		hfallene.setText("Gefallene H: "+ hFallene);
	}



	public Zeichnen() {
		// Hier werden dem Frame Titel, Größe und die Icons zugewiesen
		setTitle("Populationssimulation");
		setSize(Constants.WINDOW_WIDTH + 10, Constants.WINDOW_HEIGHT + 110);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Backbuffer Initialisierung
		backBuffer = new BufferedImage(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

		// Textboxenbeschriftungen werden angelegt
		JLabel lab1 = new JLabel("g");
		JLabel lab2 = new JLabel("h");
		JLabel lab3 = new JLabel("s");
		JLabel lab4 = new JLabel("r");
		JLabel lab5 = new JLabel("delay");
		JLabel lücke = new JLabel("");

		// Button wird angelegt
		JButton start = new JButton("Start");

		start.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
		start.setBackground(Color.ORANGE);

		// Hier wird dem Button ein Eventlistener zugewiesen
		start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				drawAtAll = true;
				if (tLayout != null)
					tLayout.stop();

				Runnable run = new Runnable() {

					public void run() {

						drawAtAll = true;

						try {
							// Befüllt zeichnen
							zeichnen((Integer.parseInt(g.getText())), (Integer.parseInt(h.getText())),
									(Integer.parseInt(s.getText())), (Integer.parseInt(r.getText())),
									(Integer.parseInt(delay.getText())));
						} catch (Exception e) {
							// Gib die Fehlermeldung aus die aufgetreten ist
							System.out.println("Error:" + e.getMessage());
						}
					}
				};

				tLayout = new Thread(run); // Thread wird initialisiert und
				// ausgeführt // und ausgeführt
				tLayout.start();
			}
		});

		// Panel wird erzeugt und mit Farbe, Layout, Labels und Textfeldern
		// befüllt
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 10, 30, 0));
		panel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints c = new GridBagConstraints();
		panel.add(lab1, c);
		panel.add(g, c);
		panel.add(lab2, c);
		panel.add(h, c);
		panel.add(lab3, c);
		panel.add(s, c);
		panel.add(lab4, c);
		panel.add(r, c);
		panel.add(lab5, c);
		panel.add(delay, c);
		panel.add(lücke, c);
		panel.add(start, c);

		// panel wird in Frame eingefügt
		add(panel, BorderLayout.NORTH);

		//zweites Panel für Zeit, G, H und Endzeit
		JPanel panelE = new JPanel();
		panelE.add(labEnd);
		gRest.setForeground(Color.red);
		panelE.add(gRest);
		hRest.setForeground(Color.blue);
		panelE.add(hRest);
		panelE.add(zeit);
		gfallene.setForeground(Color.green);
		panelE.add(gfallene);
		hfallene.setForeground(Color.DARK_GRAY);
		panelE.add(hfallene);
		add(panelE);
		setVisible(true);
	}

	public void zeichnen(double g, double h, double s, double r, int delay) throws Exception {

		p = new Population(g, h, s, r, this, delay);
		this.g1 = new ArrayList<Kreis>();
		this.h1 = new ArrayList<Kreis>();
		erstelleKreise();
		setVisible(true);
		p.init();
	}

	public void erstelleKreise() {

		// Random wird angelegt für die X und Y-Positionen der Kreise
		Random generator = new Random();

		for (int i = 0; i < p.getG(); i++) // For-Schleife generiert Punkte von
		// der Population G
		{
			Kreis k = new Kreis();
			k.diameter = 80;

			// position x von den Kreisen von G
			k.positionX = generator.nextInt((getSize().width - 10) - 20) + 20;

			// posision y von den Kreisen von G
			k.positionY = generator.nextInt((getSize().height - 110) - 20) + 20;

			// Aktuelle Größe vom Frame ==> -110 weil dort erst der Backbuffer
			// beginnt
			k.con_width = getSize().width - 10;
			k.con_height = getSize().height - 110;

			this.g1.add(k);
		}

		for (int i = 0; i < p.getH(); i++) {// For-Schleife generiert Punkte von
			// der Population H
			Kreis k = new Kreis();

			k.diameter = 80;

			// x von den Kreisen von H
			k.positionX = generator.nextInt((getSize().width - 10) - 20) + 20;

			// posision y von den Kreisen von H
			k.positionY = generator.nextInt((getSize().height - 110) - 20) + 20;

			k.con_width = getSize().width - 10;
			k.con_height = getSize().height - 110;

			this.h1.add(k);
		}
	}

	public void loescheKreise(boolean hLoeschen) {

		int loeschenG = (int) (Math.floor(Math.random() * g1.size()));
		int loeschenH = (int) (Math.floor(Math.random() * h1.size()));

		if (g1.size() > 0 && !hLoeschen) {
			g1.remove(loeschenG);
		}
		if (h1.size() > 0 && hLoeschen) {
			h1.remove(loeschenH);
		}
	}

	// Zeichnet automatisch ==> braucht man fürs Fenster skalieren
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		draw();
	}

	public void draw() {

		if (!drawAtAll) // Wenn überhaupt auf Start geklickt wurde zeichne
			// Kreise
			return;

		// Frame wird auf neue Größe skaliert
		Constants.WINDOW_WIDTH = getSize().width - 10;
		Constants.WINDOW_HEIGHT = getSize().height - 110;

		backBuffer = new BufferedImage(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

		Graphics g = getGraphics();
		Graphics bbg = backBuffer.getGraphics();

		// Backbuffer malt Hintergrund Weiß
		bbg.setColor(Color.white);
		bbg.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

		// gibt Farbe für H an
		bbg.setColor(Color.blue);

		// Malt Kreise von H
		for (Kreis zeichnen : h1) {

			float faktorX = ((float) Constants.WINDOW_WIDTH / (float) zeichnen.con_width) * (float) zeichnen.positionX;
			float faktorY = ((float) Constants.WINDOW_HEIGHT / (float) zeichnen.con_height)
					* (float) zeichnen.positionY;

			bbg.fillOval((int) (faktorX), (int) (faktorY), 10, 10);
		}

		// gibt Farbe für G an
		bbg.setColor(Color.red);

		// Malt Kreise von G
		for (Kreis zeichnen : g1) {

			// Fragt ab wie die derzeitige Fenstergröße ist und gibt dem Kreis
			// eine neu skalierte Position proportional zum Window
			float faktorX = ((float) Constants.WINDOW_WIDTH / (float) zeichnen.con_width) * (float) zeichnen.positionX;
			float faktorY = ((float) Constants.WINDOW_HEIGHT / (float) zeichnen.con_height)
					* (float) zeichnen.positionY;

			bbg.fillOval((int) (faktorX), (int) (faktorY), 10, 10);
		}

		// Backbuffer wird g zugewiesen
		g.drawImage(backBuffer, 0, 100, null);
	}

	public static void main(String[] args) {
		new Zeichnen();
	}
}