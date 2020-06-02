package com.RLRLitems.RLRLApi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RLRLitems.RLRLApi.entity.Product;
import com.RLRLitems.RLRLApi.entity.Team;
import com.RLRLitems.RLRLApi.repository.ProductRepository;
import com.RLRLitems.RLRLApi.repository.TeamRepository;

@Service
public class ProductService {
	private static final Logger logger = LogManager.getLogger(ProductService.class);
	
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TeamRepository teamRepo;
	
	public Iterable<Product> getProducts(){
		return repo.findAll();
	}
	
	public Iterable<Product> getProductsByTeamId(Long teamId){
		return repo.findByTeamId(teamId);
		
	}
	
	public Product createProduct(Product product, Long teamId) {
		product.setTeam(teamRepo.findOne(teamId));
		Team updateTeam = teamRepo.findOne(teamId);
		updateTeam.getProducts().add(product);
		teamRepo.save(updateTeam);
		return repo.save(product);
	}
	
	public Product updateProduct(Product product, Long id) throws Exception {
		try {
			Product updateProduct = repo.findOne(id);
			updateProduct.setName(product.getName());
			updateProduct.setDescription(product.getDescription());
			updateProduct.setPrice(product.getPrice());
			return repo.save(updateProduct);
		} catch(Exception e) {
			logger.error("Exception occured while trying to update product: " + id, e);
			throw new Exception("Unable to update product.");
		}
	}
	
	public void removeProduct(Long id) throws Exception{
		try {
			repo.delete(id);
		} catch (Exception e) {
			logger.error("Exception occured while trying to delete product: " + id, e);
			throw new Exception("Unable to delete product.");
		}
	}
	


}
