package client_system;

public class ReservationSystem {
	public static void main(String argv[]) {
		ReservationControl reservationControl = new ReservationControl();
		MainFrame mainFrame = new MainFrame(reservationControl);
		mainFrame.setBounds(5,5,900,455);
		mainFrame.setVisible(true);
	}
}