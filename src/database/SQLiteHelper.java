package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * This class is responsible for creating the database for the app, which, will include keywords and functions linked to them. Create a SQLite database, embedded in the device.
 * 
 * @author Raul Pera Pairó
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public final static String TABLE_NAME = "ORDENES";
	public final static String COL_ORDENES = "ORDEN";
	public final static String COL_FUNCIONES = "FUNCION";

	String sqlCreate = "CREATE TABLE " + TABLE_NAME + " (" + COL_ORDENES + " TEXT, " + COL_FUNCIONES + " TEXT)";

	/**
	 * Is constructor of the class takes care of creating the DB start
	 * 
	 * @param context
	 */
	public SQLiteHelper(Context context) {
		super(context, "DATABASE_ORDENES", null, 1);
	}

	/**
	 * This function is responsible for creating the tables in the DB, and call the function that loads the functions able to run the app.
	 * 
	 * @param SQLiteDatabase
	 *            Recibe un objeto SQLiteDatabase
	 * */
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(sqlCreate);
		cargarFuncionesConocidas(db);
	}

	/**
	 * This function is responsible for loading all keywords with their respective roles in the tables in the DB.
	 * 
	 * @param SQLiteDatabase
	 *            Get a DB object to begin creating records
	 * */
	public void cargarFuncionesConocidas(SQLiteDatabase db) {

		ContentValues values;

		values = new ContentValues();
		values.put(COL_ORDENES, "LLAMA");
		values.put(COL_FUNCIONES, "llamar");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "LLAMES");
		values.put(COL_FUNCIONES, "llamar");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "LLAMAR");
		values.put(COL_FUNCIONES, "llamar");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "SOCORRO");
		values.put(COL_FUNCIONES, "socorrer");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "AUXILIO");
		values.put(COL_FUNCIONES, "socorrer");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "AYUDA");
		values.put(COL_FUNCIONES, "socorrer");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "INSTRUCCIONES");
		values.put(COL_FUNCIONES, "instrucciones");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "ALARMA");
		values.put(COL_FUNCIONES, "setAlarma");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "DESPIÉRTAME");
		values.put(COL_FUNCIONES, "setAlarma");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "AVÍSAME");
		values.put(COL_FUNCIONES, "setAlarma");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "DESPERTADOR");
		values.put(COL_FUNCIONES, "setAlarma");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "TIEMPO");
		values.put(COL_FUNCIONES, "consultarElTiempo");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "CLIMA");
		values.put(COL_FUNCIONES, "consultarElTiempo");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "TEMPERATURA");
		values.put(COL_FUNCIONES, "consultarElTiempo");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "SALUDA");
		values.put(COL_FUNCIONES, "saludar");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "PRESÉNTATE");
		values.put(COL_FUNCIONES, "presentarse");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "PRESÉNTARSE");
		values.put(COL_FUNCIONES, "presentarse");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "BRILLO");
		values.put(COL_FUNCIONES, "cambiarBrillo");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "MENSAJE");
		values.put(COL_FUNCIONES, "enviarSMS");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "NOTICIAS");
		values.put(COL_FUNCIONES, "noticias");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "NOTICIA");
		values.put(COL_FUNCIONES, "noticias");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "PERIÓDICO");
		values.put(COL_FUNCIONES, "noticias");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "HORA");
		values.put(COL_FUNCIONES, "hora");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "POSICIÓN");
		values.put(COL_FUNCIONES, "localizacion");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "PERDIDO");
		values.put(COL_FUNCIONES, "localizacion");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "ESTOY");
		values.put(COL_FUNCIONES, "localizacion");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "ENCUENTRO");
		values.put(COL_FUNCIONES, "localizacion");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "UBICACIÓN");
		values.put(COL_FUNCIONES, "localizacion");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "UBICAME");
		values.put(COL_FUNCIONES, "localizacion");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "MÚSICA");
		values.put(COL_FUNCIONES, "reproducirMusica");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "IDIOTA");
		values.put(COL_FUNCIONES, "noInsultes");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "TONTO");
		values.put(COL_FUNCIONES, "noInsultes");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "TONTA");
		values.put(COL_FUNCIONES, "noInsultes");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "CHISTE");
		values.put(COL_FUNCIONES, "chistes");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "CHISTES");
		values.put(COL_FUNCIONES, "chistes");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "VOLUMEN");
		values.put(COL_FUNCIONES, "controlVolumen");
		db.insert(TABLE_NAME, null, values);

		values = new ContentValues();
		values.put(COL_ORDENES, "NOTIFICACIONES");
		values.put(COL_FUNCIONES, "notification");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "NOCHES");
		values.put(COL_FUNCIONES, "buenasNoches");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "TRADUCE");
		values.put(COL_FUNCIONES, "traduccion");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "TRADÚCEME");
		values.put(COL_FUNCIONES, "traduccion");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "TRADUCIR");
		values.put(COL_FUNCIONES, "traduccion");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "FECHA");
		values.put(COL_FUNCIONES, "fecha");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "DÍA");
		values.put(COL_FUNCIONES, "fecha");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "DIA");
		values.put(COL_FUNCIONES, "fecha");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "COLOR");
		values.put(COL_FUNCIONES, "detectorColor");
		db.insert(TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(COL_ORDENES, "RADIO");
		values.put(COL_FUNCIONES, "reproductorRadio");
		db.insert(TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
