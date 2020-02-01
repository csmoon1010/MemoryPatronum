package techtown.org.memorypatronum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory2);

        for(int i = 0; i < 8; i++){
            contents[i] = "";
        }

        Intent intent = getIntent();
        contents[0] = String.valueOf(intent.getIntExtra("YEAR", 0));
        contents[1] = String.valueOf(intent.getIntExtra("MONTH", 0));
        contents[2] = String.valueOf(intent.getIntExtra("DAY", 0));

        DiaryList adapter = new DiaryList(DMemoryActivity2.this);
        list =  (ListView)findViewById(R.id.diaryList);
        list.setAdapter(adapter);
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
}

