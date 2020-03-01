package techtown.org.memorypatronum;

import android.app.Application;

public class MyApplication extends Application { //공용 변수 정의
    private String loginID;
    public String getLoginID(){
        return loginID;
    }
    public void setLoginID(String loginID){
        this.loginID = loginID;
    }
}
