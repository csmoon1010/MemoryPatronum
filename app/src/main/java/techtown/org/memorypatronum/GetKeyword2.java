package techtown.org.memorypatronum;

import android.util.Log;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GetKeyword2 {//glgl
    ArrayList<String> morph = new ArrayList<>();
    //ArrayList<String> pos = new ArrayList<>();
    ArrayList<String> nounmorph = new ArrayList<>(); //추출한 morph
    ArrayList<String> nounpos = new ArrayList<>(); //추출한 pos
    HashMap<String, Integer> words = new HashMap<>(); //단어-인덱스
    //HashMap<Integer, String> posOfWord = new HashMap<>(); //단어-인덱스
    HashMap<String, Double> idf = new HashMap<>(); //단어-idf값
    INDArray weightMatrix;
    float[][] table;
    int n; //문장의 개수
    int m; //단어의 개수
    String temp = "";

    GetKeyword2(ArrayList<String> nounmorph, ArrayList<String> nounpos) {
        //this.morph.addAll(morph);
        //this.pos.addAll(pos);
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
                //posOfWord.put(index, nounpos.get(i));
                index++;
            }
        }

        String temp = "";
        for(String w : words.keySet()){
            temp = temp + w + " " + words.get(w) + " / ";
        }
        Log.i("index", temp);

        m = index;
        table = new float[m][m];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < m; j++){
                table[i][j] = 0;
            }
        }
    }

    public int cooccurrence(int window){
        for(int i = 0; i < nounmorph.size(); i++){
            String target = nounmorph.get(i);
            if(!words.containsKey(target))  continue;
            int left = Math.max(i-window, 0);
            int right = Math.min(i+window, nounmorph.size()-1);
            //Log.i("leftright", left + " " + right);
            for(int k = left; k <= right; k++) {
                if(words.containsKey(nounmorph.get(k))){
                    if ((nounmorph.get(k)).equals(target)) continue;
                    else {
                        int a = words.get(target);
                        int b = words.get(nounmorph.get(k));
                        table[a][b] = table[a][b] + 1.0f;
                        table[b][a] = table[b][a] + 1.0f;
                    }
                }
            }
        }

        String temp = "";
        for(int i = 0; i < m; i++){
            for(int k = 0; k < m; k++){
                temp = temp + table[i][k] + " ";
            }
            temp = temp + "/";
        }
        Log.i("cooccurrence", temp);
        return n;
    }

    public HashMap<String, Float> textrank(float d, int iter){
        weightMatrix = Nd4j.create(table);
        long matrix_size = weightMatrix.size(1);
        for(int i = 0; i < matrix_size; i++){
            float sum = 0;
            weightMatrix.putScalar(matrix_size*i + i, 0);
            INDArray targetColumn = weightMatrix.getColumn(i);
            for(int k = 0; k < matrix_size; k++){
                sum += targetColumn.getFloat(k);
            }
            if(sum != 0){
                for(int k = 0; k < matrix_size; k++){
                    float temp = targetColumn.getFloat(k);
                    float num = (temp / sum);
                    weightMatrix.putScalar(matrix_size*k + i, num);
                }
            }
        }
        temp = "";
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                temp = temp + weightMatrix.getFloat(i*m + j) + " ";
            }
            temp = temp + "/";
        }
        Log.i("finalMatrix", temp);


        INDArray finalMatrix = Nd4j.ones(matrix_size, 1);
        INDArray biasMatrix = Nd4j.zeros(matrix_size, 1).addi(1-d);
        for(int i = 0; i < matrix_size; i++){
            float num = biasMatrix.getFloat(i);
            biasMatrix.putScalar(i, Math.round(num*100)/100.0); //0.15로
        }


        HashMap<String, Float> tempHash = new HashMap<>();

        for(int i = 0; i < iter; i++){
            INDArray tempMatrix = weightMatrix.mmul(finalMatrix);
            tempMatrix = tempMatrix.mul(d);
            finalMatrix = tempMatrix.add(biasMatrix);
        }

        HashMap<String, Float> resultHash = new HashMap<>();
        for(String key : words.keySet()){
            int index = words.get(key);
            float num = finalMatrix.getFloat(index, 0);
            resultHash.put(key, num);
        }
        return resultHash;
    }

}

