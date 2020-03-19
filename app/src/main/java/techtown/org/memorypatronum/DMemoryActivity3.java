package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//수정창
public class DMemoryActivity3 extends AppCompatActivity {
    EditText titleText;
    TextView dateText;
    EditText contentsText;

    private static String IP_ADDRESS;
    private static String TAG = "phptest";

    String id;
    String did;
    String dateString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory3);
        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        titleText = (EditText) findViewById(R.id.editTitle);
        dateText = (TextView)findViewById(R.id.editDate);
        contentsText = (EditText) findViewById(R.id.editContents);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        did = intent.getStringExtra("did");
        dateString = intent.getStringExtra("date");
        dateText.setText(dateString);
        getDiary task = new getDiary();
        task.execute("http://" + IP_ADDRESS + "/getDiary.php", id, did);
    }
    public void onEditCancelClick(View view){
        Intent i = new Intent(getApplicationContext(), DMemoryActivity2.class);
        i.putExtra("did", did);
        i.putExtra("showDate", dateString);
        DMemoryActivity3.this.finish();
        startActivity(i);
    }
    public void onEditCompleteClick(View view){
        updateDiary task2 = new updateDiary();
        task2.execute("http://" + IP_ADDRESS + "/updateDiary.php", id, did,
                titleText.getText().toString(), contentsText.getText().toString());
    }

    class getDiary extends AsyncTask<String, Void, String> {

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "POST response  - " + result);

            String[] diary = result.split("</br>");
            titleText.setText(diary[0]);
            contentsText.setText(diary[1]);
        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String did = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&did=" + did;


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

                Log.d(TAG, "getCalendar: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    class updateDiary extends AsyncTask<String, Void, String> {

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "POST response  - " + result);
            Intent i = new Intent(getApplicationContext(), DMemoryActivity2.class);
            i.putExtra("did", did);
            i.putExtra("showDate", dateString);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            startActivity(i);
            DMemoryActivity3.this.finish();
        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String did = (String)params[2];
            String title = (String)params[3];
            String contents = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&did=" + did + "&title=" + title + "&contents=" + contents;


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

                Log.d(TAG, "getCalendar: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}