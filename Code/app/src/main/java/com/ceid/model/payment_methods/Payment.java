package com.ceid.model.payment_methods;
import com.ceid.model.users.Customer;
import com.ceid.util.PositiveInteger;

public class Payment {
    private Customer person;
    private PositiveInteger amount;

    public Customer getPerson() {
        return person;
    }

    public void setPerson(Customer person) {
        this.person = person;
    }

    public PositiveInteger getAmount() {
        return amount;
    }

    public void setAmount(PositiveInteger amount) {
        this.amount = amount;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    enum Method {WALLET,CASH};
    private Method method;
    

    public Payment(Customer person,int amount,Method method){
        this.person=person;
        this.amount=new PositiveInteger(amount);
        this.method=method;
    }

    
}

