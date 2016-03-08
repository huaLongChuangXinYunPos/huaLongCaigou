package Tool;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VoleyToll {
	static RequestQueue requestQueue = null;

	public static RequestQueue getRequestQueue(Context context) {
		if (requestQueue == null)
			requestQueue = Volley.newRequestQueue(context); // 创建请求队列
		return requestQueue;
	}

}
