/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantsale.Tools;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author rudolf
 */
public class MsgBox {

    public static void info(String infoMessage) {
	JOptionPane.showMessageDialog(null, infoMessage, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void info(String infoMessage, String title) {
	JOptionPane.showMessageDialog(null, infoMessage, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // ref : http://stackoverflow.com/questions/16511039/is-there-a-way-to-only-have-the-ok-button-in-a-joptionpane-showinputdialog-and
    public static String input(String inputMessage, String title) {
        String[] options = {"OK"};
        JPanel pane = new JPanel();
        JLabel lb = new JLabel(inputMessage);
        JTextField tf = new JTextField(10);
        
        pane.add(lb);
	pane.add(tf);
        
	int selected = JOptionPane.showOptionDialog(null, pane, title, JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	return tf.getText();
    }

    // ref : http://stackoverflow.com/a/15853127
    public static boolean ask(String askMessage) {
        if (JOptionPane.showConfirmDialog(null, askMessage, "CONFIRM",
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            return true;
        return false;
    }
}
