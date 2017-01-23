package org.optaplanner.examples.curriculumcourse.domain;


import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AssignmentDependency {
    public AssignmentDependency() {
        super();
    }
    
    public void generateDependencyFile(String inputFilePath, String outputFilePath) throws IOException,
                                                                     ParserConfigurationException,
                                                                     TransformerException {
    
            CSVReader reader = null;       
            reader = new CSVReader(new FileReader(inputFilePath));
            DocumentBuilderFactory dbFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
             // root element
            Element rootElement = doc.createElement("subjects");
            doc.appendChild(rootElement);
                    
            String [] row;
            while ((row = reader.readNext()) != null) {
                if(row.length>=8 && ReadGradeSheet.isNumeric(row[0])) // here check if the current row is a valid data row and not a header or blank
                {
                    //read each row from the CSV file           
                    String assignmentTitle = row[ReadGradeSheet.ASSIGNMENT_TITLE_COL];   
                    String subject = row[ReadGradeSheet.COURSE_NAME_COL]; 
                    subject = subject.replaceAll("\\s","");
                    // search if the Subject is already added 
                    NodeList nl =  rootElement.getElementsByTagName(subject);
                     
                     // if subject is not already added, add subject as a new node in the xml under root node -> <subjects>
                     if(nl.getLength()==0) {
                         Element subjectElement = doc.createElement(subject);    
                         Element assignmentElement = doc.createElement("assignment");
                         assignmentElement.setAttribute("rank", "0");
                         Element nameElement = doc.createElement("name");
                         nameElement.appendChild(doc.createTextNode(assignmentTitle));
                         assignmentElement.appendChild(nameElement);
                         subjectElement.appendChild(assignmentElement);
                         rootElement.appendChild(subjectElement);   
                     }
                     // if subject is already added
                     else {
                        boolean assignmentExists = false;
                        Node nNode = nl.item(0);
                        // check if assignment is already added 
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element)nNode;
                            NodeList assignmentList = eElement.getElementsByTagName("assignment");
                            for (int i = 0; i<assignmentList.getLength();i++) {
                                if (assignmentList.item(i).getTextContent().equals(assignmentTitle)) {
                                    assignmentExists = true;
                                    break;
                                }
                            }
                            // if assignment is not added, 
                            if(!assignmentExists) {
                                Element assignmentElement = doc.createElement("assignment");
                                assignmentElement.setAttribute("rank", Integer.toString(assignmentList.getLength()));
                                Element nameElement = doc.createElement("name");
                                nameElement.appendChild(doc.createTextNode(assignmentTitle));
                                assignmentElement.appendChild(nameElement);
                                nNode.appendChild(assignmentElement);
                            }
                        }
                     }
                }
            }
        
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputFilePath));
            transformer.transform(source, result);
    } 
    

    public static boolean isLeftRankLessThanRight(String assignmentLeft, String assignmentRight, String subject) {
        try {
                File inputFile = new File("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/json.xml");
                SAXReader reader = new SAXReader();
                org.dom4j.Document document = reader.read(inputFile);
            
                subject = subject.replaceAll("\\s","");
            
                org.dom4j.Node leftNode = document.selectSingleNode("/subjects/"+subject+"/assignment[name='"+assignmentLeft+"']" );
                org.dom4j.Node rightNode = document.selectSingleNode("/subjects/"+subject+"/assignment[name='"+assignmentRight+"']" );
                 
                int leftRank = Integer.parseInt(leftNode.valueOf("@rank"));
                int rightRank = Integer.parseInt(rightNode.valueOf("@rank"));
            
                if (leftRank<rightRank) 
                    return true;
                else 
                    return false;
                
             } catch (DocumentException e) {
                e.printStackTrace();
             } 
        return false;
    }


    public static void swapAssignmmentRanks(String assignmentLeft, String assignmentRight, String subject) {
        

        try {
            File inputFile = new File("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/json.xml");
            SAXReader reader = new SAXReader();
            org.dom4j.Document document;
            document = reader.read(inputFile);
            org.dom4j.Element classElement = document.getRootElement();

            org.dom4j.Node leftNode = document.selectSingleNode("/subjects/*/assignment[name='"+assignmentLeft+"']" );
            String leftNodeSubject = leftNode.getParent().getName();
            
            org.dom4j.Node rightNode = document.selectSingleNode("/subjects/*/assignment[name='"+assignmentRight+"']" );
            String righNodeSubject = rightNode.getParent().getName();
            
            if (!leftNodeSubject.equals(righNodeSubject)) {
                System.out.println("This move is not allowed.");
            }
            else {
                //swap
                org.dom4j.Element leftElement = null;
                 org.dom4j.Element rightElement = null;
                if(leftNode.getNodeType() == Node.ELEMENT_NODE ) {
                     leftElement = (org.dom4j.Element)leftNode;
                     
                }
                
                if(rightNode.getNodeType() == Node.ELEMENT_NODE) {
                    rightElement = (org.dom4j.Element)rightNode;
                }
   
                leftElement.selectSingleNode("name").setText(assignmentRight);
                rightElement.selectSingleNode("name").setText(assignmentLeft);
                
                 OutputFormat format = OutputFormat.createPrettyPrint();
                         XMLWriter writer;
                Writer filewriter = new FileWriter("/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/json.xml");
              
                    writer = new XMLWriter( filewriter, format );
                    writer.write( document );
                    writer.close();
                
             }
            
           
            
            
        
        } catch (DocumentException e) {
            
        }
        catch (UnsupportedEncodingException e) {
         }
        catch (IOException e) {
                 e.printStackTrace();
              }
    }

}
