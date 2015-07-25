This repository is for the Calendar system program for ICS 314 of Summer 2015.
Team members: Tsun Chu, Ryan Buillard, Matt Cieslak



Below is an early version of the program, not removed from this file for the moment.


/**
 * @description   iCal first Deliverable: Create an .ics file that is readable on any public .ics
 *                related application.
 * @author        Tsun Chu, Ryan Buillard, 
 * @date          2015.10.07
 */

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class I_Cal 
{
	//main method
	public static void main(String args[]) throws IOException
	{
		//Event specs for calendar event
		String event;
		String startTime;
		String endTime;
		String location;
		
		Scanner scan = new Scanner(System.in);
		System.out.printf("Please enter name of event: "); event = scan.nextLine();
		System.out.printf("Enter start time in the following formatted example -> 20150711T044403Z : "); startTime = scan.nextLine();
		System.out.printf("Enter start time in the following formatted example -> 20150711T070000Z : "); endTime = scan.nextLine();
		System.out.printf("Please enter the location/address: "); location = scan.nextLine();
		
		FileWriter fw = new FileWriter(event + ".ics");
		fw.write("BEGIN:VCALENDAR\nPRODID:-//Google Inc//Google Calendar 70.9054//EN\n" +
				"VERSION:2.0\nCALSCALE:GREGORIAN\nMETHOD:PUBLISH\nX-WR-CALNAME:jurberry@gmail.com" + 
				"\nX-WR-TIMEZONE:Pacific/Honolulu\nBEGIN:VEVENT\nDTSTART:" + startTime + 
				"\nDTEND:" + endTime + "\nDTSTAMP:" + LocalDateTime.now() + "\nUID:m64ebtqv877mke9dbobsnrotq4@google.com" +
				"\nCLASS:PUBLIC\nCREATED:" + LocalDateTime.now() + "\nDESCRIPTION:First iCal Deliverable" +
				"\nLAST-MODIFIED:" + LocalDateTime.now() + "\nLOCATION:\"" + location + 
				"\"\nSEQUENCE:0\nSTATUS:CONFIRMED\nSUMMARY:" + event + "\nTRANSP:OPAQUE" +
				"\nEND:VEVENT\nEND:VCALENDAR");
		scan.close();
		fw.close();
	}
}
