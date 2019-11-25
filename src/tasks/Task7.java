package tasks;

import common.Company;
import common.Task;
import common.Vacancy;

import java.util.*;

/*
Из коллекции компаний необходимо получить всевозможные различные названия вакансий
 */
public class Task7 implements Task {

  private Set<String> vacancyNames(Collection<Company> companies) {
// O(n)
    var titlesMap = new HashMap<String,Integer>();
    var titlesList = new ArrayList<String>();

    companies.forEach(company -> company.getVacancies().forEach(vacancy -> titlesMap.put(vacancy.getTitle(),0)));
    companies.forEach(company -> company.getVacancies().forEach(vacancy -> titlesList.add(vacancy.getTitle())));

    return new HashSet<>(titlesList);
//    return titlesMap.keySet();
  }

  @Override
  public boolean check() {
    Vacancy vacancy1 = new Vacancy(1, "vacancy 1");
    Vacancy vacancy2 = new Vacancy(2, "vacancy 2");
    Vacancy vacancy3 = new Vacancy(3, "vacancy 1");
    Company company1 = new Company(1, "company 1", Set.of(vacancy1, vacancy2));
    Company company2 = new Company(2, "company 2", Set.of(vacancy3));
    return vacancyNames(Set.of(company1, company2)).equals(Set.of("vacancy 1", "vacancy 2"));
  }
}
