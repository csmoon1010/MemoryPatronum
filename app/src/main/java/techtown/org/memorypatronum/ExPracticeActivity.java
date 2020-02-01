package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ExPracticeActivity extends AppCompatActivity {
    TextView explainText;
    Integer level;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_practice);
        Intent intent = getIntent();
        explainText = (TextView)findViewById(R.id.explainText);
        level = intent.getIntExtra("LEVEL",0);
        explainText.setText("level :" + level);
    }
}
