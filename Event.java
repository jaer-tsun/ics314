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
	public double latitude;
	public double longitude;
	public int startTime;
	
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
		
		//Parse the raw String to extract start time and coordinates of this event
		Pattern timePattern = Pattern.compile(".*DTSTART:\\d{8}T(\\d{4}).*Z.*", Pattern.DOTALL); //regex to match time
		Pattern coordPattern = Pattern.compile(".*GEO:(-?\\d+\\.?\\d*);(-?\\d+\\.?\\d*).*", Pattern.DOTALL); //regex to match coordinates

		Matcher timeMatch = timePattern.matcher(rawFile);
		Matcher coordMatch = coordPattern.matcher(rawFile);

		if(timeMatch.matches()){
			startTime = Integer.parseInt(timeMatch.group(1));
		}else{
			System.out.println("NO TIME MATCH");
		}
		if(coordMatch.matches()){
			latitude = Double.parseDouble(coordMatch.group(1));
			longitude = Double.parseDouble(coordMatch.group(2));
		}else{
			System.out.println("NO COORD MATCH");
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
	
	public String getFilename(){
		return filename;
	}
	public String getRawFile(){
		return rawFile;
	}
	public double getLatitude(){
		return latitude;
	}
	public double getLongitude(){
		return longitude;
	}
	public int getStartTime(){
		return startTime;
	}
}
