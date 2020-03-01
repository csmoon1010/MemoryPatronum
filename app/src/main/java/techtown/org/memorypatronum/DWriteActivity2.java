package techtown.org.memorypatronum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

public class DWriteActivity2 extends AppCompatActivity {
    public String[] questions;
    String[] answers;
    Integer qNum;
    TextView questionText;
    EditText answerText;

    private static String IP_ADDRESS = "192.168.219.183";
    private static String TAG = "phptest";

    public String calendarText;
    Date calendarDate;

    public String diaryNum;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write2);
        qNum = 0;
        questionText = (TextView)findViewById(R.id.questionText);
        answerText = (EditText) findViewById(R.id.answerText);

        questions = new String[7];
        questions[0] = "무엇을 했나요? \n한 가지만 골라 간략히 적어주세요.";
        questions[1] = "1. 누구와 함께 했나요?";
        questions[2] = "2. 어디서 했나요?";
        questions[3] = "3. 몇 시에 했나요?";
        questions[4] = "4. 어떤 옷을 입었나요?";
        questions[5] = "5. 날씨는 어땠나요?";
        questions[6] = "6. 그 일에 대해 기록하고 싶은 것들을\n더 적어주세요.";
        questionText.setText(questions[qNum]);

        answers = new String[7];
        for(int i = 0; i < 7; i++){
            answers[i] = "";
        }

        Intent intent = getIntent();
        calendarText = intent.getStringExtra("CALENDAR");
        calendarDate = java.sql.Date.valueOf(calendarText);


    }

    public void onNextClick2(View view){
        answers[qNum] = String.valueOf(answerText.getText());
        Log.d("qNUM", qNum+"");
        Log.d("answer", answers[qNum]);
        if(answers[qNum].equals("")){ //내용 없음을 표현할 수 있는 방법 찾아보기
            Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(qNum < 6){
                qNum++;
                questionText.setText(questions[qNum]);
                answerText.setText(answers[qNum]);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("저장하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //이제까지 쓴 것 DB에 저장(answers 배열)
                        MyApplication myApp = (MyApplication)getApplication();
                        String id = myApp.getLoginID();

                        getDiaryNum task1 = new getDiaryNum();
                        task1.execute("http://" + IP_ADDRESS + "/getMembers.php", id);

                        //Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
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
    }

    public void onPreviousClick(View view){
        if(qNum > 0){
            qNum--;
            questionText.setText(questions[qNum]);
            answerText.setText(answers[qNum]);
        }
        else{
            finish();
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
            String what = (String)params[4];
            String who = (String)params[5];
            String location = (String)params[6];
            String hour = (String)params[7];
            String clothes= (String)params[8];
            String weather = (String)params[9];
            String etc = (String)params[10];

            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&did=" + did + "&calendar=" + calendar +
                    "&what=" + what + "&who=" + who + "&location=" + location + "&hour=" + hour +
                    "&clothes=" + clothes + "&weather=" + weather + "&etc=" + etc;


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
            String what = answers[0];
            String who = answers[1];
            String location = answers[2];
            String hour = answers[3];
            String clothes = answers[4];
            String weather = answers[5];
            String etc = answers[6];
            DiaryInsertData task2 = new DiaryInsertData();
            task2.execute("http://" + IP_ADDRESS + "/diary_insert.php", id, did, calendar,
                    what, who, location, hour, clothes, weather, etc);
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

