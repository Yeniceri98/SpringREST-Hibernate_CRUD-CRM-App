package com.springdemo.service;

import java.util.List;

import com.springdemo.entity.Customer;

public interface CustomerService {
	
	List<Customer> getCustomers();

	void saveCustomer(Customer theCustomer);

	Customer getCustomerById(int theId);

	void deleteCustomer(int theId);

}


