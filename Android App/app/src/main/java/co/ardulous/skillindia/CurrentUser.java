package co.ardulous.skillindia;

import java.lang.ref.SoftReference;

/**
 * Created by ardulous on 22/12/17.
 */

public class CurrentUser {
    private static String ufName,ulName,phoneMail,profileUrl;
    private static int mType;
    public CurrentUser(String fname,String lname,String modeVal,int member)
    {
        ufName=fname;
        ulName=lname;
        phoneMail=modeVal;
        mType=member;
    }
    public CurrentUser(String fname,String lname,String modeVal,String picture,int member)
    {
        ufName=fname;
        ulName=lname;
        phoneMail=modeVal;
        profileUrl=picture;
        mType=member;
    }
    public String getUfName(){
        return ufName;
    }
    public String getUlName(){
        return ulName;
    }
    public String getPhoneMail(){
        return phoneMail;
    }
    public String getProfileUrl()
    {
        return profileUrl;
    }
}
