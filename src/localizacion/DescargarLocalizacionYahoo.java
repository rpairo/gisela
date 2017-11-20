package localizacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import weather.GetWeatherYahoo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

/**
 * 
 * This class is responsible for downloading information from the location, and store it in the device.
 * 
 * @author Raul Pera Pairó
 * 
 */
public class DescargarLocalizacionYahoo extends AsyncTask<Bundle, Void, String> {

	ArrayList<String> items = new ArrayList<String>();
	Bundle bund;
	private Boolean endSpeaking = false;
	private double latitude, longitude;
	private boolean socorrer;

	/**
	 * This function is responsible for accessing the website, download the XML and store it on the device.
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(Bundle... urls) {

		bund = urls[0];
		this.latitude = bund.getDouble("latitude");
		this.longitude = bund.getDouble("longitude");
		this.socorrer = bund.getBoolean("socorrer");

		try {

			URL url = new URL(bund.getString("url"));

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.connect();

			File SDCardRoot = Environment.getExternalStorageDirectory();

			File file = new File(SDCardRoot, "Weather.xml");

			FileOutputStream fileOutput = new FileOutputStream(file);

			InputStream inputStream = urlConnection.getInputStream();

			int totalSize = urlConnection.getContentLength();

			int downloadedSize = 0;

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {

				fileOutput.write(buffer, 0, bufferLength);

				downloadedSize += bufferLength;
			}

			fileOutput.close();
		} catch (MalformedURLException e) {
			System.out.println("La URL no funciona");
		} catch (IOException e) {
			System.out.println("Internet no esta conectado");
		}

		return "";
	}

	/**
	 * This function is responsible for reading the downloaded XML and parse it to obtain the headlines and descriptions. Once finished parsing, call the class responsible for promulgating information
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String cadena) {

		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot, "Weather.xml");

		ArrayList<String> contenidosNoticias = new ArrayList<String>();

		try {

			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String woeid = null;
			String barrio = null;
			String calle = null;

			XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();
			myParser.setInput(br);

			int event = myParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();

				switch (event) {

				case XmlPullParser.START_TAG:

					if (name.equals("woeid")) {
						woeid = myParser.nextText();
					} else if (name.equals("neighborhood")) {
						barrio = myParser.nextText();
					} else if (name.equals("line1")) {
						calle = myParser.nextText();
					}

					break;

				case XmlPullParser.END_TAG:
					break;
				}

				event = myParser.next();
			}

			this.bund.putString("woeid", woeid);
			this.bund.putString("barrio", barrio);
			this.bund.putString("calle", calle);

			if (!this.socorrer)
				new HablaLocalizacion().execute(bund);
			else {
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
}