package View;

import ClientController.ApplicationController;

import Model.*;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;

public class Application
{
	public static volatile String msgFromClient[]; // Messages sent by client
	protected ApplicationController theClient;
	private JFrame frmRentalProperty;
	public static volatile ArrayList<Property> properties;
	public Application(ApplicationController c)
	{
		this.theClient = c;
		msgFromClient = new String[100];
		for(int i = 0; i < msgFromClient.length; i++)
		{
			msgFromClient[i] = "";
		}
		initialize();
	}
	
	/**
	 * Create and launch the application.
	 */
	public static void mainGUI(ApplicationController c) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application(c);
					window.frmRentalProperty.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRentalProperty = new JFrame();
		frmRentalProperty.getContentPane().setForeground(Color.BLACK);
		frmRentalProperty.setTitle("Rental Property");
		frmRentalProperty.setBounds(100, 100, 544, 366);
		frmRentalProperty.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRentalProperty.getContentPane().setLayout(null);
		
		JButton btnManager = new JButton("Manager");
		btnManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Manager Selected");
				theClient.msgFromGUI[0] = "Manager"; // Tells client it is behaving like a manager.
				frmRentalProperty.dispose();
				ManagerGUI mg = new ManagerGUI(theClient);
				mg.ManagerScreen(theClient);
			}
		});
		btnManager.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnManager.setBounds(222, 136, 85, 21);
		frmRentalProperty.getContentPane().add(btnManager);
		
		JButton btnLandlord = new JButton("Landlord");
		btnLandlord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Landlord Selected");
				theClient.msgFromGUI[0] = "Landlord"; // Tells client it is behaving like a landlord.
				frmRentalProperty.dispose();
				LandlordGUI lg = new LandlordGUI(theClient);
				lg.LandlordScreen(theClient);
			}
		});
		btnLandlord.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnLandlord.setBounds(222, 167, 85, 21);
		frmRentalProperty.getContentPane().add(btnLandlord);
		
		JButton btnRenter = new JButton("Renter");
		btnRenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Renter Selected");
				theClient.msgFromGUI[0] = "Renter"; // Tells client it is behaving like a renter.
				frmRentalProperty.dispose();
				RenterGUI rg = new RenterGUI(theClient);
				rg.RenterScreen(theClient);
			}
		});
		btnRenter.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnRenter.setBounds(222, 198, 85, 21);
		frmRentalProperty.getContentPane().add(btnRenter);
		
		JLabel lblRentalPropertyApplication = new JLabel("\tRental Property Application");
		lblRentalPropertyApplication.setFont(new Font("Segoe UI Symbol", Font.BOLD | Font.ITALIC, 25));
		lblRentalPropertyApplication.setBounds(98, 10, 360, 75);
		frmRentalProperty.getContentPane().add(lblRentalPropertyApplication);
		
		JLabel lblPleaseChooseThe = new JLabel("Please choose the type of user");
		lblPleaseChooseThe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPleaseChooseThe.setBounds(186, 113, 173, 13);
		frmRentalProperty.getContentPane().add(lblPleaseChooseThe);
		
		JLabel lblEnsfGroup = new JLabel("ENSF 480 Group 15: Daryl Dang, Will Huang, and Perjot Sidhu");
		lblEnsfGroup.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEnsfGroup.setBounds(124, 306, 351, 13);
		frmRentalProperty.getContentPane().add(lblEnsfGroup);
	}
	
	protected void flushCLientBuffer(int start, int end)
	{
		for(int i = start; i <= end; i++)
		{
			msgFromClient[i] = "";
		}
	}
	protected void waitByMili(long mili)
	{
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void waitForMsg(int index)
	{
		while(msgFromClient[index] == "") {
			//System.out.println(msgFromClient[index]);
			//msgFromClient[index] = msgFromClient[index];
		}
	}
	
	protected void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
