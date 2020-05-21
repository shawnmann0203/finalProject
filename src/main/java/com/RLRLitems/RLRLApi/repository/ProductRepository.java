package com.RLRLitems.RLRLApi.repository;

import org.springframework.data.repository.CrudRepository;

import com.RLRLitems.RLRLApi.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	public Iterable<Product> findByTeamId(Long teamId);
}
