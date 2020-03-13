package techtown.org.memorypatronum;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

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

import java.util.Collection;
import java.util.HashSet;

public class DMemoryActivity extends AppCompatActivity {
    MaterialCalendarView memoryCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //for merge
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory1);
        memoryCalendar = (MaterialCalendarView) findViewById(R.id.memoryCalendar);
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();

        dates = getData();
        EventDecorator decorator = new EventDecorator(Color.RED, dates);
        memoryCalendar.addDecorator(decorator);

        memoryCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DMemoryActivity.this);
                builder.setTitle("일기목록");
                // DB에서 일기 제목(무엇) 받아오고 다음 액티비티로 date 보냄
                String[] example = new String[1];
                example[0] = "hello";
                builder.setItems(example, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), DMemoryActivity2.class);
                        intent.putExtra("YEAR", date.getYear());
                        intent.putExtra("MONTH", date.getMonth());
                        intent.putExtra("DAY", date.getDay());
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        CalendarDay day = CalendarDay.from(2020, 1, 21);
        dates.add(day);
        return dates;
    }
}
