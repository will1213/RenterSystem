package Model;

import java.io.Serializable;

public class MyDate implements Serializable
{
	private int day;
	private int month;
	private int year;
	
	/*** Constructors ***/
	public MyDate()
	{
		setCurrentDate();
	}
	
	public MyDate(int day, int month, int year)
	{
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	/*** Methods ***/
	public void setCurrentDate()
	{
		String tempString = java.time.LocalDate.now().toString();
		String delims = "[-]+";
		String[] tokens = tempString.split(delims);
		year = Integer.parseInt(tokens[0]);
		month = Integer.parseInt(tokens[1]);
		day = Integer.parseInt(tokens[2]);
	}
	
	@Override
	public String toString()
	{
		return (month + "/" + day + "/" + year + "\n");
	}
	
	/*** Getters ***/
	public int getDay()
	{
		return day;
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getYear()
	{
		return year;
	}
}