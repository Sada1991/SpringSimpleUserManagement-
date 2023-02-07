package net.igap.service;

import static net.igap.utils.Helpers.validateDate;
import static net.igap.utils.Helpers.validateFixLengthDigitNationalCode;
import static net.igap.utils.Helpers.validateGender;

import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.entity.Person;
import net.igap.repository.PersonRepository;
import net.igap.service.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  public Person savePerson(CreateAccountDto accountDto){

    Person newPerson = new Person();

    newPerson.setFirstName(accountDto.getFirstName());
    newPerson.setLastName(accountDto.getLastName());
    newPerson.setGender(validateGender(accountDto.getGender()));
    newPerson.setDateOfBirth(validateDate(accountDto.getDateOfBirth()));
    newPerson.setNationalCode(validateFixLengthDigitNationalCode(accountDto.getNationalCode()));

    return personRepository.save(newPerson);
  }


}
