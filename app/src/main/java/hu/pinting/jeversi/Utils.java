package hu.pinting.jeversi;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Utils {
    /**
     * Show a message on the screen.
     * @param message
     */
    public static void showMessage(Context context, String message) {
        if(context == null) {
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Show a message in a dialog box on the screen.
     * @param message
     */
    public static void showDialog(Context context, String message) {
        if(context == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message).show();
    }
}
