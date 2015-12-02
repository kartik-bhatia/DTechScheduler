package dtech.scheduler.model.pojo;


public class Room {
    int rownum;
    String name;
    int capacity;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public int getRownum() {
        return rownum;
    }
}
