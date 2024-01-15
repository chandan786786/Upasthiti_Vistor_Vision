package Upasthiti.vistor_vision.in.eAttendance.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import Upasthiti.vistor_vision.in.eAttendance.Activity.LoginActivity;
import Upasthiti.vistor_vision.in.eAttendance.Activity.MainActivity;
import Upasthiti.vistor_vision.in.eAttendance.Utilitites.CommonPref;
import Upasthiti.vistor_vision.in.eAttendance.Utilitites.GlobalVariables;
import Upasthiti.vistor_vision.in.eAttendance.Utilitites.Utiilties;
import Upasthiti.vistor_vision.in.eAttendance.database.WebServiceHelper;


public class LogoutService extends AsyncTask<String, Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;
    public LogoutService(Activity activity){
       this.activity=activity;
        dialog1= new ProgressDialog(this.activity);
        alertDialog=new AlertDialog.Builder(this.activity).create();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog1.setMessage("Loading...");
        dialog1.setCancelable(false);
        dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        if (Utiilties.isOnline(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                result = WebServiceHelper.logoutempolys(strings);
            }else{
                alertDialog.setMessage("Your device must have atleast Kitkat or Above Version");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }else{
            Toast.makeText(activity, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        //SUCCESS Your User Id is Mobile No and Password :- Gr@ba993123
        if (dialog1.isShowing())dialog1.dismiss();
        if (res==null){
            alertDialog.setMessage("Something went wrong !");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }else{
            if (res.contains("SUCCESS")){
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Log Out Successful !\n"+res);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CommonPref.clearLog(activity);
                        CommonPref.clearpropic(activity);
                        GlobalVariables.isLogged = false;
                        GlobalVariables.UserId = "";
                        Intent i = new Intent(activity, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                        activity.finish();
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }else{
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Log Out Unsuccessful !\n"+res);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }
    }
}
