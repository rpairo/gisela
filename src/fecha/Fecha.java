package fecha;

import java.util.Calendar;

import speaker.Speaker;

public class Fecha {

	int dayNumber, day, month, year;
	String dia, mes;

	public Fecha(Speaker voiceSpeaker) {

		Calendar c = Calendar.getInstance();

		this.dayNumber = c.get(Calendar.DAY_OF_MONTH);
		this.day = c.get(Calendar.DAY_OF_WEEK);
		this.month = c.get(Calendar.MONTH);
		this.year = c.get(Calendar.YEAR);

		switch (this.day) {
		case 2:
			this.dia = "lunes";
			break;
		case 3:
			this.dia = "martes";
			break;
		case 4:
			this.dia = "miércoles";
			break;
		case 5:
			this.dia = "jueves";
			break;
		case 6:
			this.dia = "viernes";
			break;
		case 7:
			this.dia = "sábado";
			break;
		case 1:
			this.dia = "domingo";
			break;
		}

		switch (this.month) {
		case 0:
			this.mes = "enero";
			break;
		case 1:
			this.mes = "febrero";
			break;
		case 2:
			this.mes = "marzo";
			break;
		case 3:
			this.mes = "abril";
			break;
		case 4:
			this.mes = "mayo";
			break;
		case 5:
			this.mes = "junio";
			break;
		case 6:
			this.mes = "julio";
			break;
		case 7:
			this.mes = "agosto";
			break;
		case 8:
			this.mes = "septiembre";
			break;
		case 9:
			this.mes = "octubre";
			break;
		case 10:
			this.mes = "noviembre";
			break;
		case 11:
			this.mes = "diciembre";
			break;
		}

		// int time = c.get(Calendar.HOUR_OF_DAY);
		// int min = c.get(Calendar.MINUTE);

		Boolean endSpeaking = false;
		voiceSpeaker.habla("Hoy es " + this.dia + ", " + this.dayNumber + " de " + this.mes + ", del " + this.year);
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);

		// System.out.println("Son las: " + time + " y " + min);
	}
}