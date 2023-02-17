package client_system;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

public class MainFrame extends Frame implements ActionListener,WindowListener {
	ReservationControl reservationControl;
	//パネルインスタンスの生成
	Panel panelNorth; //上部パネル
	Panel panelNorthSub1; //上部パネルの上
	Panel panelNorthSub2; //上部ぱねるの下
	Panel panelCenter; //中央パネル
	Panel panelSouth; //下部のパネル
	
	//ボタンインタスタンスの生成
	Button buttonLog; //ログイン・ログアウトボタン
	Button buttonExplanation; //教室概要説明ボタン
	Button buttonReservation; //新規予約を行うボタン
	Button buttonDisplay; //予約状況表示ボタン
	
	Button buttonUserDisplay;//ユーザ予約確認表示ボタン
	Button buttonCancel;//キャンセルボタン
	
	//選択ボックスのインスタンス生成
	ChoiceFacility choiceFacility;
	//テキストフィールドのインスタンス生成
	TextField tfLoginID; //ログインIDを表示するテキストフィールド
	TextField tfYear, tfMonth, tfDay; //年月日を入力するテキストフィールド
	//テキストエリアのインスタンス
	TextArea textMessage;
	
	//LoginIDを代入する変数
	String logID = "";
	List<String> resList;
	//mainFrameのコンストラクタ
	public MainFrame(ReservationControl rc) {
		reservationControl = rc;
		//ボタンの生成
		buttonLog = new Button("ログイン");
		buttonExplanation = new Button("教室概要");
		buttonReservation = new Button("新規予約");
		buttonDisplay = new Button("予約状況表示");
		buttonCancel = new Button("予約キャンセル");
		buttonUserDisplay = new Button("ユーザ予約確認");

		choiceFacility = new ChoiceFacility();
		
		//テキストフィールドの生成
		tfYear = new TextField("", 4);
		tfMonth = new TextField("", 2);
		tfDay = new TextField("", 2);

		//ログインID用表示ボックスの生成
		tfLoginID = new TextField("未ログイン",12);
		tfLoginID.setEditable(false);
		
		//上中下のパネルを使うため、レイヤーマネジャーにborderLoutを設定
		setLayout(new BorderLayout());

		panelNorthSub1 = new Panel();
		panelNorthSub1.add(new Label("教室予約システム　"));
		panelNorthSub1.add(buttonLog);
		panelNorthSub1.add(new Label("        ログインID:"));
		panelNorthSub1.add(tfLoginID);
		//panelNorthSub1.add(buttonTest);

		//上部パネルの中央パネルに教室チョイス及び教室概要ボタンを追加
		panelNorthSub2= new Panel();
		panelNorthSub2.add(buttonDisplay);
		panelNorthSub2.add(new Label("  "));
		panelNorthSub2.add(new Label("教室"));
		panelNorthSub2.add(choiceFacility);
		panelNorthSub2.add(buttonExplanation);
		panelNorthSub2.add(new Label("  "));
		//panelNorthSub2.add(buttonUserDisplay);

		//上部パネルに2つのパネルを追加
		panelNorth = new Panel(new BorderLayout());
		panelNorth.add(panelNorthSub1, BorderLayout.NORTH);
		panelNorth.add(panelNorthSub2, BorderLayout.CENTER);

		//メイン画面（MainFrame）に上部パネルを追加
		add(panelNorth, BorderLayout.NORTH);
		

		//中央パネルにテキストメッセージ欄を設定
		panelCenter = new Panel();
		textMessage = new TextArea(20, 80);
		textMessage.setEditable(false);
		panelCenter.add(textMessage);
		//メイン画面（MainFrame）に中央パネルを追加
		add(panelCenter, BorderLayout.CENTER);
		
		//パネル下部に新規予約ボタンを追加
		panelSouth = new Panel();
		panelSouth.add(buttonReservation);
		panelSouth.add(new Label("  "));
		panelSouth.add(buttonUserDisplay);
		panelSouth.add(new Label("  "));
		panelSouth.add(buttonCancel);
		add(panelSouth, BorderLayout.SOUTH);
		
		//ボタンのアクションリスナを追加
		buttonLog.addActionListener(this);
		buttonExplanation.addActionListener(this);
		buttonReservation.addActionListener(this);
		buttonCancel.addActionListener(this);
		addWindowListener(this);
		buttonDisplay.addActionListener(this);
		buttonUserDisplay.addActionListener(this);
	}


	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}


	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String result = new String();
		if(e.getSource() == buttonExplanation) {
			result = reservationControl.getFacilityExplanation(choiceFacility.getSelectedItem());
		} else if (e.getSource() == buttonReservation){
			result = reservationControl.makeReservation(this);
		} else if (e.getSource() == buttonDisplay){
			//result = reservationControl.showReservation(tfYear.getText()+"-"+tfMonth.getText()+"-"+tfDay.getText());
			result = reservationControl.showReservation(this);
		}else if (e.getSource() == buttonLog) {
			result = reservationControl.loginLogout(this);
		}else if(e.getSource() == buttonCancel) {
			//logIDはreservationControl.logスinLogout()で入力したログインIDが代入されている
			//javaは複数引数を与えられるのでインタンスとｌｏｇIDを引数で渡す
			result = reservationControl.cancelReservation(this,logID);
		}else if(e.getSource() == buttonUserDisplay) {
			resList = reservationControl.outmyreservation(logID);
			for (String str : resList) {
				result += str + "\r\n";
			}
		}
		textMessage.setText(result);
	}
}