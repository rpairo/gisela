package weather;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import speaker.Speaker;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * 
 * This class is responsible for sending the query request to the server of yahoo! then show
 * 
 * @author Raul Pera Parió
 * 
 */
public class GetWeatherYahoo extends AsyncTask<Bundle, Void, String> {

	MyWeather myWeather, weatherResult;
	private Boolean endSpeaking = false;
	Speaker voiceSpeaker;
	private Bundle bun;
	private String woeid;
	private String barrio;
	String weatherString;
	Document weatherDoc;

	/**
	 * 
	 * This function is responsible for initializing variables and execute functions in the correct order.
	 * 
	 * @param Speaker
	 * 
	 */
	@Override
	protected String doInBackground(Bundle... arg0) {

		this.bun = arg0[0];
		this.voiceSpeaker = (Speaker) bun.getSerializable("speaker");
		this.woeid = bun.getString("woeid");
		this.barrio = bun.getString("barrio");

		this.myWeather = new MyWeather();

		weatherString = QueryYahooWeather();

		return null;
	}

	/**
	 * This function is responsible for parsing the information downloaded from the servidoros yahoo!, To assemble an object with that information MyWheater
	 * 
	 * @param Document
	 * @return MyWhather
	 */
	private MyWeather parseWeather(Document srcDoc) {

		myWeather.description = srcDoc.getElementsByTagName("description").item(0).getTextContent();

		Node locationNode = srcDoc.getElementsByTagName("yweather:location").item(0);
		myWeather.city = locationNode.getAttributes().getNamedItem("city").getNodeValue().toString();
		myWeather.region = locationNode.getAttributes().getNamedItem("region").getNodeValue().toString();
		myWeather.country = locationNode.getAttributes().getNamedItem("country").getNodeValue().toString();

		Node windNode = srcDoc.getElementsByTagName("yweather:wind").item(0);
		myWeather.windChill = windNode.getAttributes().getNamedItem("chill").getNodeValue().toString();
		myWeather.windDirection = windNode.getAttributes().getNamedItem("direction").getNodeValue().toString();
		myWeather.windSpeed = windNode.getAttributes().getNamedItem("speed").getNodeValue().toString();

		Node astronomyNode = srcDoc.getElementsByTagName("yweather:astronomy").item(0);
		myWeather.sunrise = astronomyNode.getAttributes().getNamedItem("sunrise").getNodeValue().toString();
		myWeather.sunset = astronomyNode.getAttributes().getNamedItem("sunset").getNodeValue().toString();

		Node conditionNode = srcDoc.getElementsByTagName("yweather:condition").item(0);
		myWeather.conditiontext = conditionNode.getAttributes().getNamedItem("text").getNodeValue().toString();
		myWeather.conditiondate = conditionNode.getAttributes().getNamedItem("date").getNodeValue().toString();
		myWeather.temperatura = conditionNode.getAttributes().getNamedItem("temp").getNodeValue().toString();
		myWeather.setCodigo(conditionNode.getAttributes().getNamedItem("code").getNodeValue().toString());

		return myWeather;
	}

	/**
	 * This class is responsible for converting the String received a Document
	 * 
	 * @param String
	 * @return Document
	 */
	private Document convertStringToDocument(String src) {
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dest;
	}

	/**
	 * 
	 * This function is responsible for making the request to the server of yahoo! information for the weather
	 * 
	 * @return String
	 */
	private String QueryYahooWeather() {

		String qResult = "";
		String queryString = "http://weather.yahooapis.com/forecastrss?w=" + this.woeid + "&u=c";

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}
				qResult = stringBuilder.toString();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return qResult;
	}

	/**
	 * This function is responsible for issuing the weather information
	 * 
	 * @param String
	 * 
	 */
	protected void onPostExecute(String result) {

		weatherDoc = convertStringToDocument(weatherString);
		do{
		if (!(weatherDoc == null))
			weatherResult = parseWeather(weatherDoc);
		}while(weatherDoc == null);

		String fraseClimatica = "La temperatura actual en " + this.barrio + " es de " + myWeather.temperatura + " grados. " + myWeather.getCodigo();

		this.endSpeaking = false;

		this.voiceSpeaker.habla(fraseClimatica);
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);
	}

	private void conseguirWoeid() {

	}
}