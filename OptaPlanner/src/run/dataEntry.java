package run;
import java.util.Comparator;

public class dataEntry implements Comparable<dataEntry> {
    
    int studentCode;
    String period;
    String courseName;
    String courseCode;
    String teacherName;
    String room;
    
    public dataEntry(){
        
    }
    public dataEntry(int studentCode,String period, String courseName, String courseCode, String teacherName, String room) {    
        this.setStudentCode(studentCode);
        this.setCourseCode(courseCode);
        this.setCourseName(courseName);
        this.setRoom(room);
        this.setTeacherName(teacherName);
        this.setPeriod(period);
    }


    public void setStudentCode(int studentCode) {
        this.studentCode = studentCode;
    }

    public int getStudentCode() {
        return studentCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public int compareTo(dataEntry o) {
        return this.getStudentCode() - o.getStudentCode();
    }
}
