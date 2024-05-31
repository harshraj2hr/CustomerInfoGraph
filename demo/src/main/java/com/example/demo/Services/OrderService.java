package com.example.demo.Services;

import com.example.demo.Controllers.Utils.ConversionUtils;
import com.example.demo.Models.CustomerContactDTO;
import com.example.demo.Models.Entities.CustomerDO;
import com.example.demo.Models.OrderRequestDTO;
import com.example.demo.Repository.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderDetailsRepository orderDetailsRepository;

  public CustomerContactDTO placeOrder(OrderRequestDTO orderRequestDTO) {
    sanitizeOrderRequest(orderRequestDTO);
    var existingCustomerDOs = validateExistingCustomerDetails(orderRequestDTO);
    List<CustomerDO> customerDOS;
    if(CollectionUtils.isEmpty(existingCustomerDOs))
      customerDOS = createNewCustomer(orderRequestDTO);
    else {
      existingCustomerDOs = updateLinkPrecedence(existingCustomerDOs) ?
          orderDetailsRepository.findByPhoneNumberOrEmail(orderRequestDTO.getPhoneNumber(),
              orderRequestDTO.getEmail()) : existingCustomerDOs;
      customerDOS = createNewCustomerByContactDetails(orderRequestDTO, existingCustomerDOs);
    }

    return ConversionUtils.convertToCustomerContactDTO(customerDOS);
  }

  private List<CustomerDO> createNewCustomerByContactDetails(OrderRequestDTO orderRequestDTO, List<CustomerDO> existingOrders) {
    boolean matchFound = existingOrders.stream()
        .anyMatch(order -> order.getEmail().equals(orderRequestDTO.getEmail())
            && order.getPhoneNumber().equals(orderRequestDTO.getPhoneNumber()));

    if (!matchFound) {
      CustomerDO newCustomer = new CustomerDO();
      newCustomer.setPhoneNumber(orderRequestDTO.getPhoneNumber());
      newCustomer.setEmail(orderRequestDTO.getEmail());
      newCustomer.setLinkPrecedence(CustomerDO.LinkPrecedence.SECONDARY);
      newCustomer.setLinkSequence(existingOrders.stream()
              .map(orderRequest -> Objects.isNull(orderRequest.getLinkSequence())?0:orderRequest.getLinkSequence()).mapToInt(Integer::intValue).max()
          .orElse(0) + 1);
      existingOrders.add(orderDetailsRepository.save(newCustomer));
    }
    return existingOrders;
  }

  private boolean updateLinkPrecedence(List<CustomerDO> existingOrders) {
    if (existingOrders.size() <= 1)
      return false;

    existingOrders.sort(Comparator.comparing(CustomerDO::getCreatedAt).reversed());

    List<CustomerDO> primaryOrders = existingOrders.stream()
        .filter(order -> order.getLinkPrecedence() == CustomerDO.LinkPrecedence.PRIMARY)
        .toList();

    if (primaryOrders.size() <= 1)
      return false;

    CustomerDO earliestPrimaryOrder = primaryOrders.get(primaryOrders.size() - 1);

    List<CustomerDO> updatedOrders = primaryOrders.stream()
        .filter(order -> !order.equals(earliestPrimaryOrder))
        .peek(order -> {
          order.setLinkPrecedence(CustomerDO.LinkPrecedence.SECONDARY);
          order.setLinkSequence(existingOrders.stream()
              .map(orderRequest -> Objects.isNull(orderRequest.getLinkSequence())?0:orderRequest.getLinkSequence())
              .mapToInt(Integer::intValue).max().orElse(0)+1);
        })
        .toList();

    updatedOrders.forEach(orderDetailsRepository::save);
    return true;
  }

  private List<CustomerDO> createNewCustomer(OrderRequestDTO orderRequestDTO) {
    var customer = new CustomerDO();
    customer.setEmail(orderRequestDTO.getEmail());
    customer.setPhoneNumber(orderRequestDTO.getPhoneNumber());
    customer.setLinkPrecedence(CustomerDO.LinkPrecedence.PRIMARY);
    return Collections.singletonList(orderDetailsRepository.save(customer));
  }

  private void sanitizeOrderRequest(OrderRequestDTO orderRequestDTO) {
    orderRequestDTO.setEmail(StringUtils.trimAllWhitespace(orderRequestDTO.getEmail()).toLowerCase(Locale.ROOT));
    orderRequestDTO.setPhoneNumber(StringUtils.trimAllWhitespace(orderRequestDTO.getPhoneNumber()).toLowerCase(Locale.ROOT));
  }

  private List<CustomerDO> validateExistingCustomerDetails(OrderRequestDTO orderRequestDTO) {
    return orderDetailsRepository.findByPhoneNumberOrEmail(orderRequestDTO.getPhoneNumber(), orderRequestDTO.getEmail());
  }

  public void isValidInput(OrderRequestDTO orderRequestDTO) throws Exception {
    if(Strings.isBlank(orderRequestDTO.getEmail()) && Strings.isBlank(orderRequestDTO.getPhoneNumber()))
      throw new Exception("Invalid details");
  }
}
