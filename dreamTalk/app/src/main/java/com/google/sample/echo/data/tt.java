package com.google.sample.echo.data;

import static android.util.Log.println;

/**
 * Created by aj on 3/27/17.
 */

public class tt extends test{
    public  int print(){
        return fi;
    }
    public enum MyE {

        GOOGLE("G"), YAHOO("Y"), EBAY("E"), PAYPAL("P");

        private String companyLetter;

        private MyE(String s) {
            companyLetter = s;
        }

        public String getCompanyLetter() {
            return companyLetter;
        }
    }
    public void PEnum(MyE en){
        System.out.println("this is enum"+en.GOOGLE);
    }
}
