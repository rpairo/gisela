package weather;

/**
 * 
 * This feature serves as a template to assemble all the information needed to manage weather
 * 
 * @author Raul Pera Pairó
 *
 */
public class MyWeather {

	String description;
	String city;
	String region;
	String country;

	String windChill;
	String windDirection;
	String windSpeed;

	String sunrise;
	String sunset;

	String temperatura;
	int codigo;

	String conditiontext;
	String conditiondate;

	/**
	 * 
	 * overrides the toString method to display the characteristics of the weather.
	 * 
	 */
	public String toString() {

		return "\n- " + description + " -\n\n" + "city: " + city + "\n" + "region: " + region + "\n" + "country: " + country + "\n\n"

		+ "Wind\n" + "chill: " + windChill + "\n" + "direction: " + windDirection + "\n" + "speed: " + windSpeed + "\n\n"

		+ "Sunrise: " + sunrise + "\n" + "Sunset: " + sunset + "\n\n"

		+ "Condition: " + conditiontext + "\n" + conditiondate + "\n";

	}

	/**
	 * This function is responsible for returning a "humanized" phrase from the received code.
	 * 
	 * @return String
	 */
	public String getCodigo() {

		String condicion = null;

		switch (this.codigo) {

		case 0:
			condicion = "cuidado, se ha detectado un tornado en tu zona.";
			break;
		case 1:
			condicion = "cuiado, se ha detectado una tormenta tropical en tu zona.";
			break;
		case 2:
			condicion = "cuidado, se ha detectado un huracan en tu zona.";
			break;
		case 3:
			condicion = "cuidado, se han detectado tormentas electricas severas.";
			break;
		case 4:
			condicion = "cuidado, se han detectado tormentas electricas.";
			break;
		case 5:
			condicion = "y está cayendo lluvia y nieve.";
			break;
		case 6:
			condicion = "y está cayendo lluvia y aguanieve.";
			break;
		case 7:
			condicion = "y está cayendo nieve y aguanieve.";
			break;
		case 8:
			condicion = "y está cayendo llovizna helada.";
			break;
		case 9:
			condicion = "y está cayendo llovizna.";
			break;
		case 10:
			condicion = "y está cayendo lluvia helada.";
			break;
		case 11:
			condicion = "y está lloviendo";
			break;
		case 12:
			condicion = "y está cayendo un chaparrón.";
			break;
		case 13:
			condicion = "cuidado, se han detectado rafagas de nieve.";
			break;
		case 14:
			condicion = "y está cayendo una ligera lluvia de nieve.";
			break;
		case 15:
			condicion = "cuidado, se ha detectado una ventisca en tu zona.";
			break;
		case 16:
			condicion = "esta nevando";
			break;
		case 17:
			condicion = "esta granizando";
			break;
		case 18:
			condicion = "esta cayendo aguanieve.";
			break;
		case 19:
			condicion = "el ambiente está lleno de polvo en suspensión.";
			break;
		case 20:
			condicion = "el ambiente está nebuloso.";
			break;
		case 21:
			condicion = "cuidado, se ha detectado neblina.";
			break;
		case 22:
			condicion = "el ambiente se encuentra nublado por humo.";
			break;
		case 23:
			condicion = "el dia está borrascoso.";
			break;
		case 24:
			condicion = "el ambiente es ventoso.";
			break;
		case 25:
			condicion = "hace frio.";
			break;
		case 26:
			condicion = "y está nublado.";
			break;
		case 27:
			condicion = "y hace una noche en su mayoría nublada.";
			break;
		case 28:
			condicion = "y hace un día en su mayor parte nubládo.";
			break;
		case 29:
			condicion = "y hace una noche parcialmente nublada.";
			break;
		case 30:
			condicion = "y hace un día parcialmente nublado.";
			break;
		case 31:
			condicion = "y hace una noche despejada.";
			break;
		case 32:
			condicion = "y hace un dia soleado.";
			break;
		case 33:
			condicion = "y hace una noche hermosa.";
			break;
		case 34:
			condicion = "y hace un día hermoso.";
			break;
		case 35:
			condicion = "y está cayendo lluvia y granizo.";
			break;
		case 36:
			condicion = "y hace un dia caluroso.";
			break;
		case 37:
			condicion = "se han detectado tormentas aisladas.";
			break;
		case 38:
			condicion = "se han detectado tormentas electricas dispersas.";
			break;
		case 39:
			condicion = "se detectan tormentas dispersas.";
			break;
		case 40:
			condicion = "se han detectado lluvias dispersas.";
			break;
		case 41:
			condicion = "cuidado, se han detectado fuertes nevadas.";
			break;
		case 42:
			condicion = "cuidado, se han detectado fuertes nevadas dispersas.";
			break;
		case 43:
			condicion = "cuidado se han detectado fuertes nevadas.";
			break;
		case 44:
			condicion = "y hace un día parcialmente nublado.";
			break;
		case 45:
			condicion = "y hace un día tormentoso.";
			break;
		case 46:
			condicion = "se han detectado lluvias de nieve.";
			break;
		case 47:
			condicion = "se han detectado chaparrones tormentosos aislados.";
			break;
		}

		return condicion;
	}

	/**
	 * is a setter for the code of the weather
	 * 
	 * @param String
	 */
	public void setCodigo(String codigo) {
		this.codigo = Integer.parseInt(codigo);
	}
}