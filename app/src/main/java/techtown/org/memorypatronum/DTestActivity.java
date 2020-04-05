package techtown.org.memorypatronum;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class DTestActivity extends AppCompatActivity {
    MaterialCalendarView testCalendar;

    private static String IP_ADDRESS;
    private static String TAG = "phptest";

    HashSet<CalendarDay> dates;
    String[] calendars;
    String[] doWhat;
    String[] didList;

    String calendarDate;
    String dayOfWeek;
    String showDate;

    Toolbar myToolbar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_test1);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        testCalendar = (MaterialCalendarView)findViewById(R.id.testCalendar);
        progressDialog = new ProgressDialog(DTestActivity.this);
        dates = new HashSet<CalendarDay>();
        String id = myApp.getLoginID();
        getCalendar task = new getCalendar();
        task.execute("http://" + IP_ADDRESS + "/getCalendar.php", id);

        testCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                MyApplication myApp = (MyApplication)getApplication();
                String id = myApp.getLoginID();

                Date selectedDate = new Date(date.getDate().getTime());
                calendarDate = selectedDate.toString();

                //요일 계산
                int year = date.getYear();
                int month = date.getMonth();
                int dayOfMonth = date.getDay();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
                String[] dayString =  {"일요일", "월요일", "화요일", "수요일",
                        "목요일", "금요일", "토요일"};
                dayOfWeek = dayString[dayNum-1];
                showDate = year + "년 " + month + "월 " + dayOfMonth + "일 " + dayOfWeek;
                getWhat task2 = new getWhat();
                task2.execute("http://" + IP_ADDRESS + "/getWhat.php", id, calendarDate);
            }
        });
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(10, color));
        }
    }

    private HashSet<CalendarDay> getData(){
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 0; i < calendars.length; i++){
            try{
                java.util.Date date = fm.parse(calendars[i]);
                CalendarDay day = CalendarDay.from(date);
                dates.add(day);
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
        return dates;
    }
    class getCalendar extends AsyncTask<String, Void, String> {

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

            calendars = result.split("\\s");
            dates = getData();
            EventDecorator decorator = new EventDecorator(R.color.themeColor, dates);
            testCalendar.addDecorator(decorator);
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

                Log.d(TAG, "getCalendar: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    class getWhat extends AsyncTask<String, Void, String> {

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
            if(result != ""){
                String[] temp = result.split("</br>");
                doWhat = new String[temp.length];
                didList = new String[temp.length];

                for(int i = 0; i < temp.length; i++){
                    String[] temp2 = temp[i].split("<did>");
                    doWhat[i] = temp2[0];
                    didList[i] = temp2[1];
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DTestActivity.this);
                builder.setTitle("일기목록");
                // DB에서 일기 제목(무엇) 받아오고 다음 액티비티로 date 보냄
                builder.setItems(doWhat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), DTestActivity2.class);
                        //test할 did, showDate보내기
                        intent.putExtra("did", didList[i]);
                        intent.putExtra("showDate", showDate);
                        LoadingActivity task = new LoadingActivity();
                        task.execute();
                        DTestActivity.this.finish();
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else{
                Toast.makeText(getApplicationContext(), "해당 날짜에 일기가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String calendar = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&calendar=" + calendar;


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

                Log.d(TAG, "getWhat: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    private class LoadingActivity extends AsyncTask<Void, Void, Void>{
        ProgressDialog asyncDialog = new ProgressDialog(DTestActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage("로딩중입니다.");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

