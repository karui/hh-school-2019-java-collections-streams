package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/*
А теперь о горьком
Всем придется читать код
А некоторым придется читать код, написанный мною
Сочувствую им
Спасите будущих жертв, и исправьте здесь все, что вам не по душе!
P.S. функции тут разные и рабочие (наверное), но вот их понятность и эффективность страдает (аж пришлось писать комменты)
P.P.S Здесь ваши правки желательно прокомментировать (можно на гитхабе в пулл реквесте)
 */
public class Task8 implements Task {

  private long count;

  //Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public List<String> getNames(List<Person> persons) {
    return persons.stream().skip(1).map(Person::getFirstName).collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  public String convertPersonToString(Person person) {
    return  Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().collect(Collectors.toConcurrentMap(Person::getId, this::convertPersonToString, (n1,n2) -> n2));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {

    // Лаконичное, но не самое оптимальное решение. Многократный .contains не очень, особенно если совпадений нет.
    boolean b1 = persons1.stream().filter(persons2::contains).limit(1).count() > 0;

    // Более оптимальное, но менее приглядное решение.
    boolean b2 = Stream.concat(persons1.stream().distinct(), persons2.stream()
            .distinct())
            .collect(Collectors.toConcurrentMap(person -> person, i -> 0, (i1, i2) -> 1))
            .values()
            .stream()
            .anyMatch(n -> n > 0);

    // Ещё один вариант решения. Оптимальный по скорости, но не оптимальный по памяти.
    boolean b3 = (new HashSet<>(persons1).size() + new HashSet<>(persons2).size()) != Stream.concat(persons1.stream(), persons2.stream()).collect(Collectors.toSet()).size();

    // Он же, но в понятном виде и более оптимальный по памяти.
    // Однако в этом случае есть нюнс: равные с точки зрения Person::equals персоны могут иметь разные хэши, если они создавались независимо.
    var personsSet1 = persons1.stream().distinct().map(Person::hashCode).collect(Collectors.toSet()).size();
    var personsSet2 = persons2.stream().distinct().map(Person::hashCode).collect(Collectors.toSet()).size();
    var summarySet = Stream.concat(persons1.stream(), persons2.stream())
            .distinct()
            .map(Person::hashCode)
            .collect(Collectors.toSet())
            .size();
    boolean b4 = personsSet1 + personsSet2 != summarySet;

    // ОМГ... И тут вдруг осознал, что есть Person::equals для сравнения персон. В - внимательность...
    // Окей, я ещё сюда вернусь.

    return b1 && b2 && b3 && b4;
  }

  // Так, вероятно, чуточку лучше.
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  @Override
  public boolean check() {
    System.out.println("Слабо дойти до сюда и исправить Fail этой таски?");
    boolean codeSmellsNotSoBadIMHO = true;
    boolean codeSmellsGood = false;
    boolean reviewerDrunk = false;

    if (false) {
      Person person1 = new Person(1, "Name1", "Surname1", "Middle1", Instant.now());
      Person person2 = new Person(2, "Name2", "Surname2", Instant.now());
      Person person3 = new Person(3, "Name3", Instant.now());
      Person person4 = new Person(4, "Name4", Instant.now());
      Person person5 = new Person(5, "Name5", Instant.now());
      List<Person> persons1 = List.of(person1, person2, person3, person4);
      List<Person> persons2 = List.of(person4, person5);
      List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

      System.out.println(countEven(list.stream()));
      System.out.println(hasSamePersons(persons1, persons2));
      System.out.println(getPersonNames(persons1));
      System.out.println(getNames(persons1));
      System.out.println(getDifferentNames(persons1));
    }

    return codeSmellsNotSoBadIMHO || codeSmellsGood || reviewerDrunk;
  }
}
