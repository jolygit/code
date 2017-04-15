package com.google.sample.echo.data;

/**
 * Created by aj on 2/22/17.
 */

public class MyContact {
    private String firstName;
    private String lastName;
    private String username;
    private boolean image;
    private boolean selected; // default is false. When check box is selected becomes true
    private boolean bold;// this is used to show unread message from this peer
    private String  message; //this is used to show parts of the unread message in the contacts list like in skype
    //constructor
    public MyContact(String firstName, String lastName,String username, boolean image, boolean selected,boolean bold,String chatmsg){

        this.firstName=firstName;
        this.lastName=lastName;
        this.username=username;
        this.image = image;
        this.selected= selected;
        this.bold=bold;
        this.message=chatmsg;

    }

    //getters
    public String getfirstName() { return firstName; }
    public String getlastName() {return lastName; }
    public String getUsername() {return username;}
    public boolean getStatus() { return image; }
    public boolean getBold() {return bold;}
    public String getMessage() { return message; }
    public boolean getSelected() { return selected;}
    public void setStatus(boolean st) { image=st; }
    public boolean setSelected(boolean selection){selected=selection; return true;}
    public void setBold(boolean bld) { this.bold=bld; }
    public void setMessage(String msg) { this.message=msg; }
}
