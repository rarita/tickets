package ru.griga.tickets.model;

public enum ItineraryType {
    AIRCRAFT,
    TRAIN,
    BUS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
