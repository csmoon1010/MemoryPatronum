package techtown.org.memorypatronum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

//저장된 일기 창
public class FoodResult_MIND extends AppCompatActivity {
    //String dateString;
    String calendar;
    //String id;
    //String did;
    //TextView titleText;
    //TextView contentsText;
    private ArrayList<PersonalData2> mArrayList;
    private UsersAdapter2 mAdapter;
    private RecyclerView mRecyclerView;
    //private RecyclerView mRecyclerView2;
    private TextView mTextViewResult;
    private String mJsonString;


    /*private TextView mTextViewResult2;
    ArrayList<HashMap<String, String>> mArrayList2;
    ListView mlistView2;
    String mJsonString2;*/
    private ArrayList<PersonalData3> mArrayList2;
    private UsersAdapter3 mAdapter2;
    private RecyclerView mRecyclerView2;
    private TextView mTextViewResult2;
    private String mJsonString2;










    //TextView dateText;
    private static String IP_ADDRESS = "memorypatronum.dothome.co.kr";
    private static String TAG = "phptest";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_result__mind);

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mArrayList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UsersAdapter2(this,mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(mRecyclerView);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        /*ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);*/

        //2번째 출력할때 나오는 애들!!
        mTextViewResult2 = (TextView)findViewById(R.id.textView_main_result2);

        //mlistView2 = (ListView) findViewById(R.id.listView_main_list2);
        mArrayList2 = new ArrayList<>();
        mRecyclerView2 = (RecyclerView) findViewById(R.id.listView_main_list2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mAdapter2 = new UsersAdapter3(this, mArrayList2);
        mRecyclerView2.setAdapter(mAdapter2);
        mTextViewResult2.setMovementMethod(new ScrollingMovementMethod());




        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        //titleText = (TextView)findViewById(R.id.memoryTitle);
        //dateText = (TextView)findViewById(R.id.memoryDate);
        //contentsText = (TextView)findViewById(R.id.foodContents);

        Intent intent = getIntent();
        //dateString = intent.getStringExtra("showDate");
        calendar = intent.getStringExtra("CALENDAR");
        String calendarText = calendar;
        //dateText.setText(dateString);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        //id = myApp.getLoginID();
        //did = intent.getStringExtra("did");
        getFood task = new getFood();
        task.execute("http://" + IP_ADDRESS + "/querydate.php", calendarText);

        mArrayList2.clear();
        mAdapter2.notifyDataSetChanged();

        GetData task2 = new GetData();
        task2.execute("http://" + IP_ADDRESS + "/fac.php", calendarText);







    }
    public void onPreviousClick(View view){
        Intent i = new Intent(getApplicationContext(), FoodOutput_cal.class);
        FoodResult_MIND.this.finish();
        startActivity(i);
    }


    class getFood extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {

        super.onPreExecute();

            //progressDialog = ProgressDialog.show(FoodResult_MIND.this,
                    //"식단을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            //Log.d(TAG, "POST response  - " + result);
            //String foodmorning_final = result;
            //contentsText.setText(foodmorning_final); 이거 쓰고아래줄안썼을때 잘나옴

            //progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }


        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            //String id = (String)params[1];
            //String did = (String)params[2];
            String calendarText = (String)params[1];
            //String type = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "calendarText=" + calendarText;


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

    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_CURNAMENEW = "curName_new";
        String TAG_TYPE = "type";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String curName_new = item.getString(TAG_CURNAMENEW);

                String type = item.getString(TAG_TYPE);


                PersonalData2 personalData2 = new PersonalData2();


                personalData2.setMember_type(type);
                personalData2.setMember_curName_new(curName_new);


                mArrayList.add(personalData2);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int i = viewHolder.getAdapterPosition();
            //Toast.makeText(getApplicationContext(), mArrayList.get(i).getMember_curName_new(), Toast.LENGTH_SHORT).show();

            String curName_new = mArrayList.get(i).getMember_curName_new();
            String type = mArrayList.get(i).getMember_type();
            mArrayList.remove(i);
            //mArrayList.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();

            DeleteData task3 = new DeleteData();
            task3.execute("http://" + IP_ADDRESS + "/deleteFood_morning.php", calendar, curName_new, type);

            DeleteData2 task4 = new DeleteData2();
            task4.execute("http://" + IP_ADDRESS + "/deleteFood_lunch.php", calendar, curName_new, type);

            DeleteData3 task5 = new DeleteData3();
            task5.execute("http://" + IP_ADDRESS + "/deleteFood_dinner.php", calendar, curName_new, type);

            DeleteData4 task6 = new DeleteData4();
            task6.execute("http://" + IP_ADDRESS + "/deleteFood_snack.php", calendar, curName_new, type);
            //task3.execute("http://" + IP_ADDRESS + "/deleteFood")

        }
    };



    class DeleteData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {

            super.onPreExecute();

            //progressDialog = ProgressDialog.show(FoodResult_MIND.this,
            //"식단을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            //Log.d(TAG, "POST response  - " + result);
            //String foodmorning_final = result;
            //contentsText.setText(foodmorning_final); 이거 쓰고아래줄안썼을때 잘나옴

            //progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                //mJsonString = result;
                //showResult();
            }


        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            //String id = (String)params[1];
            //String did = (String)params[2];
            String calendarText = (String)params[1];
            String curName_new = (String)params[2];
            String type=(String)params[3];
            //String type = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "calendarText="+ calendarText+ "&curName_new=" + curName_new +"&type=" + type;


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


    class DeleteData2 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {

            super.onPreExecute();

            //progressDialog = ProgressDialog.show(FoodResult_MIND.this,
            //"식단을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            //Log.d(TAG, "POST response  - " + result);
            //String foodmorning_final = result;
            //contentsText.setText(foodmorning_final); 이거 쓰고아래줄안썼을때 잘나옴

            //progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                //mJsonString = result;
                //showResult();
            }


        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            //String id = (String)params[1];
            //String did = (String)params[2];
            String calendarText = (String)params[1];
            String curName_new = (String)params[2];
            String type=(String)params[3];
            //String type = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "calendarText="+ calendarText+ "&curName_new=" + curName_new +"&type=" + type;


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


    class DeleteData3 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {

            super.onPreExecute();

            //progressDialog = ProgressDialog.show(FoodResult_MIND.this,
            //"식단을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            //Log.d(TAG, "POST response  - " + result);
            //String foodmorning_final = result;
            //contentsText.setText(foodmorning_final); 이거 쓰고아래줄안썼을때 잘나옴

            //progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                //mJsonString = result;
                //showResult();
            }


        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            //String id = (String)params[1];
            //String did = (String)params[2];
            String calendarText = (String)params[1];
            String curName_new = (String)params[2];
            String type=(String)params[3];
            //String type = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "calendarText="+ calendarText+ "&curName_new=" + curName_new +"&type=" + type;


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


    class DeleteData4 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        @SuppressWarnings("unused")
        protected void onPreExecute() {

            super.onPreExecute();

            //progressDialog = ProgressDialog.show(FoodResult_MIND.this,
            //"식단을 불러오는 중입니다.", null, true, true);
        }

        @Override
        @SuppressWarnings("unused")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            //Log.d(TAG, "POST response  - " + result);
            //String foodmorning_final = result;
            //contentsText.setText(foodmorning_final); 이거 쓰고아래줄안썼을때 잘나옴

            //progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                //mJsonString = result;
                //showResult();
            }


        }

        @Override
        @SuppressWarnings("unused")
        protected String doInBackground(String... params) {
            //String id = (String)params[1];
            //String did = (String)params[2];
            String calendarText = (String)params[1];
            String curName_new = (String)params[2];
            String type=(String)params[3];
            //String type = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "calendarText="+ calendarText+ "&curName_new=" + curName_new +"&type=" + type;


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








    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FoodResult_MIND.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult2.setText(result);
            Log.d(TAG, "POST response - " +result);

            if(result == null){
                mTextViewResult2.setText(errorString);
            }else{
                mJsonString2 = result;
                showResult2();
            }
        }

        @Override
        protected String doInBackground(String... params){
            String severURL = (String)params[0];
            String calendarText = (String)params[1];

            String postParameters = "calendarText=" + calendarText;


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
