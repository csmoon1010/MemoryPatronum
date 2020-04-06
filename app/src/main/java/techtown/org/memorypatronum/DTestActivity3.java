package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class DTestActivity3 extends AppCompatActivity {
    TextView dateText;
    TextView titleText;
    TextView questionText;
    TextView resultText;
    TextView numText;
    EditText answerText;
    Button nextB;
    Button checkB;

    String date;
    String title;

    ArrayList<String> keyword;
    String contents;
    String[] sentences;
    String[] questions;

    String question = "";
    String answer = "";
    int correctNum = 0;
    int totalNum = 0;
    AnswerConstruct a;

    int size;
    Komoran komoran;
    class AnswerConstruct{
        int sentenceNum;
        int beginIndex;
        int endIndex;
        String answer;

        AnswerConstruct(int a, int b, int c, String s){
            sentenceNum = a;
            beginIndex = b;
            endIndex = c;
            answer = s;
        }
    }
    LinkedList<AnswerConstruct> answers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_test3);
        dateText = (TextView)findViewById(R.id.testDate);
        titleText = (TextView) findViewById(R.id.testTitle);
        questionText = (TextView)findViewById(R.id.questionText);
        answerText = (EditText) findViewById(R.id.answerText);
        resultText = (TextView)findViewById(R.id.resultText);
        numText = (TextView)findViewById(R.id.numText);
        nextB = (Button)findViewById(R.id.nextB);
        checkB = (Button)findViewById(R.id.checkB);
        checkB.setEnabled(false);

        Intent intent = getIntent();
        date = intent.getStringExtra("showDate");
        title = intent.getStringExtra("title");
        dateText.setText(date);
        titleText.setText(title);

        keyword = intent.getStringArrayListExtra("keyword");
        contents = intent.getStringExtra("contents");

        size = intent.getIntExtra("size", 0);
        sentences = new String[size];
        questions = new String[size];

        answers = new LinkedList<>();

        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        int num = 0;
        boolean start = false;
        int startIndex = 0;
        KomoranResult analyzeResultList = komoran.analyze(contents);
        List<Token> tokenList = analyzeResultList.getTokenList();
        for(Token token : tokenList) {
            if (!start) {
                startIndex = token.getBeginIndex();
                start = true;
            }
            if (token.getPos().equals("SF")) {
                sentences[num] = contents.substring(startIndex, token.getEndIndex());
                start = false;
                num++;
            }
        }

        for(int i = 0; i < sentences.length; i++){
            analyzeResultList = komoran.analyze(sentences[i]);
            tokenList = analyzeResultList.getTokenList();
            StringBuilder builder = new StringBuilder(sentences[i]);
            for(Token token : tokenList){
                if(keyword.contains(token.getMorph())){
                    for(int k = token.getBeginIndex(); k < token.getEndIndex(); k++){
                        builder.setCharAt(k, '○');
                    }
                    answers.add(new AnswerConstruct(i, token.getBeginIndex(), token.getEndIndex(), token.getMorph()));
                }
            }
            questions[i] = builder.toString();
        }
        String temp = "";
        for(int i = 0; i < answers.size(); i++){
            temp = temp + answers.get(i).sentenceNum + " " +answers.get(i).answer + " " + answers.get(i).beginIndex + " " + answers.get(i).endIndex + "\n";
        }
        Log.i("answers", temp);

        answerText.setText("");
        if(!answers.isEmpty()){
            a = answers.poll();
            question = questions[a.sentenceNum];
            int length = question.length();
            questionText.setText(
                    Html.fromHtml(question.substring(0, a.beginIndex) + "<font color = \"#03A9F4\">" +
                            question.substring(a.beginIndex, a.endIndex) + "</font>" +
                            question.substring(a.endIndex, length)));
            answer = a.answer;
            totalNum++;
        }
        nextB.setEnabled(false);
        checkB.setEnabled(true);
    }

    public void nextTest(View view){
        answerText.setText("");
        resultText.setText("");
        if(!answers.isEmpty()){
            a = answers.poll();
            question = questions[a.sentenceNum];
            int length = question.length();
            questionText.setText(
                    Html.fromHtml(question.substring(0, a.beginIndex) + "<font color = \"#03A9F4\">" +
                            question.substring(a.beginIndex, a.endIndex) + "</font>" +
                            question.substring(a.endIndex, length)));
            answer = a.answer;
            totalNum++;
            numText.setText("[문제" + totalNum + "]");
        }
        else{
            questionText.setText("테스트가 종료되었습니다.");
            Intent intent = new Intent(getApplicationContext(), DTestActivity4.class);
            intent.putExtra("total", totalNum);
            intent.putExtra("correct", correctNum);
            finish();
            startActivity(intent);
        }
        nextB.setEnabled(false);
        checkB.setEnabled(true);
    }

    public void checkAnswer(View view){
        if((answerText.getText().toString()).equals(answer)){
            resultText.setText("정답입니다.");
            correctNum++;
        }
        else{
            String s = "틀렸습니다. 정답은 " + answer;
            resultText.setText(s);
        }

        StringBuilder builder = new StringBuilder(question);
        for(int i = a.beginIndex; i < a.endIndex; i++){
            char target = answer.charAt(i-a.beginIndex);
            builder.setCharAt(i, target);
        }
        questions[a.sentenceNum] = builder.toString();

        for(int i = 0; i < answers.size(); i++){
            if((answers.get(i).answer).equals(answer)){
                a = answers.get(i);
                builder = new StringBuilder(questions[a.sentenceNum]);
                for(int k = a.beginIndex; k < a.endIndex; k++){
                    char target = answer.charAt(k-a.beginIndex);
                    builder.setCharAt(k, target);
                }
                questions[a.sentenceNum] = builder.toString();
                answers.remove(i);
            }
        }
        nextB.setEnabled(true);
        checkB.setEnabled(false);
    }

}
