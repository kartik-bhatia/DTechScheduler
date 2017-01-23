package org.optaplanner.examples.curriculumcourse.domain;

import java.util.Date;

/**
 * class for Assignment
 * @author kartikbhatia
 *
 */
public class Assignment {
	private String assignmentTitle;
	private String grade;
	private int studentId;
        private String studentName;
	private int teacherId;
	private String courseName;
	private String teacherName;
	private Date dueDate;
	
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getAssignmentTitle() {
		return assignmentTitle;
	}
	public void setAssignmentTitle(String assignmentName) {
		this.assignmentTitle = assignmentName;
	}
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		return ("StudentId=" + this.getStudentId() + "Subject = " + this.getCourseName() + " AssignmentTitle="+this.getAssignmentTitle() 
				+ " Grade=" +this.getGrade());
	}

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }
}


