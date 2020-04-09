package techtown.org.memorypatronum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nd4j.linalg.io.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.sql.Date;
import java.util.HashSet;

public class FoodOutput_cal extends AppCompatActivity {
    MaterialCalendarView resultCalendar;

    private static String IP_ADDRESS;
    private static String TAG = "phptest";

    HashSet<CalendarDay> dates1;
    HashSet<CalendarDay> dates2;
    HashSet<CalendarDay> dates3;

    String[] calendars;
    String[] doWhat;
    String[] didList;

    String calendarDate;
    String dayOfWeek;
    String showDate;

    private UsersAdapter3 mAdapter2;//새로추가
    private TextView mTextViewResult2;//새로추가
    private String mJsonString2;//새로추가
    private ArrayList<PersonalData3> mArrayList2;//새로추가





    Toolbar myToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_output_cal);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        resultCalendar = (MaterialCalendarView) findViewById(R.id.resultCalendar);
        dates1 = new HashSet<CalendarDay>();
        dates2 = new HashSet<CalendarDay>();
        dates3 = new HashSet<CalendarDay>();

        String id = myApp.getLoginID();
        getCalendar task = new getCalendar();
        task.execute("http://" + IP_ADDRESS + "/getCalendar2.php", id);//아이디 입력하면 입력된 날짜 가져옴

        String temp1 = "";
        for(CalendarDay date : dates1){
            temp1 = temp1 + date + " ";
        }
        Log.i("dates1", temp1);


        String temp2 = "";
        for(CalendarDay date : dates2){
            temp2 = temp2 + date + " ";
        }
        Log.i("dates2", temp2);


        String temp3 = "";
        for(CalendarDay date : dates3){
            temp3 = temp3 + date + " ";
        }
        Log.i("dates3", temp3);

        //real final
        /*EventDecorator decorator = new EventDecorator(R.color.red, dates1);
        resultCalendar.addDecorator(decorator);

        EventDecorator decorator2 = new EventDecorator(R.color.yellow, dates2);
        resultCalendar.addDecorator(decorator2);

        EventDecorator decorator3 = new EventDecorator(R.color.green, dates3);
        resultCalendar.addDecorator(decorator3);*/


        resultCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
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
                task2.execute("http://" + IP_ADDRESS + "/getWhat2.php", id, calendarDate);
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
            case R.id.mic:
                Toast.makeText(getApplicationContext(), "mic clicked", Toast.LENGTH_SHORT).show();
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





    private HashSet<CalendarDay> getData (){
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();
        String id = myApp.getLoginID();

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

    private CalendarDay parsingDate (String c){

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        CalendarDay day = null;

        try{
            java.util.Date date = fm.parse(c);
            day = CalendarDay.from(date);
            //dates.add(day);

        }catch(ParseException e){
            e.printStackTrace();
        }

        return day;
    }





    class getCalendar extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FoodOutput_cal.this,
                    "달력을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            MyApplication myApp = (MyApplication)getApplication();
            IP_ADDRESS = myApp.getipAddress();
            String id = myApp.getLoginID();

            calendars = result.split("\\s");

            for(int i=0; i<calendars.length; i++){
                GetData task3 = new GetData();
                task3.execute("http://" + IP_ADDRESS + "/fac.php", id, calendars[i]);
            }





            /*dates = getData();
            EventDecorator decorator = new EventDecorator(R.color.themeColor, dates);
            resultCalendar.addDecorator(decorator);*/

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
        //ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {
            super.onPreExecute();

            /*progressDialog = ProgressDialog.show(DMemoryActivity.this,
                    "일기를 불러오는 중입니다.", null, true, true);*/
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
            if(result != ""){

                Intent intent = new Intent(getApplicationContext(), FoodResult_MIND.class);
                intent.putExtra("CALENDAR", calendarDate.toString());
                startActivity(intent);
                finish();

            }
            else{
                Toast.makeText(getApplicationContext(), "해당 날짜에 식단이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
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


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        String calendarText;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FoodOutput_cal.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult2.setText(result);
            Log.d(TAG, "GetData" +result);

            int count = StringUtils.countOccurrencesOf(result, "O");
            Log.i("count", count+"");

            CalendarDay day = parsingDate(calendarText);
            Log.i("calendarText", calendarText);
            Log.i("day", day+"");
            HashSet<CalendarDay> dates = new HashSet<>();
            dates.add(day);

            EventDecorator decorator;
            if(count==0 || count==1 || count==2){
                decorator = new EventDecorator(Color.RED, dates);
                resultCalendar.addDecorator(decorator);
            }
            else if(count==3 || count==4 || count==5){
                decorator = new EventDecorator(Color.rgb(255, 204, 0), dates);
                resultCalendar.addDecorator(decorator);
            }
            else{
                decorator = new EventDecorator(Color.GREEN, dates);
                resultCalendar.addDecorator(decorator);
            }



            /*if(result == null){
                mTextViewResult2.setText(errorString);
            }else{
                mJsonString2 = result;
                showResult2();
            }*/

            //dates = getData();

        }

        @Override
        protected String doInBackground(String... params){
            String severURL = (String)params[0];
            String id = (String)params[1];
            calendarText = (String)params[2];

            String postParameters = "&id=" + id + "&calendarText=" + calendarText;


            try {
                URL url = new URL(severURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

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
                String line;
                //String linenew;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);


                }



                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e){
                Log.d(TAG, "InsertData:Error", e);
                errorString = e.toString();
                return  null;
            }
        }
    }



    private void showResult2(){

        String TAG_JSON2="webnautes";
        String TAG_MIND="webnautes";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString2);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON2);

            for(int i=0; i<jsonArray.length(); i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String mind = item.getString(TAG_MIND);
                //
                //

                PersonalData3 personalData3 = new PersonalData3();

                personalData3.setMember_mind(mind);

                //HashMap<String,String>hashMap = new HashMap<>();

                //hashMap.put(TAG_MIND, mind);
                //
                //

                //mArrayList2.add(hashMap);
                mArrayList2.add(personalData3);
                mAdapter2.notifyDataSetChanged();
            }

            /*ListAdapter adapternew = new SimpleAdapter(
                    FoodResult_MIND.this, mArrayList2, R.layout.item_list3,
                    new String[]{TAG_MIND},
                    new int[]{R.id.textView_listMIND}
            );

            mlistView2.setAdapter(adapternew);*/
        }catch (JSONException e){

            Log.d(TAG, "showResult2 :", e);
        }

    }
















}