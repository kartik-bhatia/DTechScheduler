package dtech.scheduler.model.pojo;

import java.util.ArrayList;

public class SubjectNode {
    long subjectId;
    String subjectName;
    ArrayList<AssignmentNode> assignmentNodes = new ArrayList<AssignmentNode> ();

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setAssignmentNodes(ArrayList<AssignmentNode> assignmentNodes) {
        this.assignmentNodes = assignmentNodes;
    }

    public ArrayList<AssignmentNode> getAssignmentNodes() {
        return assignmentNodes;
    }
}
