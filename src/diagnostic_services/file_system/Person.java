package diagnostic_services.file_system;

public interface Person {
	default int getId() {
		return 0;
	};
	
	default String getName() {
		return "";
	}
}