package dtech.scheduler.ui.bean;

import dtech.scheduler.model.util.ADFUtil;

import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.datatransfer.DataFlavor;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DropEvent;

import org.optaplanner.examples.curriculumcourse.domain.AssignmentDependency;

public class AssignmentRankingBean {

    private RichTreeTable assignmentTreeTable;

    public DnDAction dropAssignment(DropEvent dropEvent) {
        String destinationAssignmentTitle = (String)dropEvent.getDropComponent().getAttributes().get("value");
        System.out.println("Destination = " + destinationAssignmentTitle);
        
        DataFlavor<Object> df = DataFlavor.getDataFlavor(Object.class);
        String sourceAssignmentTitle = (String)dropEvent.getTransferable().getData(df);
        if (sourceAssignmentTitle != null ) {
            System.out.println("  KB Source = " + sourceAssignmentTitle);
        }
        AssignmentDependency.swapAssignmmentRanks(sourceAssignmentTitle, destinationAssignmentTitle, "");
        ADFUtil.invokeEL("#{bindings.Execute.execute}");
        AdfFacesContext.getCurrentInstance().addPartialTarget(getAssignmentTreeTable());
        return DnDAction.NONE;
    }

    public void setAssignmentTreeTable(RichTreeTable assignmentTreeTable) {
        this.assignmentTreeTable = assignmentTreeTable;
    }

    public RichTreeTable getAssignmentTreeTable() {
        return assignmentTreeTable;
    }
}
