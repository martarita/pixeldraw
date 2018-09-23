import javax.swing.JFrame;

public class BubleDrawGUI extends JFrame {

	public static void main(String[] args) {
		JFrame frame = new JFrame( "Okienko z bąbelkamiGUI"); // nadanie tytułu apki
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // zamknięcie okna powoduje konczenie działania apki
		frame.getContentPane().add(new BubblePanel()); //otwarcie nowej kanwy rysunku
		frame.setSize( new java.awt.Dimension(600, 400));
		frame.setVisible(true);
	}

}
