package com.batch.example.config;

import com.batch.example.dto.ConsumerRequestDTO;
import com.batch.example.enumeration.Gender;
import com.batch.example.model.Consumer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.batch.item.ItemProcessor;

public class ConsumerProcessor implements ItemProcessor<ConsumerRequestDTO, Consumer> {

  @Override
  public Consumer process(ConsumerRequestDTO consumerRequestDTO) throws Exception {
    return Consumer.builder()
        .personId(UUID.fromString(consumerRequestDTO.getPersonId()))
        .firstName(consumerRequestDTO.getFirstName())
        .lastName(consumerRequestDTO.getLastName())
        .email(consumerRequestDTO.getEmail())
        .country(consumerRequestDTO.getCountry())
        .birthday(localDateTimeFormat(consumerRequestDTO.getBirthday()))
        .gender(consumerRequestDTO.getGender().equals("Male") ? Gender.MALE : Gender.FEMALE)
        .age(Period.between(localDateTimeFormat(consumerRequestDTO.getBirthday()), LocalDate.now()).getYears())
        .build();
  }

  private LocalDate  localDateTimeFormat(String birthday) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime formatDateTime = LocalDateTime.parse(birthday, formatter);

    return formatDateTime.toLocalDate();
  }
}
