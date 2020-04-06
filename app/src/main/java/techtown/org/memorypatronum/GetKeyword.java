package techtown.org.memorypatronum;

import android.util.Log;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

import java.util.ArrayList;
import java.util.HashMap;

public class GetKeyword {
    ArrayList<String> nounmorph = new ArrayList<>(); //추출한 morph
    ArrayList<String> nounpos = new ArrayList<>(); //추출한 pos
    HashMap<String, Integer> words = new HashMap<>(); //단어-인덱스
    HashMap<String, Double> idf = new HashMap<>(); //단어-idf값
    INDArray finalMatrix;
    float[][] table;
    int n; //문장의 개수
    int m; //단어의 개수
    String temp = "";

    GetKeyword(ArrayList<String> nounmorph, ArrayList<String> nounpos) {
        this.nounmorph.addAll(nounmorph);
        this.nounpos.addAll(nounpos);

        //문장 개수와 단어 분류(hashmap)
        int index = 0;
        n = 0;
        for (int i = 0; i < nounmorph.size() && i < nounpos.size(); i++) {
            if ((nounpos.get(i)).equals("SF")) {
                n++;
            } else if (!words.containsKey(nounmorph.get(i))) {
                words.put(nounmorph.get(i), index);
                index++;
            }
        }
        m = index;
        table = new float[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                table[i][j] = 0;
            }
        }

        Log.i("nm", n + " " + m);
    }

    void calculateTF() {
        int sentenceNum = 0;
        for (int i = 0; i < nounmorph.size() && i < nounpos.size(); i++) {
            if ((nounpos.get(i)).equals("SF")) {
                sentenceNum++;
            }
            if (words.containsKey(nounmorph.get(i))) {
                if (sentenceNum < n && words.get(nounmorph.get(i)) < m)
                    table[sentenceNum][words.get(nounmorph.get(i))] += 1;
            }
        }

        INDArray wordMatrix = Nd4j.create(table); // matrix로 변경
        INDArray norm = wordMatrix.norm1(1);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(norm.getFloat(i) != 0)   table[i][j] = table[i][j] / norm.getFloat(i);
            }
        }
    }

    void caculateIDF() {
        temp = "";
        String setemp = "";
        for (String key : words.keySet()) {
            int inSentence = 0;
            for (int i = 0; i < n && words.get(key) < m; i++) {
                if (table[i][words.get(key)] != 0) {
                    inSentence++;
                }
            }
            double temp = Math.log(n / 1+ inSentence) + 1;
            setemp = setemp + inSentence + " ";
            idf.put(key, temp);
        }
        for (String key : idf.keySet()) {
            temp = temp + key + " " + idf.get(key) + "/";
        }
        Log.i("idf", temp);
        Log.i("isentence", setemp);
    }

    public int calcualteTFIDF() {
        calculateTF();
        /*caculateIDF();
        temp = "";
        for (int i = 0; i < n; i++) {
            for (String key : words.keySet()) {
                if (words.get(key) < m) {
                    table[i][words.get(key)] *= idf.get(key);
                }
            }
        }
        Log.i("tfidf", temp);*/


        float[][] transTable = new float[m][n];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                transTable[i][j] = table[j][i];
            }
        }

        INDArray wordMatrix = Nd4j.create(table);
        INDArray transMatrix = Nd4j.create(transTable);

        temp = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                temp = temp + wordMatrix.getFloat(i*m + j) + " ";
            }
            temp = temp + "/";
        }
        Log.i("wordMatrix", temp);


        finalMatrix = transMatrix.mmul(wordMatrix);
        temp = "";
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                temp = temp + finalMatrix.getFloat(i*m + j) + " ";
            }
            temp = temp + "/";
        }
        Log.i("finalMatrix", temp);

        return n;
    }

    public HashMap<String, Float> textrank(float d){
        long matrix_size = finalMatrix.size(1);
        for(int i = 0; i < matrix_size; i++){
            float sum = 0;
            finalMatrix.putScalar(matrix_size*i + i, 0);
            INDArray targetRow = finalMatrix.getRow(i);
            for(int k = 0; k < matrix_size; k++){
                sum += targetRow.getFloat(k);
            }
            if(sum != 0){
                for(int k = 0; k < matrix_size; k++){
                    float temp = targetRow.getFloat(k);
                    float num = (temp / sum) * (-d);
                    finalMatrix.putScalar(matrix_size*k + i, num);
                }
            }
            else{
                for(int k = 0; k < matrix_size; k++){
                    float temp = targetRow.getFloat(k);
                    float num = temp * (-d);
                    finalMatrix.putScalar(matrix_size*k + i, num);
                }
            }
            finalMatrix.putScalar(matrix_size*i + i, 1);
        }

        temp = "";
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                temp = temp + finalMatrix.getFloat(i*m + j) + " ";
            }
            temp = temp + "/";
        }
        Log.i("finalMatrix", temp);

        INDArray tempMatrix = Nd4j.zeros(matrix_size, 1).addi(1-d);
        for(int i = 0; i < m; i++){
            float num = tempMatrix.getFloat(i);
            tempMatrix.putScalar(i, Math.round(num*100)/100.0); //0.15로
        }

        temp = "";
        for (int i = 0; i < m; i++) {
            temp = temp + tempMatrix.getFloat(i) + " ";
        }
        Log.i("tempMatrix", temp);


        finalMatrix = InvertMatrix.invert(finalMatrix, true);
        for(int i = 0; i < m; i++){ //소수점 2자리까지
            for(int j = 0;j < m; j++){
                float num = finalMatrix.getFloat(i*m + j);
                finalMatrix.putScalar(i*m+j, Math.round(num*100));
            }
        }
        temp = "";
        for (int i = 0; i < m; i++) {
            for(int j = 0; j < m; j++)
            {
                temp = temp + finalMatrix.getFloat(i*m + j) + " ";
            }
        }
        Log.i("inverseMatrix", temp);

        INDArray resultMatrix = finalMatrix.mmul(tempMatrix);

        HashMap<String, Float> resultHash = new HashMap<>();
        for(String key : words.keySet()){
            int index = words.get(key);
            float num = resultMatrix.getFloat(index, 0);
            resultHash.put(key, num);
        }
        finalMatrix = null;
        return resultHash;
    }
}

