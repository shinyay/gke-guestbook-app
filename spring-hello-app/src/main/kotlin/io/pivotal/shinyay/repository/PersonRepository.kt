package io.pivotal.shinyay.repository

import io.pivotal.shinyay.entity.Gender
import io.pivotal.shinyay.entity.Person
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct

@Repository
class PersonRepository {

    val persons = mutableListOf<Person>()

    fun findAll() = persons

    fun findById(id: Int) = persons.singleOrNull{ it.id == id }

    fun save(person: Person): Person {
        person.id = (persons.maxBy { it.id }?.id ?: 0) + 1
        persons.add(person)
        return person
    }

    fun update(person: Person): Person {
        val index = persons.indexOfFirst { it.id == person.id }
        if(index >= 0) {
            persons[index] = person
        }
        return person
    }

    fun removeById(id: Int): Boolean = persons.removeIf{ it.id == id }

    @PostConstruct
    fun demoData() {
        save(Person(1, "Rei Ayanami", 14, Gender.FEMALE))
        save(Person(2, "Shinji Ikari", 14, Gender.MALE))
        save(Person(3, "Soryu Asuka Langley", 14, Gender.FEMALE))
        save(Person(4, "Torji Suzuhara", 14, Gender.MALE))
        save(Person(5, "Kaoru Nagisa", 14, Gender.MALE))
        save(Person(6, "Mari Makinami", 33, Gender.FEMALE))
    }
}