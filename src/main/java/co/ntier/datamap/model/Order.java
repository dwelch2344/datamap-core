package co.ntier.datamap.model;

import java.util.Date;

import lombok.Data;

@Data
public class Order {
	
	private Long id, ownerId;
	private String customer;
	private Date date;
	
	public Order(Long id, Long ownerId, String customer) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.customer = customer;
		this.date = new Date();
	}
	
}
