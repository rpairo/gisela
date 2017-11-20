package chistes;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

/**
 * 
 * This class is responsible for downloading information from the newspapers, and store it in the device.
 * 
 * @author Raul Pera Pairó
 * 
 */
public class DescargarChistes extends AsyncTask<Bundle, Void, String> {

	ArrayList<String> items = new ArrayList<String>();
	Bundle bund;
	private Boolean endSpeaking = false;

	/**
	 * This function is responsible for accessing the website, download the XML and store it on the device.
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(Bundle... urls) {

		bund = urls[0];

		try {

			URL url = new URL(bund.getString("url"));

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.connect();

			File SDCardRoot = Environment.getExternalStorageDirectory();

			File file = new File(SDCardRoot, "Chistes.xml");

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
			e.printStackTrace();
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
		File file = new File(SDCardRoot, "Chistes.xml");

		ArrayList<String> contenidosNoticias = new ArrayList<String>();

		try {

			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			ArrayList<String> titulares = new ArrayList<String>();

			XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myParser = xmlFactoryObject.newPullParser();
			myParser.setInput(br);

			int event = myParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();

				switch (event) {

				case XmlPullParser.START_TAG:

					if (name.equals("description")) {

						titulares.add(myParser.nextText());

					}

					break;

				case XmlPullParser.END_TAG:
					break;
				}

				event = myParser.next();
			}

			this.bund.putStringArrayList("titulares", titulares);
			new HablaChistes().execute(bund);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
}