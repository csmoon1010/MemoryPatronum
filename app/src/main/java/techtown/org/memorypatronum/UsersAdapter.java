package techtown.org.memorypatronum;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.CollationElementIterator;
import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private static String IP_ADDRESS = "memorypatronum.dothome.co.kr";
    private static String TAG = "phpexample";
    private TextView mTextViewResult;

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;
    //private String calendarText = null;

    public String calendarText;
    //public String[] listItems;
    public int i;
    public String id;
    public String foodNum;
    public String fid;
    public String curName;
    public String calendar;

    public TextView RealtimeInsert;








    public UsersAdapter(Activity context, ArrayList<PersonalData> list, String calendarText, int i, String id, TextView RealtimeInsert) {
        this.RealtimeInsert = RealtimeInsert;
        this.calendarText = calendarText;
        this.i = i;
        this.id = id;

        this.context = context;
        this.mList = list;

    }



    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;


        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.textView_list_name);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, int position) {


        viewholder.name.setText(mList.get(position).getMember_name());

        viewholder.itemView.setTag(position);
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curName = viewholder.name.getText().toString();
                //String curName2 = curName;
                calendar = calendarText;

                //먼저 getMembers_food.php에서 fid를 받아와야함. 그 다음에 InsertData 수행


                getfid task2 = new getfid();
                task2.execute("http://" + IP_ADDRESS + "/getMembers_food.php", id);

                /*InsertData task = new InsertData();

                switch (i) {
                    case 0:
                        task.execute("http://" + IP_ADDRESS + "/insert_food0.php", id, fid, calendar, curName);
                        break;

                    case 1:
                        task.execute("http://" + IP_ADDRESS + "/insert_food1.php", id, fid, calendar, curName);
                        break;

                    case 2:
                        task.execute("http://" + IP_ADDRESS + "/insert_food2.php", id, fid, calendar, curName);
                        break;

                    case 3:
                        task.execute("http://" + IP_ADDRESS + "/insert_food3.php", id, fid, calendar, curName);
                        break;

                }
                Toast.makeText(view.getContext(), "음식이 저장되었습니다", Toast.LENGTH_SHORT).show();*/





            }

        });


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }




    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(UsersAdapter.this.context,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            /*mTextViewResult.setText(result);*/
            Log.d(TAG, "POST response  - " + result);

        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String fid = (String)params[2];

            String calendar = (String)params[3];
            String curName = (String)params[4];


            String serverURL = (String)params[0];
            String postParameters = "&id=" + id + "&fid=" + fid + "&calendar=" + calendar + "&curName=" + curName;


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





    class getfid extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(UsersAdapter.this.context,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            foodNum = String.valueOf(Integer.parseInt(result)+1);

            fid = foodNum;
            Log.d("fid", "POST response  - " + fid);


            InsertData task = new InsertData();
            //Log.d("i",i+"");
            //Log.d("calendar", calendar);
            //Log.d("curName", curName);
            switch (i) {
                case 0:
                    task.execute("http://" + IP_ADDRESS + "/insert_food0.php", id, fid, calendar, curName);
                    break;

                case 1:
                    task.execute("http://" + IP_ADDRESS + "/insert_food1.php", id, fid, calendar, curName);
                    break;

                case 2:
                    task.execute("http://" + IP_ADDRESS + "/insert_food2.php", id, fid, calendar, curName);
                    break;

                case 3:
                    task.execute("http://" + IP_ADDRESS + "/insert_food3.php", id, fid, calendar, curName);
                    break;

            }
            //Toast.makeText(view.getContext(), "음식이 저장되었습니다", Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();
            TextView realtimeinsert = (TextView)RealtimeInsert;
            realtimeinsert.append(curName + "을(를) 추가하였습니다.\n");


            /*mTextViewResult.setText(result);*/
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String serverURL = (String)params[0];

            String postParameters = "&id=" + id;

            /*String calendar = (String)params[1];
            String curName = (String)params[2];


            String serverURL = (String)params[0];
            String postParameters = "&calendar=" + calendar + "&curName=" + curName;*/


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
