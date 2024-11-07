package com.debbugeando_ideas.best_travel.infraestructure.helpers;

import com.debbugeando_ideas.best_travel.util.exceptions.ForbiddenCustomerException;
import org.springframework.stereotype.Component;

@Component
public class BlackListHelper {

    public void isInBlackListCustomer(String customerId){

        if (customerId.equals("GOTW771012HMRGR087")){
            throw new ForbiddenCustomerException();
        }

    }
}
