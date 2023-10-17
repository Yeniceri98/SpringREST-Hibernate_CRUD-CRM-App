package com.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	// Need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;		// NOT: spring-mvc-crud-demo-servlet.xml'in 45. sayfasındaki beanId
	
	@Override
	// @Transactional		// NOT: Service class'ını oluşturunca buradan silip orada tanımladık. CustomerServiceImpl kısmında transaction'ı açıp kapatmak best practice olur
	public List<Customer> getCustomers() {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Create a query
		Query<Customer> theQuery = currentSession.createQuery("FROM Customer ORDER BY id", Customer.class);
		
		// Execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		// Return the results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Save the customer
		// currentSession.save(theCustomer);
		currentSession.saveOrUpdate(theCustomer);		// NOT: Üst satırdaki gibi "save()" veya "update()" metodunu da kullanabilirdik fakat bu şekildeyken customer yoksa save; varsa update eder
	}

	@Override
	public Customer getCustomerById(int theId) {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Retrive customer from the database
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {

		// Get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// Delete object with primary key
		Query theQuery = currentSession.createQuery("DELETE FROM Customer WHERE id=:customerId");
		theQuery.setParameter("customerId", theId);

		theQuery.executeUpdate();
	}
}

/*
	Data Access Object (DAO)	
	https://www.baeldung.com/java-dao-pattern#:~:text=The%20Data%20Access%20Object%20(DAO,mechanism)%20using%20an%20abstract%20API
	https://hasscript.com/755/yaz%C4%B1lmda-dao-nedir
	
	@Transactional Annotation
	Spring provides @Transactional annotation
	It automatically "begin" and "end" a transcation for Hibernate code
	There is no need for us to explicitly do this in the code. This happens behind the scene
	Start ve commit transaction yapıyorduk. Annotation sayesinde bu ikisine artık ihtiyaç olmaz
	
	@Repository Annotation
	DAO implementasyonlarında kullanılır
	Spring will automatically register the DAO implementation thanks to component-scanning
	Spring also provides translation of any JDBC related exceptions

	REST Controller ---> Service ---> DAO
*/