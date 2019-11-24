package tasks;

import common.Person;
import common.PersonService;
import common.Task;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис (он выдает несортированный Set<Person>)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимпотику работы
 */
public class Task1 implements Task {

  // !!! Редактируйте этот метод !!!
  private List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = PersonService.findPersons(personIds);

// Вариант 1. Хороший. Сложность O(n).
// Но, скорее всего и больше, т.к. создание мапы(построение индекса) может быть больше O(1). Возможно, заблуждаюсь.
    var personsMap = persons.stream().collect(Collectors.toMap(Person::getId, p->p));
    var personsList = personIds.stream().map(personsMap::get).collect(Collectors.toList());

// IDEA развернула стримы в цикл. Вот тут не очень понятно: Collectors.toMap возвращает Map или HashMap?
// Или: говорим Map - подразумеваем HashMap?
/*
    var personsMap = new HashMap<>();
    for (Person p : persons) {
      if (personsMap.put(p.getId(), p) != null) {
        throw new IllegalStateException("Duplicate key");
      }
    }
    var personsList = new ArrayList<>();
    for (Integer personId : personIds) {
      Object o = personsMap.get(personId);
      personsList.add(o);
    }
*/

// Вариант 2. Возможный. Затрудняюсь оценить сложность. Не меньше O(n·log(n)) т.к. sort, а то и O(n²) т.к. indexOf.
    var personsList2 = new ArrayList<>(persons);
    Collections.sort(personsList2, Comparator.comparing(person -> personIds.indexOf(person.getId())));

    return personsList;
  }

  @Override
  public boolean check() {
    List<Integer> ids = List.of(9, 8, 7, 1, 2, 3);

    return findOrderedPersons(ids).stream()
        .map(Person::getId)
        .collect(Collectors.toList())
        .equals(ids);
  }

}
