package model;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }

    <T extends Number> void printDouble(T num) {
        System.out.println(num.doubleValue());
    }

    public void addNumbers(List<? super Integer> list) {
        list.add(10);
    }

    List<?> anything = new ArrayList<String>();
    Object obj = anything.get(0);

    public void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    private <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

}