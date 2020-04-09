package techtown.org.memorypatronum;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import static java.lang.Thread.sleep;

public class DTestActivity2 extends AppCompatActivity {
    private static String IP_ADDRESS;
    private static String TAG = "phptest";
    String id;
    String did;
    String dateString;
    String title;
    TextView explain;

    Komoran komoran;
    ArrayList<String> morph;
    ArrayList<String> pos;
    ArrayList<String> nounmorph;
    ArrayList<String> nounpos;
    String strToAnalyze;

    Toolbar myToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_test2);
        explain = (TextView)findViewById(R.id.explainText);

        MyApplication myApp = (MyApplication)getApplication();
        IP_ADDRESS = myApp.getipAddress();

        //toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        Intent intent = getIntent();
        id = myApp.getLoginID();
        did = intent.getStringExtra("did");
        dateString = intent.getStringExtra("showDate");
        getDiary task = new getDiary();
        task.execute("http://" + IP_ADDRESS + "/getDiary.php", id, did);

        //토큰화
        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        morph = new ArrayList<>();
        pos = new ArrayList<>();
        nounmorph = new ArrayList<>(); //NNG, NNP, NR, SN, SSG를 뽑은 토큰들
        nounpos = new ArrayList<>();
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), DTestActivity.class);
                DTestActivity2.this.finish();
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeQ(View view){
        LoadingActivity task = new LoadingActivity();
        task.execute();
        KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);
        List<Token> tokenList = analyzeResultList.getTokenList();
        String[] nouns = new String[]{"NNG", "NNP", "NR", "SN", "SF"};
        String[] xs = new String[]{"XSV", "XSA", "XSN"};
        String[] stopwords = new String[]{"오늘", "어제", "내일", "요즘"};
        boolean stop = false;
        for(int i = 0; i < tokenList.size(); i++){
            stop = false;
            for (int k = 0; k < nouns.length; k++) {
                if ((tokenList.get(i).getPos()).equals(nouns[k])) {
                    if(i+1 < tokenList.size()){ //접미사 처리
                        if((tokenList.get(i+1).getPos()).equals(xs[0]) || (tokenList.get(i+1).getPos()).equals(xs[1])){
                            break;
                        }
                    }

                    for(String s : stopwords){ //일기 특성상 자주 등장하여 의미가 없는 불용어
                        if((tokenList.get(i).getMorph()).equals(s)){
                            stop = true;
                            break;
                        }
                    }
                    if(!stop){
                        nounmorph.add(tokenList.get(i).getMorph());
                        nounpos.add(tokenList.get(i).getPos());
                    }
                    break;
                }
            }
        }
        komoran = null;

        GetKeyword2 getKeyword2 = new GetKeyword2(nounmorph, nounpos);
        int size = getKeyword2.cooccurrence(2);
        HashMap<String, Float> resultHash;
        resultHash = getKeyword2.textrank(0.85f, 20);
        //TF-IDF 계산
        /*GetKeyword getKeyword = new GetKeyword(nounmorph, nounpos);
        int size = getKeyword.calcualteTFIDF();
        HashMap<String, Float> resultHash;
        resultHash = getKeyword.textrank(0.85f);*/
        String temp = "";
        for(String key : resultHash.keySet()){
            temp = temp + key + " " + resultHash.get(key) + "/";
        }
        Log.i("result", temp);
        List<String> resultString = keywordSort(resultHash);
        ArrayList<String> keyword = new ArrayList<>();
        int limit = Math.round(resultString.size() * 0.2f); //상위 20%의 키워드
        for(int i = 0; i < resultString.size(); i++){
            if(i == 5)  break;
            else{
                keyword.add(resultString.get(i));
                Log.i("keyword", resultString.get(i));
            }
        }

        Intent intent = new Intent(getApplicationContext(), DTestActivity3.class);
        intent.putStringArrayListExtra("keyword", keyword);
        intent.putExtra("contents", strToAnalyze);
        intent.putExtra("size", size);
        intent.putExtra("showDate", dateString);
        intent.putExtra("title", title);
        startActivity(intent);
        finish();
    }

    public static List<String> keywordSort(HashMap<String, Float> map){
        List<String> list = new ArrayList<>();
        list.addAll(map.keySet());

        Collections.sort(list, (o1, o2) -> (map.get(o2).compareTo(map.get(o1)))); //내림차순
        return list;
    }

    //DB에서 가져오기
    class getDiary extends AsyncTask<String, Void, String> {
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
            Log.d(TAG, "POST response  - " + result);

            String[] diary = result.split("</br>");
            title = diary[0];
            String temp = "'" + title + "'\n 회상테스트를 시작하려면\n 아래 버튼을 누르세요.";
            explain.setText(temp);
            strToAnalyze = diary[1];
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

    private class LoadingActivity extends AsyncTask<Void, Void, Void>{
        ProgressDialog asyncDialog = new ProgressDialog(DTestActivity2.this);

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
