package techtown.org.memorypatronum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import techtown.org.memorypatronum.MainActivity;
import techtown.org.memorypatronum.R;


public class LoginActivity extends AppCompatActivity {
    private static String IP_ADDRESS;
    private static String TAG = "phptest";
    String id;
    String password;


    private EditText mEditTextID;
    private EditText mEditTextPassword;
    private TextView mTextViewResult;

    @Override
    @SuppressWarnings("unused")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 없애기
        setContentView(R.layout.activity_login);

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();
        mEditTextID = (EditText)findViewById(R.id.editText_main_id);
        mEditTextPassword = (EditText)findViewById(R.id.editText_main_password);

        /*mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());*/

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("unused")
            public void onClick(View v) {
                String id = mEditTextID.getText().toString();
                String password = mEditTextPassword.getText().toString();

                MyApplication myApp = (MyApplication)getApplication();
                myApp.setLoginID(id);
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/new.php", id, password);

                mEditTextID.setText("");
                mEditTextPassword.setText("");
            }
        });

    }



    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "로그인 중입니다.", null, true, true);
        }


        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
            String[] login = result.split("</br>");
            if(login[0].equals("로그인 성공")){
                Toast.makeText(getApplicationContext(), login[1], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                SharedPreference.setAccount(LoginActivity.this, id);
                LoginActivity.this.finish();
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                mEditTextID.setText("");
                mEditTextPassword.setText("");
            }
        }


        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {

            id = (String)params[1];
            password = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&password=" + password;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    public void onRegisterClick(View view){
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
    }

}