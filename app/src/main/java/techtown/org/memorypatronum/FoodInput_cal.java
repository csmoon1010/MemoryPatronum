package techtown.org.memorypatronum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.util.Calendar;

public class FoodInput_cal extends AppCompatActivity {
    String[] listItems;
    MaterialCalendarView writeCalendar;
    TextView dateText;
    Calendar calendar;
    Calendar calendar2;
    String selectedDate;
    Date resultDate;


    Intent speechIntent;

    Toolbar myToolbar;


    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input_cal);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        //MyApplication myApp = (MyApplication) getApplication();
        //String id = myApp.getLoginID();

        writeCalendar = (MaterialCalendarView)findViewById(R.id.writeCalendar);
        dateText = (TextView)findViewById(R.id.dateText);
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar2.add(calendar2.MONTH, -1);
        writeCalendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMaximumDate(CalendarDay.from(calendar))
                .setMinimumDate(CalendarDay.from(calendar2))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        writeCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                int year = date.getYear();
                int month = date.getMonth();
                int dayOfMonth = date.getDay();
                calendar.set(year, month, dayOfMonth);
                int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
                String[] dayString =  {"일요일", "월요일", "화요일", "수요일",
                        "목요일", "금요일", "토요일"};
                String dayOfWeek = dayString[dayNum-1];
                selectedDate = String.format("%d년 %d월 %d일 ", year, (month + 1), dayOfMonth);
                dateText.setText(selectedDate);
                dateText.append(dayOfWeek);
                resultDate = new java.sql.Date(date.getDate().getTime());
            }
        });

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    }




    public void onCheckClick1(View view){//확인버튼 누르면 dialog 뜨게 하기. else문에다가 dialog 들어가게 하면 될듯
        if(dateText.getText().equals("○○○○년 ○○월 ○○일 ○요일")){
            Toast.makeText(getApplicationContext(), "달력에서 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
        else{

            listItems = new String[]{"아침", "점심", "저녁", "간식"};
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(FoodInput_cal.this);
            mBuilder.setTitle("먹은 밥 시간대를 고르세요");



            mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //MyApplication myApp = (MyApplication) getApplication();
                    //String id = myApp.getLoginID();
                    Intent intent = new Intent(getApplicationContext(), foodsearch_morning.class);
                    intent.putExtra("showDate", dateText.getText().toString());
                    intent.putExtra("CALENDAR", resultDate.toString());
                    intent.putExtra("MLDS", i);
                    //intent.putExtra("ID", id.toString());



                    startActivity(intent);


                    //finish();

                }
            });
             mBuilder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

            //Intent intent = new Intent(getApplicationContext(), foodsearch_morning.class);

            //intent.putExtra("showDate", dateText.getText().toString());
            //intent.putExtra("CALENDAR", resultDate.toString());
            //startActivity(intent);
            //finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            /*case R.id.mic:
                on = getResources().getDrawable(R.drawable.ic_mic_black_18dp, null);
                off = getResources().getDrawable(R.drawable.ic_mic_off_black_18dp, null);
                micItem = (ActionMenuItemView) findViewById(R.id.mic);
                speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                vRecognizer = new vRecog(speechIntent, mRecognizer, getApplicationContext(), micItem, 1, on, off);

                vRecognizer.checkPermission(DWriteActivity.this);
                Toast.makeText(getApplicationContext(), "mic clicked", Toast.LENGTH_SHORT).show();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }


}
