package techtown.org.memorypatronum;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DMemoryActivity3 extends AppCompatActivity {
    EditText titleText;
    EditText contentsText;

    private static String IP_ADDRESS = "192.168.219.183";
    private static String TAG = "phptest";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_memory4);

    }
}
