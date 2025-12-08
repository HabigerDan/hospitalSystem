package roles;

public class Patient implements Person{
  private final String firstName;
  private final String lastName;
  private final int id;

  public Patient(String firstName, String lastName, int id) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.id = id;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
