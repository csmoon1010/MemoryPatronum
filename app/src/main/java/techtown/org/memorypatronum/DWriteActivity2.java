package techtown.org.memorypatronum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DWriteActivity2 extends AppCompatActivity {
    String titleString;
    String contentsString;
    EditText titleText;
    EditText contentsText;
    TextView dateText;

    private static String IP_ADDRESS;
    private static String TAG = "phptest";

    public String calendarText;
    //Date calendarDate;

    public String diaryNum;
    Intent intent;

    Toolbar myToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write2);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        titleText = (EditText)findViewById(R.id.diaryTitle);
        contentsText = (EditText)findViewById((R.id.diaryContents));
        dateText = (TextView)findViewById(R.id.diaryDate);

        Intent intent = getIntent();
        calendarText = intent.getStringExtra("CALENDAR");
        dateText.setText(intent.getStringExtra("showDate"));
        //calendarDate = java.sql.Date.valueOf(calendarText);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("작성을 취소하시겠습니까?\n취소하면 작성했던 모든 내용이 사라집니다.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DWriteActivity2.this.finish();
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
                break;
            case R.id.mic:
                Toast.makeText(getApplicationContext(), "mic clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNextClick2(View view){
        titleString = String.valueOf(titleText.getText());
        contentsString = String.valueOf(contentsText.getText());
        if(titleString.equals(""))
            Toast.makeText(getApplicationContext(), "제목을 작성해주세요.", Toast.LENGTH_SHORT).show();
        else if(contentsString.equals(""))
            Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("저장하시겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyApplication myApp = (MyApplication) getApplication();
                    String id = myApp.getLoginID();

                    getDiaryNum task1 = new getDiaryNum();
                    task1.execute("http://" + IP_ADDRESS + "/getMembers.php", id);
                }
            });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    class DiaryInsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DWriteActivity2.this,
                    "저장중입니다", null, true, true);
        }


        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "POST response  - " + result);
            finish();
        }


        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String did = (String)params[2];
            String calendar = (String)params[3];
            String title = (String)params[4];
            String contents = (String)params[5];

            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&did=" + did + "&calendar=" + calendar +
                    "&title=" + title + "&contents=" + contents;

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

    class getDiaryNum extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            diaryNum = String.valueOf(Integer.parseInt(result)+1);
            MyApplication myApp = (MyApplication)getApplication();
            String id = myApp.getLoginID();
            String did = diaryNum;
            String calendar = calendarText;
            String title = titleString;
            String contents = contentsString;
            DiaryInsertData task2 = new DiaryInsertData();
            task2.execute("http://" + IP_ADDRESS + "/diary_insert.php", id, did, calendar, title, contents);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String serverURL = (String)params[0];
            String postParameters = "&id=" + id;


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
}

