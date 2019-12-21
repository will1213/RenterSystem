package ServerController;

import java.sql.*;
import java.util.ArrayList;
import java.util.TimeZone;

import javax.activation.DataSource;
import javax.naming.InitialContext;

import com.sun.xml.internal.bind.CycleRecoverable.Context;

import Model.*;

public class DataBase {
	private static final int ID = 0;
	Connection myConn;
    Statement stm;
    private static DataBase singleton;
    
  	private DataBase() throws SQLException{
    	

    	//myConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:3306:ENSF480","root", "ensf480db"); 
	   myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "ensf480db");
  		// myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ENSF480?serverTimezone=" + TimeZone.getDefault().getID(), "root", "ensf480db");
       stm = myConn.createStatement();
       
    }
  	
  	public static DataBase getDataBase() throws SQLException{
  		
  		if(singleton == null) {
  			singleton = new DataBase();
  		}
  		 return singleton;
  		
  	}
   public void getNotification(RegisteredRenter r) throws SQLException{
	   
	   ArrayList<Property> cart = new ArrayList<Property>();
	   String query = "selct type from regrenter where email = '"+r.getEmai()+"'";
	   ResultSet rs = stm.executeQuery(query);
	   rs.next();
	   String s = rs.getString("type");
	   
	   cart.addAll(searchByType(s));
	   
	   query = "selct numBath from regrenter where email = '"+r.getEmai()+"'";
	   rs = stm.executeQuery(query);
	   rs.next();
	   int numBath = rs.getInt("numBath");
	   
	   cart.addAll(searchByBath(numBath));
	   
	   
	   query = "selct numBed from regrenter where email = '"+r.getEmai()+"'";
	   rs = stm.executeQuery(query);
	   rs.next();
	   int numBed = rs.getInt("numBed");
	   
	   cart.addAll(searchByBed(numBed));
	   
	   query = "selct furnished from regrenter where email = '"+r.getEmai()+"'";
	   rs = stm.executeQuery(query);
	   rs.next();
	   boolean furnished = rs.getBoolean("furnished");
	   int furnish;
	   if(furnished) {
		   furnish = 1;
	   }
	   else {
		   furnish = 0;
	   }
	   cart.addAll(searchByFurnish(furnish));
	   
	   
	   query = "selct quadrant from regrenter where email = '"+r.getEmai()+"'";
	   rs = stm.executeQuery(query);
	   rs.next();
	   String quadrant = rs.getString("addressQuadrant");
	   
	   cart.addAll(searchByQuadrant(quadrant));
	   
	   r.setCart(cart);
   }
   
   public Report generateReport(MyDate a,MyDate b) throws SQLException {
	   
	   //STR_TO_DATE('07-25-2012','%m-%d-%y');STR_TO_DATE('07-25-2012','%m-%d-%y')
	   //String query = "select count(*) from Property where regDate >= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d') and regDate <= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d')"; 
	   //String query = "select count(*) from Property where regDate == STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d')"; 
	   //System.out.println("DDD"+a.getDay());
	   //System.out.println("MMM"+a.getMonth());
	   //System.out.println("YYY"+a.getYear());
	   String query = "select count(*) from Property where regDate >= '"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //String query ="select count(*) from Property where regDate >= '2019-11-24'";
	  // String query = "select count(*) from Property where regDate >= '"+Integer.toString(a.getYear())+Integer.toString(a.getMonth())+Integer.toString(a.getDay())+"'"; 
	   //String query = "select count(*) from Property";
	   //String query = "select count(*) from Property where regDate >= '"+ a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+ b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   ResultSet rs = stm.executeQuery(query);
	   rs.next();
	   int numList = rs.getInt("count(*)");
	   
	   query = "select count(*) from Property where state = 'rented' and regDate >= '"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //query = "select count(*) from Property where state = 'rented' and regDate >= '"+ a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+ b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //query = "select count(*) from Property where state = 'rented' and regDate >= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d') and regDate <= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d')"; 
	   rs = stm.executeQuery(query);
	   rs.next();
	   int numRented = rs.getInt("count(*)");
	   
	   query = "select * from Property where state = 'rented' and regDate >= '"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //query = "select * from Property where state = 'rented' and regDate >= '"+ a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+ b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //query = "select * from Property where state= 'rented' and regDate >= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d') and regDate <= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d')"; 
	   rs = stm.executeQuery(query); 
	   ArrayList<Property> rentedProperty = convertToProperty(rs);
	   
	   query = "select count(*) from Property where state = 'active' and regDate >= '"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //query = "select count(*) from Property where state = 'active' and regDate >= '"+ a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"' and regDate <= '"+ b.getYear()+"-"+b.getMonth()+"-"+b.getDay()+"'";
	   //query = "select count(*) from Property where state = 'active' and regDate >= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d') and regDate <= STR_TO_DATE('"+a.getYear()+"-"+a.getMonth()+"-"+a.getDay()+"', '%y-%m-%d')"; 
	   rs = stm.executeQuery(query);
	   rs.next();
	   int numActive = rs.getInt("count(*)");
	   
	   Report r = new Report(numList,numRented,numActive,rentedProperty);
	   
	   return r;
   }
   public boolean loginRenter(String username, String password) throws SQLException {
	  // System.out.println("this is username :"+username);
	   //System.out.println("this is pass"+password);
	   String query = "Select username from account where username = '" + username + "' and password = '" + password + "'";
	   //String query = "Select username from account where username = 'willhuang' and password = 'willhuang'";
	  // String query = "Select username from account where username = '" + username + "' and password = '" + password + "'";
	   //System.out.println("TTTTTTTTTTTTTTTt");
	   //System.out.println(stm.execute(query));
	   //if(stm.execute(query))
		 //  return true;
	   //else 
		 //  return false;
	  ResultSet rs = stm.executeQuery(query);
	   //rs.next();
	  // String username1 = rs.getString("username");
	  // System.out.println(username1);
	   if(rs.next())
		   return true;
	   else 
		   return false;
	 //  return  stm.execute(query);
   }
   public boolean loginManager(String username, String password) throws SQLException {
	   String query = "select username from Manager where username = '" + username + "'and password = '" + password + "'";
	   ResultSet rs = stm.executeQuery(query);
	   if(rs.next())
		   return true;
	   else 
		   return false;
	   
   }
   public void getAllUserName() {
	   //String query = "select username from Account where username = " + username + "and password = " + password + ")";
	  //return  stm.execute(query);
   }
   public ArrayList<Property> getAllProperty() throws SQLException {
	   String query = "select * from property";
	   ResultSet rs = stm.executeQuery(query);
	   return  convertToProperty(rs);
	   
   }
   public boolean registerAccount(String username, String password) throws SQLException {
	   
	   String query = "select username from Account where username = '" + username+"'";
	   
	   //ResultSet rs = stm.executeQuery(query);
	   if(stm.execute(query) == false) {
		   query ="insert into Account (username, password) values ('" + username+ "', '"+ password + "')";
		   stm.execute(query);
		   return true;
	   }
	   else {
		   return false;
	   }
		   
	  
   }
   
   public boolean registerPropertyWithoutPosting(String type, int numBath, int numBed, int furnished, String state, String street, String city, String country, String province, String quadrant, String Fname, String Lname, String email, String date, int gender) throws SQLException {
	   String query = "insert into Property (type,numBath,numBed,furnished,state,addressStreet,addressCity,addressCountry,addressProvince,addressQuadrant,ownerFname,ownerLname,ownerEmail,regDate,gender)"
	   		+ " values ('" +  type + "', '"+ Integer.toString(numBath)+"', '"+ Integer.toString(numBed)+"', '"+ Integer.toString(furnished)+"', '"+ state+"', '"+street+"', '"+city+"', '"+country+"', '"+province+"', '"+quadrant+"', '"+Fname+"', '"+Lname+"', '"+email+"', DATE '"+date+"', "+Integer.toString(gender)+")";
	   if(stm.execute(query))
		   return true;
	   else {
		   return false;
	   }
   }
   public boolean registerPropertyWithPosting(String type, int numBath, int numBed, int furnished, String state, String street, String city, String country, String province, String quadrant, String Fname, String Lname, String email, String date, int gender, int fee, int period) throws SQLException {
	   String query = "insert into Property (type,numBath,numBed,furnished,state,addressStreet,addressCity,addressCountry,addressProvince,addressQuadrant,ownerFname,ownerLname,ownerEmail,regDate,gender,fees,period)"
	   		+ " values ('" +  type + "', '"+ Integer.toString(numBath)+"', '"+ Integer.toString(numBed)+"', '"+ Integer.toString(furnished)+"', '"+ state+"', '"+street+"', '"+city+"', '"+country+"', '"+province+"', '"+quadrant+"', '"+Fname+"', '"+Lname+"', '"+email+"', DATE '"+date+"', '"+Integer.toString(gender)+"', '"+Integer.toString(fee)+"', "+Integer.toString(period)+")";
	   if(stm.execute(query))
		   return true;
	   else {
		   return false;
	   }
   }
   
   public boolean settingState(int ID,String state) throws SQLException {
	   String query = "update property set state = '"+ state +"' where ID = " + Integer.toString(ID);
	   if(stm.execute(query))
		   return true;
	   else {
		   return false;
	   }
   }
  
   
   private  ArrayList<Property> convertToProperty(ResultSet rs) throws SQLException {
	   ArrayList<Property> toSend = new ArrayList<>();
	   while (rs.next()) {
		   
		   
		   int ID = rs.getInt("ID");
		   Date d = rs.getDate("regDate");
		   MyDate myDate = new MyDate(d.getDate(),d.getMonth(),d.getYear());
		   
		   String type2 = rs.getString("type");
		   int BathNum = rs.getInt("numBath");
		   int BedNum = rs.getInt("numBed");
		   boolean furnished  = rs.getBoolean("furnished");
		   String state = rs.getString("state");
		   
		   String street = rs.getString("addressStreet");
		   String city = rs.getString("addressCity");
		   String country = rs.getString("addressCountry");
		   String province = rs.getString("addressProvince");
		   String ownderQuadrant = rs.getString("addressQuadrant");
		   
		   Address address = new Address(street,city,country,province,ownderQuadrant);
		   
		   String ownerFname = rs.getString("ownerFname");
		   String ownerLname = rs.getString("ownerLname");
		   String ownerEmail = rs.getString("ownerEmail");
		  // Date d2 = rs.getDate("ownerBirthday");
		   //MyDate birthday = new MyDate(d2.getDate(),d2.getMonth(),d2.getYear());
		   MyDate birthday = new MyDate();
		   boolean gender = rs.getBoolean("gender");
		   String gender2;
		   if(gender) {
			   gender2="M";
		   }
		   else {
			   gender2="F";
		   }
		   Name n = new Name(ownerFname,ownerLname);
		   
		   Landlord l = new Landlord(n,birthday,gender2,null,ownerEmail);
		   Property p = new Property(ID,myDate,l,type2,BathNum,BedNum,furnished,state,address);
		   toSend.add(p);
	   }
	   return toSend;
   }
   public ArrayList<Property> search(String type, int numBath, int numBed, int furnished, String quadrant) throws SQLException{
		String query = "select * from property where type = '"+type+"' and numBath = "+Integer.toString(numBath)+" and numBed = "+Integer.toString(numBed)+" and furnished = "+Integer.toString(furnished) + " and addressQuadrant = '"+quadrant+"'";
	  // String query = "select * from Property where type = '"+type+"' and "; 
	   ResultSet rs = stm.executeQuery(query);
		 return convertToProperty(rs);
	}
   public String searchOwnerEmailByID(int ID) throws SQLException {
	   String query = "select ownerEmail from Property where ID = "+ Integer.toString(ID);
	   ResultSet rs = stm.executeQuery(query);
	   return rs.getString("ownerEmail");
   }
   public  ArrayList<Property> searchByType(String type) throws SQLException {
	   String query = "select * from Property where type = '"+type+"'";
	   ResultSet rs = stm.executeQuery(query);
	   return convertToProperty(rs);
   }
   
   public  ArrayList<Property> searchByBath(int num) throws SQLException {
	   String query = "select * from Property where numBath = "+ Integer.toString(num);
	   ResultSet rs = stm.executeQuery(query);
	   return convertToProperty(rs);
   }
 
   public  ArrayList<Property> searchByBed(int num) throws SQLException {
	   String query = "select * from Property where numBed = "+ Integer.toString(num);
	   ResultSet rs = stm.executeQuery(query);
	   return convertToProperty(rs);
   }
   
   public  ArrayList<Property> searchByFurnish(int furnished) throws SQLException {
	   String query = "select * from Property where furnished = "+ Integer.toString(furnished);
	   ResultSet rs = stm.executeQuery(query);
	   return convertToProperty(rs);
	   
   }
   
   public  ArrayList<Property> searchByQuadrant(String quadrant) throws SQLException {
	   String query = "select * from Property where addressQuadrant = '"+ quadrant+"'";
	   ResultSet rs = stm.executeQuery(query);
	   return convertToProperty(rs);
	   
   }
   
   public ArrayList<Property> searchByID(int ID) throws SQLException {
	   String query = "select * from Property where ID = "+Integer.toString(ID);
	   ResultSet rs = stm.executeQuery(query);
	   return convertToProperty(rs);
	   
   }
   
   public ArrayList<String> searchOwnerByEmail(String email) throws SQLException{
	   ArrayList<String> toSend= new ArrayList<String>();
	   String query = "select ownerEmail, ownerFname, ownerLname, gender from Property where ownerEmail = '"+email+"'";
	   ResultSet rs = stm.executeQuery(query);
	   rs.next();
	   toSend.add(rs.getString("ownerEmail"));
	   toSend.add(rs.getString("ownerFname"));
	   toSend.add(rs.getString("ownerLname"));
	   String gender;
	   if(rs.getBoolean(("gender")))
		   gender = "1";
	   else 
		   gender = "0";
	   toSend.add(gender);
	   return toSend;
   }
   


}
