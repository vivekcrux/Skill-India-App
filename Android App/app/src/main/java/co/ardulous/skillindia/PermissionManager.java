package co.ardulous.skillindia;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;

/*
* Class to Manage all Permissions for the App
* Used to Check for permissions
* Ask for Permissions
* And to define what is to be done in response for Permissions
*/
public class PermissionManager {

    //Interface used to define The Logic to be implemented in response for PermissionResult
    //Is implemented where a new Instance of the Class is to be created
    public interface PermissionResultListener {
        void onGranted(String permission);
        void onDenied(String permission);
    }

    //Stores all the PermissionResultListeners defined in the app
    private static ArrayList<PermissionResultListener> listenerList = new ArrayList<>();

    //The function used when user needs to ask/check for new permissions
    public static void askForPermission(Context context, String[] permissions, PermissionResultListener listener) {
        //Stores the new PermissionResultListener in the ListenerList
        int requestCode = listenerList.size();
        listenerList.add(listener);

        //Asks for permission from system
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }

    //Defines the Logic to be executed on receiving Permission Results
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        try {
            PermissionResultListener resultListener = listenerList.get(requestCode);

            Log.d("TAG", "onRequestPermissionsResult: "+requestCode+" "+resultListener);

            for(int i = 0; i < permissions.length; i++) {
                if(results[i] == PackageManager.PERMISSION_GRANTED)
                    resultListener.onGranted(permissions[i]);
                else
                    resultListener.onDenied(permissions[i]);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("TAG", "onRequestPermissionsResult: Listener Missing");
        }
    }
}
