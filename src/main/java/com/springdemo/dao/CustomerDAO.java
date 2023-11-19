package com.springdemo.dao;

import java.util.List;

import com.springdemo.entity.Customer;

public interface CustomerDAO {
	
	List<Customer> getCustomers();

	void saveCustomer(Customer theCustomer);

	Customer getCustomerById(int theId);

	void deleteCustomer(int theId);

}
