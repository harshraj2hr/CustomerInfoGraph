package com.example.demo.Controllers.Utils;

import com.example.demo.Models.CustomerContactDTO;
import com.example.demo.Models.Entities.CustomerDO;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConversionUtils {
  public static CustomerContactDTO convertToCustomerContactDTO(List<CustomerDO> customerDOs) {
    Set<String> emails = new HashSet<>();
    Set<String> phoneNumbers = new HashSet<>();
    Integer primaryContactId = null;
    List<Integer> secondaryContactIds = new ArrayList<>();

    for (CustomerDO customer : customerDOs) {
      emails.add(customer.getEmail());
      phoneNumbers.add(customer.getPhoneNumber());

      if (customer.getLinkPrecedence() == CustomerDO.LinkPrecedence.PRIMARY) {
        primaryContactId = customer.getId();
      } else if (customer.getLinkPrecedence() == CustomerDO.LinkPrecedence.SECONDARY) {
        secondaryContactIds.add(customer.getId());
      }
    }
    
    return new CustomerContactDTO(primaryContactId, new ArrayList<>(emails), new ArrayList<>(phoneNumbers), secondaryContactIds);
  }
}
