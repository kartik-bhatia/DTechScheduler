package dtech.scheduler.model.pojo;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;


public class SubjectNodeCollection {
    public SubjectNodeCollection() {
        super();
    }
       
    ArrayList<SubjectNode> subjects = new ArrayList<SubjectNode> ();

    public void readXMLFile() {
        subjects = new ArrayList<SubjectNode> ();
        File inputFile = new File("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/json.xml");
        try {
            
        DocumentBuilderFactory dbFactory =
        DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        
        Element root  = doc.getDocumentElement();
        NodeList subjectsNode = root.getChildNodes();
            for (int i=0;i<subjectsNode.getLength();i++){
                Node subjectNode = subjectsNode.item(i);
                if (subjectNode.getNodeType() == Node.ELEMENT_NODE){
                   
                   //create a new SubjectNode and set its subjectname
                    SubjectNode subject = new SubjectNode();
                    subject.setSubjectName(subjectNode.getNodeName());
                   
                    NodeList assignments = subjectNode.getChildNodes();
                    for (int j=0;j<assignments.getLength();j++) {
                        Node assignmentNode = assignments.item(j);
                        if (assignmentNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element assignmentElement = (Element) assignmentNode;
                            
                            //create a new AssignmentNode
                            AssignmentNode assignment = new AssignmentNode();
                            assignment.setAssignmentTitle(assignmentElement.getElementsByTagName("name").item(0).getTextContent());
                            assignment.setRank(Integer.parseInt(assignmentElement.getAttribute("rank")));
                            subject.getAssignmentNodes().add(assignment);
                            
                        }
                    }
                    subjects.add(subject);
                }  
            }
        } catch (ParserConfigurationException e) {
            
        } catch (IOException e) {
            System.out.println("XML file at path "+inputFile+" is not Readable.");
        } catch (SAXException e) {
            
        }
        
    }

    public void setSubjects(ArrayList<SubjectNode> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<SubjectNode> getSubjects() {
        this.readXMLFile();
        return subjects;
    }
}
