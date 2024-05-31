package com.example.demo.Repository;

import com.example.demo.Models.Entities.CustomerDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<CustomerDO, Integer> {


  @Query(value = "SELECT * FROM customers where phone_number = ?1 or email = ?2", nativeQuery = true)
  List<CustomerDO> findByPhoneNumberOrEmail(String phoneNumber, String email);
}
