package com.example.texteditor.observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            System.out.println(observers);
            observer.update();
        }
    }
}
