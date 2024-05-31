package com.example.demo.Controllers;

import com.example.demo.Models.CustomerContactDTO;
import com.example.demo.Models.OrderRequestDTO;
import com.example.demo.Services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;

  @PostMapping(value = "/identify")
  public ResponseEntity<CustomerContactDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
    log.info("Request received placeOrder {}", orderRequestDTO);
    ResponseEntity<CustomerContactDTO> responseEntity;
    try {
      orderService.isValidInput(orderRequestDTO);
      responseEntity = new ResponseEntity<>(orderService.placeOrder(orderRequestDTO), HttpStatus.OK);
      return responseEntity;
    } catch (Exception e) {
      log.error("Exception while processing order request", e);
      responseEntity = new ResponseEntity<CustomerContactDTO>(new CustomerContactDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return responseEntity;
  }
}
