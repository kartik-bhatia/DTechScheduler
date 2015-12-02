package org.optaplanner.examples.curriculumcourse.solver.score;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Student;

import java.util.*;

/**
 * Created by shpandha on 9/7/2015.
 */
public class StudentEasyScoreCalculator implements EasyScoreCalculator<CourseSchedule> {

    /**
     * A very simple implementation. The double loop can easily be removed by using Maps as shown in
     *
     */
    public HardSoftScore calculateScore(CourseSchedule courseSchedule) {
        int hardScore = 0;
        int softScore = 0;

        List<Lecture> lectureList = courseSchedule.getLectureList();

        Map<String, Set<Period>> studentPeriodSetMap = new HashMap<String, Set<Period>>();
        Set<Period> studentPeriod =  new HashSet<Period>();
        for (Lecture lecture : lectureList) {
            List<Student> studentList = lecture.getStudentList();
            studentPeriod.add(lecture.getPeriod());
            for (Student student : studentList) {
                if (studentPeriodSetMap.containsKey(student.getCode())){
                    Set<Period> periodSet = studentPeriodSetMap.get(student.getCode());
                    if (periodSet.contains(lecture.getPeriod())) {
                        hardScore -= 1;
                    } else {
                        if (periodSet != null) {
                            periodSet.add(lecture.getPeriod());
                        } else {
                            periodSet = new HashSet<Period>();
                            periodSet.add(lecture.getPeriod());
                        }
                    }
                }
                Set<Period> periodSet = new HashSet<Period>();
                periodSet.add(lecture.getPeriod());
                studentPeriodSetMap.put(student.getCode(), periodSet);
            }
        }


//        for (CloudComputer computer : cloudBalance.getComputerList()) {
//            int cpuPowerUsage = 0;
//            int memoryUsage = 0;
//            int networkBandwidthUsage = 0;
//            boolean used = false;
//
//            // Calculate usage
//            for (CloudProcess process : cloudBalance.getProcessList()) {
//                if (computer.equals(process.getComputer())) {
//                    cpuPowerUsage += process.getRequiredCpuPower();
//                    memoryUsage += process.getRequiredMemory();
//                    networkBandwidthUsage += process.getRequiredNetworkBandwidth();
//                    used = true;
//                }
//            }
//
//            // Hard constraints
//            int cpuPowerAvailable = computer.getCpuPower() - cpuPowerUsage;
//            if (cpuPowerAvailable < 0) {
//                hardScore += cpuPowerAvailable;
//            }
//            int memoryAvailable = computer.getMemory() - memoryUsage;
//            if (memoryAvailable < 0) {
//                hardScore += memoryAvailable;
//            }
//            int networkBandwidthAvailable = computer.getNetworkBandwidth() - networkBandwidthUsage;
//            if (networkBandwidthAvailable < 0) {
//                hardScore += networkBandwidthAvailable;
//            }
//
//            // Soft constraints
//            if (used) {
//                softScore -= computer.getCost();
//            }
//        }
        return HardSoftScore.valueOf(hardScore, softScore);
    }

}
