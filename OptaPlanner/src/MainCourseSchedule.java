import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.curriculumcourse.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shpandha on 9/6/2015.
 */
public class MainCourseSchedule {
    public static void main(String[] args) {

        Teacher t1 = new Teacher();
        t1.setCode("t1");

        Teacher t2 = new Teacher();
        t2.setCode("t2");

        Course c1 = new Course();
        c1.setCode("eco");
        c1.setTeacher(t1);

        Course c2 = new Course();
        c2.setCode("math");
        c2.setTeacher(t2);

        Day d1 = new Day();
        d1.setDayIndex(3);

        Day d2 = new Day();
        d2.setDayIndex(4);

        List<Teacher> teacherList = new ArrayList<Teacher>();
        teacherList.add(t1);
        teacherList.add(t2);

        List<Course> coursesList = new ArrayList<Course>();
        coursesList.add(c1);
        coursesList.add(c2);

        List<Day> dayList = new ArrayList<Day>();
        dayList.add(d1);
        dayList.add(d2);

        CourseSchedule cs = new CourseSchedule();
        cs.setTeacherList(teacherList);
        cs.setDayList(dayList);
        cs.setCourseList(coursesList);

        createLectureList(cs);

        // Build the Solver
        SolverFactory solverFactory = SolverFactory.createFromXmlResource(
                "org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        solver.solve(cs);
        CourseSchedule solvedCS = (CourseSchedule) solver.getBestSolution();

    }
    private static void createLectureList(CourseSchedule schedule) {
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
