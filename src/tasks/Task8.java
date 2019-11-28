package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;
import java.util.Arrays;
import java.util.Objects;
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
//  Проверка на пустой список лишняя. Если входной список пуст, то и стрим вернёт пустой список
//  Изменять переданный объект не здорово, лучше пропустить первый элемент
    return persons.stream().skip(1).map(Person::getFirstName).collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
//  Можно исключить distinct(), т.к. стрим всё равно собирается в сет.
//  Но проще создать сет от списка, и он сам исключит дубли.
    return new HashSet<>(getNames(persons));
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  public String convertPersonToString(Person person) {
//  Дважды выводился SecondName. Больше похоже на багу.
//  Если SecondName не задан, а getFirstName задан, то получаем пробел в начале.
//  Можно, конечно, добавить в if'ы проверку на пустой result, но зачем?
    return  Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
//  Здесь вопросов не возникло. Разве что каждый цикл дёргается проверка на наличие ключа.
//  В стриме в коллекте получается разрешать коллизию по мере её возникновения. 
    return persons.stream().collect(Collectors.toConcurrentMap(Person::getId, this::convertPersonToString, (oldName, newName) -> oldName));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
// Вложенный цикл не оптимален. O(m·n). Его можно слегка улучшить, переместив return:
/*
    for (Person person1 : persons1) {
      for (Person person2 : persons2) {
        if (person1.equals(person2)) {
          return true;
        }
      }
    }
    return false;
*/

// Но можно добиться и O(n)
//  На входе у нас коллекции, сделаем из них сеты, чтобы исключить повторы и улучшить работу contains
    HashSet<Person> persons1Set = new HashSet<Person>(persons1);
    HashSet<Person> persons2Set = new HashSet<Person>(persons2);
    boolean b1 = persons1Set.stream().anyMatch(persons2Set::contains);

//  Первый способ читабельный и понятный, но второй мне больше нравится. Дашь совет?
    int personsJointSize = Stream.concat(persons1Set.stream(), persons2Set.stream()).collect(Collectors.toSet()).size();
    boolean b2 = (persons1Set.size() + persons2Set.size()) > personsJointSize;

    return b1 && b2;
  }

  // Выглядит вроде неплохо...
  public long countEven(Stream<Integer> numbers) {
//  Вынос переменной за метод нарушал потокобезопасность. Избавимся от неё через потоковый count
    return numbers.filter(num -> num % 2 == 0).count();
  }

  @Override
  public boolean check() {
    System.out.println("Слабо дойти до сюда и исправить Fail этой таски?");
    boolean codeSmellsBetter = true;
    boolean codeSmellsGood = false;
    boolean reviewerDrunk = false;


    return codeSmellsBetter || codeSmellsGood || reviewerDrunk;
  }
}
