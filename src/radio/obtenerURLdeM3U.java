package radio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class obtenerURLdeM3U extends AsyncTask<Bundle, Void, String> {

	Bundle bund;
	String url;

	@Override
	protected String doInBackground(Bundle... params) {

		this.bund = params[0];
		// mp = (MediaPlayer) bund.getSerializable("media");
		this.url = bund.getString("url");
		Log.d("URL", this.url);
		String cadena = this.parse(this.url);
		
		if (cadena != null)
			Log.d("URL", cadena);

		return cadena;
	}

	public static String parse(String urlM3u) {

		String ligne = null;

		try {
			URL urlPage = new URL(urlM3u);
			HttpURLConnection connection = (HttpURLConnection) urlPage.openConnection();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			StringBuffer stringBuffer = new StringBuffer();

			while ((ligne = bufferedReader.readLine()) != null) {
				if (ligne.contains("http")) {
					connection.disconnect();
					bufferedReader.close();
					inputStream.close();
					return ligne;
				}
				stringBuffer.append(ligne);
			}

			connection.disconnect();
			bufferedReader.close();
			inputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String url) {
		/*
		 * MediaPlayer mp = new MediaPlayer(); try { mp.setDataSource(url); mp.prepare(); mp.start(); } catch (IllegalArgumentException e) { // TODO Auto-generated catch block e.printStackTrace(); } catch (SecurityException e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IllegalStateException e) { // TODO Auto-generated catch block e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}
}
