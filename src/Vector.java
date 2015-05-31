/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Vector {
    private float x;
    private float y;
    private float z;
    
    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }
    
    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }
    
    public Vector multiply(float f) {
        return new Vector(x * f, y * f, z * f);
    }
    
    public Vector crossProduct(Vector v) {
        return new Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }
    
    public Vector divide(float f) {
        return new Vector(x / f, y / f, z / f);
    }
    
    public float innerProduct(Vector v) {
        return (x * v.x + y * v.y + z * v.z);
    }
    
    public void normalize() {
        float length = length();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }
    
    public float length() {
        return ((float) Math.sqrt(x * x + y * y + z * z));
    }
    
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
