package co.ardulous.skillindia;

/**
 * Created by ardulous on 19/12/17.
 */

public class PhoneUsers {
    private String FirstName,LastName,phoneNumber,password;
    private int memberType;//1 for student and 2 for trainer
    public PhoneUsers(String fname,String lname,String phone,String pass,int mtype)
    {
        FirstName=fname;
        LastName=lname;
        phoneNumber=phone;
        password=pass;
        memberType=mtype;
    }
    public String getFirstName()
    {
        return FirstName;
    }
    public String getLastName()
    {
        return LastName;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getPassword()
    {
        return password;
    }
    public int getMemberType()
    {
        return memberType;
    }
}
