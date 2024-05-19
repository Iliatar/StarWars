package Iliatar.utils.randomSelector;

import Iliatar.ship.Ship;

public record PriorityItem<T> (T object, double priority) implements Priority {
    public double getPriority() {return priority;}
    public T getObject() {return object;}
}
