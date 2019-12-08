import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuizGUI extends JFrame implements ActionListener {
    private int score = 0;
    private JButton start, exit, nextButton;
    private Image img = Toolkit.getDefaultToolkit().getImage("D:\\Java\\background1.jpg");
    private int ques = 24;
    private Question[] questions;
    private JLabel questionLabel;
    private JLabel questionNumber;
    private JRadioButton[] jRadioButtons;
    private ButtonGroup buttonGroup;

    private QuizGUI() {
        this.setContentPane(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, this);
            }
        });
        setLayout(null);
        setSize(600, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) ((screenSize.getWidth() - this.getWidth()) / 2), (int) ((screenSize.getHeight() - this.getHeight()) / 2));
        startMenu();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new QuizGUI();
    }

    private void startMenu() {
        JLabel menuLabel = new JLabel("QuizApp", SwingConstants.CENTER);
        menuLabel.setSize(200, 80);
        menuLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        menuLabel.setForeground(Color.BLACK);
        menuLabel.setLocation((this.getWidth() - menuLabel.getWidth()) / 2, 80);
        menuLabel.setFont(new Font("Helvetica", Font.BOLD, 45));
        start = new JButton("START");
        start.setSize(100, 50);
        start.setLocation((this.getWidth() - start.getWidth()) / 2, 250);
        Hover(start);
        start.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        exit = new JButton("EXIT");
        exit.setSize(100, 50);
        exit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        exit.setLocation((this.getWidth() - exit.getWidth()) / 2, 350);
        Hover(exit);
        add(menuLabel);
        add(start);
        add(exit);
    }

    private void Hover(JButton jButton) {
        jButton.getModel().addChangeListener(changeEvent -> {
            ButtonModel model = (ButtonModel) changeEvent.getSource();
            if (model.isRollover()) {
                jButton.setBackground(new Color(100, 130, 200));
            } else {
                jButton.setBackground(Color.white);
            }
        });
        jButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == exit) {
            this.dispose();
            System.exit(0);
        } else if (actionEvent.getSource() == start) {
            this.getContentPane().removeAll();
            try {
                questionsGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.update(this.getGraphics());
        } else if (actionEvent.getSource() == nextButton) {
            if (ques < questions.length - 1 && buttonGroup.getSelection() != null) {
                this.getContentPane().removeAll();
                check();
                ques += 1;
                System.out.println(score);
                buildUI(questions, questionNumber, questionLabel, jRadioButtons);
                this.update(this.getGraphics());
            } else if (buttonGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Please Select an option to continue!!!", "Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                resultGUI();
                this.update(this.getGraphics());
            }
        }
    }

    private void check() {
        for (int i = 0; i <= ques; i++) {
            String correct = questions[i].getCorrectAnswer();
            if (buttonGroup.getSelection().getActionCommand().equals(correct))
                score += 5;
        }
    }

    private void resultGUI() {
        JLabel scoreLabel = new JLabel("Your Score : " + score);
        scoreLabel.setSize(100, 25);
        scoreLabel.setLocation((this.getHeight() - scoreLabel.getHeight()) / 2, (this.getWidth() - scoreLabel.getWidth()) / 2);
    }

    private void questionsGUI() throws IOException {
        QuestionBank questionBank = new QuestionBank();
        questions = questionBank.getData();
        questionLabel = new JLabel();
        questionNumber = new JLabel();
        nextButton = new JButton("NEXT");
        jRadioButtons = new JRadioButton[4];
        nextButton.setSize(150, 50);
        nextButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        nextButton.setLocation(150, 475);
        Hover(nextButton);
        questionNumber.setBounds(10, 30, 590, 20);
        questionNumber.setFont(new Font("Helvetica", Font.BOLD, 20));
        questionNumber.setForeground(Color.BLACK);
        questionLabel.setBounds(10, 70, 590, 65);
        questionLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        questionLabel.setForeground(Color.BLACK);

        buildUI(questions, questionNumber, questionLabel, jRadioButtons);
    }

    private void buildUI(Question[] questions, JLabel questionNumber, JLabel questionLabel, JRadioButton[] jRadioButtons) {
        DisplayQuestion display = displayQuestion(questions, ques);
        showQuestion(questionNumber, questionLabel, display, jRadioButtons, ques, questions);
        add(nextButton);
    }

    private void showQuestion(JLabel questionNumber, JLabel questionLabel, DisplayQuestion display, JRadioButton[] jRadioButtons, int ques, Question[] questions) {
        questionNumber.setText("Question " + (ques + 1) + " :  ");
        questionLabel.setText("<html>" + StringEscapeUtils.unescapeHtml4(questions[ques].getQuestion()) + "</html>");
        add(questionNumber);
        add(questionLabel);

        int n = 50;
        for (int j = 0; j < display.getAnswers().length; j++) {
            jRadioButtons[j] = new JRadioButton();
            jRadioButtons[j].setFont(new Font("Helvetica", Font.BOLD, 20));
            jRadioButtons[j].setForeground(Color.BLACK);
            jRadioButtons[j].setOpaque(false);
            jRadioButtons[j].setBounds(30, 100 + n, 300, 50);
            jRadioButtons[j].setActionCommand(StringEscapeUtils.unescapeHtml4(display.getAnswers()[j]));
            jRadioButtons[j].setText(StringEscapeUtils.unescapeHtml4(display.getAnswers()[j]));
            System.out.println(ques + " : " + display.getAnswers()[j]);
            n += 75;
        }
        buttonGroup = new ButtonGroup();
        for (int k = 0; k < display.getAnswers().length; k++) {
            buttonGroup.add(jRadioButtons[k]);
            add(jRadioButtons[k]);
        }
    }


    private DisplayQuestion displayQuestion(Question[] questions, int ques) {
        String[] samp = questions[ques].getAnswers();
        String[] answers = new String[samp.length + 1];
        System.arraycopy(samp, 0, answers, 0, samp.length);
        answers[samp.length] = questions[ques].getCorrectAnswer();
        return new DisplayQuestion(questions[ques], answers);
    }
}
