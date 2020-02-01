package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseActivity2 extends AppCompatActivity {
    Integer level;
    TextView voice;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2);
        Intent intent = getIntent();
        voice = (TextView)findViewById(R.id.voice);
        level = intent.getIntExtra("LEVEL", 0);
    }

    public void onPracticeClick(View view){
        Intent intent2 = new Intent(getApplicationContext(), ExPracticeActivity.class);
        intent2.putExtra("LEVEL", level);
        startActivity(intent2);
    }

    public void onTestClick(View view){
        Intent intent3 = new Intent(getApplicationContext(), ExTestActivity.class);
        intent3.putExtra("LEVEL", level);
        startActivity(intent3);
    }
}

