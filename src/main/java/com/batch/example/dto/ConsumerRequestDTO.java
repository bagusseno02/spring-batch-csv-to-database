package com.batch.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerRequestDTO {

  private String personId;

  private String firstName;

  private String lastName;

  private String email;

  private String country;

  private String gender;

  private String birthday;

  private int age;
}
