import java.util.List;

public class AppointmentIterator implements Iterator<Appointment> {
	private final List<Appointment> appointments;
	private int currentIndex = 0;

	public AppointmentIterator(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < appointments.size();
	}

	@Override
	public Appointment next() {
		return appointments.get(currentIndex++);
	}
}
