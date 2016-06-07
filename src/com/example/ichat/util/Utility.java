
package com.example.ichat.util;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utility {
	
	public static boolean checkNetworkConnection(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo !=null && networkInfo.isConnected();
		
	}
	/**
	 * 提示加载不成功原因
	 * @param context
	 */
	public static void noNetworkAlert(Context context){
		Toast.makeText(context, "网络信号不好或手机没有连接网络", Toast.LENGTH_LONG).show();
	}
}
