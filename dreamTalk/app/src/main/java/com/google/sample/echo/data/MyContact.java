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
    //constructor
    public MyContact(String firstName, String lastName,String username, boolean image, boolean selected){

        this.firstName=firstName;
        this.lastName=lastName;
        this.username=username;
        this.image = image;
        this.selected= selected;

    }

    //getters
    public String getfirstName() { return firstName; }
    public String getlastName() {return lastName; }
    public String getUsername() {return username;}
    public boolean getStatus() { return image; }
    public void setStatus(boolean st) { image=st; }
    public boolean getSelected() { return selected;}
    public boolean setSelected(boolean selection){selected=selection; return true;}
}
