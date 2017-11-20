package gisela;

import radio.GestorRadios;
import musica.ReproductorMusica;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is responsible for launching the app when there is a screen unlock
 * 
 * @author Raul Pera Pairó
 *  
 */
public class ControlDesbloqueo extends BroadcastReceiver {

	/**
	 * This function receives the action of the unlock screen, and will be responsible for launching the app
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (Principal.active == 0) {
			if (intent.getAction() != null) {
				if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

					Intent s = new Intent(context, Principal.class);
					s.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					// s.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

					s.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(s);
				}
			}
		} else if (Principal.active == 1) {
			if (intent.getAction() != null) {
				if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

					Intent s = new Intent(context, ReproductorMusica.class);
					s.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//					 s.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

					s.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(s);
				}
			}
		} else if (Principal.active == 2) {
			if (intent.getAction() != null) {
				if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

					Intent s = new Intent(context, GestorRadios.class);
					s.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//					 s.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

					s.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(s);
				}
			}
		}
	}
}