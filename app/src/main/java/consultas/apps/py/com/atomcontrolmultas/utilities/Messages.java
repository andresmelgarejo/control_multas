package consultas.apps.py.com.atomcontrolmultas.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Andres on 29/1/2016.
 */
public class Messages {

    public static void alertMessage(final Context context, final String mensaje){
        try{
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Mensaje");
            dialog.setMessage(mensaje)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialog.create();
            dialog.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void messageToastCenter(final Context context, final String mensaje){
        Toast toast;
        toast = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
