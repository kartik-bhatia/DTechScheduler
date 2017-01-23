package dtech.scheduler.model.pojo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.Serializable;

import java.util.ArrayList;

public class SlotCollection {
    public SlotCollection() {
        super();
        try {
            readFile();
        } catch (IOException e) {
        }
    }

    ArrayList<Slot> slots = new ArrayList<Slot>();

    // The name of the file to open.
    String fileName = "/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/flatfiles/slots.ctt";
   //   String fileName = ("../../flatfiles/slots.ctt");
    
    // This will reference one line at a time
    String line = null;


    void readFile() throws IOException {
        try {
            System.out.println(" OUT system dir : "+ System.getProperty("user.dir"));
            
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                String lineContents[] = line.split(" ");
                Slot slot = new Slot();
                slot.setSlotTime(lineContents[0]);
                slot.setThurs(Boolean.parseBoolean(lineContents[1]));
                slot.setFri(Boolean.parseBoolean(lineContents[2]));
                slots.add(slot);
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    void writeFile() {
        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                bufferedWriter.write(slot.getSlotTime() + " " + slot.getThurs() + " " + slot.getFri());
                bufferedWriter.newLine();
            }


            // Always close files.
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    public void saveSlots() {
        this.writeFile();
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public Serializable createSnapshot() {
        return null;
    }

    public void restoreSnapshot(Serializable p0) {
    }

    public void removeSnapshot(Serializable p0) {
    }

    public boolean isTransactionDirty() {
        return false;
    }

    public void rollbackTransaction() {
    }

    public void commitTransaction() {
    }
}
