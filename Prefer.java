package kr.co.softtrain.polltalk.common_util;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class GeneralUtil extends Activity {
	// m_context : getApplicationContext();
	private Context m_context = null;
	public String m_strPreferenceFileName = null;

	public GeneralUtil() {

	}

	public GeneralUtil(Context contextParam) {
		m_context = contextParam;
	}

	public GeneralUtil(Context contextParam, String strPreferenceFileName) {
		this(contextParam);
		m_strPreferenceFileName = new String(strPreferenceFileName);
	}

	/**
	 * Preference 파일에서 특정 키의 값을 가져온다<br>
	 */
	public FuncParamString getStringFromPreference(String strKey) {
		FuncParamString objRet = new FuncParamString();
		// A, B 둘 중 하나라도 참이라면~, A가 참이면, B를 검사하지 않음
		if ((m_strPreferenceFileName == null) || (m_context == null)) {
			// error 1
			objRet.setnRetVal(1);
			objRet.setstrRetVal("");
			return objRet;
		}
		SharedPreferences prefs = m_context.getSharedPreferences(
				m_strPreferenceFileName, Context.MODE_PRIVATE);
		String strGetValue = prefs.getString(strKey, "");
		objRet.setstrRetVal(strGetValue);
		return objRet;
	}

	/**
	 * Preference 파일에서 특정 키의 값을 저장한다<br>
	 */
	public int setStringToPreference(String strKey, String strSetValue) {
		int nRet = 0;
		// A, B 둘 중 하나라도 참이라면~, A가 참이면, B를 검사하지 않음
		if ((m_strPreferenceFileName == null) || (m_context == null)) {
			// error 1
			nRet = 1;
			return nRet;
		}
		// 데이타를 저장합니다.
		SharedPreferences prefs = m_context.getSharedPreferences(
				m_strPreferenceFileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(strKey, strSetValue);
		editor.commit();
		return nRet;
	}

	/**
	 * 네트워크 연결 체크 함수<br>
	 */
	public boolean checkNetworkConnected() {
		boolean bRet = false;
		if (m_context == null) {
			return bRet;
		}

		try {
			ConnectivityManager manager = (ConnectivityManager) m_context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
			if (activeNetwork == null) {
				bRet = false;
				return bRet;
			}
			// determine the type of network
			switch (activeNetwork.getType()) {
			case ConnectivityManager.TYPE_WIMAX: // 4g 망 체크
				bRet = true;
				break;
			case ConnectivityManager.TYPE_WIFI: // wifi망 체크
				bRet = true;
				break;
			case ConnectivityManager.TYPE_MOBILE: // 3g 망 체크
				bRet = true;
				break;
			}
		} catch (Exception e) {
			bRet = false;
			return bRet;
		}

		return bRet;
	}

	/**
	 * sd card 가 아닌 안드로이드 시스템 내부 - apk 설치 위치 안에 파일을 저장하는 함수<br>
	 */
	public int InternalStoreageWrite(String strFileName) {
		// "samplefile.txt"
		int nRet = 0;
		if (m_context == null) {
			// error 2
			nRet = 2;
			return nRet;
		}

		try {
			final String TESTSTRING = new String("Hello Android");

			// have to context.openFileOutput, if not, NullpointException occur
			FileOutputStream fOut = m_context.openFileOutput(strFileName,
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			osw.write(TESTSTRING);
			osw.flush();
			osw.close();
		} catch (Exception e) {
			if (SetLog.bLogAdjust == true) {
				SetLog obj_Log = new SetLog();
				obj_Log.CustomLog("w", "Exception MSG - ", e.getMessage());
				obj_Log = null;
			}
			// error 1
			nRet = 1;
		}

		return nRet;
	}

	/**
	 * sd card 가 아닌 안드로이드 시스템 내부 - apk 설치 위치 안에 파일을 불러와사 사용하는 함수<br>
	 */
	public int InternalStoreageRead(String strFileName) {
		// "samplefile.txt"
		int nRet = 0;
		if (m_context == null) {
			// error 2
			nRet = 2;
			return nRet;
		}
		try {
			final String TESTSTRING = new String("Hello Android");
			// have to context.openFileOutput, if not, NullpointException occur
			FileInputStream fIn = m_context.openFileInput(strFileName);
			InputStreamReader isr = new InputStreamReader(fIn);

			char[] inputBuffer = new char[TESTSTRING.length()];

			// Fill the Buffer with data from the file
			isr.read(inputBuffer);

			// Transform the chars to a String
			String readString = new String(inputBuffer);

			// Check if we read back the same chars that we had written out
			boolean isTheSame = TESTSTRING.equals(readString);

			if (SetLog.bLogAdjust == true) {
				SetLog obj_Log = new SetLog();
				obj_Log.CustomLog("w", "File Content - ", "" + isTheSame);
				obj_Log = null;
			}
		} catch (Exception e) {
			if (SetLog.bLogAdjust == true) {
				SetLog obj_Log = new SetLog();
				obj_Log.CustomLog("w", "Exception MSG - ", e.getMessage());
				obj_Log = null;
			}
			// error 1
			nRet = 1;
		}

		return nRet;
	}

	/**
	 * 안드로이드 단말기에서 핸드폰 번호를 api 를 이용해서 얻어오는 함수<br>
	 */
	public FuncParamString getPhoneNumber() {
		FuncParamString objRet = new FuncParamString();
		String strPhoneNum = null;
		try {
			TelephonyManager tMgr = (TelephonyManager) m_context
					.getSystemService(Context.TELEPHONY_SERVICE);
			strPhoneNum = tMgr.getLine1Number();
		} catch (Exception e) {
			if (SetLog.bLogAdjust == true) {
				SetLog obj_Log = new SetLog();
				obj_Log.CustomLog("w", "Exception MSG - ", e.getMessage());
				obj_Log = null;
			}
			// error 1
			objRet.setnRetVal(1);
			objRet.setstrRetVal("");
			return objRet;
		}

		objRet.setstrRetVal(strPhoneNum);
		return objRet;
	}
}
