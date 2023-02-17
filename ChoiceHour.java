package client_system;

import java.awt.*;

public class ChoiceHour extends Choice {
	ChoiceHour(){
		resetRange(9,21); //時刻の範囲を9～21とする
	}

	//指定できる時刻の範囲を設定
	public void resetRange(int start, int end) {
		removeAll();                              //選択ボックスの内容をクリア　
		while (start <= end) {                    //選択可能範囲の時刻を設定
			String h = String.valueOf(start);
			if (h.length() == 1) {
				h = "0" + h;
			}
			add(h);                               //選択」ボックスに文字列を追加
			start++;
		}
	}
	
	public String getFirst() {
		return getItem(0);
	}
	
	public String getLast() {
		return getItem(getItemCount() - 1);
	}
}