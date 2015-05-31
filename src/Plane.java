/***************************************************************
* file: Plane.java
* author: Nikko Medina
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/28/2015
*
* purpose: This class contains all of the methods required to construct and show
* an OpenGL window with the given objects and runtime code.
****************************************************************/
public class Plane {
    Vector normal;
    Vector point;
    float dist;
    
    public void set3Points(Vector v1, Vector v2, Vector v3) {
        Vector aux1, aux2;
        aux1 = v1.subtract(v2);
        aux2 = v3.subtract(v2);
        
        normal = aux2.crossProduct(aux1);
        normal.normalize();
        point = v2;
    }
}
