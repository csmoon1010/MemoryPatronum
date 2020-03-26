package techtown.org.memorypatronum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.sql.Date;

public class DWriteActivity extends AppCompatActivity {
    MaterialCalendarView writeCalendar;
    TextView dateText;
    Calendar calendar;
    Calendar calendar2;
    String selectedDate;
    Date resultDate;
    ActionMenuItemView micItem;

    Intent speechIntent;
    SpeechRecognizer mRecognizer;
    VoiceRecognition vRecognizer;
    Drawable on;
    Drawable off;

    Handler handler;
    Runnable runnable;

    Toolbar myToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write1);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

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
                finish();
                break;
            case R.id.mic:
                on = getResources().getDrawable(R.drawable.ic_mic_black_18dp, null);
                off = getResources().getDrawable(R.drawable.ic_mic_off_black_18dp, null);
                micItem = (ActionMenuItemView) findViewById(R.id.mic);
                speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                vRecognizer = new vRecog(speechIntent, mRecognizer, getApplicationContext(), micItem, 1, on, off);

                vRecognizer.checkPermission(DWriteActivity.this);
                Toast.makeText(getApplicationContext(), "mic clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Thread SST = new Thread(new Runnable(){
        @Override
        public void run() {
            vRecognizer.checkPermission(DwriteActivity.this);
        }
    });*/

    class vRecog extends VoiceRecognition{
        vRecog(Intent i, SpeechRecognizer r, Context c, ActionMenuItemView itm, int m, Drawable on, Drawable off){
            super(i, r, c, itm, m, on, off);
        }

        @Override
        public void checkVoice(String s) {
            Log.i("dwrite", "message" + s);
            //s = s.replaceAll("[^0-9]", " ");
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            selectedDate = s;
            dateText.setText(s);
        }
    }

    public void onNextClick1(View view){
        if(dateText.getText().equals("○○○○년 ○○월 ○○일 ○요일")){
            Toast.makeText(getApplicationContext(), "달력에서 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), DWriteActivity2.class);
            intent.putExtra("showDate", dateText.getText().toString());
            intent.putExtra("CALENDAR", resultDate.toString());
            startActivity(intent);
            finish();
        }
    }

    public void onVoiceClick(View view){
        /*handler.post(runnable);
        try{
            runnable.
        }catch(InterruptedException e){
            e.printStackTrace();
        }*/
        vRecognizer.checkPermission(DWriteActivity.this);
        //Log.i("MAIN", "CHECKVOICE");
        //checkVoice(vRecognizer.returnString);
    }

    public void onDestroy(){
        super.onDestroy();
        if(vRecognizer != null){
            vRecognizer = null;
        }
    }
}

