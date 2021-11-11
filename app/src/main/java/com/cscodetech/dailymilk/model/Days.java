package com.cscodetech.dailymilk.model;

public class Days {

    public boolean select;
    public String dayname;

    public Days(boolean select, String dayname) {
        this.select = select;
        this.dayname = dayname;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }
}
