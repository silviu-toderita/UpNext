package model;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Abstract class for comparing dates with fuzzy precision
public abstract class DateTest {

    // EFFECTS: Asserts "fuzzy" equality of 2 dates
    protected void assertEqualDate(Date date0, Date date1) {
        assertEquals(date0.getTime() / 100, date1.getTime() / 100);
    }

}
