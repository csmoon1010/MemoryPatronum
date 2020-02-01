package techtown.org.memorypatronum;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DWriteActivity2 extends AppCompatActivity {
    public String[] questions;
    String[] answers;
    Integer qNum;
    TextView questionText;
    EditText answerText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write2);
        qNum = 0;
        questionText = (TextView)findViewById(R.id.questionText);
        answerText = (EditText) findViewById(R.id.answerText);

        questions = new String[7];
        questions[0] = "무엇을 했나요? \n한 가지만 골라 간략히 적어주세요.";
        questions[1] = "1. 누구와 함께 했나요?";
        questions[2] = "2. 어디서 했나요?";
        questions[3] = "3. 몇 시에 했나요?";
        questions[4] = "4. 어떤 옷을 입었나요?";
        questions[5] = "5. 날씨는 어땠나요?";
        questions[6] = "6. 그 일에 대해 기록하고 싶은 것들을\n더 적어주세요.";
        questionText.setText(questions[qNum]);

        answers = new String[7];
        for(int i = 0; i < 7; i++){
            answers[i] = "";
        }
    }

    public void onNextClick2(View view){
        answers[qNum] = String.valueOf(answerText.getText());
        if(answers[qNum].equals("")){ //내용 없음을 표현할 수 있는 방법 찾아보기
            Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(qNum < 6){
                qNum++;
                questionText.setText(questions[qNum]);
                answerText.setText(answers[qNum]);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("저장하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //이제까지 쓴 것 DB에 저장(answers 배열)
                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }

    public void onPreviousClick(View view){
        if(qNum > 0){
            qNum--;
            questionText.setText(questions[qNum]);
            answerText.setText(answers[qNum]);
        }
        else{
            finish();
        }
    }
}

