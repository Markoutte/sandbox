package me.markoutte.sandbox.swing.bugs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-11-17
 */
public class TestApp {

    private JFrame frame;

    public TestApp() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnRun = new JButton("run");
        btnRun.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JDialog dialog = getChildDialog();
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setVisible(true);
            }

        });
        frame.getContentPane().add(btnRun, BorderLayout.CENTER);
    }

    public JDialog getChildDialog() {

        JDialog dialog = new JDialog();
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setBounds(100, 100, 450, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JTextField(), BorderLayout.CENTER);
        panel.add(new JTextField(), BorderLayout.NORTH);
        panel.add(new JTextField(), BorderLayout.SOUTH);
        panel.add(new JLabel("Blah"), BorderLayout.EAST);
        panel.add(new JLabel("Blah"), BorderLayout.WEST);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        return dialog;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestApp window = new TestApp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
