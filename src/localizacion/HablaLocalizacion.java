package localizacion;

import java.io.File;

import speaker.Speaker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

/**
 * 
 * This class is responsible for giving the news that has been parsed by the class DescargaLocalizacionYahoo
 * 
 * @author Raul Pera Pairó
 * 
 */
public class HablaLocalizacion extends AsyncTask<Bundle, Void, String> {

	private Speaker voiceSpeaker;
	private Boolean endSpeaking = false;
	private int numeroNoticias = 7;

	private String woeid;
	private String barrio;
	private String calle;

	@Override
	protected void onPostExecute(String result) {
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}

	/**
	 * 
	 * This function will be responsible for dictating the news in a separate thread, so as not to block the app
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(Bundle... params) {

		Bundle bun = params[0];
		this.voiceSpeaker = (Speaker) bun.getSerializable("speaker");
		this.woeid = bun.getString("woeid");
		this.barrio = bun.getString("barrio");
		this.calle = bun.getString("calle");

		this.endSpeaking = true;
		this.voiceSpeaker.habla("Te encuentras en la calle: " + this.calle + ", " + this.barrio);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		do {
			this.endSpeaking = this.voiceSpeaker.tts.isSpeaking();
		} while (this.endSpeaking);

		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot, "Weather.xml");
		file.delete();
		
		return null;
	}
}