package Tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IsNetConnected {
	 public static boolean isNetConnected(Context context) {
	        ConnectivityManager manager = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = manager.getActiveNetworkInfo();
	        if (netInfo != null) {
	            return netInfo.isConnected();
	        }
	        return false;
	    }
}
