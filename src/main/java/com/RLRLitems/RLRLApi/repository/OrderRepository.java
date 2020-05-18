package com.RLRLitems.RLRLApi.repository;

import org.springframework.data.repository.CrudRepository;

import com.RLRLitems.RLRLApi.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
