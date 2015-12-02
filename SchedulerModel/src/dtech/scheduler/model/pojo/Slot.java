package dtech.scheduler.model.pojo;

import java.util.ArrayList;

public class Slot {
    public Slot() {
        super();
        slotLOV.add(Boolean.TRUE);
        slotLOV.add(Boolean.FALSE);
    }
    String slotTime;
    Boolean thurs;
    Boolean fri;
    
    ArrayList<Boolean> slotLOV = new ArrayList<Boolean>();
    
    

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setThurs(Boolean thurs) {
        this.thurs = thurs;
    }

    public Boolean getThurs() {
        return thurs;
    }

    public void setFri(Boolean fri) {
        this.fri = fri;
    }

    public Boolean getFri() {
        return fri;
    }

    public ArrayList<Boolean> getSlotLOV() {
        return slotLOV;
    }
}
