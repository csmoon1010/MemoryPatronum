package techtown.org.memorypatronum;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import static java.lang.Thread.sleep;

//저장된 일기 창
public class DMemoryActivity2 extends AppCompatActivity {
    String dateString;
    String id;
    String did;
    EditText titleText;
    TextView contentsText;
    TextView dateText;
    private static String IP_ADDRESS;
    private static String TAG = "phptest";

    TextToSpeech tts;

    Toolbar myToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory2);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        titleText = (EditText) findViewById(R.id.memoryTitle);
        dateText = (TextView)findViewById(R.id.memoryDate);
        contentsText = (TextView)findViewById(R.id.memoryContents);
        contentsText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        dateString = intent.getStringExtra("showDate");
        dateText.setText(dateString);

        id = myApp.getLoginID();
        did = intent.getStringExtra("did");
        getDiary task = new getDiary();
        task.execute("http://" + IP_ADDRESS + "/getDiary.php", id, did);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    int language = tts.setLanguage(Locale.KOREAN);
                    if(language == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getApplicationContext(), "지원하지 않는 언어", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "TTS작업 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //toolbar에 main_toolbar.xml 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_toolbar2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), DMemoryActivity.class);
                DMemoryActivity2.this.finish();
                startActivity(i);
                break;
            case R.id.sound:
                Toast.makeText(getApplicationContext(), "음성 출력", Toast.LENGTH_SHORT).show();
                //음성출력
                String speech1 = "일기를 읽어드립니다";
                String speech2 = titleText.getText().toString();
                String speech3 = contentsText.getText().toString();
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) { //권한허용 안함 --> 권한요청
                    ActivityCompat.requestPermissions(DMemoryActivity2.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
                else{ //권한 허용함
                    try{
                        Log.i("speech", speech3);
                        tts.setPitch(1.0f);
                        tts.setSpeechRate(1.0f);
                        tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null, null);
                        try{
                            sleep(3000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        tts.speak(speech2, TextToSpeech.QUEUE_FLUSH, null, null);
                        try{
                            sleep(3000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        tts.speak(speech3, TextToSpeech.QUEUE_FLUSH, null, null);
                    }catch(SecurityException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDestroy(){
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void onEditClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("내용을 수정하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent editIntent = new Intent(getApplicationContext(), DMemoryActivity3.class);
                editIntent.putExtra("id", id);
                editIntent.putExtra("did", did);
                editIntent.putExtra("date", dateString);
                DMemoryActivity2.this.finish();
                startActivity(editIntent);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void onDeleteClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("일기를 삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteDiary deleteTask = new deleteDiary();
                deleteTask.execute("http://" + IP_ADDRESS + "/deleteDiary.php", id, did);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    class getDiary extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DMemoryActivity2.this,
                    "일기를 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
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

    class deleteDiary extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DMemoryActivity2.this,
                    "일기를 삭제하는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), DMemoryActivity.class);
            DMemoryActivity2.this.finish();
            startActivity(i);
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
}

