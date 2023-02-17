package client_system;

import java.awt.*;
import java.awt.event.*;

public class ReservationDialog extends Dialog implements ActionListener, WindowListener, ItemListener {
	
	boolean canceled;
	
	//パネル
	Panel panelNorth;
	Panel panelCenter;
	Panel panelSouth;
	
	//入力用コンポーネント
	ChoiceFacility choiceFacility;            //教室選択用ボックス
	TextField      tfYear, tfMonth, tfDay;    //年月日のテキストフィールド
	ChoiceHour     startHour;                 //予約開始時間（時）の選択ボックス
	ChoiceMinute   startMinute;               //予約開始時間（分）の選択ボックス
	ChoiceHour     endHour;                   //予約終了時間(時)の選択ボックス
	ChoiceMinute   endMinute;                 //予約終了時間(分)の選択ボックス
	
	//ボタン
	Button         buttonOK;                  //OKボタン
	Button         buttonCancel;              //キャンセルボタン
	
	public ReservationDialog(Frame owner) {
		//基底クラスのコンストラクタを呼び出す
		super(owner, "新規予約", true);
		
		//初期値キャンセルを設定
		canceled = true;
		
		//教室選択ボックスの生成
		choiceFacility = new ChoiceFacility();
		//テキストフィールドの生成(年月日)
		tfYear = new TextField("", 4);
		tfMonth = new TextField("", 2);
		tfDay = new TextField("", 2);
		//開始時刻(時分)選択ボックスの生成
		startHour = new ChoiceHour();
		startMinute = new ChoiceMinute();
		//終了時刻(時分)選択ボックスの生成
		endHour = new ChoiceHour();
		endMinute = new ChoiceMinute();
		
		//ボタンの生成
		buttonOK = new Button("予約実行");
		buttonCancel = new Button("キャンセル");
		
		//パネルの生成
		panelNorth = new Panel();
		panelCenter = new Panel();
		panelSouth = new Panel();
		
		//上部パネルに教室選択ボックス,年月日入力欄を配置
		panelNorth.add(new Label("教室 "));
		panelNorth.add(choiceFacility);
		panelNorth.add(new Label("予約日"));
		panelNorth.add(tfYear);
		panelNorth.add(new Label("年"));
		panelNorth.add(tfMonth);
		panelNorth.add(new Label("月"));
		panelNorth.add(tfDay);
		panelNorth.add(new Label("日"));
		
		//中央パネルに予約開始時刻,終了時刻入力選択ボックスを追加
		panelCenter.add(new Label("予約時間"));
		panelCenter.add(startHour);
		panelCenter.add(new Label("時"));
		panelCenter.add(startMinute);
		panelCenter.add(new Label("分～"));
		panelCenter.add(endHour);
		panelCenter.add(new Label("時"));
		panelCenter.add(endMinute);
		panelCenter.add(new Label("分"));
		
		//下部パネルに2つのボタンを追加
		panelSouth.add(buttonCancel);
		panelSouth.add(new Label("  "));
		panelSouth.add(buttonOK);
		
		//ReservationDialogをBorderLayoutに設定し,３つのパネルを追加
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth,BorderLayout.SOUTH);
		
		//window Listenerを追加
		addWindowListener(this);
		//ボタンにアクションリスナを追加
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		//教室選択ボックス,時・分選択ボックスそれぞれに項目Listenerを追加
		choiceFacility.addItemListener(this);
		startHour.addItemListener(this);
		endHour.addItemListener(this);
		
		//選択された教室によって,時刻の範囲を設定
		resetTimeRange();
		
		//大きさの設定,ウィンドウのサイズ変更不可の設定
		this.setBounds(100, 100, 500, 150);
		setResizable(false);
		
	}
	private void	resetTimeRange() {
		//時刻の範囲を設定する
		startHour.resetRange(9, 21);
		endHour.resetRange(9, 21);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if( e.getSource() == choiceFacility) {
			String startTime = startHour.getSelectedItem();
			String endTime = endHour.getSelectedItem();
			resetTimeRange();
			startHour.select(startTime);
			endHour.select(endTime);
		} else if(e.getSource() == startHour) {
			//開始時刻が変更されたら,終了時刻入力欄の時を開始時刻に合わせる
			int start = Integer.parseInt(startHour.getSelectedItem());
			String endTime = endHour.getSelectedItem();
			endHour.resetRange(start, Integer.parseInt(endHour.getLast()));
			if(Integer.parseInt(endTime) >= start) {
				endHour.select(endTime);
			}
		} else if(e.getSource() == startHour) {
			//終了時刻が変更されたら,開始時刻入力欄の時を開始時刻に合わせる
			int end = Integer.parseInt(endHour.getSelectedItem());
			String startTime = startHour.getSelectedItem();
			startHour.resetRange(Integer.parseInt(startHour.getFirst()),end);
			if(Integer.parseInt(startTime) >= end) {
				startHour.select(startTime);
			}
		}
	}
	
	
	@Override
	public void windowOpened(WindowEvent e) {
	
		
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		dispose();
		
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
		if(e.getSource() == buttonCancel) {
			setVisible(false);
			dispose();
		} else if(e.getSource() == buttonOK) {
			canceled = false;
			setVisible( false);
			dispose();
		}
	}
	
	
}