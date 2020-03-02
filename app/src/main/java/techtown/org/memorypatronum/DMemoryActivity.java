package techtown.org.memorypatronum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.sql.Date;
import java.util.HashSet;

public class DMemoryActivity extends AppCompatActivity {
    MaterialCalendarView memoryCalendar;

    private static String IP_ADDRESS = "192.168.219.183";
    private static String TAG = "phptest";

    HashSet<CalendarDay> dates;
    String[] calendars;
    String[] doWhat;

    CalendarDay calendarDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory1);
        memoryCalendar = (MaterialCalendarView) findViewById(R.id.memoryCalendar);
        dates = new HashSet<CalendarDay>();

        MyApplication myApp = (MyApplication)getApplication();
        String id = myApp.getLoginID();
        getCalendar task = new getCalendar();
        task.execute("http://" + IP_ADDRESS + "/getCalendar.php", id);


        memoryCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                MyApplication myApp = (MyApplication)getApplication();
                String id = myApp.getLoginID();
                calendarDay = date;

                Date selectedDate = new Date(date.getDate().getTime());
                String calendar = selectedDate.toString();
                Toast.makeText(getApplicationContext(), calendar, Toast.LENGTH_SHORT).show();
                getWhat task2 = new getWhat();
                task2.execute("http://" + IP_ADDRESS + "/getWhat.php", id, calendar);
            }
        });
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
        //DB에서 받아오기

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
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DMemoryActivity.this,
                    "달력을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            calendars = result.split("\\s");
            dates = getData();
            EventDecorator decorator = new EventDecorator(Color.RED, dates);
            memoryCalendar.addDecorator(decorator);
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
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DMemoryActivity.this,
                    "일기를 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
            doWhat = result.split("</br>"); //</br>을 기준으로 자르는 방식

            AlertDialog.Builder builder = new AlertDialog.Builder(DMemoryActivity.this);
            builder.setTitle("일기목록");
            // DB에서 일기 제목(무엇) 받아오고 다음 액티비티로 date 보냄
            builder.setItems(doWhat, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), DMemoryActivity2.class);
                    intent.putExtra("YEAR", calendarDay.getYear());
                    intent.putExtra("MONTH", calendarDay.getMonth());
                    intent.putExtra("DAY", calendarDay.getDay());
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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
}
