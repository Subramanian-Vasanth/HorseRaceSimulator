import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.InputMismatchException;

/**
 * Simulation of horse races and championships
 * 
 * @author McFarewell, Vasanth Subramanian
 * @version 1.0.1
 */
public class Race
{
    private int raceLength;
    public List<Horse> horses = new ArrayList<>();
    private final int numberOfLanes;
    public Map<Horse,Double> results= new HashMap<>();
    public Map<Horse, Integer> points = new HashMap<>();
    public Map<Horse, Integer> overallPoints = new HashMap<>();
    public double fall;
    public int placement;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     * @param lanes the number of lanes in the race
     */
    public Race(int distance,int lanes)
    {
        // initialise instance variables
        raceLength = distance;
        numberOfLanes=lanes;
        fall=-1.0;
        placement=lanes;
    }
    
    /**
     * Adds a horse to the race
     * 
     * @param theHorse the horse to be added to the race
     */
    public void addHorse(Horse theHorse) {
        if (horses.size() < numberOfLanes) {
            horses.add(theHorse);
        } else {
            System.out.println("Cannot add more horses. Maximum number of lanes reached.");
        }
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */

     public void startRace() {
        // Reset all horses to the start
        for (Horse horse : horses) {
            horse.goBackToStart();
            horse.reset();
        }

        double time = 0.0; // Initialize time

        double fall = -1.0;
        int placement = numberOfLanes;

        // Continue the race until all horses finish or fall
        while (!allHorsesFinishedOrFallen()) {
            // Clear the terminal
            clearTerminal();

            // Move each horse
            for (Horse horse : horses) {
                moveHorse(horse);
                if (horse.hasFallen() && !results.containsKey(horse)) {
                    results.put(horse, fall--);
                    updateConfidence(horse, -1); // Decrease confidence if the horse falls
                }
                if (raceWonBy(horse) && !results.containsKey(horse)) {
                    results.put(horse, time);
                    updateConfidence(horse, placement); // Increase confidence if the horse wins
                    placement--; // Decrease placement for the next horse
                }
            }

            // Print the race positions and horse confidence
            printRace();

            // Increment time by 0.1 seconds
            time += 0.1;

            // Wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        clearTerminal();
        printRace();

        results = sortMap(results);
        calculatePoints();
        printResults();
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse) {
        // If the horse has fallen
        if (theHorse.hasFallen()) {
            return;
        }
        //If it has already finished
        if(theHorse.getDistanceTravelled()==raceLength){
            return;
        }
    
        // Calculate the probability of movement based on confidence
        double confidenceFactor = 0.2 + 0.8 * theHorse.getConfidence();
    
        // Calculate additional randomness based on track conditions or other factors, can be implemented better in the future
        double additionalRandomness = Math.random() * 0.2 - 0.1; // Adjust range as needed
    
        // Combined movement probability
        double movementProbability = confidenceFactor + additionalRandomness;
    
        // Generate a random number to determine movement
        double randomValue = Math.random();
    
        // Move the horse forward if random value falls within the movement probability
        if (randomValue < movementProbability) {
            theHorse.moveForward();
        } else {
            // Generate a random number to determine if the horse falls
            double fallProbability = 0.12 * theHorse.getConfidence() * theHorse.getConfidence();
            if (Math.random() < fallProbability) {
                theHorse.fall();
            }
        }
    }
    
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        clearTerminal();
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        for (Horse horse : horses) {
            printLane(horse);
            System.out.print(" " + horse.getName() + " Confidence: " + horse.getConfidence() + " ");
            if (horse.hasFallen()) System.out.print("Fallen");
            else if (results.containsKey(horse)) System.out.print("Finished, time: " + Math.round(results.get(horse) * 100.0) / 100.0);
            System.out.println();
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u2322');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    /**
     * Helper method to get the race length from the user.
     * @param scanner The Scanner object for user input
     * @return The race length entered by the user
     */
    private static int getRaceLength(Scanner scanner) {
        int raceLength;
        while (true) {
            try {
                System.out.print("Enter the race length: ");
                raceLength = scanner.nextInt();
                if (raceLength <= 0) {
                    throw new IllegalArgumentException("Race length must be a positive integer.");
                }
                break; // Break out of the loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return raceLength;
    }

    /**
     * Helper method to get the number of horses participating from the user.
     * @param scanner The Scanner object for user input
     * @return The number of horses entered by the user
     */
    private static int getNumberOfHorses(Scanner scanner) {
        int numberOfHorses;
        while (true) {
            try {
                System.out.print("Enter the number of horses: ");
                numberOfHorses = scanner.nextInt();
                if (numberOfHorses <= 0) {
                    throw new IllegalArgumentException("Number of horses must be a positive integer.");
                }
                break; // Break out of the loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return numberOfHorses;
    }

    /**
     * Helper method to get the name of a horse from the user.
     * @param scanner The Scanner object for user input
     * @param horseNumber The number of the horse for which the name is being entered
     * @return The name of the horse entered by the user
     */
    private static String getHorseName(Scanner scanner, int horseNumber) {
        System.out.print("Enter name for horse " + horseNumber + ": ");
        return scanner.next();
    }

    /**
     * Helper method to get the symbol of a horse from the user.
     * @param scanner The Scanner object for user input
     * @param horseNumber The number of the horse for which the symbol is being entered
     * @return The symbol of the horse entered by the user
     */
    private static char getHorseSymbol(Scanner scanner, int horseNumber) {
        System.out.print("Enter symbol for horse " + horseNumber + ": ");
        return scanner.next().charAt(0);
    }

    /**
     * Helper method to get the confidence of a horse from the user.
     * @param scanner The Scanner object for user input
     * @param horseNumber The number of the horse for which the confidence is being entered
     * @return The confidence of the horse entered by the user
     */
    private static double getHorseConfidence(Scanner scanner, int horseNumber) {
        double horseConfidence;
        while (true) {
            try {
                System.out.print("Enter confidence for horse " + horseNumber + ": ");
                horseConfidence = scanner.nextDouble();
                if (horseConfidence < 0 || horseConfidence > 1) {
                    throw new IllegalArgumentException("Confidence must be a number between 0 and 1.");
                }
                break; // Break out of the loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return horseConfidence;
    }


    /**
     * Update the confidence of a horse based on its performance in the current race.
     * Confidence increases if the horse finishes well, decreases if it falls.
     * @param horse The horse whose confidence needs to be updated
     * @param placement The placement of the horse in the race (-1 if the horse fell)
     */
    public void updateConfidence(Horse horse, int placement) {
        double currentConfidence = horse.getConfidence();

        if (placement > 0) {
            // Increase confidence more for higher positions
            double increase = 0.2 / (numberOfLanes-placement+1); // Adjust this factor as needed
            horse.setConfidence(Math.min(currentConfidence + increase, 0.99)); // capping max confidence at 0.99
        } else {
            // Decrease confidence
            double decrease = 0.2; // Adjust this factor as needed
            horse.setConfidence(Math.max(currentConfidence - decrease, 0.1)); // capping min confidence at 0.1
        }
    }

    /**
     * Prints the overall results including each horse's position and points earned across all races.
     */
    private void printOverallResults(Map<Horse, Integer> overallPoints) {
        System.out.println("Overall Results:");
        // Sort the horses based on their overall points
        List<Horse> sortedHorses = new ArrayList<>(overallPoints.keySet());
        Collections.sort(sortedHorses, Comparator.comparingInt(overallPoints::get).reversed());

        // Print the overall position and points of each horse
        int position = 1;
        for (Horse horse : sortedHorses) {
            int pointsEarned = overallPoints.get(horse);
            System.out.println("Position " + position + ": " + horse.getName() + " - Points: " + pointsEarned);
            position++;
        }
    }


    /**
     * Calculates the points earned by each horse once the race finishes, and updates the overall points earned by each horse in the championship.
     */
    public void calculatePoints() {
        int numberOfLanes = horses.size();
        int pointsEarned = numberOfLanes;

        // Sort the race results by time taken to finish
        List<Map.Entry<Horse, Double>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));

        // Track the previous time to detect ties in the race
        double previousTime = -0.1;

        // Iterate over the sorted entries to assign points to horses
        for (Map.Entry<Horse, Double> entry : entries) {
            // Get the horse and its finishing time
            Horse horse = entry.getKey();
            double time = entry.getValue();

            // Decrement points earned if there's a new position or a tie
            if (time >= 0 && time != previousTime) {
                pointsEarned--;
                previousTime = time;
            }

            // Assign points to the horse based on its finishing position
            // If the horse didn't finish, assign 0 points
            points.put(horse, time >= 0 ? pointsEarned : 0);

            // Update overall points earned by the horse in the championship
            overallPoints.put(horse, overallPoints.getOrDefault(horse, 0) + (time >= 0 ? pointsEarned : 0));
        }
    }

    /**
     * Calculates the points earned by each horse once the race finishes, and updates the overall points earned by each horse in the championship if called at the end
     */
    public void calculatePoints(Boolean end) {
        int numberOfLanes = horses.size();
        int pointsEarned = numberOfLanes;

        // Sort the race results by time taken to finish
        List<Map.Entry<Horse, Double>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));

        // Track the previous time to detect ties in the race
        double previousTime = -0.1;

        // Iterate over the sorted entries to assign points to horses
        for (Map.Entry<Horse, Double> entry : entries) {
            // Get the horse and its finishing time
            Horse horse = entry.getKey();
            double time = entry.getValue();

            // Decrement points earned if there's a new position or a tie
            if (time >= 0 && time != previousTime) {
                pointsEarned--;
                previousTime = time;
            }

            // Assign points to the horse based on its finishing position
            // If the horse didn't finish, assign 0 points
            points.put(horse, time >= 0 ? pointsEarned : 0);

            // Update overall points earned by the horse in the championship
            if (end==true) overallPoints.put(horse, overallPoints.getOrDefault(horse, 0) + (time >= 0 ? pointsEarned : 0));
        }
    }


    /**
     * Prints the race results including each horse's position and points earned.
     */
    private void printResults() {

        // Sort the race results by time taken to finish
        List<Map.Entry<Horse, Double>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));

        // Track the position of horses in the race
        int position = 1;

        // Iterate over the sorted entries to print results
        for (Map.Entry<Horse, Double> entry : entries) {
            // Get the horse and its finishing time
            Horse horse = entry.getKey();
            double time = entry.getValue();

            // Print the horse's name, time taken, position, and points earned
            if (time >= 0) {
                System.out.print(horse.getName() + ": ");
                System.out.print("Time: " + Math.round(time * 100.0) / 100.0 + " seconds, ");
                System.out.print("Position: " + position + ", ");
                System.out.println("Points: " + points.get(horse));
            }
            // Increment the position for the next horse
            position++;
        }

        // Reset position for horses that did not finish
        position = results.size();

        for (Map.Entry<Horse, Double> entry : entries) {
            // Get the horse and its finishing time
            Horse horse = entry.getKey();
            double time = entry.getValue();
            if (time < 0) {
                // If the horse didn't finish, print DNF (Did Not Finish)
                System.out.print(horse.getName() + ": ");
                System.out.print("Position: DNF ");
                System.out.println("Points: " + points.get(horse));
            }
        }
    }


    /**
     * Clears the terminal window.
     */
    private void clearTerminal() {
        try {
            // Clearing terminal based on the operating system
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            // Handle exceptions
            System.out.println("Error: Failed to clear terminal.");
        }
    }

    /**
     * Main Method of the Race class, Takes user input to create and start a race
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    
        // Getting user choice between Single Race and Championship
        int choice;
        while (true) {
            System.out.println("Welcome to Horse Race Simulator!");
            System.out.println("Choose an option:");
            System.out.println("1. Single Race");
            System.out.println("2. Championship");
            System.out.print("Enter your choice (1 or 2): ");
            try {
                choice = scanner.nextInt();
                if (choice != 1 && choice != 2) {
                    throw new IllegalArgumentException("Invalid choice. Please enter 1 or 2.");
                }
                break; // Break out of the loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Getting horse details
        List<Horse> horses = new ArrayList<>();
        int numberOfHorses= getNumberOfHorses(scanner);
        Race race = new Race(0, numberOfHorses); // Create an instance of the Race class

        for (int i = 0; i < numberOfHorses; i++) {
            String horseName = getHorseName(scanner, i + 1);
            char horseSymbol = getHorseSymbol(scanner, i + 1);
            double horseConfidence = getHorseConfidence(scanner, i + 1);
            horses.add(new Horse(horseSymbol, horseName, horseConfidence));
        }

        if (choice == 1) {
            runSingleRace(scanner, horses); // Call the method for a single race
        } else if (choice == 2) {
            race.runChampionship(scanner, horses); // Call the method for a championship
        }

        scanner.close();
    }

    /**
     * Run a single race based on user input.
     * @param scanner The Scanner object for user input
     */
    private static void runSingleRace(Scanner scanner,List<Horse> horses) {
        // Getting race details from the user
        int raceLength = getRaceLength(scanner);
        Race singleRace = new Race(raceLength, horses.size());
        for(Horse horse:horses){
            singleRace.addHorse(horse);
        }

        // Start the single race
        singleRace.startRace();
    }

    /**
     * Run a championship consisting of multiple races.
     * @param scanner The Scanner object for user input
     * @param horses The list of horses participating in the championship
     */
    public void runChampionship(Scanner scanner, List<Horse> horses) {
        // Prompt for the number of races in the championship
        System.out.print("Enter the number of races in the championship: ");
        int numRaces = scanner.nextInt();
    
        // Initialize overall points for each horse to 0
        Map<Horse, Integer> overallPoints = new HashMap<>();
        for (Horse horse : horses) {
            overallPoints.put(horse, 0);
        }
    
        // For each race in the championship
        for (int i = 1; i <= numRaces; i++) {
            System.out.println("Race " + i + " of " + numRaces);
    
            int length = getRaceLength(scanner);
            // Transfer overall points data to the new Race object
            Race newRace = new Race(length, numberOfLanes);
            newRace.horses = horses; // Transfer horses
            newRace.overallPoints = overallPoints; // Transfer overallPoints data
            newRace.startRace();

    
            // Update overall points for each horse based on the current race results
            for (Map.Entry<Horse, Integer> entry : points.entrySet()) {
                Horse horse = entry.getKey();
                int pointsEarned = entry.getValue();
                overallPoints.put(horse, overallPoints.get(horse) + pointsEarned);
            }
    
            // Prompt to continue to the next race
            if (i < numRaces) {
                System.out.print("Do you want to continue to the next race? (y/n): ");
                String input = scanner.next();
                if (!input.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
    
        // Print overall results after all races are finished
        printOverallResults(overallPoints);
    }


    /**
     * Check if all horses have finished the race or fallen.
     *
     * @return true if all horses have finished or fallen, false otherwise
     */
    public boolean allHorsesFinishedOrFallen() {
        for (Horse horse : horses) {
            if (horse.getDistanceTravelled() == raceLength || horse.hasFallen()) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Sort the given map to order the competitors
     * 
     * @param map the map we are going to sort, which has the data on the competitors
     * @return returns the sorted map
     */
    public static Map<Horse, Double> sortMap(Map<Horse, Double> map) {
        // Convert Map to List of Map.Entry objects
        List<Map.Entry<Horse, Double>> list = new ArrayList<>(map.entrySet());
    
        // Sort the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Horse, Double>>() {
            @Override
            public int compare(Map.Entry<Horse, Double> o1, Map.Entry<Horse, Double> o2) {
                double value1 = o1.getValue();
                double value2 = o2.getValue();
    
                // Sort normal values in ascending order
                if (value1 >= 0 && value2 >= 0) {
                    return Double.compare(value1, value2);
                }
                // Sort negative values in ascending order
                else if (value1 < 0 && value2 < 0) {
                    return Double.compare(value1, value2);
                }
                // Place negative values at the end
                else {
                    return value1 >= 0 ? -1 : 1;
                }
            }
        });
    
        // Create a new LinkedHashMap to preserve the insertion order
        Map<Horse, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Horse, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
    
        return sortedMap;
    }   


    public void race(double time) {

        // Move each horse
        for (Horse horse : horses) {
            moveHorse(horse);
            if (horse.hasFallen() && !results.containsKey(horse)) {
                results.put(horse, fall--);
                updateConfidence(horse, -1); // Decrease confidence if the horse falls
            }
            if (raceWonBy(horse) && !results.containsKey(horse)) {
                results.put(horse, time);
                updateConfidence(horse, placement); // Increase confidence if the horse wins
                placement--; // Decrease placement for the next horse
            }
        }
    }
    
}
