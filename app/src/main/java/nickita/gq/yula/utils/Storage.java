package nickita.gq.yula.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nickita.gq.yula.model.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 14/7/17.
 */
public class Storage {
    public static void writeStringToPreferences(Context context, String prefName, String content) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Values.PREFS_TAG, MODE_PRIVATE).edit();
        editor.putString(prefName, content);
        editor.commit();
    }

    public static String readStringFromPreferences(Context context, String prefName) {
        SharedPreferences prefs = context.getSharedPreferences(Values.PREFS_TAG, MODE_PRIVATE);
        return prefs.getString(prefName, "");
    }

    public static void saveUserToMemory(Context context, User user) {
        try {
            FileOutputStream myFileOutputStream = new FileOutputStream(context.getFilesDir() + Values.USER_FILENAME);
            ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
            myObjectOutputStream.writeObject(user);
            myObjectOutputStream.close();
            Log.i("Info|Storage:", "Saved to:" + context.getFilesDir());
        } catch (Exception e) {
            Log.e("Error|Storage:", Log.getStackTraceString(e));
        }
    }

    public static User readUserFromMemory(Context context) {
        User user = null;
        try {
            FileInputStream myFileInputStream = new FileInputStream(context.getFilesDir() + Values.USER_FILENAME);
            ObjectInputStream myObjectInputStream = new ObjectInputStream(myFileInputStream);
            user = (User) myObjectInputStream.readObject();
            myObjectInputStream.close();
            Log.i("Info|Storage:", "Read from:" + context.getFilesDir());
        } catch (Exception e) {
            Log.e("Error|Storage:", Log.getStackTraceString(e));
        }
        return user;
    }

    public static boolean deleteUserFromMemory(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir + Values.USER_FILENAME);
        Log.i("Info|Storage:", "Deleted:" + dir + Values.USER_FILENAME);
        return file.delete();
    }

}
