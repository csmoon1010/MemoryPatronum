package techtown.org.memorypatronum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

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
    ActionMenuItemView micItem;

    private static String IP_ADDRESS;
    private static String TAG = "phptest";

    String id;
    String did;
    String dateString;

    Intent speechIntent;
    SpeechRecognizer mRecognizer;
    VoiceRecognition vRecognizer;
    Drawable on;
    Drawable off;
    EditText currentText;
    String existText;
    int position;

    Toolbar myToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory3);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

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

    //toolbar에 main_toolbar.xml 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), DMemoryActivity2.class);
                i.putExtra("did", did);
                i.putExtra("showDate", dateString);
                DMemoryActivity3.this.finish();
                startActivity(i);
                break;
            case R.id.mic:
                int currentView = getCurrentFocus().getId();
                currentText = (EditText)findViewById(currentView);
                existText = currentText.getText().toString();
                position = currentText.getSelectionStart();
                on = getResources().getDrawable(R.drawable.ic_mic_black_18dp, null);
                off = getResources().getDrawable(R.drawable.ic_mic_off_black_18dp, null);
                micItem = (ActionMenuItemView) findViewById(R.id.mic);
                speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                vRecognizer = new DMemoryActivity3.vRecog(speechIntent, mRecognizer, getApplicationContext(), micItem, 1, on, off);
                vRecognizer.checkPermission(DMemoryActivity3.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //음성인식
    class vRecog extends VoiceRecognition{
        vRecog(Intent i, SpeechRecognizer r, Context c, ActionMenuItemView itm, int m, Drawable on, Drawable off){
            super(i, r, c, itm, m, on, off);
        }

        @Override
        public void checkVoice(String s) {
            Log.i("dwrite", "message" + s);
            if(existText.isEmpty()){
                currentText.setText(s);
                currentText.requestFocus();
            }
            else    currentText.setText(existText.substring(0, position) + s + existText.substring(position));
            currentText.setSelection(position + s.length());
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(vRecognizer != null){
            vRecognizer = null;
        }
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
