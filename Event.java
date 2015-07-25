import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Event
 * 
 * @description Event object class for use in the ICS 314 calendar program.
 * @author Tsun Chu, Ryan Buillard, Matt Cieslak
 * @date 2015.24.07
 *
 */

public class Event implements Comparable {
	public String filename;
	public String rawFile;
	public Double latitude = null;
	public Double longitude = null;
	public int startTime;
	public int endTime;
	public String summary;
	
	/**
	 * Event Class Constructor
	 */
	public Event(String filename) throws FileNotFoundException, IOException{
		//Read file to get raw text as a String
		this.filename = filename;
		BufferedReader br = new BufferedReader(new FileReader(filename));
		rawFile = "";

		String oneLine;
		while((oneLine = br.readLine()) != null){
			rawFile +=  oneLine + "\n";
		}
		br.close();
		
		/* Parsing relevant fields for this event.*/
		
		//Parse start time.
		Pattern startTimePattern = Pattern.compile(".*DTSTART:\\d{8}T(\\d{4}).*Z.*", Pattern.DOTALL); 
		Matcher startTimeMatch = startTimePattern.matcher(rawFile);
		if(startTimeMatch.matches()){
			startTime = Integer.parseInt(startTimeMatch.group(1));
		}else{
			System.out.println("NO START TIME MATCH");
		}
		
		//Parse end time.
		Pattern endTimePattern = Pattern.compile(".*DTEND:\\d{8}T(\\d{4}).*Z.*", Pattern.DOTALL); 
		Matcher endTimeMatch = endTimePattern.matcher(rawFile);
		if(endTimeMatch.matches()){
			endTime = Integer.parseInt(endTimeMatch.group(1));
		}else{
			System.out.println("NO END TIME MATCH");
		}
		
		//Parse coordinates.
		Pattern coordPattern = Pattern.compile(".*GEO:(-?\\d+\\.?\\d*);(-?\\d+\\.?\\d*).*", Pattern.DOTALL); 
		Matcher coordMatch = coordPattern.matcher(rawFile);
		if(coordMatch.matches()){
			latitude = Double.parseDouble(coordMatch.group(1));
			longitude = Double.parseDouble(coordMatch.group(2));
		}else{
			//System.out.println("no coordinates");
		}
		
		//Parse summary
		Pattern summPattern = Pattern.compile(".*SUMMARY:([^\n]+)\n.*", Pattern.DOTALL);
		Matcher summMatch = summPattern.matcher(rawFile);
		if(summMatch.matches()){
			summary = summMatch.group(1);
		}else{
			System.out.println("NO SUMMARY MATCH");
		}
	}
	
	/**
	 * @method parseFields
	 * @param void
	 * @return          void
	 * @description     extracts fields from the raw text of an .ics file and sets 
	 *                  them to the variables of this Event.
	 */
	private void parseFields(String rawFile){

	}
	
	/**
	 * @method addComment
	 * @param commentString
	 * @description adds a comment field to the raw text of this .ics file, 
	 * 				containing the content of commentString
	 */
	public void addComment(String commentString){
		String[] eventParts = rawFile.split("END:VEVENT");
		String updatedEvent = eventParts[0] + "COMMENT:" + commentString + "\nEND:VEVENT" + eventParts[1];
		rawFile = updatedEvent;
	}
	
	/**
	 * @method compareTo
	 * @param otherEvent: Event
	 * @return int
	 * @description Compares the otherEvent to this event, returns -1 if this event
	 *              starts before other, 0 if they start at the same time, or 1 if 
	 *              this event starts after the other.
	 * 
	 */
	public int compareTo(Object otherEvent) throws ClassCastException {
		if(!(otherEvent instanceof Event)){
			throw new ClassCastException("This is not a valid Event.");
		}
		if(this.startTime < ((Event)otherEvent).getStartTime()){
			return -1;
		}else if(this.startTime == ((Event)otherEvent).getStartTime()){
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * @method startTimeToString
	 * @return the start time of this event as a String
	 * @description Returns the start time of this event as an easy to read string, 
	 * 				like 1:34 am.
	 */
	public String startTimeToString(){
		String startTimeString = "";
		String dayHalf;
		
		int hour = startTime/100;
		int minute = startTime - (hour*100);
		
		if(hour > 12){
			hour = hour - 12;
		}
		startTimeString += hour + ":";
		
		if(minute < 10){
			startTimeString += "0" + minute;
		}else{
			startTimeString += minute;
		}
		
		if(startTime < 1200) {
			startTimeString += " AM";
		}else{
			startTimeString += " PM";
		}

		return startTimeString;
	}
	
	/**
	 * @method endTimeToString
	 * @return the end time of this event as a String
	 * @description Returns the end time of this event as an easy to read string, 
	 * 				like 2:34 am.
	 */
	public String endTimeToString(){
		String endTimeString = "";
		String dayHalf;
		
		int hour = endTime/100;
		int minute = endTime - (hour*100);
		
		if(hour > 12){
			hour = hour - 12;
		}
		endTimeString += hour + ":";
		
		if(minute < 10){
			endTimeString += "0" + minute;
		}else{
			endTimeString += minute;
		}
		
		if(endTime < 1200) {
			endTimeString += " AM";
		}else{
			endTimeString += " PM";
		}

		return endTimeString;
	}
	
	public String getFilename(){
		return filename;
	}
	public String getRawFile(){
		return rawFile;
	}
	public Double getLatitude(){
		return latitude;
	}
	public Double getLongitude(){
		return longitude;
	}
	public int getStartTime(){
		return startTime;
	}
	public int getEndTime(){
		return endTime;
	}
	public String getSummary(){
		return summary;
	}
}
