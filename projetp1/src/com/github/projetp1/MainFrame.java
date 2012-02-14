package com.github.projetp1;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

/**
 * @author  alexandr.perez
 */
public class MainFrame extends JFrame {

	private JPanel contentPane;
	/**
	 * @uml.property  name="lblTest"
	 */
	private JLabel lblTest;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		lblTest = new JLabel("Test");
		contentPane.add(lblTest, BorderLayout.CENTER);
	}

	/**
	 * @return
	 * @uml.property  name="lblTest"
	 */
	public JLabel getLblTest() {
		return lblTest;
	}
}
