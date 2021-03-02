package test;

import model.Task;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Abstract class for comparing equality of classes for TimeOut application
public abstract class EqualityTests {

    // EFFECTS: Asserts equality of 2 dates based on date only, ignores time
    protected void assertEqualDate(Date date0, Date date1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date0);
        int yearDate0 = cal.get(Calendar.YEAR);
        int monthDate0 = cal.get(Calendar.MONTH);
        int dayDate0 = cal.get(Calendar.DATE);

        cal.setTime(date1);

        assertEquals(yearDate0, cal.get(Calendar.YEAR));
        assertEquals(monthDate0, cal.get(Calendar.MONTH));
        assertEquals(dayDate0, cal.get(Calendar.DATE));
    }

    // EFFECTS: Asserts equality of 2 tasks by their attributes
    protected void assertEqualTask(Task task0, Task task1) {
        assertEquals(task0.getLabel(), task1.getLabel());
        assertEqualDate(task0.getDueDate(), task1.getDueDate());
    }

}
