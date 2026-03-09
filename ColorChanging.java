

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ColorChanging {

    public static void main(String[] args) {
      // --- 1. SETTING UP THE WINDOW (THE FRAME) ---
        // Create the main window that holds everything.

        JFrame jFrame = new JFrame("Color Changer ");
        jFrame.setSize(800,700);

        // This stops the program when you click the 'X' in the corner!
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        // --- 2. THE BACKGROUND CANVAS (THE PANEL) ---
        // A JPanel is like a blank piece of construction paper we put in the window.
        // We will change *its* color.

        JPanel jPanel = new JPanel();
        // The Panel needs a layout to know where to put things (Buttons go at the bottom).
        jPanel.setLayout(new BorderLayout());

        // --- 3. CREATING THE BUTTONS ---

        JButton btnRed = new JButton("Red");
        JButton btnGreen = new JButton("Green");
        JButton btnBlue = new JButton("Blue");

        // This small panel will hold the buttons neatly in a row at the bottom.
        JPanel jPanelButtons = new JPanel();
        jPanelButtons.add(btnRed);
        jPanelButtons.add(btnGreen);
        jPanelButtons.add(btnBlue);

        // --- 4. ADDING ACTIONS (THE FUN PART) ---
        // This is where we tell each button what to do when it is clicked.
        
        // Red Button's hidden instructions:

        btnRed.addActionListener(e -> {
          jPanel.setBackground(Color.RED);
        });

        // Green Button's hidden instructions: 

        btnGreen.addActionListener(e -> {
          jPanel.setBackground(Color.GREEN);
        });

        // Blue Button's hidden instructions:

        btnBlue.addActionListener(e -> {
          jPanel.setBackground(Color.BLUE);
        });

        // --- 5. PUTTING IT ALL TOGETHER ---
        // We add the button row to the bottom (SOUTH) of our main panel.

        jPanel.add(jPanelButtons, BorderLayout.SOUTH);

        // Then we add the main panel (the canvas + buttons) to the frame.
        jFrame.add(jPanel);
        // Finally, make the window visible so you can see it!
        jFrame.setVisible(true);


    }
    
}
