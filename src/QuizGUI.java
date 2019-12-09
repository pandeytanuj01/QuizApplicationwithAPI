import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuizGUI extends JFrame implements ActionListener {
    private int score = 0;
    private JButton start, exit, nextButton, back;
    private Image img = Toolkit.getDefaultToolkit().getImage("assets\\background1.jpg");
    private int ques = 0;
    private Question[] questions;
    private JLabel questionLabel;
    private JLabel questionNumber;
    private JRadioButton[] jRadioButtons;
    private ButtonGroup buttonGroup;
    QuestionBank questionBank;

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
            int result = Integer.parseInt((String) JOptionPane.showInputDialog(
                    this,
                    "Select No. of questions you want",
                    "No. of Questions",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    5
            ));
            try {
                questionBank = new QuestionBank("https://opentdb.com/api.php?amount=" + result +"&category=18");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.getContentPane().removeAll();
            questionsGUI();
            this.update(this.getGraphics());
        } else if (actionEvent.getSource() == nextButton) {
            if (buttonGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Please Select an option to continue!!!", "Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                if (ques < questions.length - 1 && buttonGroup.getSelection() != null) {
                    check(ques);
                    this.getContentPane().removeAll();
                    ques += 1;
                    System.out.println(score);
                    buildUI(questions, questionNumber, questionLabel, jRadioButtons);
                } else {
                    check(ques);
                    this.getContentPane().removeAll();
                    resultGUI();
                }
                this.update(this.getGraphics());
            }
        } else if (actionEvent.getSource() == back) {
            this.getContentPane().removeAll();
            startMenu();
            this.update(this.getGraphics());
        }
    }

    private void check(int quesNum) {
        String correct = questions[quesNum].getCorrectAnswer();
        if (buttonGroup.getSelection().getActionCommand().equals(correct))
            score += 5;
    }

    private void resultGUI() {
        back = new JButton("BACK TO MENU");
        Hover(back);
        back.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        back.setBackground(Color.WHITE);
        back.setSize(150, 50);
        back.setLocation((this.getWidth() - back.getWidth()) / 2, 450);
        JLabel scoreLabel = new JLabel("Your Score : " + score);
        JLabel co = new JLabel("Correct: " + score / 5);
        JLabel inco = new JLabel("Incorrect: " + (questions.length - (score / 5)));
        scoreLabel.setSize(400, 50);
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setLocation((this.getWidth() - scoreLabel.getWidth()) / 2, 150);
        scoreLabel.setFont(new Font("Helvetica", Font.BOLD, 45));
        co.setSize(350, 50);
        co.setForeground(Color.BLACK);
        co.setLocation((this.getWidth() - scoreLabel.getWidth()) / 2, 250);
        co.setFont(new Font("Helvetica", Font.BOLD, 25));
        inco.setSize(350, 50);
        inco.setForeground(Color.BLACK);
        inco.setLocation((this.getWidth() - scoreLabel.getWidth()) / 2, 350);
        inco.setFont(new Font("Helvetica", Font.BOLD, 25));
        add(scoreLabel);
        add(inco);
        add(co);
        add(back);
    }

    private void questionsGUI() {
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
            jRadioButtons[j].setBounds(30, 100 + n, 550, 50);
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
