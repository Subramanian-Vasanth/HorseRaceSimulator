import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.Map;
import java.util.Comparator;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Simulation of horse races and championships, with graphical user interface.
 * 
 * @author Vasanth Subramanian
 * @version 1.0
 */

public class HorseRaceGUI extends JFrame {
    private BackgroundPanel mainPanel;
    private BackgroundPanel detailsPanel;
    private JTextField raceLengthField;
    private JTextField numberOfHorsesField;
    private BackgroundPanel enterHorseDetailsPanel;
    private BackgroundPanel racePanel;
    private BackgroundPanel championshipDetailsPanel;

    private int numberOfHorses;
    private int raceLength;
    private String raceBG;
    private int numberOfRaces;
    private Race currentRace=null;

    public HorseRaceGUI() {
        // Setting up the main frame
        setTitle("Horse Race Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        // Create the main panel and set its layout
        mainPanel = new BackgroundPanel("HorseBG.png");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Add components to the main panel
        addWelcomeMessage();
        addOptionButtons();

        // Add the main panel to the frame
        add(mainPanel);

        // set the frame visible
        setVisible(true);
    }

    private void addWelcomeMessage() {
        JLabel welcomeLabel = new JLabel("Welcome to Horse Race Simulator!");
        welcomeLabel.setForeground(Color.white);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(welcomeLabel);
    }

    private void addOptionButtons() {
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Add padding between buttons

        JButton singleRaceButton = new JButton("Single Race");
        singleRaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDetailsScreen();
            }
        });
        buttonPanel.add(singleRaceButton, gbc);

        JButton championshipButton = new JButton("Championship");
        championshipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                championshipDetails();
            }
        });
        gbc.gridy++;
        buttonPanel.add(championshipButton, gbc);

        // Add the button panel to the main panel
        mainPanel.add(buttonPanel);
    }

    private void showDetailsScreen() {
        // Create the details panel
        detailsPanel = new BackgroundPanel("HorseBG.png");
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
    
        // Add components to the details panel
        JLabel titleLabel = new JLabel("Enter Race Details");
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        detailsPanel.add(titleLabel);
    
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); 
    
        JPanel raceLengthPanel = new JPanel();
        raceLengthPanel.setOpaque(false);
        JLabel raceLengthLabel = new JLabel("Race Length:");
        raceLengthLabel.setForeground(Color.white);
        raceLengthField = new JTextField(10);
        raceLengthPanel.add(raceLengthLabel);
        raceLengthPanel.add(raceLengthField);
        raceLengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        raceLengthField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(raceLengthPanel);
    
        JPanel numberOfHorsesPanel = new JPanel();
        numberOfHorsesPanel.setOpaque(false);
    
        JLabel numberOfHorsesLabel = new JLabel("Number of Horses:");
        numberOfHorsesLabel.setForeground(Color.white);
        numberOfHorsesField = new JTextField(10);
        numberOfHorsesPanel.add(numberOfHorsesLabel);
        numberOfHorsesPanel.add(numberOfHorsesField);
        numberOfHorsesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        numberOfHorsesField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(numberOfHorsesPanel);
    
        detailsPanel.add(inputPanel);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(detailsPanel);
                add(mainPanel);
                revalidate();
                repaint();
            }
        });
        buttonPanel.add(backButton);
    
        JButton horseDetailsButton = new JButton("Enter Horse Details");
        horseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numHorses = Integer.parseInt(numberOfHorsesField.getText());
                    int Length = Integer.parseInt(raceLengthField.getText());
                    if (numHorses < 1 || numHorses > 15) {
                        JOptionPane.showMessageDialog(null, "Number of horses must be between 1 and 15.");
                    } else if (Length < 1 || Length > 120) {
                        JOptionPane.showMessageDialog(null, "Race length must be between 1 and 120.");
                    } else {
                        numberOfHorses = numHorses;
                        raceLength = Length;
    
                        // Ask for race background
                        String[] options = {"Grass", "Dessert", "Beach"}; // Background options
                        int choice = JOptionPane.showOptionDialog(null, "Choose Race Background", "Race Background", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        String[] BGs ={"RaceBG1","RaceBG2","RaceBG3"};
                        if (choice != JOptionPane.CLOSED_OPTION) {
                            // Set the race background attribute
                            raceBG = BGs[choice] + ".png";
                            remove(detailsPanel);
                            horseDetailsScreen();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for race length and number of horses.");
                }
            }
         });
    buttonPanel.add(horseDetailsButton);
    
        detailsPanel.add(buttonPanel);
    
        // Remove the main panel and add the details panel
        remove(mainPanel);
        add(detailsPanel);
        revalidate();
        repaint();
    }
    
    private void horseDetailsScreen() {
        // Create the enter horse details panel
        enterHorseDetailsPanel = new BackgroundPanel("HorseBG.png");
        enterHorseDetailsPanel.setLayout(new BoxLayout(enterHorseDetailsPanel, BoxLayout.Y_AXIS));

        // Add components to the panel
        JLabel titleLabel = new JLabel("Enter Horse Details");
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        enterHorseDetailsPanel.add(titleLabel);

        List<JTextField> nameFields = new ArrayList<>();
        List<JComboBox<String>> skinComboBoxes = new ArrayList<>();
        List<JTextField> confidenceFields = new ArrayList<>();

        for (int i = 0; i < numberOfHorses; i++) {
            JPanel horsePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            horsePanel.setOpaque(false);

            JLabel nameLabel = new JLabel("Horse " + (i + 1) + " name:");
            nameLabel.setForeground(Color.white);
            horsePanel.add(nameLabel);

            JTextField nameField = new JTextField(15);
            nameField.setName("nameField_" + i); // Set a unique name for each field
            horsePanel.add(nameField);
            nameFields.add(nameField);

            JLabel skinLabel = new JLabel("Horse " + (i + 1) + " skin:");
            skinLabel.setForeground(Color.white);
            horsePanel.add(skinLabel);

            String[] skinOptions = {"Reddish-Brown", "White", "Grey", "Golden", "Black", "Brown", "Yellow", "Blue"};
            JComboBox<String> skinComboBox = new JComboBox<>(skinOptions);
            skinComboBox.setName("skinComboBox_" + i); // Set a unique name for each combo box
            horsePanel.add(skinComboBox);
            skinComboBoxes.add(skinComboBox);

            JLabel confidenceLabel = new JLabel("Horse " + (i + 1) + " confidence:");
            confidenceLabel.setForeground(Color.white);
            horsePanel.add(confidenceLabel);

            JTextField confidenceField = new JTextField(5);
            confidenceField.setName("confidenceField_" + i); // Set a unique name for each field
            horsePanel.add(confidenceField);
            confidenceFields.add(confidenceField);

            enterHorseDetailsPanel.add(horsePanel);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(enterHorseDetailsPanel);
                add(mainPanel);
                revalidate();
                repaint();
            }
        });
        buttonPanel.add(backButton);

        JButton nextButton = new JButton("Start Race");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Horse> horses = new ArrayList<>();
                for (int i = 0; i < numberOfHorses; i++) {
                    JTextField nameField = nameFields.get(i);
                    JComboBox<String> skinComboBox = skinComboBoxes.get(i);
                    JTextField confidenceField = confidenceFields.get(i);

                    // Validate the horse details
                    String name = nameField.getText();
                    String selectedSkin = (String) skinComboBox.getSelectedItem();
                    String confidenceText = confidenceField.getText();

                    if (name.isEmpty() || confidenceText.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields for Horse " + (i + 1) + ".");
                        return; // Exit the method if any field is empty
                    }

                    int skinNumber=0; 
                    switch (selectedSkin) {
                        case "Reddish-Brown":
                            skinNumber= 1;
                            break;
                        case "White":
                            skinNumber=2;
                            break;
                        case "Grey":
                            skinNumber=3;
                            break;
                        case "Golden":
                            skinNumber=4;
                            break;
                        case "Black":
                            skinNumber=5;
                            break;
                        case "Brown":
                            skinNumber=6;
                            break;
                        case "Yellow":
                            skinNumber=7;
                            break;
                        case "Blue":
                            skinNumber=8;
                            break;
                        }
                    BufferedImage horseImage = loadImage("Horse" + skinNumber + ".png");

                    double confidence = Double.parseDouble(confidenceText);
                    if (confidence <= 0 || confidence >= 1) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a confidence value between 0 and 1 for Horse " + (i + 1) + ".");
                        return; // Exit the method without adding invalid horse details to the list
                    }

                    // Add valid Horse objects to the list
                    horses.add(new Horse(' ', name, confidence, horseImage));
                }

                // Start the race
                remove(enterHorseDetailsPanel);
                startRace(horses);
            }
        });
        buttonPanel.add(nextButton);

        enterHorseDetailsPanel.add(buttonPanel);

        // Remove the main panel and add the enter horse details panel
        remove(mainPanel);
        add(enterHorseDetailsPanel);
        revalidate();
        repaint();
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showResultsScreen(Map<Horse, Double> results, Map<Horse, Integer> points) {
        remove(racePanel);

        for(Horse horse:currentRace.horses){
            horse.reset();
        }
    
        List<Map.Entry<Horse, Double>> entries = new ArrayList<>(results.entrySet());
    
        // Create a table to display the results
        String[] columnNames = {"Position", "Horse Name", "Skin", "Time (seconds)", "Points"};
        Object[][] rowData = new Object[entries.size()][5];
    
        // Populate the table data
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Horse, Double> entry = entries.get(i);
            Horse horse = entry.getKey();
            double time = entry.getValue();
            int position = i + 1;
            int score = points.get(horse);
    
            // Get the horse skin as an ImageIcon
            ImageIcon horseSkin = new ImageIcon(horse.getHorseImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    
            rowData[i][0] = String.valueOf(position);
            rowData[i][1] = horse.getName();
            rowData[i][2] = horseSkin;
            rowData[i][3] = time >= 0 ? String.valueOf(Math.round(time * 100.0) / 100.0) : "DNF";
            rowData[i][4] = String.valueOf(score);
        }
    
        // Create a JTable to display the results
        JTable table = new JTable(rowData, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(70); // Set row height to accommodate the horse skins
    
        // Set custom cell renderer for the image column
        table.getColumnModel().getColumn(2).setCellRenderer(new ImageIconCellRenderer());
    
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
    
        // Create a panel to hold the scroll pane and button
        BackgroundPanel panel = new BackgroundPanel("HorseBG.png");
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Create a button to return to the main menu
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(e -> {
            // Remove the results screen panel and add the main panel
            currentRace = null;
            remove(panel);
            add(mainPanel);
            revalidate();
            repaint();
        });
        panel.add(mainMenuButton, BorderLayout.SOUTH);
    
        // Remove the race panel and add the results screen panel
        remove(racePanel);
        add(panel);
        revalidate();
        repaint();
    }
    
    
    private void startRace(List<Horse> horses) {
        // Reset all horses to the start
        for (Horse horse : horses) {
            horse.goBackToStart();
            horse.reset();
        }
    
        currentRace = new Race(raceLength, horses.size());
        currentRace.horses = horses;
    
        // Start a new thread for the race simulation
        Thread raceThread = new Thread(() -> {
            double time = 0.0;
            while (!currentRace.allHorsesFinishedOrFallen()) {
                currentRace.race(time);
                time += 0.1;
    
                // Call SwingUtilities.invokeLater to update the GUI on the EDT
                SwingUtilities.invokeLater(() -> displayRace(currentRace));
    
                // Wait for 100 milliseconds
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            // After the race is finished, update the results
            currentRace.results = Race.sortMap(currentRace.results);
            currentRace.calculatePoints();
        });
    
        // Start the race simulation thread
        raceThread.start();
    }
    

    private void displayRace(Race race) {
        // Create the race panel
        racePanel = new BackgroundPanel("HorseBG.png");
        racePanel.setLayout(new BorderLayout()); 
    
        // Create an instance of RaceDisplayPanel with the provided horses and raceLength
        RaceDisplayPanel raceDisplayPanel = new RaceDisplayPanel(race.horses, raceLength, raceBG);
    
        // Add the RaceDisplayPanel to the top of the race panel
        racePanel.add(raceDisplayPanel, BorderLayout.NORTH);
    
        // Create a statistics panel
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
    
        // Add statistics for each horse
        StringBuilder statsText = new StringBuilder();
        int positionTrack =1;
        Horse expWinner=null;
        int maxDist=0;
        boolean raceWon=false;
        for(Horse horse: race.horses){
            if (horse.getDistanceTravelled()==raceLength)raceWon=true;
            if (horse.getConfidence()<raceLength && horse.getDistanceTravelled()>maxDist){
                expWinner=horse;
                maxDist=expWinner.getDistanceTravelled();
            }
        }
        for (Horse horse : race.horses) {
            statsText.append("Name: ").append(horse.getName()).append(", Confidence: ").append(String.valueOf(Math.round(horse.getConfidence() * 100.0) / 100.0)).append("\n");
    
            if (horse.hasFallen()) {
                statsText.append("Fallen, Position: DNF, Points: 0\n");
            } else if (horse.getDistanceTravelled() >= raceLength) {
                double time = race.results.get(horse);
                int position = positionTrack;
                positionTrack++;
                race.calculatePoints();
                int points = race.points.get(horse);
                double averageSpeed = raceLength / time;
    
                statsText.append("Finished, Time: ").append(String.valueOf(Math.round(time * 100.0) / 100.0)).append(", Position: ").append(position)
                        .append(", Points: ").append(points).append(", Average Speed: ").append(String.valueOf(Math.round(averageSpeed * 100.0) / 100.0)).append("\n");
            } else {

                if(raceWon==false && expWinner!=null &&expWinner.equals(horse)){
                    statsText.append("Expected Winner\n");
                }
                else statsText.append("\n");
            }
        }
    
        JTextArea statsTextArea = new JTextArea(statsText.toString());
        statsTextArea.setEditable(false);
        statsPanel.add(new JScrollPane(statsTextArea), BorderLayout.CENTER);
    
        // Add statistics panel to the center of the race panel
        racePanel.add(statsPanel, BorderLayout.CENTER);

        // Add a button to show race results at the bottom
        JButton showResultsButton = new JButton("Show Results");
        showResultsButton.addActionListener(e -> {
            if (race.allHorsesFinishedOrFallen()) {
                showResultsScreen(race.results, race.points);
            } else {
                JOptionPane.showMessageDialog(null, "Please wait until the race is complete.", "Race in Progress", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        racePanel.add(showResultsButton, BorderLayout.SOUTH);

        // Remove the details panel and add the race panel
        remove(detailsPanel);
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                remove(component); // Found a JPanel
            }
        }
        add(racePanel);
        revalidate();
        repaint();
    }

    private void championshipDetails() {
        // Create the championship details panel
        championshipDetailsPanel = new BackgroundPanel("HorseBG.png");
        championshipDetailsPanel.setLayout(new BoxLayout(championshipDetailsPanel, BoxLayout.Y_AXIS));
    
        // Add components to the championship details panel
        JLabel titleLabel = new JLabel("Enter Championship Details");
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        championshipDetailsPanel.add(titleLabel);
    
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
    
        JPanel raceCountPanel = new JPanel();
        raceCountPanel.setOpaque(false);
        JLabel raceCountLabel = new JLabel("Number of Races (1-10):");
        raceCountLabel.setForeground(Color.white);
        JTextField raceCountField = new JTextField(10);
        raceCountPanel.add(raceCountLabel);
        raceCountPanel.add(raceCountField);
        raceCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align label to center
        raceCountField.setAlignmentX(Component.CENTER_ALIGNMENT); // Align text field to center
        inputPanel.add(raceCountPanel);
    
        JPanel numberOfHorsesPanel = new JPanel();
        numberOfHorsesPanel.setOpaque(false);
        JLabel numberOfHorsesLabel = new JLabel("Number of Horses:");
        numberOfHorsesLabel.setForeground(Color.white);
        numberOfHorsesField = new JTextField(10);
        numberOfHorsesPanel.add(numberOfHorsesLabel);
        numberOfHorsesPanel.add(numberOfHorsesField);
        numberOfHorsesLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align label to center
        numberOfHorsesField.setAlignmentX(Component.CENTER_ALIGNMENT); // Align text field to center
        inputPanel.add(numberOfHorsesPanel);
    
        championshipDetailsPanel.add(inputPanel);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(championshipDetailsPanel);
                add(mainPanel);
                revalidate();
                repaint();
            }
        });
        buttonPanel.add(backButton);
    
        JButton horseDetailsButton = new JButton("Enter Horse Details");
        horseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numHorses = Integer.parseInt(numberOfHorsesField.getText());
                    int numRaces = Integer.parseInt(raceCountField.getText());
                    if (numHorses < 1 || numHorses > 15) {
                        JOptionPane.showMessageDialog(null, "Number of horses must be between 1 and 15.");
                    } else if (numRaces < 1 || numRaces > 10) {
                        JOptionPane.showMessageDialog(null, "Number of races must be between 1 and 10.");
                    } else {
                        numberOfHorses = numHorses;
                        numberOfRaces = numRaces;
                        remove(championshipDetailsPanel);
                        championshipHorseDetails();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for number of races and number of horses.");
                }
            }
         });
        buttonPanel.add(horseDetailsButton);
    
        championshipDetailsPanel.add(buttonPanel);
    
        // Remove the main panel and add the championship details panel
        remove(mainPanel);
        add(championshipDetailsPanel);
        revalidate();
        repaint();
    }
    
    private void championshipHorseDetails() {
        // Create the enter horse details panel
        enterHorseDetailsPanel = new BackgroundPanel("HorseBG.png");
        enterHorseDetailsPanel.setLayout(new BoxLayout(enterHorseDetailsPanel, BoxLayout.Y_AXIS));
    
        // Add components to the panel
        JLabel titleLabel = new JLabel("Enter Horse Details");
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        enterHorseDetailsPanel.add(titleLabel);
    
        List<JTextField> nameFields = new ArrayList<>();
        List<JComboBox<String>> skinComboBoxes = new ArrayList<>();
        List<JTextField> confidenceFields = new ArrayList<>();
    
        for (int i = 0; i < numberOfHorses; i++) {
            JPanel horsePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            horsePanel.setOpaque(false);
    
            JLabel nameLabel = new JLabel("Horse " + (i + 1) + " name:");
            nameLabel.setForeground(Color.white);
            horsePanel.add(nameLabel);
    
            JTextField nameField = new JTextField(15);
            nameField.setName("nameField_" + i); 
            horsePanel.add(nameField);
            nameFields.add(nameField);
    
            JLabel skinLabel = new JLabel("Horse " + (i + 1) + " skin:");
            skinLabel.setForeground(Color.white);
            horsePanel.add(skinLabel);
    
            String[] skinOptions = {"Reddish-Brown", "White", "Grey", "Golden", "Black", "Brown", "Yellow", "Blue"};
            JComboBox<String> skinComboBox = new JComboBox<>(skinOptions);
            skinComboBox.setName("skinComboBox_" + i); 
            horsePanel.add(skinComboBox);
            skinComboBoxes.add(skinComboBox);
    
            JLabel confidenceLabel = new JLabel("Horse " + (i + 1) + " confidence:");
            confidenceLabel.setForeground(Color.white);
            horsePanel.add(confidenceLabel);
    
            JTextField confidenceField = new JTextField(5);
            confidenceField.setName("confidenceField_" + i); 
            horsePanel.add(confidenceField);
            confidenceFields.add(confidenceField);
    
            enterHorseDetailsPanel.add(horsePanel);
        }
    
        // Add input fields for race length and background
        JPanel raceSettingsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        raceSettingsPanel.setOpaque(false);
        JLabel raceLengthLabel = new JLabel("Race Length:");
        raceLengthLabel.setForeground(Color.white);
        raceSettingsPanel.add(raceLengthLabel);
        JTextField raceLengthField = new JTextField(10);
        raceSettingsPanel.add(raceLengthField);
        JLabel raceBGLabel = new JLabel("Race Background:");
        raceBGLabel.setForeground(Color.white);
        raceSettingsPanel.add(raceBGLabel);
        String[] bgOptions = {"Grass", "Dessert", "Beach"};
        JComboBox<String> raceBGComboBox = new JComboBox<>(bgOptions);
        raceSettingsPanel.add(raceBGComboBox);
        enterHorseDetailsPanel.add(raceSettingsPanel);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(enterHorseDetailsPanel);
                add(championshipDetailsPanel);
                revalidate();
                repaint();
            }
        });
        buttonPanel.add(backButton);
    
        JButton nextButton = new JButton("Start Championship");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Horse> horses = new ArrayList<>();
                for (int i = 0; i < numberOfHorses; i++) {
                    JTextField nameField = nameFields.get(i);
                    JComboBox<String> skinComboBox = skinComboBoxes.get(i);
                    JTextField confidenceField = confidenceFields.get(i);
    
                    // Validate the horse details
                    String name = nameField.getText();
                    String selectedSkin = (String) skinComboBox.getSelectedItem();
                    String confidenceText = confidenceField.getText();
    
                    if (name.isEmpty() || confidenceText.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields for Horse " + (i + 1) + ".");
                        return; // Exit the method if any field is empty
                    }
    
                    int skinNumber=0; 
                    switch (selectedSkin) {
                        case "Reddish-Brown":
                            skinNumber= 1;
                            break;
                        case "White":
                            skinNumber=2;
                            break;
                        case "Grey":
                            skinNumber=3;
                            break;
                        case "Golden":
                            skinNumber=4;
                            break;
                        case "Black":
                            skinNumber=5;
                            break;
                        case "Brown":
                            skinNumber=6;
                            break;
                        case "Yellow":
                            skinNumber=7;
                            break;
                        case "Blue":
                            skinNumber=8;
                            break;
                        }
                    double confidence;
                    try {
                        confidence = Double.parseDouble(confidenceText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid confidence value for Horse " + (i + 1) + ".");
                        return; // Exit the method if confidence is invalid
                    }
    
                    if (confidence <= 0 || confidence >= 1) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a confidence value between 0 and 1 for Horse " + (i + 1) + ".");
                        return; // Exit the method without adding invalid horse details to the list
                    }
    
                    // Add valid Horse objects to the list
                    horses.add(new Horse(' ',name,  confidence,loadImage("Horse" + skinNumber + ".png")));
                }
    
                // Validate race length
                int Length;
                try {
                    Length = Integer.parseInt(raceLengthField.getText());
                    if (Length < 1 || Length > 120) {
                        JOptionPane.showMessageDialog(null, "Race length must be between 1 and 120.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number for race length.");
                    return;
                }
    
                // Extract selected race background
                String selectedBG = (String) raceBGComboBox.getSelectedItem();
                switch (selectedBG) {
                    case "Grass":
                        selectedBG="RaceBG1";
                        break;
                    case "Dessert":
                        selectedBG="RaceBG2";
                        break;
                    case "Beach":
                        selectedBG="RaceBG3";
                        break;
                }
    
                // Start the championship
                remove(enterHorseDetailsPanel);

                raceLength=Length;
                raceBG=selectedBG;

                championshipRace(horses);
            }
        });
        buttonPanel.add(nextButton);
    
        enterHorseDetailsPanel.add(buttonPanel);
    
        // Remove the championship details panel and add the enter horse details panel
        remove(championshipDetailsPanel);
        add(enterHorseDetailsPanel);
        revalidate();
        repaint();
    }
    

    private void championshipRace(List<Horse> horses){

        // Reset all horses to the start
        
        if (currentRace==null){
            currentRace = new Race(raceLength, horses.size());
            currentRace.horses = horses;
        }
        else{
            Race newRace = new Race(raceLength,currentRace.horses.size());
            newRace.horses=currentRace.horses;
            newRace.overallPoints=currentRace.overallPoints;
            currentRace=newRace;
        }

        for (Horse horse : currentRace.horses) {
            horse.goBackToStart();
            horse.reset();
        }
    
        // Start a new thread for the race simulation
        Thread raceThread = new Thread(() -> {
            double time = 0.0;
            while (!currentRace.allHorsesFinishedOrFallen()) {
                currentRace.race(time);
                time += 0.1;
    
                // Call SwingUtilities.invokeLater to update the GUI on the EDT
                SwingUtilities.invokeLater(() -> displayChampionship(currentRace));
    
                // Wait for 100 milliseconds
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            // After the race is finished, update the results
            currentRace.results = Race.sortMap(currentRace.results);
            currentRace.calculatePoints();
        });
    
        // Start the race simulation thread
        raceThread.start();
    }
    
    
    private void displayChampionship(Race race) {
        // Check if the detailsPanel is not null before removing it
        if (detailsPanel != null) {
            remove(detailsPanel);
        }
    
        // Check if any other components of type JPanel exist and remove them
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component != null && component instanceof JPanel) {
                remove(component); // Found a non-null JPanel
            }
        }
    
        // Create the race panel
        racePanel = new BackgroundPanel("HorseBG.png");
        racePanel.setLayout(new BorderLayout()); // Use BorderLayout for better layout control
    
        // Create an instance of RaceDisplayPanel with the provided horses and raceLength
        RaceDisplayPanel raceDisplayPanel = new RaceDisplayPanel(race.horses, raceLength, raceBG + ".png");
    
        // Add the RaceDisplayPanel to the center of the race panel
        racePanel.add(raceDisplayPanel, BorderLayout.NORTH);
    
        // Create a statistics panel
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
    
        // Add statistics for each horse
        StringBuilder statsText = new StringBuilder();
        int positionTrack = 1;
        Horse expWinner = null;
        int maxDist = 0;
        boolean raceWon = false;
        for (Horse horse : race.horses) {
            if (horse.getDistanceTravelled() == raceLength) raceWon = true;
            if (horse.hasFallen()==false && horse.getDistanceTravelled() > maxDist) {
                expWinner=horse;
                maxDist = expWinner.getDistanceTravelled();
            }
        }
        for (Horse horse : race.horses) {
            statsText.append("Name: ").append(horse.getName()).append(", Confidence: ").append(String.valueOf(Math.round(horse.getConfidence() * 100.0) / 100.0)).append("\n");
    
            if (horse.hasFallen()) {
                statsText.append("Fallen, Position: DNF, Points: 0\n");
            } else if (horse.getDistanceTravelled() >= raceLength) {
                double time = race.results.get(horse);
                int position = positionTrack;
                positionTrack++;
                race.calculatePoints(false);
                int points = race.points.get(horse);
                double averageSpeed = raceLength / time;
    
                statsText.append("Finished, Time: ").append(String.valueOf(Math.round(time * 100.0) / 100.0)).append(", Position: ").append(position)
                        .append(", Points: ").append(points).append(", Average Speed: ").append(String.valueOf(Math.round(averageSpeed * 100.0) / 100.0)).append("\n");
            } else {
    
                if (raceWon == false && expWinner != null && expWinner.equals(horse)) {
                    statsText.append("Expected Winner\n");
                } else statsText.append("\n");
            }
        }
    
        JTextArea statsTextArea = new JTextArea(statsText.toString());
        statsTextArea.setEditable(false);
        statsPanel.add(new JScrollPane(statsTextArea), BorderLayout.CENTER);
    
        // Add statistics panel to the south of the race panel
        racePanel.add(statsPanel, BorderLayout.CENTER);
    
        // Add a button to show results
        String buttonName;
        if (numberOfRaces == 1) {
            buttonName = "Show Results";
        } else {
            buttonName = "Next Race";
        }
        JButton nextRaceButton = new JButton(buttonName);
        nextRaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfRaces--;
                if (numberOfRaces > 0) {
                    askForRaceDetailsAndStartNextRace();
                } else {
                    showOverallResultsScreen(currentRace.overallPoints);
                }
            }
        });
        racePanel.add(nextRaceButton, BorderLayout.SOUTH); // Add button at the bottom
    
        // Add the race panel to the frame
        add(racePanel);
        revalidate();
        repaint();
    }
    

    private void askForRaceDetailsAndStartNextRace() {
        // Ask for race length
        String raceLengthInput = JOptionPane.showInputDialog(null, "Enter race length:");
        
        // Validate race length input
        int length;
        try {
            length = Integer.parseInt(raceLengthInput);
            if (length<1 || length>120) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number for race length.");
            return;
        }
        
        // Options for race backgrounds
        String[] raceBGOptions = {"Grass", "Dessert", "Beach"}; // Add more options if needed
    
        // Show a combo box with race background options
        String raceBGInput = (String) JOptionPane.showInputDialog(
                null,
                "Select race background:",
                "Race Background",
                JOptionPane.QUESTION_MESSAGE,
                null,
                raceBGOptions,
                raceBGOptions[0]); // Default selection
    
        // Start the next race with the updated race length and background
        raceLength = length;
        raceBG = "";
        switch (raceBGInput) {
            case "Grass":
                raceBG="RaceBG1";
                break;
            case "Dessert":
                raceBG="RaceBG2";
                break;
            case "Beach":
                raceBG="RaceBG3";
                break;
        }
        championshipRace(null);
    }
    
    private void showOverallResultsScreen(Map<Horse, Integer> overallResults) {
        // Reset skins of all horses
        for (Horse horse : currentRace.horses) {
            horse.reset(); 
        }
    
        // Create a sorted list of horses based on points
        List<Horse> sortedHorses = new ArrayList<>(overallResults.keySet());
        sortedHorses.sort(Comparator.comparingInt(overallResults::get).reversed());
    
        // Create the overall results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);
    
        // Create the table to display results
        String[] columnNames = {"Position", "Name", "Skin", "Points"};
        Object[][] rowData = new Object[sortedHorses.size()][4];
    
        for (int i = 0; i < sortedHorses.size(); i++) {
            Horse horse = sortedHorses.get(i);
            BufferedImage horseImage = horse.getHorseImage();
            ImageIcon horseIcon = new ImageIcon(horseImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    
            rowData[i][0] = i + 1; // Position
            rowData[i][1] = horse.getName(); // Name
            rowData[i][2] = horseIcon; // Horse image
            rowData[i][3] = overallResults.get(horse); // Points
        }
    
        JTable resultsTable = new JTable(rowData, columnNames);
        resultsTable.setRowHeight(40); // Set row height to match image height
    
        // Set custom cell renderer for the image column
        resultsTable.getColumnModel().getColumn(2).setCellRenderer(new ImageIconCellRenderer());
    
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Add a button to go to the main menu
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the results screen panel and add the main panel
                    currentRace = null;
                    remove(resultsPanel);
                    add(mainPanel);
                    revalidate();
                    repaint();
            }
        });
        resultsPanel.add(mainMenuButton, BorderLayout.SOUTH);
    
        // Remove the current panel and add the overall results panel
        remove(racePanel); 
        add(resultsPanel);
        revalidate();
        repaint();
    }
    
    
    // Custom cell renderer for displaying ImageIcon
    class ImageIconCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
                setText(null);
            } else {
                super.setValue(value);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HorseRaceGUI();
            }
        });
    }
}
