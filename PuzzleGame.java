import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class PuzzleGame extends JFrame {

    private JButton[][] buttons = new JButton[4][4];
    private int[][] buttonNumbers = new int[4][4];
    private int emptyRow = 3, emptyCol = 3;

    public PuzzleGame() {
        setTitle("15 Puzzle Game");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for "New Game" and "Cheat" buttons and move counter
        JPanel topPanel = new JPanel();
        JButton newGameButton = new JButton("Nytt spel");
        JButton cheatButton = new JButton("Fuska");

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shufflePuzzle();
            }
        });

        cheatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cheating();
            }
        });
        topPanel.add(newGameButton);
        topPanel.add(cheatButton);
        add(topPanel, BorderLayout.NORTH);

        // skapa panel för spelet
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(4, 4));
        createButtons(gamePanel);
        add(gamePanel, BorderLayout.CENTER);

        shufflePuzzle(); //ge nummer and shuffle
        setVisible(true);
    }

    private void createButtons(JPanel gamePanel) {
        for (int row = 0; row < 4; row++) { //Yttre loopen för varje row
            for (int column = 0; column < 4; column++) { //Inre loopen för varje kolumn i en row
                //För varje position skapar loopen en ny knapp (JButton),

                buttons[row][column] = new JButton();
                buttons[row][column].setFont(new Font("Arial", Font.BOLD, 30));
                buttons[row][column].setBackground(Color.WHITE);
                buttons[row][column].setForeground(Color.BLACK);
                buttons[row][column].addActionListener(new ButtonClickListener(row, column));
                gamePanel.add(buttons[row][column]);
            }
        }
    }

    // shufflePuzzle skriver nummer på knapparna och sen blandar och sist lägger till tom knapp
    private void shufflePuzzle() {
        ArrayList<Integer> buttonNumbers = new ArrayList<>();
        for (int i = 1; i <= 15; i++) buttonNumbers.add(i);

        Collections.shuffle(buttonNumbers); //Shuffle blandar alla 15 nummer i arrayen
        buttonNumbers.add(0); // lägg till sista knappen som är tom

        // Nu ska vi sätta nummer till knapparna och göra sista knappen tom
        int nextNumber = 0; // vilken plats i buttonNumbers array vi ska titta på
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                int currentNumber = buttonNumbers.get(nextNumber++);
                this.buttonNumbers[row][column] = currentNumber;

                if (currentNumber == 0) { //Om vi hittar nummer 0 ska vi göra den tomma knappen
                    buttons[row][column].setText("");
                    buttons[row][column].setBackground(Color.LIGHT_GRAY);
                    emptyRow = row;
                    emptyCol = column;
                } else {
                    buttons[row][column].setText(String.valueOf(currentNumber));
                    buttons[row][column].setBackground(Color.WHITE);
                }
            }
        }
    }

    // Fuska så att alla knappar är redan 12345..
    private void cheating() {
        int numberCounting = 1;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                if (row == 3 && column == 3) { // positionen som är längst ner i höger sida
                    buttons[row][column].setText("");
                    buttons[row][column].setBackground(Color.lightGray);
                    emptyRow = row;
                    emptyCol = column;
                } else { // annars sätt text från 1 till 15 genom att räkna upp numberCounting
                    buttons[row][column].setText(String.valueOf(numberCounting));
                    buttons[row][column].setBackground(Color.WHITE);
                    numberCounting++;
                }
            }
        }
    }

    // Kolla om jag vinner eller inte
    private boolean isPuzzleSolved() {
        int expectedNum = 1; // Om jag vinner ska första button ha nummer 1 och sen 2..3..4..5...
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                if (row == 3 && column == 3) { // Kolla om button är längst ner till höger är tom och return true
                    return buttons[row][column].getText().equals("");
                } else if (!buttons[row][column].getText().equals(String.valueOf(expectedNum))) {
                    return false; // Om det inte är tom ruta, och expectedNum inte är samma som nummer på button, har jag inte vunnit
                }
                expectedNum++; // Efter varje button kontrollerad ska vi öka expectedNum för att jämföra med nästa nummer mellan 1 och 15
            }
        }
        return true; // Om alla nummer är rätt ordning från 1 till 15 och sista button är tom har jag vunnit spelet
    }
    private class ButtonClickListener implements ActionListener {
        private int row, column;

        public ButtonClickListener(int row, int column) {
            this.row = row;
            this.column = column;
        }
// Kolla om en klickad knapp är brevid den tomma knappen
private boolean canMoveToEmptySpace(int row, int col) {
    // Är den ovanför tomma?
    if (emptyRow == row + 1 && emptyCol == col) {
        return true;
    }
    // Är den under tomma?
    if (emptyRow == row - 1 && emptyCol == col) {
        return true;
    }
    // Är den till vänster om tomma?
    if (emptyRow == row && emptyCol == col + 1) {
        return true;
    }
    // Är den till höger om tomma?
    if (emptyRow == row && emptyCol == col - 1) {
        return true;
    }
    return false;
}
        //Det här händer om man klickar på knappen och canMoveToEmptySpace = true
        //Vi flyttar på knappen och kollar om har vunnit eller inte
        @Override
        public void actionPerformed(ActionEvent e) {
            if (canMoveToEmptySpace(row, column)) {
                // Sätt knappens nummer på den tomma knappen
                buttons[emptyRow][emptyCol].setText(buttons[row][column].getText());
                buttons[emptyRow][emptyCol].setBackground(Color.WHITE);

                // Gör den tryckta knappen till nya tomma knappen
                buttons[row][column].setText("");
                buttons[row][column].setBackground(Color.lightGray);

                // Ändra så vi vet var är den tomma knappen nu
                emptyRow = row;
                emptyCol = column;

                // Kolla om jag har vunnit och visa grattis texten
                if (isPuzzleSolved()) {
                    JOptionPane.showMessageDialog(null, "Grattis, du vann!");
                }
            }
        }
    }

    // Main metod som skapar en PuzzleGame som kör allting
    public static void main(String[] args) {
        new PuzzleGame();
    }
}