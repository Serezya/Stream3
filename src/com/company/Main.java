package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        //При использовании stream:
        //Количество военнообязанных: 600479
        //Средний возраст мужчин: 49.49385698468835
        //Количество потенциально работоспособных: 5459567
        //Время выполнения: 0.6265208 s

        //При использовании parallelStream
        //Количество военнообязанных: 599702
        //Средний возраст мужчин: 49.509047076182824
        //Количество потенциально работоспособных: 5458402
        //Время выполнения: 0.3104006 s

        //parallelStream приобретает смысл при количестве объектов ~ 1_000_000 на машине с процессором Intel(R) Core(TM) i5-7300HQ CPU @ 2.50GHz

        List<String> names = Arrays.asList("Иванов", "Петров", "Сидоров");
        List<People> peoples = new ArrayList<>();
        for (int i = 0; i < 12_000_000; i++) {
            peoples.add(new People(names.get(
                    new Random().nextInt(names.size())),
                    new Random().nextInt(100),
                    Sex.randomSex()));
        }
        long startTime = System.nanoTime();

        Stream<People> stream = peoples.parallelStream();
        long countMilitar = stream.filter(people -> people.getSex() == Sex.MAN && (people.getAge() >= 18 && people.getAge() <= 27)).count();
        System.out.println("Количество военнообязанных: " + countMilitar);

        Stream<People> averageMan = peoples.parallelStream();
        double result = averageMan.filter(people -> people.getSex() == Sex.MAN).mapToInt(People::getAge).average().getAsDouble();
        System.out.println("Средний возраст мужчин: " + result);

        Stream<People> potentialWorks = peoples.parallelStream();
        long countWorks = potentialWorks.filter(people -> (people.getSex() == Sex.MAN && (people.getAge() >= 18 & people.getAge() <= 65))
                || people.getSex() == Sex.WOMEN && (people.getAge() >= 18 & people.getAge() <= 60))
                .count();
        System.out.println("Количество потенциально работоспособных: " + countWorks);

        long stopTime = System.nanoTime();
        double processTime = (double) (stopTime - startTime) / 1_000_000_000.0;
        System.out.println("Время выполнения: " + processTime + " s");
    }
}
