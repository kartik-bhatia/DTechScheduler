package run;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.util.List;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.common.app.CommonApp;
import org.optaplanner.examples.common.business.SolutionBusiness;
import org.optaplanner.examples.common.persistence.AbstractSolutionExporter;
import org.optaplanner.examples.common.persistence.AbstractSolutionImporter;
import org.optaplanner.examples.common.persistence.SolutionDao;
import org.optaplanner.examples.common.swingui.SolutionPanel;
import org.optaplanner.examples.curriculumcourse.domain.AssignmentDependency;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Student;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseDao;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseExporter;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseImporter;
import org.optaplanner.examples.curriculumcourse.swingui.CurriculumCoursePanel;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;

import org.optaplanner.examples.curriculumcourse.domain.Assignment;


import java.io.FileOutputStream;

import java.io.FileNotFoundException;

import javax.xml.crypto.Data;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.ArrayList;
import java.util.Collections;

public class RunScheduler extends CommonApp {

    CourseSchedule courseSchedule;
    File parameterFile =
        new File("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/testgrades.ctt");
    SolutionBusiness solutionBusiness = createSolutionBusiness();

    public CourseSchedule run() {
        solutionBusiness.importSolution(parameterFile);
        //solution = txtInputBuilder.readSolution();
        Solution planningProblem = solutionBusiness.getSolution();
        //Solver solver = solverFactory.buildSolver();
        planningProblem = solutionBusiness.solve(planningProblem);

        courseSchedule = (CourseSchedule)planningProblem;
        //System.out.print(planningProblem.toString());

        printCSV(courseSchedule.getLectureList());


        printPDFMainSchedule(courseSchedule.getLectureList());
       
     //   printPDFStudentSchedule(courseSchedule.getLectureList());
        
        
        for (Lecture lecture : courseSchedule.getLectureList()) {

            System.out.println("Lecture: " + lecture.getLabel());
            //  System.out.println("Room: "+ lecture.getRoom().getLabel());
            System.out.println("Teacher: " + lecture.getTeacher().getLabel());
            //  System.out.println("Period: " + lecture.getPeriod().getLabel());
            System.out.println("Students: " + lecture.getStudentList());
            System.out.println("------");
        }
        System.out.println("SCORE = " + courseSchedule.getScore());
        return courseSchedule;
    }
    
    void printPDFMainSchedule(List<Lecture> lecturList)  {
        Document document = new Document();
           try
           {    
               PdfWriter.getInstance(document, new FileOutputStream("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/pdf.pdf"));
               document.open();
               PdfPTable table = new PdfPTable(4); // 3 columns.
               table.setWidthPercentage(100); //Width 100%
               table.setSpacingBefore(10f); //Space before table
               table.setSpacingAfter(10f); //Space after table
        
               //Set Column widths
               float[] columnWidths = {1f, 4f, 1f, 1f};
               table.setWidths(columnWidths);
               
               for (Lecture lecture : courseSchedule.getLectureList()) {
        
                   PdfPCell period = new PdfPCell(new Paragraph(lecture.getPeriod().getLabel()));
                   PdfPCell course = new PdfPCell(new Paragraph(lecture.getCourse().getCourseName() + " - " + lecture.getCourse().getCode()));
                   PdfPCell teacher = new PdfPCell(new Paragraph(lecture.getCourse().getTeacher().getName()));
                   PdfPCell room = new PdfPCell(new Paragraph(lecture.getRoom().getLabel()));
                   
                    table.addCell(period);
                    table.addCell(course);
                    table.addCell(teacher);
                    table.addCell(room);
               }
                   
               document.add(table);
               document.close();
               
    } catch (FileNotFoundException e) {
        } catch (DocumentException e) {
        }
    }

    void printPDFStudentSchedule(List<Lecture> lecturList)  {
        System.out.println("Entering printPDFStudentSchedule");
        List data = new ArrayList<dataEntry>();

        for (Lecture lecture : courseSchedule.getLectureList()) {
            for (Student student : lecture.getStudentList()){
                    
                dataEntry newEntry = new dataEntry(Integer.parseInt(student.getCode()),lecture.getPeriod().getLabel(), lecture.getCourse().getCourseName(),
                                                   lecture.getCourse().getCode(), lecture.getCourse().getTeacher().getName(), lecture.getRoom().getLabel());
                data.add(newEntry);
            }
        }
        System.out.println("Entering SORT ");
        Collections.sort(data);
        System.out.println("SORTING DONE ");
        
        int i = 0;
        int temp = 0;
        dataEntry de = (dataEntry)data.get(i);
        while (i<data.size()){
                   
            Document document = new Document();
               try
               {    
                   PdfWriter.getInstance(document, new FileOutputStream("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/StudentSchedule/"+de.getStudentCode()+".pdf"));
                   document.open();
                   PdfPTable table = new PdfPTable(4); // 3 columns.
                   table.setWidthPercentage(100); //Width 100%
                   table.setSpacingBefore(10f); //Space before table
                   table.setSpacingAfter(10f); //Space after table
            
                   //Set Column widths
                   float[] columnWidths = {1f, 4f, 1f, 1f};
                   table.setWidths(columnWidths);
               
                   temp=de.getStudentCode();
                   while (temp==de.getStudentCode()){
                       PdfPCell period = new PdfPCell(new Paragraph(de.getPeriod()));
                       PdfPCell course = new PdfPCell(new Paragraph(de.getCourseName() + " - " + de.getCourseCode()));
                       PdfPCell teacher = new PdfPCell(new Paragraph(de.getTeacherName()));
                       PdfPCell room = new PdfPCell(new Paragraph(de.getRoom()));
                       
                        table.addCell(period);
                        table.addCell(course);
                        table.addCell(teacher);
                        table.addCell(room);
                       
                       i++;
                       if(i<data.size())
                            de = (dataEntry)data.get(i);
                   }
               
                   document.add(table);
                   document.close();
                   }
                 catch (FileNotFoundException e) {
                } catch (DocumentException e) {
                }
        }
        System.out.println("Exiting printPDFStudentSchedule");
    }

        
    


    public void printCSV(List<Lecture> lectureList) {
        CSVWriter writer = null;

        try {
            writer = new CSVWriter(new FileWriter("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/course.csv"));
        
            for (Lecture lecture : courseSchedule.getLectureList()) {
                for (Student student : lecture.getStudentList()){
                        
                        String[] str = new String[8];
                        str[0] = lecture.getDay().getLabel();
                        str[1] = lecture.getPeriod().getLabel();
                        str[2] = lecture.getCourse().getCode();
                        str[3] = lecture.getCourse().getCourseName();
                        str[4] = student.getCode();
                        str[5] = student.getName();
                        str[6] = lecture.getCourse().getTeacher().getName();
                        str[7] = lecture.getRoom().getLabel();
                    
                        writer.writeNext(str);
                    
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.print(e.getStackTrace());
        }
    }

    public void generateAssignmentDependency() throws IOException,
                                                      ParserConfigurationException,
                                                      TransformerException {
        AssignmentDependency ad = new AssignmentDependency();
        ad.generateDependencyFile(CurriculumCourseImporter.DATA_INPUT_FILE,
                                  "/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/json.xml");
    }

    public void switchAssignmentRanks(String assignmentLeft,
                                      String assignmentRight, String subject) {
        AssignmentDependency.swapAssignmmentRanks(assignmentLeft,
                                                  assignmentRight, subject);
    }

    public static final String SOLVER_CONFIG =
        "org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml";


    public RunScheduler() {
        super("Course timetabling",
              "Curriculum course scheduling\n\n" +
                "Assign lectures to periods and rooms.",
                CurriculumCoursePanel.LOGO_PATH);
    }

    @Override
    protected Solver createSolver() {
        SolverFactory solverFactory =
            SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        return solverFactory.buildSolver();
    }

    @Override
    protected SolutionPanel createSolutionPanel() {
        return new CurriculumCoursePanel();
    }

    @Override
    protected SolutionDao createSolutionDao() {
        return new CurriculumCourseDao();
    }

    @Override
    protected AbstractSolutionImporter createSolutionImporter() {
        return new CurriculumCourseImporter();
    }

    @Override
    protected AbstractSolutionExporter createSolutionExporter() {
        return new CurriculumCourseExporter();
    }
}
