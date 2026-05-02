import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages scheduling rules and storage of appointments.
 */
public class AppointmentScheduler {
    private final List<Appointment> scheduledAppointments;

    public AppointmentScheduler() {
        this.scheduledAppointments = new ArrayList<>();
    }

    /**
     * Attempts to schedule an appointment following these rules:
     *  1) same‐day only
     *  2) within [09:00..17:00]
     *  3) no two appointments at the same start time
     *
     * @return true if successfully scheduled; false otherwise.
     */
    public boolean scheduleAppointment(Appointment appt) {
        LocalDateTime start = appt.getStartTime();
        LocalDate today = LocalDate.now();

        // Rule 1: same‐day only
        if (!start.toLocalDate().equals(today)) {
            return false;
        }

        // Rule 2: business hours
        LocalTime t = start.toLocalTime();
        if (t.isBefore(LocalTime.of(9, 0)) || t.isAfter(LocalTime.of(17, 0))) {
            return false;
        }

        // Rule 3: no overlap
        for (Appointment existing : scheduledAppointments) {
            if (existing.getStartTime().equals(start)) {
                return false;
            }
        }

        scheduledAppointments.add(appt);
        return true;
    }

    /**
     * Returns a List of all appointments on the given date.
     */
    public List<Appointment> getDailySchedule(LocalDate date) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appt : scheduledAppointments) {
            if (appt.getDate().equals(date)) {
                result.add(appt);
            }
        }
        return result;
    }

    public Iterator<Appointment> getDailyScheduleIterator(LocalDate date) {
        return new AppointmentIterator(getDailySchedule(date));
    }
}
