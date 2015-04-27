/***************************************************************
* file: Project1.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 4/26/2015
*
* purpose: This is the driver program for the project. It begins the window object
* and does any initial setup that might be needed.
****************************************************************/
import java.util.ArrayList;
import java.util.List;

public class Project3 {
    //method: main
    //purpose: Drives this program.
    public static void main(String[] args) {
        List<Renderable> objects = new ArrayList<Renderable>();
        objects.add(new Block(0f, 0f, 0f, 10));
        Scene scene = new Scene(objects);
        
        WindowContainer window = new WindowContainer(scene);
        window.start();
    }
}
