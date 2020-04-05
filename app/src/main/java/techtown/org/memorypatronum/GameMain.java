package techtown.org.memorypatronum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GameMain extends AppCompatActivity {
    ListView list;
    String[] Levels = {
            "Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
    Toolbar myToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        CustomList adapter = new CustomList(GameMain.this);
        list = (ListView)findViewById(R.id.levelList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int level, long l) {

                switch(level){
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), GameLevel_1.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent2 = new Intent(getApplicationContext(), GameLevel_2.class);
                        startActivity(intent2);
                        break;

                    case 2:
                        Intent intent3 = new Intent(getApplicationContext(), GameLevel_3.class);
                        startActivity(intent3);
                        break;

                    case 3:
                        Intent intent4 = new Intent(getApplicationContext(), GameLevel_4.class);
                        startActivity(intent4);
                        break;

                    case 4:
                        Intent intent5 = new Intent(getApplicationContext(), GameLevel_5.class);
                        startActivity(intent5);
                        break;
                }

            }
        });
    }

    //toolbar에 main_toolbar.xml 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.account:
                Toast.makeText(getApplicationContext(), "account clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        public CustomList(Activity context){
            super(context, R.layout.listview_exercise, Levels);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_exercise, null, true);
            /*Button itemButton = (Button)rowView.findViewById(R.id.itemButton);
            itemButton.setText(Exercises[position]);*/
            TextView itemText = (TextView)rowView.findViewById(R.id.itemText);
            itemText.setText(Levels[position]);
            return rowView;
        }
    }
}

