package ru.griga.tickets.shared.model;

public enum ItineraryType {
    AIRCRAFT,
    TRAIN,
    BUS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
