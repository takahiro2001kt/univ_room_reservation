package client_system;

import java.awt.Dialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mysql.jdbc.StringUtils;

public class ReservationControl {
	Connection sqlCon;
	Statement sqlStmt;
	String sqlUserID = "IDを入力";
	String sqlPassword = "パスワードを入力";
	int reservation_id = 0;
	String sql;
	String reservationUserID;
	
	private boolean flagLogin;

	ReservationControl(){
		flagLogin = false;
	}

	private void connectDB() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			String url = "jdbc:mysql://localhost?useUnicode=true&characterEncoding=SJIS";
			sqlCon = DriverManager.getConnection(url,sqlUserID,sqlPassword);
			sqlStmt = sqlCon.createStatement();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	//SQLから切断
	private void closeDB() {
		try {
			sqlStmt.close();
			sqlCon.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//自分が予約したもの一覧を表示するシステム
	public List<String> outmyreservation(String user_id) {
		System.out.print("flag_status : "+flagLogin);
		String res = "";
		String date = "";
		String start = "";
		String end = "";
		String reservation_id ="";
		List<String> result = new ArrayList<String>();
		
			//DBに接続
			connectDB();
			//facilityテーブルに存在するuser_idが一致するデータを選択
			try {
				sql = "SELECT * from db_reservation.reservation WHERE user_id ='" + user_id + "' AND day >= CURDATE() ORDER BY day, start_time;";
				ResultSet rs = sqlStmt.executeQuery(sql);
				rs.last();
				int number_of_row = rs.getRow();
				rs.beforeFirst();
				
				if (number_of_row == 0) {
					result.add(user_id + "の予約はありません.");
				}else {
					result.add(user_id + "の予約を表示します.");
					result.add("    ID     教室名      予約日                 予約時間");
					while(rs.next()) {
						reservation_id = rs.getString("reservation_id");
						res = rs.getString("facility_id");
						date = rs.getString("day");
						date = date.substring(0,4) + "年" + date.substring(5,7) + "月" + date.substring(8,10) + "日";
						start = rs.getString("start_time").substring(0,5);
						end = rs.getString("end_time").substring(0,5);
						result.add("　" + reservation_id + "　"+ res +" 教室 " + date+ " " + start+ "～"  + end);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			closeDB();
	return result;
	}

	//ログイン・ログアウト処理
	public String loginLogout( MainFrame frame) {
		String res = "";
		String str = "";
		List<String> strList;
		if( flagLogin) {
			flagLogin = false;
			frame.buttonLog.setLabel(" ログイン ");
			frame.tfLoginID.setText("未ログイン");
		} else {
			LoginDialog Id= new LoginDialog(frame);
			Id.setBounds( 100, 100, 350, 150);
			Id.setResizable( false);
			Id.setVisible(true);
			Id.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

			if( Id.canceled) {
				return "";
			}

			reservationUserID = Id.tfUserID.getText();
			String password = Id.tfPassword.getText();

			connectDB();
			try {
				String sql = "SELECT * FROM db_reservation.user WHERE user_id ='" + reservationUserID + "';";
				ResultSet rs = sqlStmt.executeQuery(sql);
				if( rs.next()) {
					String password_form_db = rs.getString("password");
					if( password_form_db.equals(password)) {
						flagLogin = true;
						frame.buttonLog.setLabel("ログアウト");
						frame.tfLoginID.setText(reservationUserID);
						
						//Mainframe.javaのstring型変数logIDにログインが成功したIDを代入する
						frame.logID = reservationUserID;
						
						//ログイン後に自分の予約一覧を表示する機能
						strList = outmyreservation(reservationUserID);
						for (String strs : strList) {
							str += strs + "\r\n";
						}
						res = str;
						return res;
					} else {
						res = "　IDまたはパスワードが違います.";
					}
				} else {
					res = " IDが違います.";
				}
			} catch ( Exception e) {
					e.printStackTrace();
			}
			closeDB();
		}
		return res;
	}

	public String getFacilityExplanation(String facility_id) {
		String res = "";
		connectDB();
		try {
			String sql = "SELECT * from db_reservation.facility WHERE facility_id ='" + facility_id + "';";
			ResultSet rs = sqlStmt.executeQuery(sql);
			if (rs.next()) {
				res = rs.getString("explanation");
			} else {
				res = "教室番号が違います.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB();
		return res;
		}

	//新規予約メソッド
	public String makeReservation(MainFrame frame) {
		String res = "";
		String sql = "";
		
		if( flagLogin ) {      //ログイン判定
			//新規予約画面の生成
			ReservationDialog rd = new ReservationDialog( frame);
			
			//新規予約画面を表示
			rd.setVisible( true);
			if( rd.canceled) {
				return res;
			}
			
			String ryear_str = rd.tfYear.getText();
			String rmonth_str = rd.tfMonth.getText();
			String rday_str = rd.tfDay.getText();
			
			if (rmonth_str.length() == 1) {
				rmonth_str = "0" + rmonth_str;
			}
			if (rday_str.length() == 1) {
				rday_str = "0" + rday_str;
			}
			String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;
			
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				df.setLenient(false);
				String convData = df.format(df.parse( rdate));
				if( ! rdate.equals( convData)) {
					res = "日付の書式を修正してください（年：西暦4桁,月:1~12,日:1~31（各月末日まで））";
					return res;
				}
			}catch ( ParseException p) {
				p.printStackTrace();
				res = "日付の書式を修正してください（年：西暦4桁,月:1~12,日:1~31（各月末日まで））";
				return res;
			}
			String facility = rd.choiceFacility.getSelectedItem();
			String st = rd.startHour.getSelectedItem() + ":" + rd.startMinute.getSelectedItem() + ":00"; //予約時間が入っている
			String et = rd.endHour.getSelectedItem() + ":" + rd.endMinute.getSelectedItem() + ":00";
			if( st.compareTo( et) >= 0) {
				res = "開始時刻と終了時刻が同じか終了時刻のほうが早くなっています.";
			}else if((st.matches("09:00:00")) ||( st.matches("09:15:00"))){
				res = "教室が空いていません.開始時間は09:30以降に設定してください.";
				return res;
			}else if(et.matches("21:45:00")){
				res = "教室が閉まっています.終了時間は21:30以前に設定してください.";
				return res;
			}else {
				connectDB();
				try {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					df.setLenient(false);
					String convData = df.format(df.parse( rdate));
					
					sql = "select * from db_reservation.reservation where not (end_time <=" +"'"+ st +"'"+ " or " + "start_time >=" +"'"+ et +"'"+ ") and facility_id =" + "'" + facility + "' and day ="+"'" + convData + "';";
					ResultSet rs = sqlStmt.executeQuery(sql);
					
					rs.last();
					int number_of_row = rs.getRow();
					rs.beforeFirst();
					
					if (number_of_row != 0) {
						res = "既に予約されています.日付の値を修正してください.";
					} else {
						try {
							Calendar	justNow = Calendar.getInstance();
							SimpleDateFormat	resDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String now = resDate.format( justNow.getTime());
							connectDB();
						    sql = "INSERT INTO db_reservation.reservation ( facility_id, user_id, date, day, start_time, end_time) VALUES('"+facility+"', '"+reservationUserID +"','"+ now +"', '"+ rdate +"', '"+ st +"', '"+ et +"');";
							sqlStmt.executeUpdate(sql);
						    ResultSet rs2 = sqlStmt.executeQuery(sql);
						} catch (Exception e) {
							e.printStackTrace();
							res = "フォーマットが違います。";
						}
						res = "予約されました.";
						closeDB();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}else {
			res = "ログインしてください.";
		}
		return res;
	  }
	
	
	//これキャンセル
	public String cancelReservation(MainFrame frame, String logid) {
		String cancelText = "";
		String res = "";
		if (flagLogin) {
		connectDB();
		//outmyreservationの戻り値を代入する変数
		List<String> currentReservationList;
		
		CancelDialog Id= new CancelDialog(frame,logid);
		
		//ダイアログを表示させる前にテキストエリアに文言をセットする
		currentReservationList = outmyreservation(logid);
		//outmyreservationの戻り値はstringのlist型なので、テキストエリアにセットできるようstring型に変換する
		for (String str : currentReservationList) {
			cancelText += str +	"\n";
		}
		//CancelDialog内のテキストエリアcurrentReservationに自分のIDが予約した一覧を表示する
		Id.currentReservation.setText(cancelText);
		Id.setBounds( 100, 100, 500, 550);
		Id.setResizable( false);
		Id.setVisible(true);
		Id.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		closeDB();
		//System.out.print(res);**/
		} else {
			res = "ログインしてください.";
		}
		return res;
	}
	
	//予約状況表示の関数
	public String showReservation(MainFrame frame){
    	String res = "";
    	ShowReservationDialog rd = new ShowReservationDialog(frame);
    	
    	//予約状況確認画面を表示
		rd.setVisible( true);
		if ( rd.canceled) {
			return res;
		}
		//予約状況確認画面から年月日を取得
		String cyear_str = rd.tfYear.getText().toString();
		String cmonth_str = rd.tfMonth.getText().toString();
		String cday_str = rd.tfDay.getText().toString();
		
		//年月日が空白の場合
		
		//月と日が一桁だったら前に0を付与
		if(cmonth_str.
				length() == 1) {
			cmonth_str = "0" + cmonth_str;
		}
		if(cday_str.length() == 1) {
			cday_str = "0" + cday_str;
		}
		
		if((cyear_str.length() == 4) || (cmonth_str.length() == 2) || (cday_str.length() == 2)) {
			String cdate = cyear_str + "-" + cmonth_str + "-" + cday_str;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setLenient(false);
			String convData;
			try {
				convData = df.format(df.parse(cdate));
				if( !cdate.equals( convData)) {
					res = "日付の書式を修正してください（年：西暦4桁,月:1~12,日:1~31（各月末日まで））";
					//System.out.println("convData"+convData +"cdate" + cdate);
				}else {
					String facility = rd.choiceFacility.getSelectedItem();
					connectDB();
					String sql =" SELECT * from db_reservation.reservation WHERE facility_id ='" + facility + "' and day ='" + cdate + "' ORDER BY start_time;";
					ResultSet rs;
					try {
						rs = sqlStmt.executeQuery(sql);
						rs.last();
						int number_of_row = rs.getRow();
						rs.beforeFirst();
						res = facility + "教室" + "　" + cyear_str + "年" + cmonth_str + "月" + cday_str + "日　予約表示\r\n";
						if(number_of_row == 0) {
							res = "予約はありません.";
						}else {
							while(rs.next()) {
								String res1, res2 = "";
				    			res1 = rs.getString("start_time");
								String start_time = res1.substring(0,5);
					    		res2 = rs.getString("end_time");
					    		String end_time = res2.substring(0,5);
					    		res += ("  " + start_time + "~" + end_time + "\r\n" );
							}
							rs.close();
							closeDB();
						}
					}catch(SQLException e) {
						e.printStackTrace();
					}	
				}
				
			}catch(ParseException e){
				e.printStackTrace();
				res = "日付の値を修正してください.";
				return res;
			}
		}else {
			res = "日付の値を修正してください.";
		}
		return res;
	}
	
	public String cancel(String reservationID,Boolean status,String user_id) {
	//	System.out.print("HAPPY NEW YEAR");
		String res = "";
			if (status) {
				connectDB();
				try {
					String sql = "DELETE from db_reservation.reservation WHERE reservation_id = " + Integer.valueOf(reservationID)+" and user_id ='" + user_id + "';";
					System.out.print(sql);
					int sql_status = sqlStmt.executeUpdate(sql);
					if (sql_status != 0) {
						res = "正常にキャンセルされました.";
					} else {
						res = "IDが見つかりません.IDを再確認してください.";
					}
				}catch(Exception e) {
					e.printStackTrace();
					res = "正しい値で削除IDを入力してください.";
				}
				closeDB();
				return res;
			} else {
				res = "チェックボックスにチェックを入れてください.";
			}
		flagLogin = true;
		return res;
	}
}

