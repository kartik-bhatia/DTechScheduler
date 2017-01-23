package org.optaplanner.examples.curriculumcourse.domain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;

/**
 * class for ReadGradeSheet
 * @author kartikbhatia
 *
 */
public class ReadGradeSheet {
	

	public static int threshhold = 75;
	private String FILE_PATH; 
            //		"/Users/kartikbhatia/Documents/Data/MyProjects/DTech/test.csv";
	
	//col number start from 0
        
	public static int STUDENT_ID_COL = 0;
        public static int STUDENT_NAME_COL = 1;
	public static int ASSIGNMENT_TITLE_COL = 2;
        public static int GRADE_COL = 3;
        public static int ASSIGNED_DATE_COL = 4;
        public static int CLASS_LOCALID_COL = 5;
        public static int DUEDATE_COL = 6;
        public static int ASSIGNMENT_CATEGORY_COL = 7;
        public static int COURSE_NAME_COL = 8;
        public static int REG_WINDOW_COL = 9;
	public static int TEACHER_FIRSTNAME = 10;
        public static int TEACHER_LASTNAME = 11;
        public static int TEACHER_ID_COL = 12;
	
	
	
	public List getFailedAssignments(String inputFilePath, Date fromDate, Date toDate) throws IOException {
		CSVReader reader = null;
		long totalCount = 0;
		long rowsEligibleCount = 0;
		long toBeScheduledCount = 0;
	    List assignmentList = new ArrayList();
	   // Date today = getTodaysDate();
	    try {
	    	reader = new CSVReader(new FileReader(inputFilePath));
			String [] row;
			while ((row = reader.readNext()) != null) {
				//logic to figure out if the current assignment is failed
				if(row.length>=8 && isNumeric(row[0])) // here check if the current row is a valid data row and not a header or blank
				{
					totalCount++;
					try 
					{
						Date dueDate = convertStringToDate(row[DUEDATE_COL]);
						//System.out.println("DueDate = " + row[6]);
						if (dueDate.getTime()>=fromDate.getTime() && dueDate.getTime()<=toDate.getTime() )
						{
							rowsEligibleCount++;
							String grade="NP";
							//if(row.length==8)
							grade = row[GRADE_COL];
							if (isNumeric(grade)){
								grade = grade.substring(0,grade.length()-1); // to remove the P at the end
								if((int)Float.parseFloat(grade)<threshhold) {
									toBeScheduledCount++;
									//put this in the array as a failed assignment
									Assignment assignment  = new Assignment();
									assignment.setStudentId(Integer.parseInt(row[STUDENT_ID_COL]));
                                                                        assignment.setStudentName(row[STUDENT_NAME_COL]);
									assignment.setTeacherId(Integer.parseInt(row[TEACHER_ID_COL]));
									assignment.setCourseName(row[COURSE_NAME_COL]);
									assignment.setTeacherName(row[TEACHER_FIRSTNAME]+" "+row[TEACHER_LASTNAME]);
									assignment.setAssignmentTitle(row[ASSIGNMENT_TITLE_COL]);
									assignment.setDueDate(this.convertStringToDate(row[DUEDATE_COL]));
									assignment.setGrade(row[GRADE_COL]);
									assignmentList.add(assignment);
								}
							}
							else // grade is non - numeric
							{
								if(grade.equals("DEV") || grade.equals("M") || grade.equals("NP")||grade.equals(""))
								{
									toBeScheduledCount++;
									//put this in the array as a failed assignment
									Assignment assignment  = new Assignment();
									assignment.setStudentId(Integer.parseInt(row[STUDENT_ID_COL]));
                                                                        assignment.setStudentName(row[STUDENT_NAME_COL]);
									assignment.setTeacherId(Integer.parseInt(row[TEACHER_ID_COL]));
									assignment.setCourseName(row[COURSE_NAME_COL]);
                                                                        assignment.setTeacherName(row[TEACHER_FIRSTNAME]+" "+row[TEACHER_LASTNAME]);
									assignment.setAssignmentTitle(row[ASSIGNMENT_TITLE_COL]);
									assignment.setDueDate(this.convertStringToDate(row[DUEDATE_COL]));
									assignment.setGrade(row[GRADE_COL]);
									assignmentList.add(assignment);
								}	
							}
						}
					}
					catch (Exception e)
					{	
						System.out.println("StudentId = " + row[0] + " Assignment = " + row[4]);
						e.printStackTrace();
					}
					
				}
				
			}
	    
			
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    } 
	    catch (Exception e) {
	    	
	    }
	    finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }
	    //System.out.println("Done.");
            CSVWriter writer = new CSVWriter(new FileWriter("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/filteredrows.csv"));
            for (int i = 0 ; i<assignmentList.size() ;i++){
                
                Assignment assignment = (Assignment)assignmentList.get(i);
                String[] str = new String[8];
                str[0] = Integer.toString(assignment.getStudentId());
                str[1] = assignment.getStudentName();
                str[2] = Integer.toString(assignment.getTeacherId());
                str[3] = assignment.getCourseName();
                str[4] = assignment.getTeacherName();
                str[5] = assignment.getAssignmentTitle();
                str[6] = assignment.getDueDate().toString();
                str[7] = assignment.getGrade();
                writer.writeNext(str);
            }
            
	    writer.close();
	    System.out.println("totalProcessed = "+ totalCount + " rowsEligibleCount = " + rowsEligibleCount + " toBeScheduledCount = " + toBeScheduledCount);
	    return assignmentList;
	    
	}

	public static boolean isNumeric(String str)
	{
		if(str.equals(""))
			return false;
		else
			
			{
			//remove the extra P at the end of the numbers 
			    if(str.endsWith("P"))
			    {
			    	str = str.substring(0, str.length()-1);
			    }
				NumberFormat formatter = NumberFormat.getInstance();
				ParsePosition pos = new ParsePosition(0);
				formatter.parse(str, pos);
				return str.length() == pos.getIndex();
			}
	  
	}
	
	public  Date convertStringToDate(String dateInString) {	
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyy");
		
		String[] tempDate = dateInString.split("/");
		if (tempDate.length !=3) {
			System.out.println("Something wrong");
			System.out.print("dateInString = " + dateInString);
		}
			
		for (int i = 0;i<3;i++)
			if (tempDate[i].length()==1)
				tempDate[i] = "0" + tempDate[i];
		dateInString = tempDate[0]+tempDate[1]+tempDate[2];
		//System.out.println("newDate = "+ dateInString);
		try {
			Date date = formatter.parse(dateInString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Date getTodaysDate(){
		Calendar cal = Calendar.getInstance();
		int dd = cal.get(Calendar.DAY_OF_MONTH);
		int mm = cal.get(Calendar.MONTH);
		int yy = cal.get(Calendar.YEAR);
		Calendar newCal = Calendar.getInstance();
		newCal.set(yy,mm,dd,0,0,0);
		return newCal.getTime();
	}
	
	
}
