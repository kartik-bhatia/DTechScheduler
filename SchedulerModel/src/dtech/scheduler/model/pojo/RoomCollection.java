package dtech.scheduler.model.pojo;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RoomCollection {
    public RoomCollection() {
        super();
        try {
            readFile();
        } catch (IOException e) {
        }
        
    }
    ArrayList<Room> rooms = new ArrayList<Room> ();
    
    // The name of the file to open.
    //String fileName = "../../flatfiles/rooms.ctt";
    String fileName = "/Users/kartikbhatia/Documents/Data/MyProjects/DTech/DTechScheduler/flatfiles/rooms.ctt";
    // This will reference one line at a time
    String line = null;


    void readFile() throws IOException {
        try {
                   // FileReader reads text files in the default encoding.
                   FileReader fileReader = 
                       new FileReader(fileName);

                   // Always wrap FileReader in BufferedReader.
                   BufferedReader bufferedReader = 
                       new BufferedReader(fileReader);

                   while((line = bufferedReader.readLine()) != null) {
                       String lineContents[] = line.split(" ");
                       Room room = new Room();
                       room.setName(lineContents[0]);
                       room.setCapacity(Integer.parseInt(lineContents[1]));
                       rooms.add(room);
                   }   

                   // Always close files.
                   bufferedReader.close();         
               }
               catch(FileNotFoundException ex) {
                   System.out.println(
                       "Unable to open file '" + 
                       fileName + "'");                
               }
               catch(IOException ex) {
                   System.out.println(
                       "Error reading file '" 
                       + fileName + "'");                  
                   // Or we could just do this: 
                   // ex.printStackTrace();
               }
    }
    
    void writeFile() {
        try {
                   // Assume default encoding.
                   FileWriter fileWriter =
                       new FileWriter(fileName);

                   // Always wrap FileWriter in BufferedWriter.
                   BufferedWriter bufferedWriter =
                       new BufferedWriter(fileWriter);

                   // Note that write() does not automatically
                   // append a newline character.
                   for (int i = 0; i<rooms.size();i++) {
                       Room room = rooms.get(i);
                       bufferedWriter.write(room.getName()+ " "+ room.getCapacity());
                       bufferedWriter.newLine();
                   }
                  

                   // Always close files.
                   bufferedWriter.close();
               }
               catch(IOException ex) {
                   System.out.println(
                       "Error writing to file '"
                       + fileName + "'");
                   // Or we could just do this:
                   // ex.printStackTrace();
               }
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<Room> getRooms() throws IOException {
        return rooms;
    }
    public void saveRooms() {
        this.writeFile();
    }
    
    public void createRow() {
        Room room = new Room();
        rooms.add(room);
    }
}
