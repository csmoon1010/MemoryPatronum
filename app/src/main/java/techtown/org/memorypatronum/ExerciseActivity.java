package techtown.org.memorypatronum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseActivity extends AppCompatActivity {
    ListView list;
    String[] Exercises = {
            "1단계", "2단계", "3단계", "4단계", "5단계"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        CustomList adapter = new CustomList(ExerciseActivity.this);
        list = (ListView)findViewById(R.id.exerciseList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int level, long l) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity2.class);
                intent.putExtra("LEVEL", level+1);
                startActivity(intent);
            }
        });
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        public CustomList(Activity context){
            super(context, R.layout.listview_exercise, Exercises);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_exercise, null, true);
            TextView itemText = (TextView)rowView.findViewById(R.id.itemText);
            itemText.setText(Exercises[position]);
            return rowView;
        }
    }
}

