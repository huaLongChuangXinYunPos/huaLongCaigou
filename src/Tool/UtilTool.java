package Tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.text.TextUtils;

public class UtilTool {

	public static final int TYPE_TXT = 0;
	public static final int TYPE_IMG = 1;
	private static Handler hanler = new Handler();

	private static ExecutorService service = Executors.newFixedThreadPool(10);

	public static void get(final int type, final String path,
			final Callback callback, final String content) {
		service.execute(new Runnable() {

			@Override
			public void run() {
				try {
					if(TextUtils.isEmpty(path)){
						return ;
					}
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					if (content != null) {
						conn.setDoOutput(true);
						OutputStream out = conn.getOutputStream();
						out.write(content.getBytes());
					}
					InputStream in = conn.getInputStream();
					byte[] buffer = new byte[1024];
					int len = -1;
					ByteArrayOutputStream o = new ByteArrayOutputStream();

					while ((len = in.read(buffer)) != -1) {
						o.write(buffer, 0, len);
						if (callback.isCancelled(path)) {
							return;
						}
					}
					conn.disconnect();

					final byte[] b = o.toByteArray();
					if (type == TYPE_IMG) {
						// FileSaveRead.save(b, path);  //�����ļ�������
					}
					hanler.post(new Runnable() {

						@Override
						public void run() {
                            callback.response(path, b);
						}
					});

				} catch (Exception e) {
					System.out.println("异常");
				}

			}
		});
	}

	public interface Callback {

		public boolean isCancelled(String url);

		public void response(String url, byte[] bytes);

	}

}
