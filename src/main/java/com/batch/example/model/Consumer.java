package com.batch.example.model;

import com.batch.example.enumeration.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`consumer`")
@Builder
public class Consumer extends BaseModel {

  private UUID personId;

  private String firstName;

  private String lastName;

  private String email;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private String country;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate birthday;

  private int age;
}
