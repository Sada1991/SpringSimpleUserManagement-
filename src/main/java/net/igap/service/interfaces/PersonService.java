package net.igap.service.interfaces;

import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.entity.Person;

public interface PersonService {
  Person savePerson(CreateAccountDto accountDto);
}
