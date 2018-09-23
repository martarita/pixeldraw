import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.Color;
import java.util.Random;
import java.awt.Graphics; // klasa do grafiki
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent; 



public class BubblePanel extends JPanel {
	Random rand = new Random(); // zeby miec dostep do funkcji z klasy random tworzy sie obiekt
	ArrayList<Bubble> bubbleList; // w <> umieszcza sie typ obiektu w tym przypadku z klasy Bubble, dynamiczna struktura arrayList obiektów Bubble
	int size = 25; //domyslny rozmiar bąbelka
	Timer timer;
	int delay = 33;
	JSlider slider;
	public BubblePanel() {
		timer = new Timer(delay, new BubbleListener()); //timer działa troche jak automatyczny przycisk
		//wyzwala zdarzenie actionPerformerd 30 razy na sekunde-tj tyle co zdef w deley
		bubbleList = new ArrayList<Bubble>();
		setBackground(Color.BLACK);	
		
		JPanel panel = new JPanel();
		add(panel);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource(); //e.getSurce sprawdzamy który pprzyscisk został klikniety
				//i rzutujemy go na typ JButton i przechowujemy w zmiennej btn getSource daje referencje do miejsca skąd pochodzi zdarzenie
				//w tym przypadku getSource daje nam dostep do właściwosci przyscisku - czyli tekstu
				if (btn.getText().equals("Pause"))
				{timer.stop();
				btn.setText("Start");
				}
				else {timer.start();
				btn.setText("Pause");
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("Action Speed");
		panel.add(lblNewLabel);
		
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int speed = slider.getValue()+1; // pobiera wartość z suwaka, +1 by nie dzielić przez 0
			int delay = 1000/speed;
					// na pasku mamy o 0  do 120 co odpowiada  (1/sek) , 0 nie zatrzyma tylko bardzo spowolni
			timer.setDelay(delay);
			}
		});
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(30);
		slider.setMaximum(120);
		panel.add(slider);
		panel.add(btnPause);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bubbleList = new ArrayList<Bubble>(); // aby wyczysccic ekran tworzyymy nowa czysta arrayList			}
		repaint();}
			});
		panel.add(btnClear);
		//testBubbles(); //wywolujemy metode testBubbles z konstruktora BublePanel
		addMouseListener( new BubbleListener()); //bublePanel ma odbierac zdarzenia myszy i przesłyłac je do klasy buubleListener
		addMouseMotionListener( new BubbleListener() ); //bubblePanel ma odbierac przeciagniecia myszą
		addMouseWheelListener(new BubbleListener());
		timer.start();
	}
	public void paintComponent(Graphics canvas)
	{
		super.paintComponent(canvas);//wywołujemy oryginalna metode z klasy JPanel 
		for(Bubble b : bubbleList) //dla każdego b w trukturze bubbleList
		{
			b.draw(canvas); //na kżdym bąbelku b bedzie wywołana metoda draw aby narysowac bąbelek		
		}

	}
	public void testBubbles() {
		for(int n=0; n<100; n++)
		{
			int x = rand.nextInt(600); //okno ma szer 600 wiec współrz losowe nie większe niz
			int y = rand.nextInt(400);
			int size = rand.nextInt(50);
			bubbleList.add(new Bubble(x,y,size)); //tworzymy nowy obiekt Bubble i dodajemy go do bubbleList typu ArrayList


		}
repaint(); //przerysowuje tło i wywołuje paintComponent()

	}

 
private class BubbleListener extends MouseAdapter implements ActionListener
{
	public void mousePressed(MouseEvent e) {// przycusniecie myszy obiekt MouseEvent przechowuje współrzedne x y miejsca w którym nastąpiło kliknięcie
		// będzie mozna je wydobyc getX getY
		bubbleList.add(new Bubble (e.getX(),e.getY(),size));//utworzenie nowego obiektu Bubble (przekazanie danych do konstruktora Bubble)
		//i dodanie nowego obiektu do arrayList
		
		repaint(); //przerysowanie ekranu-zmodyfikowana bubbleList zostaje narysowana
	}
	
	public void mouseDragged(MouseEvent e) {//przeviagniecie myszy z przysnieciem obiekt MouseEvent przechowuje współrzedne x y miejsca w którym nastąpiło kliknięcie
		// będzie mozna je wydobyc getX getY
		bubbleList.add(new Bubble (e.getX(),e.getY(),size));//utworzenie nowego obiektu Bubble (przekazanie danych do konstruktora Bubble)
		//i dodanie nowego obiektu do arrayList
		
		repaint(); //przerysowanie ekranu-zmodyfikowana bubbleList zostaje narysowana
	}
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(System.getProperty("os.name").startsWith("Mac"))//kółko myszy inaczej zachowuje sie w macu a inaczej w win czy linuksie
		size+=e.getUnitsToScroll(); //pobieramy liczbe jednostek przeciagniecia koła i przekazujemy do size
		else
		size-=e.getUnitsToScroll(); 
		
		if (size <3)
		size = 3;
		
		
	}
	public void actionPerformed (ActionEvent e)
	{
		for (Bubble b :bubbleList)
			b.update();
		repaint();
	}
	
	
	
}

class Bubble  // klasy wewn zwykle sa private bo tak bezpieczniej, klasa Bubble bedzie
//dostepna tylko w klasie bubble panel to jest ENKAPSULACJA -UKRYWANIE WNETRZA PRZED INNyMI KLASAMI


{
	private int x;
	private int y;    // argumenty sa prywantne, tylko klasa bubble moze zmienic ich wartosc
	private int size;
	private Color color; //jest klasa Color w bibliotece awt
	private int xspeed, yspeed; // o tyle bedzie przesuwał sie bąbel z każdą aktualizacja ekranu
	private final int MAX_SPEED=5; // stała określająca maks jednorazowe przesunięcie się bąbla
	public Bubble (int newX, int newY, int newSize) //konstruktor/metoda, konstruktor jest public
	{x=(newX/newSize)*newSize+newSize/2; //przypisanie parametrów metody do pól klasy
	y=(newY/newSize)*newSize+newSize/2;
	size = newSize;
	color = new Color (rand.nextInt(256),
			rand.nextInt(256),
			rand.nextInt(256), 
			rand.nextInt(256)); //nextInt 256 - losowa liczba od 0 do 255, bo generuje liczby mniejsze niz max
	xspeed=yspeed=2; // mamy "prdkosci od -5 do 5, każdy bąbel z losową prędkością,
	/*yspeed=rand.nextInt(MAX_SPEED*2+1)-MAX_SPEED;
	if (xspeed==0)
			xspeed=1;
	if (yspeed==0)
		yspeed=1;*/
	
	}
	public void draw(Graphics canvas) {//kanwa Graphics i nazwie canvas
		canvas.setColor(color); //obiekt canvas ustawienie koloru
		canvas.fillRect(x-size/2, y-size/2, size, size); // i kształtu-koło (xy górny lewy rog ramy, szer wys ramy
	}
	public void update() {//przesuwamy bąbelek o 5
	x+=xspeed;
	y+=yspeed;
	if (x-size/2<=0 || x+size/2>= getWidth())
		xspeed=-xspeed;
	if (y-size/2<=0 || y+size/2>= getHeight())
		yspeed=-yspeed;
	
	
	}
}
}



