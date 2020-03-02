package techtown.org.memorypatronum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DMemoryActivity2 extends AppCompatActivity {
    ListView list;
    String[] titles = {
            "날짜/요일",
            "무엇을 했나요? \n한 가지만 골라 간략히 적어주세요.",
            "1. 누구와 함께 했나요?",
            "2. 어디서 했나요?",
            "3. 몇 시에 했나요?",
            "4. 어떤 옷을 입었나요?",
            "5. 날씨는 어땠나요?",
            "6. 그 일에 대해 기록하고 싶은 것들을\n더 적어주세요."
    };
    String[] contents = new String[8];

    private static String IP_ADDRESS = "192.168.219.183";
    private static String TAG = "phptest";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory2);

        for(int i = 0; i < 8; i++){
            contents[i] = "";
        }

        Intent intent = getIntent();
        contents[0] = intent.getStringExtra("CalendarDate") + " / " + intent.getStringExtra("dayOfWeek");

        MyApplication myApp = (MyApplication)getApplication();
        String id = myApp.getLoginID();
        getDiary task = new getDiary();
        String did = intent.getStringExtra("did");
        task.execute("http://" + IP_ADDRESS + "/getDiary.php", id, did);
    }

    public class DiaryList extends ArrayAdapter<String> {
        private final Activity context;
        public DiaryList(Activity context){
            super(context, R.layout.listview_diary, titles);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_diary, null, true);
            TextView title = (TextView)rowView.findViewById(R.id.titleText);
            TextView content = (TextView)rowView.findViewById(R.id.contentText);

            title.setText(titles[position]);
            content.setText(contents[position]);
            return rowView;
        }
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
            for(int i = 0; i < diary.length && (i+1) < contents.length; i++){
                contents[i+1] = diary[i];
            }
            DiaryList adapter = new DiaryList(DMemoryActivity2.this);
            list =  (ListView)findViewById(R.id.diaryList);
            list.setAdapter(adapter);
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

