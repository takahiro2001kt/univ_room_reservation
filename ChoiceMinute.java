package client_system;

import java.awt.*;

public class ChoiceMinute extends Choice  {
	public ChoiceMinute() {  //15刻みで予約を可能とする
		add("00");
		add("15");
		add("30");
		add("45");
	}
}