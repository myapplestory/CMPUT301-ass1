package com.example.james.csun_subbook;

import java.util.Date;

/**
 * created on Feb 2 2018
 * @author James Sun
 *
 * this class is in charge of creating a new subscription object
 * as well as returning data about a subciption
 *
 */

public class Subscribe extends Subscription{
    Subscribe(String name, Date date, Float amount, String comment){
        super(name, date, amount, comment);
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
