package com.upm.SistemasInteligentes.recommender.spotify.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class JFrameMain extends JFrame {

	private JPanel contentPane;
	private JTextField txtHolaMundo;
	private JTextField txtHolaMundo_4;
	private JTextField txtHolaMundo_1;
	private JTextField txtHolaMundo_2;
	private JTextField txtHolaMundo_3;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameMain frame = new JFrameMain();
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
	public JFrameMain() {
		setBackground(new Color(25, 20, 20));
		setForeground(Color.WHITE);
		setTitle("Spotify Recomendador");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1075, 709);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(new Color(25, 20, 20));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel tiuto = new JLabel("Recomendador Spotify");
		tiuto.setBounds(97, 21, 443, 85);
		tiuto.setFont(new Font("Dubai Light", Font.BOLD, 45));
		tiuto.setForeground(Color.WHITE);
		contentPane.add(tiuto);
		
		Box seedSongsBox = Box.createVerticalBox();
		seedSongsBox.setBounds(97, 198, 323, 415);
		contentPane.add(seedSongsBox);
		
		txtHolaMundo = new JTextField();
		txtHolaMundo.setEditable(false);
		txtHolaMundo.setHorizontalAlignment(SwingConstants.CENTER);
		txtHolaMundo.setText("Hola Mundo");
		txtHolaMundo.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		txtHolaMundo.setForeground(Color.WHITE);
		txtHolaMundo.setBackground(new Color(25, 20, 20));
		seedSongsBox.add(txtHolaMundo);
		txtHolaMundo.setColumns(10);
		
		txtHolaMundo_1 = new JTextField();
		txtHolaMundo_1.setHorizontalAlignment(SwingConstants.CENTER);
		txtHolaMundo_1.setText("Hola Mundo");
		txtHolaMundo_1.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		txtHolaMundo_1.setForeground(Color.WHITE);
		txtHolaMundo_1.setBackground(new Color(25, 20, 20));
		seedSongsBox.add(txtHolaMundo_1);
		txtHolaMundo_1.setColumns(10);
		
		txtHolaMundo_2 = new JTextField();
		txtHolaMundo_2.setHorizontalAlignment(SwingConstants.CENTER);
		txtHolaMundo_2.setText("Hola Mundo");
		txtHolaMundo_2.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		txtHolaMundo_2.setForeground(Color.WHITE);
		txtHolaMundo_2.setBackground(new Color(25, 20, 20));
		seedSongsBox.add(txtHolaMundo_2);
		txtHolaMundo_2.setColumns(10);
		
		txtHolaMundo_3 = new JTextField();
		txtHolaMundo_3.setHorizontalAlignment(SwingConstants.CENTER);
		txtHolaMundo_3.setText("Hola Mundo");
		txtHolaMundo_3.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		txtHolaMundo_3.setForeground(Color.WHITE);
		txtHolaMundo_3.setBackground(new Color(25, 20, 20));
		seedSongsBox.add(txtHolaMundo_3);
		txtHolaMundo_3.setColumns(10);
		
		txtHolaMundo_4 = new JTextField();
		txtHolaMundo_4.setHorizontalAlignment(SwingConstants.CENTER);
		txtHolaMundo_4.setText("Hola Mundo");
		txtHolaMundo_4.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		txtHolaMundo_4.setForeground(Color.WHITE);
		txtHolaMundo_4.setBackground(new Color(25, 20, 20));
		seedSongsBox.add(txtHolaMundo_4);
		txtHolaMundo_4.setColumns(10);
		
		Box seedSongsBox_1 = Box.createVerticalBox();
		seedSongsBox_1.setBounds(538, 198, 323, 415);
		contentPane.add(seedSongsBox_1);
		
		textField = new JTextField();
		textField.setText("Hola Mundo");
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setForeground(Color.WHITE);
		textField.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBackground(new Color(25, 20, 20));
		seedSongsBox_1.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setText("Hola Mundo");
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setForeground(Color.WHITE);
		textField_1.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		textField_1.setColumns(10);
		textField_1.setBackground(new Color(25, 20, 20));
		seedSongsBox_1.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setText("Hola Mundo");
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setForeground(Color.WHITE);
		textField_2.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		textField_2.setColumns(10);
		textField_2.setBackground(new Color(25, 20, 20));
		seedSongsBox_1.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setText("Hola Mundo");
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setForeground(Color.WHITE);
		textField_3.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		textField_3.setColumns(10);
		textField_3.setBackground(new Color(25, 20, 20));
		seedSongsBox_1.add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setText("Hola Mundo");
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setForeground(Color.WHITE);
		textField_4.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 25));
		textField_4.setColumns(10);
		textField_4.setBackground(new Color(25, 20, 20));
		seedSongsBox_1.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setBounds(97, 103, 323, 58);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(538, 103, 136, 58);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Recomendar");
		btnNewButton_1.setBounds(772, 121, 89, 23);
		contentPane.add(btnNewButton_1);
	}
}
