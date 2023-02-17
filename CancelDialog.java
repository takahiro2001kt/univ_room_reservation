package client_system;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Checkbox;
import java.awt.event.ItemListener;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.awt.Checkbox;
import java.awt.event.ItemListener;


//github変更箇所
public class CancelDialog extends Dialog implements ActionListener,WindowListener,ItemListener{
	boolean		canceled;
	boolean     status;
	
	ReservationControl reservationControl =new ReservationControl();

	TextArea	currentReservation;
	TextArea    ErrorMessage;
	TextField   inputID;
	Button		buttonRunCancel;
	Panel		panelCenter;
	Panel		panelCenterSub1;
	Panel		panelCenterSub2;
	Panel		panelSouth;
	Panel		panelSouthSub1;
	Panel		panelSouthSub2;
	Checkbox    cancelBox;
	String      logID;
	List<String> resList;

	
	//ログインダイヤログ2ページの入力開始
	public CancelDialog(Frame cancel, String logid) {
		super(cancel, "予約キャンセル", true);
		canceled = true;
		
		//ボタンインタスタンスの生成
		
		buttonRunCancel = new Button("予約削除を実行");
		
        //チェックボックスの作成
		cancelBox = new Checkbox("チェック有無",null,false);
		
		//テキストフィールドの作成
		currentReservation = new TextArea(20,50);
		ErrorMessage = new TextArea(5,50);
		currentReservation.setEditable(false);
		ErrorMessage.setEditable(false);
		
		//テキストフィール
		inputID = new TextField("",5);
		
		logID = logid;
		
		
		setLayout(new BorderLayout());
		
		panelCenterSub1 = new Panel();
		panelCenterSub1.add(currentReservation);
		
		panelCenterSub2 = new Panel();
		panelCenterSub2.add(ErrorMessage);
		ErrorMessage.setText("削除したいIDとチェックボックスに入力後,\nキャンセル実行ボタンを押してください.");
		
		panelSouthSub1 = new Panel();
		panelSouthSub1.add(new Label("キャンセルしたいID　："));
		panelSouthSub1.add(inputID);
		
		panelSouthSub2 = new Panel();
		panelSouthSub2.add(cancelBox);
		panelSouthSub2.add(buttonRunCancel);
		
		panelCenter = new Panel(new BorderLayout());
		panelCenter.add(panelCenterSub1, BorderLayout.NORTH);
		panelCenter.add(panelCenterSub2, BorderLayout.CENTER);
		
		panelSouth = new Panel(new BorderLayout());
		panelSouth.add(panelSouthSub1, BorderLayout.CENTER);
		panelSouth.add(panelSouthSub2, BorderLayout.SOUTH);
		
		
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);
		
		//ボタンのアクションリスナを追加
		buttonRunCancel.addActionListener(this);
		cancelBox.addItemListener(this);
		

		//Window Listenerを追加
		addWindowListener(this);


		
	}

		@Override
		public void windowOpened(WindowEvent e) {
			//TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			canceled = true;
			
			dispose();
		}

		@Override
		public void windowClosed(WindowEvent e) { //window closeの動作
			//TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void windowIconified(WindowEvent e) {
			//TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			//TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void windowActivated(WindowEvent e) {
			//TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			//TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String result = "";
			String result1 = "";
			
			if (e.getSource() == buttonRunCancel){
				String reservationid = inputID.getText();
				result = reservationControl.cancel(reservationid,status,logID);
				resList = reservationControl.outmyreservation(logID);
				for (String str : resList) {
					result1 += str + "\r\n";
				}
				ErrorMessage.setText(result);
			}
			currentReservation.setText(result1);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			status = cancelBox.getState();
			
		}
}