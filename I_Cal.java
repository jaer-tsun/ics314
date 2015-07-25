import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * I_Cal
 * 
 * @description iCal second Deliverable: Create an .ics file that is readable on any public .ics
 * related application, read and sort multiple .ics files and add a comment describing the 
 * great circle distance between them.
 * 
 * @author Tsun Chu, Ryan Buillard, Matt Cieslak
 * @date 2015.24.07
 */
public class I_Cal {
	static final double kmPerMile = 1.60934;
	
	// Main Method
	public static void main(String args[]) throws IOException {

		Scanner input = new Scanner(System.in);
		String command = "";
		boolean running = true;

	//menu options
		while(running)
		{
			System.out.println("\t\tSelect Option\n\t [1] Create new event and save it to a file.\n"+
							   "\t [2] Process Events - sort multiple files and calculate distances.\n"+
							   "\t [3] Exit\n");
			command = input.nextLine();

			
			switch (command)
			{
				case "1":   createEvent();
							break;
				case "2":   processEvents();
							break;
				case "3":   running = false;
							break;
				default:    System.out.printf("<Please enter a proper command>\n");
			}
			
		}

	}
	
	/**
	 * @method processEvents
	 * @throws FileNotFoundException, IOException
	 * @description Prompts the user to enter a number of .ics event files, 
	 * 				then sorts them by start time and adds comments to the 
	 * 				files indicating the great circle distance to the next
	 * 				chronological event.
	 */
	public static void processEvents() throws FileNotFoundException, IOException{
		ArrayList<Event> events = new ArrayList<Event>(); 
	
		readEvents(events);
		
		Collections.sort(events);
		
		System.out.println("Sorted events: ");
		//print each event in the arraylist with time
		for(Event e : events){
			System.out.println(e.getSummary() + 
							   " occurs from " +
							   e.startTimeToString() + " to " +
							   e.endTimeToString());
		}
		
		
		/*calculate great circle distance for each event*/
		boolean eventsLeft = true;
		int index1 = 0;
		int index2;
	
		
		//Find first event with coordinates
		Event event1 = events.get(index1);
			
		while((event1.getLatitude() == null) && eventsLeft){
			index1++;
			if(index1 >= events.size()){
				eventsLeft = false;
			}else{
				event1 = events.get(index1);
			}
		}
				
		//find second event with coordinates
		index2 = index1 + 1;
		Event event2 = events.get(index2);		
				
		
		do{
			while((event2.getLatitude() == null) && eventsLeft){
				index2++;
				if(index2 >= events.size()){
					eventsLeft = false;
				}else{
					event2 = events.get(index2);
				}
			}
		
			if(eventsLeft){
				//Have two events which have coordinates; calculate distance and set comment.
			
				double distanceMi = calcDistanceV2(event1.getLatitude(), event1.getLongitude(), event2.getLatitude(), event2.getLongitude());
				double distanceKm = distanceMi * kmPerMile;
				event1.addComment("Great circle distance from this event to " + 
									  event2.getSummary() + " is "+ 
									  distanceMi + "miles or " + 
									  distanceKm + "Km.");
					
				events.set(index1, event1);
				index1 = index2;
				event1 = events.get(index1);
				index2 = index1 + 1;
				if(index2 >= events.size()){
					eventsLeft = false;
				}else{
					event2 = events.get(index2);
				}
			}
		}while(eventsLeft);
		System.out.println("\n");
	
		
		try{
			writeEventFiles(events);
		}catch(IOException e){
			System.out.println("Writing events failed with message " + e.getMessage());
		}
		
		System.out.println("Distances written to event files as comments.\n");
	}
	
	/**
	 * @method readEvents
	 * @param events
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @description Prompts the user to enter several filenames for .ics files,
	 * 				then reads those files into the events ArrayList for processing.
	 */
	public static void readEvents(ArrayList<Event> events) throws FileNotFoundException, IOException{
			String filename = "";

			Scanner input = new Scanner(System.in);

			
			int i = 0; //total number of events being entered
			int j = 0; //iteration variable
			System.out.println("How many events are you checking?");
			i = input.nextInt();
			

			while (j < i) {
				System.out.println("What is the name of file " + (j+1) + "?");
				
				filename = input.next();


				try{
					events.add(new Event(filename));
				}catch(FileNotFoundException e){
					System.out.println("File not found with this name, try again.");
					j--;
				}
					
					
				j++;
			} 


	}
	
	
	
	public static void createEvent() throws IOException{
		/** Starting Variables */

		Scanner scan = new Scanner(System.in);
		// Time Variables
		String startH, startM, endH, endM;
		// Date Variables
		String event, description, classification, startD, endD;
		// Geographic Position Variables
		String location, geo1, geo2, geoA;

		// Here is where the console starts
		System.out.println("What do you want the file to be called?");

		String filename = scan.nextLine();
		FileWriter fw = new FileWriter(filename + ".ics");

		System.out.println("Please enter name of event: ");
		event = scan.nextLine();

		System.out.println("Please enter the location/address: "
		+ "Ex. University of Hawaii at Manoa, 2500 Campus Road, Honolulu, HI 96822, United States"); 
		location = scan.nextLine();
		
		System.out.println("Type in any extra notes: ");
		description = scan.nextLine();

		System.out.println("Is the classification- Public, Private or Confidential? Please type in all caps: ");
		classification = scan.nextLine();

		System.out.println("Do you want to put the geographic position? Yes or no");
		geoA = scan.nextLine();
		
		if (geoA.equalsIgnoreCase("yes")) {
			System.out.println("Enter the latitude and longitude separated by a semicolon. Ex. 37.386013;-122.082932");
			geo2 = scan.nextLine();
			geo1 = "\nGEO:";
		} 
		else {
			geo1 = "";
			geo2 = "";
		}
		
		System.out.println("What is the date of the event? Ex. 20150711");
		startD = scan.nextLine();
		
		System.out.println("Time starting? Just hour Ex. 00 for 12am, 23 for 11pm");
		startH = scan.nextLine();
		
		System.out.println("Now minute? Ex. 01 for 1 minute, 59 for 59 minutes");
		startM = scan.nextLine();
		
		System.out.println("What date is it ending?");
		endD = scan.nextLine();

		System.out.println("Time ending? Just hour Ex. 00 for 12am, 23 for 11pm");
		endH = scan.nextLine();
		
		System.out.println("Now minute? Ex. 01 for 1 minute, 59 for 59 minutes");
		endM = scan.nextLine();

		fw.write("BEGIN:VCALENDAR"
				+ "\nPRODID:-//Google Inc//Google Calendar 70.9054//EN"
				+ "\nVERSION:2.0" + "\nCALSCALE:GREGORIAN" + "\nMETHOD:PUBLISH"
				+ "\nX-WR-CALNAME:jurberry@gmail.com"
				+ "\nX-WR-TIMEZONE:Pacific/Honolulu" + "\nBEGIN:VEVENT"
				+ "\nDTSTART:" + startD + "T" + startH + startM + "00Z"
				+ "\nDTEND:" + endD + "T" + endH + endM + "00Z"
				+ "\nDTSTAMP:" + LocalDateTime.now()
				+ "\nUID:m64ebtqv877mke9dbobsnrotq4@google.com"
				+ geo1
				+ geo2
				+ "\nDESCRIPTION:" + description
				+ "\nCLASS:" + classification
				+ "\nCREATED:" + LocalDateTime.now()
				+ "\nLAST-MODIFIED:" + LocalDateTime.now()
				+ "\nLOCATION:" + location				
				+ "\nSEQUENCE:0"
				+ "\nSTATUS:CONFIRMED"
				+ "\nSUMMARY:" + event
				+ "\nTRANSP:OPAQUE" + "\nEND:VEVENT" + "\nEND:VCALENDAR");

		//Close FileWriter and Scanner

		fw.close();
	}
	
	/**
	 * @method writeEventFiles
	 * @param events
	 * @description Given an ArrayList of Events, writes each of the contained events 
	 * 				to files according to the filenames included in the Event objects.
	 */
	public static void writeEventFiles(ArrayList<Event> events) throws IOException{
		for(Event e: events){

			FileWriter fw = new FileWriter(e.getFilename());
			fw.write(e.getRawFile());
			fw.close();
		}
	}
	
	/**
	 * @method calcDistanceV2
	 * @param latA, lngA, latB, lngB : double
	 * @return          circle distance
	 * @description     Borrowed from sample online math formulas. This method will return the circle distance 
	 * 					between two points in miles. This is another version of the calculation formula that may be more efficient
	 * 					on average.
	 */
	public static double calcDistanceV2(double latA, double lngA, double latB, double lngB) 
	{
  		double earth_radius = 3955.5467; //radius of the earth in miles
        double haversine = 0.5 - Math.cos((latB - latA) * Math.PI / 180)/2 + Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * 
     					   (1 - Math.cos((lngB - lngA) * Math.PI / 180))/2;
		return earth_radius * 2 * Math.asin(Math.sqrt(haversine));
	}

	
}