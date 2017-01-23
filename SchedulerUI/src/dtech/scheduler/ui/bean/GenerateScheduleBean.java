package dtech.scheduler.ui.bean;


import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;


public class GenerateScheduleBean {
    private RichInputFile inputFile;
    private RichInputDate inputStartDate;
    private RichInputDate inputEndDate;

    public GenerateScheduleBean() {
    }

    public void generateSchedule(ActionEvent actionEvent) {
        // Add event code here...
       // RunScheduler runScheduler = new run.RunScheduler();
       // runScheduler.run();
       // System.out.print("In bean");
        
        
    }

    public void saveParameters(ActionEvent actionEvent) {
       // #{bindings.saveRooms.execute}
                    
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding saveRoomsMethod = (OperationBinding)bindings.getOperationBinding("saveRooms");
        saveRoomsMethod.execute();
        OperationBinding saveSlotsMethod = (OperationBinding)bindings.getOperationBinding("saveSlots");
        saveSlotsMethod.execute();      
    }
    
    public void  getStartDate() {
        
    }

    public void setInputFile(RichInputFile inputFile) {
        this.inputFile = inputFile;
    }

    public RichInputFile getInputFile() {
        return inputFile;
    }

    public void setInputStartDate(RichInputDate inputStartDate) {
        this.inputStartDate = inputStartDate;
    }

    public RichInputDate getInputStartDate() {
        return inputStartDate;
    }

    public void setInputEndDate(RichInputDate inputEndDate) {
        this.inputEndDate = inputEndDate;
    }

    public RichInputDate getInputEndDate() {
        return inputEndDate;
    }
}
