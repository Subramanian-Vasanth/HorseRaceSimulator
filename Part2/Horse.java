import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Write a description of class Horse here.
 * 
 * @author (Vasanth Subramanian) 
 * @version (Version 1.0.1 22/4/24)
 */
public class Horse
{
    //Fields of class Horse
    
    // making the fields private for encapsulation
    private String name;
    private char icon;
    private int distance;
    private boolean fallen;
    private double confidence;
    private BufferedImage horseImage;   
    private BufferedImage fallenImage;
    private BufferedImage displayImage;
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char _icon, String _name, double _confidence)
    {      
        name=_name;//
        icon=_icon;//
        confidence=_confidence; // should be checked if valid or not before calling this method
        distance=0;
        fallen=false;
    }

    public Horse(char symbol, String name, double confidence, BufferedImage horseImage) {
        this.icon = symbol;
        this.name = name;
        this.confidence = confidence;
        this.horseImage = horseImage;
        try{
            fallenImage=ImageIO.read(new File("Fallen.png"));
        } 
        catch(IOException e){}
        displayImage=horseImage;
    }
    
    public BufferedImage getHorseImage() {
        return displayImage;
    }


    
    
    //Other methods of class Horse

    /**
     * Method to make fallen= true
     */
    public void fall()
    {
        fallen=true;
        displayImage=fallenImage;
        return;
    }

    /**
     * Method to make fallen= flase
     */
    public void reset()
    {
        fallen=false;
        displayImage=horseImage;
        return;
    }
    
    /**
     * Method to get the confidence of the object
     * @return the confidence
     */
    public double getConfidence()
    {
        return this.confidence;
    }

    /**
     * Method to get the distance travelled by far
     * @return the distance attribute of the object
     */
    public int getDistanceTravelled()
    {
        return this.distance;
    }
    
    /**
     * Method to get the name of the horse
     * @return the name attribute of the object
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Method to return the icon/symbol of the horse
     * @return the icon attribute of the horse
     */
    public char getSymbol()
    {
        return this.icon;   
    }
    
    /**
     * Method to set the distance to 0
     */
    public void goBackToStart()
    {
        this.distance = 0;
        return;
    }
    
    /**
     * Method to see if a horse has fallen or not
     * @return returns the fallen attribute
     */
    public boolean hasFallen()
    {
        return this.fallen;
    }

    /**
     * Method to increment the distance moved by the horse by 1
     */
    public void moveForward()
    {
        this.distance+=1;
        return;
    }

    /**
     * Sets the confidence of the horse to the given value. Input needs to be validated before calling this method
     * @param newConfidence the new value for the confidence attribute
     */
    public void setConfidence(double newConfidence)
    {
        this.confidence=newConfidence;
        return;
    }
    
    /**
     * Sets the icon of the horse to the given value, input needs to be validated before this method is called
     * @param newSymbol the new icon of the horse
     */
    public void setSymbol(char newSymbol)
    {
        this.icon=newSymbol;
    }

    /**
     * Method to test the public methods
     */
    private static void printout (Horse horse){
        System.out.println("name: "+horse.name+" | icon: "+horse.icon+" | fallen: "+horse.fallen+" | distance: "+horse.distance+" | confidence: "+horse.confidence);
    }

    /**
     * Method to test the fall() method
     */
    private static void testFall(){
        Horse test = new Horse('A', "testName", 0.5);
        System.out.println("Before: ");
        printout(test);
        System.out.println("After: ");
        test.fall();
        printout(test);
        // (expected:) name: testName | icon: A | fallen: true | distance: 0 | confidence: 0.5
    }

    /**
     * Method to test the constructor method Horse
     */
    private static void testConstructor(){
        Horse test = new Horse('A', "testName", 0.5);
        printout(test);
        // (expected:) name: testName | icon: A | fallen: false | distance: 0 | confidence: 0.5
    }
    
    /**
     * main method, currently used to test code
     */
    public static void main(String[] args) {
        // testing the setConfidence and setSymbol methods
        Horse test = new Horse('A', "testName", 0.5);
        printout(test);
        test.setConfidence(0.99);
        test.setSymbol('@');
        printout(test);
        // the icon and confidence attributes should change to '@' and 0.99 respectively
    }

}
