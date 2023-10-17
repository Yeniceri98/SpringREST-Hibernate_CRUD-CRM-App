package com.springdemo.rest;

import com.springdemo.entity.Customer;
import com.springdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    // Autowire the CustomerService
    @Autowired
    private CustomerService customerService;        // Injects the dependency

    // GET OPERATION

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();      // REST Controller ---> Service ---> DAO ---> Hibernate
    }

    @GetMapping("/customers/{customerId}")
    public Customer getSingleCustomer(@PathVariable int customerId) {

        Customer customer = customerService.getCustomerById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException("Customer with the id of " + customerId + " is not found!");
        }

        return customer;
    }

    // POST OPERATION

    /*
        POST isteği atarken body'yi vermek lazım. Postmanda bu işlem için "Body" sekmesini kullanıp aşağıdaki şekilde JSON isteği attık
        {
            "firstName": "Ahmet",
            "lastName": "Yeniceri",
            "email": "ahmet@gmail.com"
        }
    */

    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {       // NOTE: @RequestBody annotation binds the POJO to a method parameter. We can access the request body as a POJO

        customer.setId(0);

        /*
            setId(0) BUT WHY?

            CustomerDAOImpl class'ında currentSession.saveOrUpdate(theCustomer); kodunu implemente etmiştik

            saveOrUpdate(...)
            if (id == empty) => INSERT new customer else UPDATE existing customer
            "empty" null veya 0 demek. Bu yüzden id'yi 0'a setleyince yeni bir customer oluşturur
            Burada id'yi 0 versek bile son id 5 ise otomatikman yeni customer'ın id'si 6 olur
        */

        customerService.saveCustomer(customer);

        return customer;
    }

    // PUT OPERATION
    // NOT: POST operasyonunda Postman'de body'yi oluşturup istek atarken id'yi vermemiştik kendisi otomatik oluşturmuştu. PUT için güncellemek istediğimiz verinin id'sini de vermeliyiz

    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer customer) {

        customerService.saveCustomer(customer);

        return customer;
    }

    // DELETE OPERATION

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {

        Customer tempCustomer = customerService.getCustomerById(customerId);

        if (tempCustomer == null) {
            throw new CustomerNotFoundException("Customer with the id of " + customerId + " is not found!");
        }

        customerService.deleteCustomer(customerId);

        return "Deleted Customer\nID: " + customerId + "\nFirst Name: " + tempCustomer.getFirstName() + "\nLast Name: " + tempCustomer.getLastName();
    }
}

/*
    NOT: config, entity, service, dao, entity paketlerindeki classları önceki DatabaseWebApp projesinden aldık. O projede Controller classı varken, burada REST Controller classını kullandık
*/