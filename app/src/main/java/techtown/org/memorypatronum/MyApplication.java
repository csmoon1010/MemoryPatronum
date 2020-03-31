package techtown.org.memorypatronum;

import android.app.Application;

public class MyApplication extends Application { //공용 변수 정의
    private String loginID;
    private String address = "memorypatronum.dothome.co.kr";
    public String getLoginID(){
        return loginID;
    }
    public void setLoginID(String loginID){
        this.loginID = loginID;
    }
    public String getipAddress(){
        return address;
    }



}
