package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Modifying
    @Query("select p from Person p where p.login = :login")
    public Person findPersonByLogin(@Param("login") String login);
}
