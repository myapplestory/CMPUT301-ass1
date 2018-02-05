package com.example.james.csun_subbook;

import java.util.Date;

/**
 * Created by James on 2/3/2018.
 *
 */

public class Subscribe extends Subscription{
    Subscribe(String name, Date date, Float amount, String comment){
        super(name, date, amount, comment);
    }
    Subscribe(String name, Date date, Float amount) {
        super(name, date, amount);
    }

    @Override
    public Float getAmount() {
        return super.getAmount();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Date getDate() {
        return super.getDate();
    }

    @Override
    public String getComment() {
        return super.getComment();
    }
}
