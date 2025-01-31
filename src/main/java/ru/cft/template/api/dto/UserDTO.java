package ru.cft.template.api.dto;

import java.time.LocalDate;

public record UserDTO(
      String surname,
      String name,
      String midName,
      String number,
      String email,
      LocalDate birthDate
) {
}
