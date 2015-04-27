/***************************************************************
* file: WindowContainer.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 4/24/2015
*
* purpose: This class contains all of the methods required to construct and show
* an OpenGL window with the given objects and runtime code.
****************************************************************/
import java.util.List;

public class Scene {
    private List<Renderable> objects;
    
    //method: constructor
    //purpose: Constructs this scene with the given set of objects.
    public Scene(List<Renderable> objects) {
        this.objects = objects;
    }
    
    //method: drawScene
    //purpose: Draws the scene based on the objects that were given when this
    //scene was constructed.
    public void drawScene() {
        for(Renderable r : objects) {
            r.render();
        }
    }
}
