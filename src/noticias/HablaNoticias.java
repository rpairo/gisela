package noticias;

import java.util.ArrayList;

import speaker.Speaker;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * 
 * This class is responsible for giving the news that has been parsed by the class DescargarNoticias
 * 
 * @author Raul Pera Pairó
 * 
 */
public class HablaNoticias extends AsyncTask<Bundle, Void, String> {

	private Speaker voiceSpeaker;
	private Boolean endSpeaking = false;
	private int numeroNoticias = 7;

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

		ArrayList<String> titulares = bun.getStringArrayList("titulares");

		ArrayList<String> titularesDepurados = new ArrayList<String>();

		for (int i = 2; i < this.numeroNoticias + 2; i++)
			titularesDepurados.add(titulares.get(i));

		for (int i = 0; i < titularesDepurados.size(); i++) {

			System.out.println("Noticia nº: " + (i + 1));
			System.out.println(titularesDepurados.get(i));

			this.endSpeaking = true;
			this.voiceSpeaker.habla(titularesDepurados.get(i));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			do {
				this.endSpeaking = this.voiceSpeaker.tts.isSpeaking();
			} while (this.endSpeaking);

		}

		return null;
	}
}