package diagnostic_services;

import java.util.Scanner;
import diagnostic_services.file_system.*;

public class DiagMain {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		FilerSystem fs = new FilerSystem();
		
		boolean verified = false;
		System.out.print("User id: ");
		
		while (!verified) {
			int userId = sc.nextInt();
			
			verified = fs.isHealthCareProvider(userId);
			if (!verified) {
				System.out.print("Invalid id, please try again: ");
			}
		}
		
		System.out.print(
"""		
What would you like to do?
1: view all current charts.
"""
		);
		
		if (sc.nextInt() == 1) {
			System.out.println();
			fs.print();
			sc.close();
		}
		
	}
}
