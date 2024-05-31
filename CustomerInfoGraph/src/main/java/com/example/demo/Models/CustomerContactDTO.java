package com.example.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerContactDTO {
       Integer primaryContactId;
       List<String> emails;
       List<String> phoneNumbers;
       List<Integer> secondaryContactIds;
}
