/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.curriculumcourse.persistence;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionImporter;
import org.optaplanner.examples.curriculumcourse.domain.*;

import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CurriculumCourseImporter extends AbstractTxtSolutionImporter {

    private static final String INPUT_FILE_SUFFIX = "ctt";
    private static final String SPLIT_REGEX = "[\\ \\t]+";
    private static final String ROOMS_INPUT_FILE = "../../flatfiles/rooms.ctt";
    private static final String SLOTS_INPUT_FILE = "../../flatfiles/slots.ctt";
    private static final String DATA_INPUT_FILE  = "../../mytestdata.csv"; 
    

    public static void main(String[] args) {
        new CurriculumCourseImporter().convertAll();
    }

    public CurriculumCourseImporter() {
        super(new CurriculumCourseDao());
    }

    @Override
    public String getInputFileSuffix() {
        return INPUT_FILE_SUFFIX;
    }

    public TxtInputBuilder createTxtInputBuilder() {
        return new CurriculumCourseInputBuilder();
    }

    public static class CurriculumCourseInputBuilder extends TxtInputBuilder {

        public Solution readSolution() throws IOException {
            CourseSchedule schedule = new CourseSchedule();
            schedule.setId(0L);
            // Name: ToyExample
            schedule.setName(readStringValue("Name:"));
            // Courses: 4
            //kartik    int courseListSize = readIntegerValue("Courses:");
            int courseListSize = 1; 
            // Rooms: 2
            int roomListSize = readIntegerValue("Rooms:");
            // Days: 5
            int dayListSize = readIntegerValue("Days:");
            // Periods_per_day: 4
            int timeslotListSize = readIntegerValue("Periods_per_day:");
            // Curricula: 2
            //kartik int curriculumListSize = readIntegerValue("Curricula:");
            int curriculumListSize = 1; //fixed at 1
            // Constraints: 8
            int unavailablePeriodPenaltyListSize = readIntegerValue("Constraints:");

            Map<String, Course> courseMap = readCourseListAndTeacherList(
                    schedule, courseListSize);
            readRoomList(
                    schedule, roomListSize);
            Map<List<Integer>, Period> periodMap = createPeriodListAndDayListAndTimeslotList(
                    schedule, dayListSize, timeslotListSize);
            readCurriculumList(
                    schedule, courseMap, curriculumListSize);
            readUnavailablePeriodPenaltyList(
                    schedule, courseMap, periodMap, unavailablePeriodPenaltyListSize);
            readEmptyLine();
            readConstantLine("END\\.");
            createLectureList(schedule);

            int possibleForOneLectureSize = schedule.getPeriodList().size() * schedule.getRoomList().size();
            BigInteger possibleSolutionSize = BigInteger.valueOf(possibleForOneLectureSize).pow(
                    schedule.getLectureList().size());
            logger.info("CourseSchedule {} has {} teachers, {} curricula, {} courses, {} lectures," +
                    " {} periods, {} rooms and {} unavailable period constraints with a search space of {}.",
                    getInputId(),
                    schedule.getTeacherList().size(),
                    schedule.getCurriculumList().size(),
                    schedule.getCourseList().size(),
                    schedule.getLectureList().size(),
                    schedule.getPeriodList().size(),
                    schedule.getRoomList().size(),
                    schedule.getUnavailablePeriodPenaltyList().size(),
                    getFlooredPossibleSolutionSize(possibleSolutionSize));
            return schedule;
        }

        /*private Map<String, Course> readCourseListAndTeacherList(
                CourseSchedule schedule, int courseListSize) throws IOException {
            Map<String, Course> courseMap = new HashMap<String, Course>(courseListSize);
            Map<String, Teacher> teacherMap = new HashMap<String, Teacher>();
            Map<String, Student> studentMap = new HashMap<String, Student>();
            List<Course> courseList = new ArrayList<Course>(courseListSize);
            readEmptyLine();
            readConstantLine("COURSES:");
            for (int i = 0; i < courseListSize; i++) {
                Course course = new Course();
                course.setId((long) i);
                // Courses: <CourseID> <Teacher> <# Lectures> <MinWorkingDays> <# Students>
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line, null);
                course.setCode(lineTokens[0]);
                course.setTeacher(findOrCreateTeacher(teacherMap, lineTokens[1]));
                course.setLectureSize(Integer.parseInt(lineTokens[2]));
                course.setMinWorkingDaySize(Integer.parseInt(lineTokens[3]));
                course.setCurriculumList(new ArrayList<Curriculum>());
                course.setStudentSize(Integer.parseInt(lineTokens[4]));

                course.setStudent(findOrCreateStudent(studentMap, lineTokens, 5));

                courseList.add(course);
                courseMap.put(course.getCode(), course);
            }
            schedule.setCourseList(courseList);
            List<Teacher> teacherList = new ArrayList<Teacher>(teacherMap.values());
            List<Student> studentList = new ArrayList<Student>(studentMap.values());
            schedule.setTeacherList(teacherList);
            schedule.setStudentList(studentList);
            return courseMap;
        }
        
        */
        //Kartik - commented this original code and added the below code to read course , student and teacher data from out.csv 
        
        private Map<String, Course> readCourseListAndTeacherList(CourseSchedule schedule, int courseListSize) throws IOException {
            Map<String, Course> courseMap = new HashMap<String, Course>(courseListSize);
            Map<String, Teacher> teacherMap = new HashMap<String, Teacher>();
            Map<String, Student> studentMap = new HashMap<String, Student>();
            List<Course> courseList = new ArrayList<Course>(courseListSize);
            
            //read the csv and get a listOf Failed Assignments
            ReadGradeSheet readGradeSheet = new ReadGradeSheet();
            List<Assignment> assignmentList = new ArrayList();
            Date fromDate = readGradeSheet.convertStringToDate("01/14/16");
    		Date toDate   = readGradeSheet.convertStringToDate("01/20/16");
            assignmentList = readGradeSheet.getFailedAssignments(DATA_INPUT_FILE, fromDate, toDate);
            
            //convert the assignment List into Course List
            for (int i =0;i<assignmentList.size();i++)
            {
            	Assignment assignment = assignmentList.get(i);
            	boolean assignmentAlreadyInCourseList = false;
            	for(int j = 0;j<courseList.size();j++)
            	{
            		Course course = courseList.get(j);
            		if (course.getCode().equals(assignment.getAssignmentTitle()))
            		{
            			assignmentAlreadyInCourseList = true;
            			Student newStudent = findOrCreateStudent(studentMap,Integer.toString(assignment.getStudentId()));
            			course.getStudentList().add(newStudent);
            			course.setStudentSize(course.getStudentSize()+1);
            		}
            	}
            	if (assignmentAlreadyInCourseList == false)
            	{
	            	//else courseList does not have an entry for the current assignment
	            	//so add that in the course List
	        		Course course = new Course();
	                course.setId((long) i);            		
	                course.setCode(assignment.getAssignmentTitle());
	                course.setTeacher(findOrCreateTeacher(teacherMap, Integer.toString(assignment.getTeacherId())));
	                course.setLectureSize(1);
	                course.setMinWorkingDaySize(1);
	                course.setCurriculumList(new ArrayList<Curriculum>());
	                course.setStudentSize(1);
	                
	                List<Student> studentList = new ArrayList<Student>();
	                Student newStudent = findOrCreateStudent(studentMap,Integer.toString(assignment.getStudentId()));
	                studentList.add(newStudent);
	                course.setStudent(studentList);
	                
	                courseList.add(course);
	                courseMap.put(course.getCode(), course);
            	}
            }
            
            schedule.setCourseList(courseList);
            List<Teacher> teacherList = new ArrayList<Teacher>(teacherMap.values());
            List<Student> studentList = new ArrayList<Student>(studentMap.values());
            schedule.setTeacherList(teacherList);
            schedule.setStudentList(studentList);
            return courseMap;
        }
        

        private static Teacher findOrCreateTeacher(Map<String, Teacher> teacherMap, String code) {
            Teacher teacher = teacherMap.get(code);
            if (teacher == null) {
                teacher = new Teacher();
                int id = teacherMap.size();
                teacher.setId((long) id);
                teacher.setCode(code);
                teacherMap.put(code, teacher);
            }
            return teacher;
        }

        private static Student findOrCreateStudent(Map<String, Student> studentMap, String code ) {
    //        List<Student> studentList = new ArrayList<>();
    //        for (int i = startIndex; i < codes.length; i++) {
    //            String code = codes[i];
                Student student = studentMap.get(code);
                if (student == null) {
                    student = new Student();
                    int id = studentMap.size();
                    student.setId((long) id);
                    student.setCode(code);
                    studentMap.put(code, student);
                }
            
            return student;
        }
        //commented this code and replaced with one that reads rooms.txt
       /* private void readRoomList(CourseSchedule schedule, int roomListSize)
                throws IOException {
            readEmptyLine();
            readConstantLine("ROOMS:");
            List<Room> roomList = new ArrayList<Room>(roomListSize);
            for (int i = 0; i < roomListSize; i++) {
                Room room = new Room();
                room.setId((long) i);
                // Rooms: <RoomID> <Capacity>
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line, 2);
                room.setCode(lineTokens[0]);
                room.setCapacity(Integer.parseInt(lineTokens[1]));
                roomList.add(room);
            }
            schedule.setRoomList(roomList);
        } */ 

        private void readRoomList(CourseSchedule schedule, int roomListSize)
                      throws IOException {
                  
                  List<Room> roomList = new ArrayList<Room>(roomListSize);
                  BufferedReader roomsBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(ROOMS_INPUT_FILE), "UTF-8"));
                  int i = 0;
                  String line = roomsBufferedReader.readLine();
                  while (line!=null) {
                      Room room = new Room();
                      room.setId((long) i++);
                      String[] lineTokens = splitBySpacesOrTabs(line, 2);
                      room.setCode(lineTokens[0]);
                      room.setCapacity(Integer.parseInt(lineTokens[1]));
                      roomList.add(room);
                      line = roomsBufferedReader.readLine();
                  }
                  schedule.setRoomList(roomList);
              }

        private Map<List<Integer>, Period> createPeriodListAndDayListAndTimeslotList(
                CourseSchedule schedule, int dayListSize, int timeslotListSize) throws IOException {
            int periodListSize = dayListSize * timeslotListSize;
            Map<List<Integer>, Period> periodMap = new HashMap<List<Integer>, Period>(periodListSize);
            List<Day> dayList = new ArrayList<Day>(dayListSize);
            for (int i = 0; i < dayListSize; i++) {
                Day day = new Day();
                day.setId((long) i);
                day.setDayIndex(i);
                day.setPeriodList(new ArrayList<Period>(timeslotListSize));
                dayList.add(day);
            }
            schedule.setDayList(dayList);
            List<Timeslot> timeslotList = new ArrayList<Timeslot>(timeslotListSize);
            for (int i = 0; i < timeslotListSize; i++) {
                Timeslot timeslot = new Timeslot();
                timeslot.setId((long) i);
                timeslot.setTimeslotIndex(i);
                timeslotList.add(timeslot);
            }
            schedule.setTimeslotList(timeslotList);
            List<Period> periodList = new ArrayList<Period>(periodListSize);
            for (int i = 0; i < dayListSize; i++) {
                Day day = dayList.get(i);
                for (int j = 0; j < timeslotListSize; j++) {
                    Period period = new Period();
                    period.setId((long) (i * timeslotListSize + j));
                    period.setDay(day);
                    period.setTimeslot(timeslotList.get(j));
                    periodList.add(period);
                    periodMap.put(Arrays.asList(i, j), period);
                    day.getPeriodList().add(period);
                }
            }
            schedule.setPeriodList(periodList);
            return periodMap;
        }

        /*private void readCurriculumList(CourseSchedule schedule,
                Map<String, Course> courseMap, int curriculumListSize) throws IOException {
            readEmptyLine();
            readConstantLine("CURRICULA:");
            List<Curriculum> curriculumList = new ArrayList<Curriculum>(curriculumListSize);
            for (int i = 0; i < curriculumListSize; i++) {
                Curriculum curriculum = new Curriculum();
                curriculum.setId((long) i);
                // Curricula: <CurriculumID> <# Courses> <MemberID> ... <MemberID>
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line);
                if (lineTokens.length < 2) {
                    throw new IllegalArgumentException("Read line (" + line
                            + ") is expected to contain at least 2 tokens.");
                }
                curriculum.setCode(lineTokens[0]);
                int coursesInCurriculum = Integer.parseInt(lineTokens[1]);
                if (lineTokens.length != (coursesInCurriculum + 2)) {
                    throw new IllegalArgumentException("Read line (" + line + ") is expected to contain "
                            + (coursesInCurriculum + 2) + " tokens.");
                }
                for (int j = 2; j < lineTokens.length; j++) {
                    Course course = courseMap.get(lineTokens[j]);
                    if (course == null) {
                        throw new IllegalArgumentException("Read line (" + line + ") uses an unexisting course("
                                + lineTokens[j] + ").");
                    }
                    course.getCurriculumList().add(curriculum);
                }
                curriculumList.add(curriculum);
            }
            schedule.setCurriculumList(curriculumList);
        } */
        
        //kartik new method for readCurriculumList so as to add all the courses in the only one curriculum 
        private void readCurriculumList(CourseSchedule schedule, Map<String, Course> courseMap, int curriculumListSize) throws IOException {
        	List<Curriculum> curriculumList = new ArrayList<Curriculum>(1);
        	Curriculum curriculum = new Curriculum();
        	curriculum.setId(1L);
        	Iterator it = courseMap.entrySet().iterator();
        	while (it.hasNext()){
        		Entry<String,Course> e = (Entry<String, Course>) it.next();
                Course course = (Course) e.getValue();
                if (course == null) {
                    throw new IllegalArgumentException("Course not in Curriculum");
                }
                course.getCurriculumList().add(curriculum);
            }
        	curriculumList.add(curriculum);
        	schedule.setCurriculumList(curriculumList);
        	
        }
        

        private void readUnavailablePeriodPenaltyList(CourseSchedule schedule, Map<String, Course> courseMap,
                Map<List<Integer>, Period> periodMap, int unavailablePeriodPenaltyListSize)
                throws IOException {
            List<UnavailablePeriodPenalty> penaltyList = new ArrayList<UnavailablePeriodPenalty>(
                    unavailablePeriodPenaltyListSize);
            
            /* read slots.ctt and add penalty constraints based on which slot is not available for scheduling */
            BufferedReader slotsBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(SLOTS_INPUT_FILE), "UTF-8"));
            int index = 0;
            int slotlinenumber = 0;
            String slotline = slotsBufferedReader.readLine();
            while (slotline!=null) {
                String[] lineTokens = splitBySpacesOrTabs(slotline, 3);
                
                if(lineTokens[1].equals("false")) {
                    Iterator it = courseMap.entrySet().iterator();
                    while (it.hasNext()){
                            Entry<String,Course> e = (Entry<String, Course>)it.next();
                    Course course = (Course)e.getValue();
                    if (course == null) {
                        throw new IllegalArgumentException("Course not in Curriculum");
                    }
                    UnavailablePeriodPenalty penalty = new UnavailablePeriodPenalty();
                    penalty.setId((long)index++);
                    penalty.setCourse(course);
                    int dayIndex = 0;
                    int timeslotIndex = slotlinenumber;
                    Period period = periodMap.get(Arrays.asList(dayIndex, timeslotIndex));
                    if (period == null) {
                        throw new IllegalArgumentException("Read line (" + slotline + ") uses an unexisting period("
                                + dayIndex + " " + timeslotIndex + ").");
                    }
                    penalty.setPeriod(period);
                    penaltyList.add(penalty);
                    }
                }
                if(lineTokens[2].equals("false")) {
                    Iterator it = courseMap.entrySet().iterator();
                    while (it.hasNext()){
                            Entry<String,Course> e = (Entry<String, Course>)it.next();
                    Course course = (Course)e.getValue();
                    if (course == null) {
                        throw new IllegalArgumentException("Course not in Curriculum");
                    }
                    UnavailablePeriodPenalty penalty = new UnavailablePeriodPenalty();
                    penalty.setId((long)index++);
                    penalty.setCourse(course);
                    int dayIndex = 1;
                    int timeslotIndex = slotlinenumber;
                    Period period = periodMap.get(Arrays.asList(dayIndex, timeslotIndex));
                    if (period == null) {
                        throw new IllegalArgumentException("Read line (" + slotline + ") uses an unexisting period("
                                + dayIndex + " " + timeslotIndex + ").");
                    }
                    penalty.setPeriod(period);
                    penaltyList.add(penalty);
                    }
                }
                slotlinenumber++;
                slotline = slotsBufferedReader.readLine();
            }
            
            
            
            
            /*read other unavailability constraints */
            readEmptyLine();
            readConstantLine("UNAVAILABILITY_CONSTRAINTS:");
            for (int i = 0; i < unavailablePeriodPenaltyListSize; i++) {
                // Unavailability_Constraints: <CourseID> <Day> <Day_Period>
                String line = bufferedReader.readLine();
                
                String[] lineTokens = splitBySemicolonSeparatedValue(line, 3);
                
                
              /*  if (lineTokens[0].equals("*"))
                {
                	Iterator it = courseMap.entrySet().iterator();
                	while (it.hasNext()){
                		Entry<String,Course> e = (Entry<String, Course>)it.next();
                        Course course = (Course)e.getValue();
                        if (course == null) {
                            throw new IllegalArgumentException("Course not in Curriculum");
                        }
                        UnavailablePeriodPenalty penalty = new UnavailablePeriodPenalty();
                        penalty.setId((long)index++);
                        penalty.setCourse(course);
                        int dayIndex = Integer.parseInt(lineTokens[1]);
                        int timeslotIndex = Integer.parseInt(lineTokens[2]);
                        Period period = periodMap.get(Arrays.asList(dayIndex, timeslotIndex));
                        if (period == null) {
                            throw new IllegalArgumentException("Read line (" + line + ") uses an unexisting period("
                                    + dayIndex + " " + timeslotIndex + ").");
                        }
                        penalty.setPeriod(period);
                        penaltyList.add(penalty);
                    }
                }
                else { */
                	UnavailablePeriodPenalty penalty = new UnavailablePeriodPenalty();
                    penalty.setId((long) index++);
                    if(courseMap.containsKey(lineTokens[0]))
                    {                    
                        penalty.setCourse(courseMap.get(lineTokens[0]));
                        int dayIndex = Integer.parseInt(lineTokens[1]);
                        int timeslotIndex = Integer.parseInt(lineTokens[2]);
                        Period period = periodMap.get(Arrays.asList(dayIndex, timeslotIndex));
                        if (period == null) {
                            throw new IllegalArgumentException("Read line (" + line + ") uses an unexisting period("
                                    + dayIndex + " " + timeslotIndex + ").");
                        }
                        penalty.setPeriod(period);
                        penaltyList.add(penalty);
                    }
                
                
            }
            schedule.setUnavailablePeriodPenaltyList(penaltyList);
        }

        private void createLectureList(CourseSchedule schedule) {
            List<Course> courseList = schedule.getCourseList();
            List<Lecture> lectureList = new ArrayList<Lecture>(courseList.size());
            long id = 0L;
            for (Course course : courseList) {
                for (int i = 0; i < course.getLectureSize(); i++) {
                    Lecture lecture = new Lecture();
                    lecture.setId((long) id);
                    id++;
                    lecture.setCourse(course);
                    lecture.setLectureIndexInCourse(i);
                    lecture.setLocked(false);
                    // Notice that we leave the PlanningVariable properties on null
                    lectureList.add(lecture);
                }
            }
            schedule.setLectureList(lectureList);
        }

    }

}
