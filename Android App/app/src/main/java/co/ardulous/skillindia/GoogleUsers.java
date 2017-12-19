package co.ardulous.skillindia;

/**
 * Created by ardulous on 19/12/17.
 */

public class GoogleUsers {
    private String FirstName,LastName,email;
    private int memberType;//1 for student and 2 for trainer
    public GoogleUsers(String fname,String lname,String mail,int mtype)
    {
        FirstName=fname;
        LastName=lname;
        email=mail;
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
    public int getMemberType()
    {
        return memberType;
    }
    public String getEmail()
    {
        return email;
    }
}
