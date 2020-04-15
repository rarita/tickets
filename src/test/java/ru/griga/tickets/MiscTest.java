package ru.griga.tickets;

import org.junit.Test;
import ru.griga.tickets.model.itinerary.SkyPickerItinerary;

public class MiscTest {

    @Test
    public void testItinerariesHashCodeEquality() {
        var x = new SkyPickerItinerary();
        var y = new SkyPickerItinerary();

        assert x.equals(y);
        assert x.hashCode() == y.hashCode();
    }

}
