package com.example.demo.Controllers.Utils;

import com.example.demo.Models.CustomerContactDTO;
import com.example.demo.Models.Entities.CustomerDO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConversionUtils {
  public static CustomerContactDTO convertToCustomerContactDTO(List<CustomerDO> customerDOs) {
    List<String> emails = new ArrayList<>();
    List<String> phoneNumbers = new ArrayList<>();
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

    return new CustomerContactDTO(primaryContactId, emails, phoneNumbers, secondaryContactIds);

  }
}
