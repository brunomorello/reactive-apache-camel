package com.sabiam.sabiamdatareplicator.beans;

import com.sabiam.sabiamdatareplicator.model.Seller;
import com.sabiam.sabiamdatareplicator.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("usersBean")
public class UsersBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersBean.class);

    @Autowired
    private SellerRepository repository;

    public List<Seller> getUsers() {
        return repository.findAll()
                .collectList()
                .block();
    }
}
