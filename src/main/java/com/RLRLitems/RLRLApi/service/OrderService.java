package com.RLRLitems.RLRLApi.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RLRLitems.RLRLApi.entity.Order;
import com.RLRLitems.RLRLApi.entity.Product;
import com.RLRLitems.RLRLApi.entity.User;
import com.RLRLitems.RLRLApi.repository.OrderRepository;
import com.RLRLitems.RLRLApi.repository.ProductRepository;
import com.RLRLitems.RLRLApi.repository.UserRepository;
import com.RLRLitems.RLRLApi.util.MemberRank;
import com.RLRLitems.RLRLApi.util.OrderStatus;





@Service
public class OrderService {
	
	private static final Logger logger = LogManager.getLogger(ProductService.class);
	private final int DELIVERY_DAYS = 7;
	
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Iterable<Order> getOrders(){
		return repo.findAll();
	}
	
	public Iterable<Order> getGuestOrders(){
		Iterable<Order> Orders = repo.findAll();
		Set<Order> guestOrders = new HashSet<Order>();
		for(Order order : Orders) {
			if(order.isGuestOrder() == true) {
				guestOrders.add(order);
			}
		}
		Iterable<Order> gOrders = guestOrders;
		return  gOrders;
	}
	
	public Order findOrderById(Long id) {
		return null;
		
	}
	
	public Order submitNewOrder(Set<Long> productIds, Long userId) throws Exception {
		try {
			User user = userRepo.findOne(userId);
			Order order = initializeNewOrder(productIds, user);
			return repo.save(order);
		} catch (Exception e) {
			logger.error("Exception occured while trying to create new order for user: " + userId, e);
			throw e;
		}
	}
	
	public Order submitNewGuestOrder(Set<Long> productIds) throws Exception{
		try {
			Order order = initializeNewGuestOrder(productIds);
			return repo.save(order);
		}catch(Exception e) {
			logger.error("Exception occured while trying to create a new guest order.", e);
			throw e;
		}
	}
	
	public Order addGuestToOrder(User guest, Long id) throws Exception {
		try {
			Order guestOrder = repo.findOne(id);
			User guestUser = guest;
			guestUser(guestUser, id);
			guestOrder.setUser(guestUser);
			userRepo.save(guestUser);
			return repo.save(guestOrder);
		} catch(Exception e) {
			logger.error("Exception occured while trying to add guest information to order id: " + id, e);
			throw e;
		}
	}
	
	
	public Order cancelOrder(Long orderId) throws Exception{
		try {
			Order cancelOrder = repo.findOne(orderId);
			cancelOrder.setStatus(OrderStatus.CANCELLED);
			return repo.save(cancelOrder);
		}catch(Exception e) {
			logger.error("Exception occured while trying to cancel order for user: " + orderId, e);
			throw new Exception("Unable to cancel order.");
		}
	}
	
	public Order deliveredOrder(Long orderId) throws Exception{
		try {
			Order deliveredOrder = repo.findOne(orderId);
			deliveredOrder.setStatus(OrderStatus.DELIVERED);
			return repo.save(deliveredOrder);
		}catch(Exception e) {
			logger.error("Exception occured while trying to update order for customer: " + orderId, e);
			throw new Exception("Unable to update order.");
		}
	}
	
	private Order initializeNewOrder(Set<Long> productIds, User user) {
		Order order = new Order();
		order.setProducts(converToProductSet(productRepo.findAll(productIds)));
		order.setOrdered(LocalDate.now());
		order.setEstimatedDelivery(LocalDate.now().plusDays(DELIVERY_DAYS));
		order.setUser(user);
		order.setInvoiceAmount(calculateOrderTotal(order.getProducts(), user.getRank()));
		order.setStatus(OrderStatus.ORDERED);
		order.setGuestOrder(false);
		addorderToProducts(order);
		return order;
	}
	
	private Order initializeNewGuestOrder(Set<Long> productIds) {
		Order order = new Order();
		User guest = new User();
		order.setProducts(converToProductSet(productRepo.findAll(productIds)));
		order.setOrdered(LocalDate.now());
		order.setEstimatedDelivery(LocalDate.now().plusDays(DELIVERY_DAYS));
		order.setInvoiceAmount(calculateOrderTotal(order.getProducts(), guest.getRank()));
		order.setStatus(OrderStatus.ORDERED);
		order.setGuestOrder(true);
		order.setUser(null);
		addorderToProducts(order);
		return order;
	}
	
	private void addorderToProducts(Order order) {
		Set<Product> products = order.getProducts();
		for (Product product: products) {
			product.getOrders().add(order);
		}
	}
	
	private double calculateOrderTotal(Set<Product> products, MemberRank level) {
		double total = 0;
		for(Product product: products) {
			total += product.getPrice();
		}
		if (level == null) {
			return total;
		}
		return total - total * level.getDiscount();
	}

	private Set<Product> converToProductSet(Iterable<Product> iterable) {
		Set<Product> set = new HashSet<Product>();
		for (Product product : iterable) {
			set.add(product);
		}
		return set;
	}
	
	private void guestUser(User guest, Long id) {
		guest.setFirstName(guest.getFirstName() + " " + guest.getLastName());
		guest.setLastName(guest.getEmail());
		guest.setEmail("GuestOrder"+ id);
		guest.setUsername("GuestOrder"+id);
		guest.setPassword("GUEST");
	}
	

	public boolean isGuestOrder(Long id) {
		Order order = repo.findOne(id);
		if(order.isGuestOrder() && order.getUser().equals(null)) {
			return true;
		}
		return false;
	}
	
	
	
	
}
