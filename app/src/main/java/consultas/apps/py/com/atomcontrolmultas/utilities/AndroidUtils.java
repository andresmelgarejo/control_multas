package consultas.apps.py.com.atomcontrolmultas.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andres on 29/1/2016.
 */
public class AndroidUtils {

    protected static final String TAG = AndroidUtils.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity == null){
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null){
                for (int i = 0; i < info.length; i++) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
