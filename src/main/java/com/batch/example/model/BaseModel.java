package com.batch.example.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
}
