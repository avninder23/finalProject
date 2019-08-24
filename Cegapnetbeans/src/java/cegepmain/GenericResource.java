/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cegepmain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.validator.constraints.Email;

@Path("application")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    Connection con;
    Statement stm;

    public Statement setConnection() {

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad305team6", "anypw2");
            stm = con.createStatement();
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return stm;
    }
    Date date = new Date();
    java.sql.Timestamp sq = new java.sql.Timestamp(date.getTime());

    JSONObject jsonobject = new JSONObject();
    int num = 0;

    @GET
    @Path("singup&{firstName}&{lastName}&{dateofbirth}&{email}&{occupation}&{qualification}&{contactnumber}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("firstName") String fn, @PathParam("lastName") String ln, @PathParam("dateofbirth") String dob,
             @PathParam("email") String email, @PathParam("occupation") String occ, @PathParam("qualification") String qual,
            @PathParam("password") String pass, @PathParam("contactnumber") String pNumber) throws SQLException {
        int userid = 0;
        String email_id = null;
        try {
            stm = setConnection();
            try {
                ResultSet rs2 = stm.executeQuery("select email_id,user_id from users where exists(select email_id from users where email_id='" + email + "')");
                if (rs2.next() == false) {
                    ResultSet rs = stm.executeQuery("SELECT USER_ID,EMAIL_id FROM USERS order by USER_ID DESC");
                    rs.next();

                    userid = rs.getInt("USER_ID");
                    email_id = rs.getString("EMAIL_ID");
                    System.out.println("USERID OF THE USER IS " + userid);
                    ++userid;

                    num = stm.executeUpdate("INSERT INTO USERS VALUES(+" + userid + "," + "'" + fn + "'" + "," + "'" + ln + "','" + dob
                            + "'," + "'" + email + "','" + occ + "','" + qual + "','" + pNumber + "'" + "," + "'" + pass + "'" + ")");
                    System.out.println("INSERT INTO USERS VALUES(+" + userid + "," + "'" + fn + "'" + "," + "'" + ln + "','" + dob
                            + "'," + "'" + email + "','" + occ + "','" + qual + "','" + pNumber + "'" + "," + "'" + pass + "'" + ")");
                    System.out.println("total inserted rows" + num);
                } else {

                    jsonobject.accumulate("STATUS", "WRONG");
                    jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsonobject.accumulate("Message", "Email is already Register");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                userid = 1000;
            }

            if (num == 1) {
                jsonobject.accumulate("STATUS", "OK");
                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsonobject.accumulate("Active", true);
                jsonobject.accumulate("User_id", userid);
                jsonobject.accumulate("Message", "You are successfully Register");

            } else if (num == 0) {

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsonobject.accumulate("STATUS", "ERROR");
            jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsonobject.toString();
    }
    ResultSet rs;

    @GET
    @Path("login&{email}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("email") String email, @PathParam("password") String pass) {

        stm = setConnection();

        try {
            System.out.println("select * from USERS WHERE EMAIL_ID=" + "'" + email + "'" + " and PASSWORD=" + "'" + pass + "'");
            rs = stm.executeQuery("select * from USERS WHERE EMAIL_ID=" + "'" + email + "'" + " AND PASSWORD=" + "'" + pass + "'");

            String fName, lName, contactnumber, userpassword, dateOfbirth;

            int user_Id = 0;

            while (rs.next()) {

                System.out.println("ok ");
                fName = rs.getString("FIRSTNAME");
                lName = rs.getString("LASTNAME");
                email = rs.getString("EMAIL_ID");
                contactnumber = rs.getString("CONTACTNUMBER");
                userpassword = rs.getString("PASSWORD");
                user_Id = rs.getInt("USER_ID");
                System.out.println("username is " + fName);
                if (user_Id != 0) {

                    jsonobject.accumulate("Status", "OK");
                    jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsonobject.accumulate("Active", true);
                    jsonobject.accumulate("User_id", user_Id);
                    jsonobject.accumulate("Password", userpassword);
                    jsonobject.accumulate("email", email);

                }
            }
            if (user_Id == 0) {
                jsonobject.clear();
                jsonobject.accumulate("Status", "WRONG");
                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsonobject.accumulate("MESSAGE", "YOUR ENTERED THE WRONG INFORMATION");
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            jsonobject.clear();
            jsonobject.accumulate("Status", "ERROR");
            jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("MESSAGE", "Database connectivity error");
        }

        return jsonobject.toString();
    }

    @GET
    @Path("message&{message}&{sender_id}&{reciever_email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("message") String message, @PathParam("sender_id") int senderId, @PathParam("reciever_email") String email) throws SQLException {
        int message_id = 0, user_id = 0;

        try {
            stm = setConnection();
            try {
                System.out.println("select user_id from users  where email_id='" + email + "'");
                ResultSet rs2 = stm.executeQuery("select user_id from users where email_id='" + email + "'");
                if (rs2.next() == true) {
                    user_id = rs2.getInt("USER_ID");
                    rs = stm.executeQuery("SELECT MESSAGE_ID FROM MESSAGE ORDER BY MESSAGE_ID DESC");
                    rs.next();
                    message_id = rs.getInt("MESSAGE_ID");

                    System.out.println("USERID OF THE USER IS " + user_id);
                    ++message_id;
                    System.out.println("INSERT INTO MESSAGE VALUES(" + message_id + "," + "'" + message + "'," + senderId + ","
                            + user_id + ",CURRENT_TIMESTAMP");
                    num = stm.executeUpdate("INSERT INTO MESSAGE VALUES(" + message_id + "," + "'" + message + "'," + senderId + ","
                            + user_id + "," + "CURRENT_TIMESTAMP" + ")");

                    System.out.println("total inserted rows" + num);
                } else {

                    jsonobject.accumulate("STATUS", "WRONG");
                    jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsonobject.accumulate("Message", "Email is not  available");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                message_id = 2000;
            }

            if (num == 1) {
                jsonobject.accumulate("STATUS", "OK");
                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsonobject.accumulate("Active", true);
                jsonobject.accumulate("Message_id", message_id);
                jsonobject.accumulate("Message", "You are successfully send message");

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsonobject.accumulate("STATUS", "ERROR");
            jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsonobject.toString();
    }

    @GET
    @Path("attendence&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonattendence(@PathParam("user_id") int userId) throws SQLException {
        int clock_id = 0;
        String clockstatus = null;

        try {
            stm = setConnection();
            try {
                if (userId != 0) {
                    System.out.println("SELECT CLOCK_ID  FROM ATTENDENCE ORDER BY CLOCK_ID DESC");
                    rs = stm.executeQuery("SELECT CLOCK_ID  FROM ATTENDENCE ORDER BY CLOCK_ID DESC");
                    rs.next();

                    clock_id = rs.getInt("CLOCK_ID");
                    System.out.println("SELECT * FROM ATTENDENCE WHERE SER_ID=" + userId + " ORDER BY CLOCK_ID DESC");
                    System.out.println("ATTENDENCE_ID OF THE USER IS " + clock_id);
                    ++clock_id;
                    ResultSet rs2 = stm.executeQuery("SELECT * FROM ATTENDENCE WHERE USER_ID=" + userId + " ORDER BY CLOCK_ID DESC");
                    System.out.println("SELECT * FROM ATTENDENCE WHERE USER_ID=" + userId + " ORDER BY CLOCK_ID DESC");
                    if (rs2.next() == true) {
                        clockstatus = rs2.getString("CLOCK_STATUS");

                        if (clockstatus.equals("IN")) {
                            clockstatus = "OUT";
                            System.out.println("dsadasd");
                             num = stm.executeUpdate("delete from attendence where USER_ID="+userId+"");
                        } else {

                            clockstatus = "IN";
                             num = stm.executeUpdate("INSERT INTO ATTENDENCE VALUES(" + clock_id + "," + "'"
                            + clockstatus + "'," + userId + ",'"+ sq.toInstant().toEpochMilli() + "'," +null+ ")");
                        }
                    } else {
                        clockstatus = "IN";
                          num = stm.executeUpdate("INSERT INTO ATTENDENCE VALUES(" + clock_id + "," + "'"
                            + clockstatus + "'," + userId  +  ",'"+sq.toInstant().toEpochMilli() + "'," +null+ ")");
                    }
                    System.out.println("INSERT INTO ATTENDENCE VALUES(" + clock_id + "," + "'"
                            + clockstatus + "'," + userId + ",'"
                            + sq.toInstant().toEpochMilli() + "'" + ")");
//                    num = stm.executeUpdate("INSERT INTO ATTENDENCE VALUES(" + clock_id + "," + "'"
//                            + clockstatus + "'," + userId + ",'"
//                            + sq.toInstant().toEpochMilli() + "'" + ")");

                    System.out.println("total inserted rows" + num);
                } else {

                    jsonobject.accumulate("STATUS", "WRONG");
                    jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsonobject.accumulate("Message", "Email is not  available");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                clock_id = 3000;
            }

            if (num == 1) {
                jsonobject.accumulate("STATUS", "OK");
                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsonobject.accumulate("Active", true);
                jsonobject.accumulate("CLOCK_ID", clock_id);
                jsonobject.accumulate("Message", "You are successfully Clock" + clockstatus);

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsonobject.accumulate("STATUS", "ERROR");
            jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsonobject.toString();
    }

    JSONArray jsonarray = new JSONArray();

    @GET
    @Path("recievemessage&{sender_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String RecieveMessagegetJson(@PathParam("sender_id") int sender_id) throws SQLException {
        String message = null, date = null, firstname, lastname, contactnumber, emailID;
        int user_id = 0, message_id, reciever_id;

        JSONObject viewmessage = new JSONObject();
        try {
            stm = setConnection();
            System.out.println("select * from Message WHERE RECIEVER_ID=" + sender_id);

            rs = stm.executeQuery("SELECT MESSAGE.SENDER_ID,USERS.CONTACTNUMBER,DATETIME,FIRSTNAME,LASTNAME,MESSAGE,"
                    + "EMAIL_ID,MESSAGE_ID FROM USERS JOIN MESSAGE ON USERS.USER_ID=MESSAGE.SENDER_ID WHERE RECIEVER_ID=" + sender_id);

            viewmessage.accumulate("Status", "OK");
            viewmessage.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            while (rs.next()) {

                message = rs.getString("MESSAGE");
                user_id = rs.getInt("SENDER_ID");
                date = rs.getString("DATETIME");

                firstname = rs.getString("FIRSTNAME");
                lastname = rs.getString("LASTNAME");
                contactnumber = rs.getString("CONTACTNUMBER");
                emailID = rs.getString("EMAIL_ID");

                message_id = rs.getInt("MESSAGE_ID");

                jsonobject.accumulate("Send_id", user_id);
                jsonobject.accumulate("date", date);
                jsonobject.accumulate("EMAIL_ID", emailID);
                jsonobject.accumulate("FIRSTNAME", firstname);
                jsonobject.accumulate("LASTNAME", lastname);
                jsonobject.accumulate("CONTACTNUMBER", contactnumber);
                jsonobject.accumulate("Message", message);

                jsonobject.accumulate("MESSAGE_ID", message_id);
                jsonarray.add(jsonobject);

                jsonobject.clear();
            }
            viewmessage.accumulate("DATA", jsonarray);
            if (user_id == 0) {
                viewmessage.clear();

                viewmessage.accumulate("Status", "WRONG");
                viewmessage.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                viewmessage.accumulate("Message", "no message");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            viewmessage.clear();
            viewmessage.accumulate("STATUS", "ERROR");
            viewmessage.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            viewmessage.accumulate("MESSAGE", "Database connectivity error");

        }
        return viewmessage.toString();
    }

    @GET
    @Path("message&{message_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String messagegetJson(@PathParam("message_id") int messageId) throws SQLException {
        String message = null, occupation, qualification, date = null, firstname, lastname, contactnumber, emailID;
        int senderuser_id = 0;

        JSONObject viewmessage = new JSONObject();
        try {
            stm = setConnection();
            System.out.println("select * from Message WHERE MESSAGE_ID=" + messageId);

            ResultSet rs2 = stm.executeQuery("select * from Message WHERE MESSAGE_ID=" + messageId);

            if (rs2.next() == true) {

                senderuser_id = rs2.getInt("SENDER_ID");
            
                rs = stm.executeQuery("SELECT * FROM USERS JOIN MESSAGE ON USERS.USER_ID=MESSAGE.SENDER_ID WHERE MESSAGE_ID=" + messageId+" AND MESSAGE.SENDER_ID="+senderuser_id);

                rs.next(); 
                message = rs.getString("MESSAGE");
                
                date = rs.getString("DATETIME");

                firstname = rs.getString("FIRSTNAME");
                lastname = rs.getString("LASTNAME");
                contactnumber = rs.getString("CONTACTNUMBER");
                emailID = rs.getString("EMAIL_ID");
                occupation=rs.getString("OCCUPATION");
                qualification=rs.getString("QUALIFICATION");

                

                jsonobject.accumulate("Status", "OK");

                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                
                jsonobject.accumulate("date", date);
                jsonobject.accumulate("EMAIL_ID", emailID);
                jsonobject.accumulate("FIRSTNAME", firstname);
                jsonobject.accumulate("LASTNAME", lastname);
                jsonobject.accumulate("CONTACTNUMBER", contactnumber);
                jsonobject.accumulate("Message", message);
                jsonobject.accumulate("OCCUPATION",occupation);
                jsonobject.accumulate("QUALIFICATION", qualification);

                
       
            }else{
            

           jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "no message");
        }
        
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}
    @GET
    @Path("presentstaff")
    @Produces(MediaType.APPLICATION_JSON)
    public String presentstaffgetJson() throws SQLException {
        
        int user_id = 0;

        JSONObject presentstaff = new JSONObject();
        JSONArray presentarraystaff=new JSONArray();
        try {
            stm = setConnection();
            System.out.println("select CLOCK_STATUS,max(ATTENDENCE_TIMESTAMP),USER_ID from ATTENDENCE group by (USER_ID,CLOCK_STATUS)\n" +
"having ATTENDENCE.CLOCK_STATUS='IN' order by user_id desc");

            ResultSet rs2 = stm.executeQuery("select CLOCK_STATUS,max(ATTENDENCE_TIMESTAMP),USER_ID from ATTENDENCE group by (USER_ID,CLOCK_STATUS)\n" +
"having ATTENDENCE.CLOCK_STATUS='IN' order by user_id desc");
            jsonobject.accumulate("Status", "OK");

                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

                
            while(rs2.next()){
            

                user_id = rs2.getInt("USER_ID");
                

                //int time_attendence=rs2.getInt("ATTENDENCE_TIMESTAMP");
                
                
                
              //  jsonobject.accumulate("date", time_attendence);
              presentstaff.accumulate("USER_ID", user_id);
               
              presentarraystaff.add(presentstaff);
                presentstaff.clear();
       
            }if(user_id==0){
                
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO PRESENT STAFF");
        }
        jsonobject.accumulate("DATA",presentarraystaff);
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}   
    
    @GET
    @Path("userlist&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String presentstaffgetJson(@PathParam("user_id") int userId) throws SQLException {
        
        String fn,ln,contactnumber,qualification,occcupation,dateofbirth,emailId;
      try {
            stm = setConnection();
            System.out.println("SELECT * FROM USERS WHERE USER_ID="+userId);

            ResultSet rs2 = stm.executeQuery("SELECT * FROM USERS WHERE USER_ID="+userId);
            
            
            jsonobject.accumulate("Status", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

                if (rs2.next() == true) {
                    userId=rs2.getInt("USER_ID");
                    fn=rs2.getString("FIRSTNAME");
                    ln=rs2.getString("LASTNAME");
                    emailId=rs2.getString("EMAIL_ID");
                    qualification=rs2.getString("QUALIFICATION");
                    occcupation=rs2.getString("OCCUPATION");
                    contactnumber=rs2.getString("CONTACTNUMBER");
                    dateofbirth=rs2.getString("DATEOFBIRTH");
                    
                    jsonobject.accumulate("FIRSTNAME", fn);
                    jsonobject.accumulate("LASTNAME",ln);
                    jsonobject.accumulate("EMAIL_ID",emailId);
                    jsonobject.accumulate("QUALIFICATION",qualification);
                    jsonobject.accumulate("OCCUPATION",occcupation);
                    jsonobject.accumulate("DATEOFBIRTH", dateofbirth);
                    jsonobject.accumulate("CONTACTNUMBER",contactnumber);
                    jsonobject.accumulate("USER_ID",userId);
           }else{
                
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO PRESENT STAFF");
        }
      
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}   
    
    @GET
    @Path("userlist2&{user_id}&{appointment}")
    @Produces(MediaType.APPLICATION_JSON)
    public String appgetJson(@PathParam("user_id") int userId,@PathParam("appointment")int app_id) throws SQLException {
        
        String fn,ln,contactnumber,qualification,occcupation,dateofbirth,emailId,timedate;
      try {
            stm = setConnection();
            System.out.println("SELECT * FROM USERS WHERE USER_ID JOIN APPOINTMENTS ON USERS."+userId+"=APPOINTMENTS."+userId+" WHERE APPOINTMENTS.SENDERUSER_ID="+userId);

            ResultSet rs2 = stm.executeQuery("SELECT * FROM appointment full JOIN users ON USERS.user_id=APPOINTMENT.senderuser_id WHERE APPOINTMENT.SENDERUSER_ID="+userId+" AND APPOINTMENT.APPOINTMENT_ID="+app_id);
            
            
            jsonobject.accumulate("Status", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

                if (rs2.next()== true) {
                    userId=rs2.getInt("USER_ID");
                    fn=rs2.getString("FIRSTNAME");
                    ln=rs2.getString("LASTNAME");
                    emailId=rs2.getString("EMAIL_ID");
                    qualification=rs2.getString("QUALIFICATION");
                    occcupation=rs2.getString("OCCUPATION");
                    contactnumber=rs2.getString("CONTACTNUMBER");
                    dateofbirth=rs2.getString("DATEOFBIRTH");
                    timedate=rs2.getString("APPOINTMENT_TIME");
                    
                    jsonobject.accumulate("FIRSTNAME", fn);
                    jsonobject.accumulate("LASTNAME",ln);
                    jsonobject.accumulate("EMAIL_ID",emailId);
                    jsonobject.accumulate("QUALIFICATION",qualification);
                    jsonobject.accumulate("OCCUPATION",occcupation);
                    jsonobject.accumulate("DATEOFBIRTH", dateofbirth);
                    jsonobject.accumulate("CONTACTNUMBER",contactnumber);
                    jsonobject.accumulate("USER_ID",userId);
                    jsonobject.accumulate("APPOINTMENT_TIME",timedate);
           }else{
                
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO PRESENT STAFF");
        }
      
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}   
    
    
    
    @GET
    @Path("searchappointment&{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String searchappointmentgetJson(@PathParam("email") String the_email) throws SQLException {
        
        int user_id = 0;
        String stn_fn,stn_ln,stn_email,stn_occ;

        JSONObject presentstaff = new JSONObject();
        JSONArray presentarraystaff=new JSONArray();
        try {
            stm = setConnection();
            System.out.println("");
 
            
            if(the_email.contains("@")){
                System.out.println("Select firstname,email_id,user_id,lastname,occupation from "
                    + "users where email_id='"+the_email+"'");
            rs = stm.executeQuery("Select firstname,user_id,lastname,email_id,occupation from "
                    + "users where email_id='"+the_email+"'");
            }else{
                System.out.println("SELECT FIRSTNAME,USER_ID,LASTNAME,OCCUPATION FROM USERS"
                    + " WHERE FIRSTNAME='"+the_email+"'");
            rs=stm.executeQuery("SELECT FIRSTNAME,USER_ID,LASTNAME,EMAIL_ID,OCCUPATION FROM USERS"
                    + " WHERE FIRSTNAME='"+the_email+"'");
                
            }
            
            jsonobject.accumulate("Status", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

                
            while(rs.next()){
            stn_fn=rs.getString("FIRSTNAME");
            stn_ln=rs.getString("LASTNAME");
          
            stn_email=rs.getString("EMAIL_ID");
            stn_occ=rs.getString("OCCUPATION");
            user_id = rs.getInt("USER_ID");

              presentstaff.accumulate("USER_ID", user_id);
              presentstaff.accumulate("FIRSTNAME", stn_fn);
              presentstaff.accumulate("LASTNAME", stn_ln);
            
              presentstaff.accumulate("EMAIL_ID", stn_email);
              presentstaff.accumulate("OCCUPATION", stn_occ);
               
              presentarraystaff.add(presentstaff);
                presentstaff.clear();
       
            }if(user_id==0){
                
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO PRESENT STAFF");
        }
        jsonobject.accumulate("DATA",presentarraystaff);
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}   
    
    
    @GET
    @Path("setappointment&{appointment_time}&{sender_id}&{reciever_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String setappointmentgetJson(@PathParam("appointment_time") String theTime,@PathParam("sender_id")
    int the_sender_id,@PathParam("reciever_id") int the_reciever_id) throws SQLException {
        int appointment_id=0;
  
                  try {
            stm = setConnection();
  
                    ResultSet rs = stm.executeQuery("SELECT APPOINTMENT_ID FROM APPOINTMENT"
                            + " order by APPOINTMENT_ID DESC");
                    rs.next();

                    appointment_id = rs.getInt("APPOINTMENT_ID");
                    System.out.println("APPOINTMENT_ID OF THE APPOINTMENT IS " + appointment_id);
                    ++appointment_id;

                    System.out.println("INSERT INTO APPOINTMENT VALUES(" +appointment_id +",'to_timestamp('"+theTime+"','YYYY-MM-DD HH24:MI:SS:FF3')," + the_sender_id +","+the_reciever_id+ ")");
                    num = stm.executeUpdate("INSERT INTO APPOINTMENT VALUES(" + appointment_id +",to_timestamp('"+theTime+"','YYYY-MM-DD HH24:MI:SS:FF3')," + the_sender_id +","+the_reciever_id+ ")");
                    
                    System.out.println("total inserted rows" + num);
                if(num==1){            
                jsonobject.accumulate("STATUS", "OK");
                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsonobject.accumulate("Active", true);
                jsonobject.accumulate("Appointment_id", appointment_id);
                jsonobject.accumulate("Message", "You are successfully SET APPOINTMENT");
        
                }else if(num==0){
                    jsonobject.accumulate("STATUS", "WRONG");
                    jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsonobject.accumulate("Message", "date and time is wrong");
                }
                stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsonobject.accumulate("STATUS", "ERROR");
            jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsonobject.toString();
        }

    
    @GET
    @Path("senderappointment&{senderuser_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String senderappointmentgetJson(@PathParam("senderuser_id") int the_senderuserId) throws SQLException {
        
        int user_id = 0;
        String stn_fn,stn_ln,stn_email,stn_occ,stn_date;

        JSONObject presentstaff = new JSONObject();
        JSONArray presentarraystaff=new JSONArray();
        try {
            stm = setConnection();
            System.out.println("");
 
            
            
            
            jsonobject.accumulate("STATUS", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            System.out.println("SELECT FIRSTNAME,RECIEVERUSER_ID,LASTNAME,EMAIL_ID,OCCUPATION,APPOINTMENT_TIME FROM USERS "
                    + "JOIN APPOINTMENT ON USERS.USER_ID=APPOINTMENT.RECIEVERUSER_ID WHERE SENDERUSER_ID="+the_senderuserId);
            
            rs=stm.executeQuery("SELECT FIRSTNAME,RECIEVERUSER_ID,LASTNAME,EMAIL_ID,OCCUPATION,APPOINTMENT_TIME FROM USERS "
                    + "JOIN APPOINTMENT ON USERS.USER_ID=APPOINTMENT.RECIEVERUSER_ID WHERE SENDERUSER_ID="+the_senderuserId);
                
            while(rs.next()){
            stn_fn=rs.getString("FIRSTNAME");
            stn_ln=rs.getString("LASTNAME");
            stn_email=rs.getString("EMAIL_ID");
            stn_date=rs.getString("APPOINTMENT_TIME");
            stn_occ=rs.getString("OCCUPATION");
            user_id = rs.getInt("RECIEVERUSER_ID");

              presentstaff.accumulate("RECIEVERUSER_ID", user_id);
              presentstaff.accumulate("FIRSTNAME", stn_fn);
              presentstaff.accumulate("LASTNAME", stn_ln);
              presentstaff.accumulate("EMAIL_ID", stn_email);
              presentstaff.accumulate("APPOINTMENT_TIME",stn_date);
              presentstaff.accumulate("OCCUPATION", stn_occ);
               
              presentarraystaff.add(presentstaff);
                presentstaff.clear();
       
            }
            jsonobject.accumulate("DATA",presentarraystaff);
            if(user_id==0){
            jsonobject.clear();
            jsonobject.accumulate("STATUS", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO appointment");
        }
        
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}   
    
    
    @GET
    @Path("recieverappointment&{recieveruser_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String recieveappointmentgetJson(@PathParam("recieveruser_id") int the_senderuserId) throws SQLException {
        
        int user_id = 0,appointment_id;
        String stn_fn,stn_ln,stn_email,stn_occ,stn_date;

        JSONObject presentstaff = new JSONObject();
        JSONArray presentarraystaff=new JSONArray();
        try {
            stm = setConnection();
            System.out.println("");
 
            
            
            
            jsonobject.accumulate("STATUS", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            System.out.println("SELECT APPOINTMENT_ID,FIRSTNAME,SENDERUSER_ID,LASTNAME,EMAIL_ID,OCCUPATION,APPOINTMENT_TIME FROM USERS "
                    + "JOIN APPOINTMENT ON USERS.USER_ID=APPOINTMENT.SENDERUSER_ID WHERE RECIEVERUSER_ID="+the_senderuserId);
            
            rs=stm.executeQuery("SELECT APPOINTMENT_ID,FIRSTNAME,SENDERUSER_ID,LASTNAME,EMAIL_ID,OCCUPATION,APPOINTMENT_TIME FROM USERS "
                    + "JOIN APPOINTMENT ON USERS.USER_ID=APPOINTMENT.SENDERUSER_ID WHERE RECIEVERUSER_ID="+the_senderuserId);
                
            while(rs.next()){
            stn_fn=rs.getString("FIRSTNAME");
            stn_ln=rs.getString("LASTNAME");
            stn_email=rs.getString("EMAIL_ID");
            stn_date=rs.getString("APPOINTMENT_TIME");
            stn_occ=rs.getString("OCCUPATION");
            user_id = rs.getInt("SENDERUSER_ID");
            appointment_id=rs.getInt("APPOINTMENT_ID");

              presentstaff.accumulate("SENDERUSER_ID", user_id);
              presentstaff.accumulate("FIRSTNAME", stn_fn);
              presentstaff.accumulate("LASTNAME", stn_ln);
              presentstaff.accumulate("EMAIL_ID", stn_email);
              presentstaff.accumulate("APPOINTMENT_TIME",stn_date);
              presentstaff.accumulate("OCCUPATION", stn_occ);
               presentstaff.accumulate("APPOINTMENTID",appointment_id);
              presentarraystaff.add(presentstaff);
                presentstaff.clear();
       
            }        jsonobject.accumulate("DATA",presentarraystaff);

            if(user_id==0){
                jsonobject.clear();
            jsonobject.accumulate("STATUS", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO appointments");
        }
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}
    
    @GET
    @Path("enterschedule&{firstclassName}&{firstlabName}&{firstcoursename}&{secondclassname}&{secondlabname}&{secondcoursename}&{email}&{days}")
    @Produces(MediaType.APPLICATION_JSON)
    public String schedulegetJson(@PathParam("firstclassName") String firstclassname,
            @PathParam("firstlabName") String firstlabname,
            @PathParam("firstcoursename") String firstcoursename,
             @PathParam("secondclassname") String secondclassname,
             @PathParam("secondlabname") String secondlabname, 
             @PathParam("secondcoursename") String secondcoursename,
            @PathParam("email") String theemail,
            @PathParam("days") String thedays) throws SQLException {
        int userid = 0,schedule_id;
        String email_id = null;
        try {
            stm = setConnection();
            try {
                rs=stm.executeQuery("SELECT USER_ID FROM USERS WHERE EMAIL_ID='"+theemail+"'");
                if(rs.next()==true){                
                    userid=rs.getInt("USER_ID");
                    
                    ResultSet rs2 = stm.executeQuery("SELECT SCHEDULE_ID FROM SCHEDULES order by SCHEDULE_ID DESC");
                    rs2.next();

                     schedule_id= rs2.getInt("SCHEDULE_ID");
                   
                    System.out.println("SCHEDULE_ID OF THE SCHEDULE IS " + schedule_id);
                    ++schedule_id;

                    num = stm.executeUpdate("INSERT INTO SCHEDULES VALUES(+" + schedule_id + "," + "'" + firstclassname + "'" + "," + "'" + firstlabname + "','" + firstcoursename
                            + "'," + "'" + secondclassname + "','" + secondlabname + "','" + secondcoursename + "','" + thedays + "'" + "," + "" + userid + "" + ")");
                    System.out.println("INSERT INTO SCHEDULES VALUES(+" + schedule_id + "," + "'" + firstclassname + "'" + "," + "'" + firstlabname + "','" + firstcoursename
                            + "'," + "'" + secondclassname + "','" + secondlabname + "','" + secondcoursename + "','" + thedays + "'" + "," + "" + userid + "" + ")");
                    System.out.println("total inserted rows" + num);
                } else {

                    jsonobject.accumulate("STATUS", "WRONG");
                    jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsonobject.accumulate("Message", "something is already Register");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                userid =7000;
            }

            if (num == 1) {
                jsonobject.accumulate("STATUS", "OK");
                jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsonobject.accumulate("Active", true);
                jsonobject.accumulate("User_id", userid);
                jsonobject.accumulate("Message", "You are successfully Register");

            } else if (num == 0) {

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsonobject.accumulate("STATUS", "ERROR");
            jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsonobject.toString();
    }

    
        @GET
    @Path("recieveschedule&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String recieveschedulegetJson(@PathParam("user_id") int userId) throws SQLException {
        
        int user_id = 0;
        String firstclassname,firstcoursename,firstlabname,
                secondclassname,secondcoursename,secondlabname,thedays;

        JSONObject presentstaff = new JSONObject();
        JSONArray presentarraystaff=new JSONArray();
        try {
            stm = setConnection();
           
            rs = stm.executeQuery("SELECT USER_ID,FIRSTHALF,FIRSTHALFCOURSE,"
                    + "FIRSTHALFLAB,SECONDHALG,"
                    + "SECONDHALFCOURSE,SECONDHALFLAB,"
                    + "DAY_NAME FROM SCHEDULES JOIN "
                    + "DAYS ON SCHEDULES.DAYS_ID=DAYS.DAY_ID WHERE SCHEDULES.USER_ID="+userId);
 System.out.println("SELECT USER_ID,FIRSTHALF,FIRSTHALFCOURSE,"
                    + "FIRSTHALFLAB,SECONDHALG,"
                    + "SECONDHALFCOURSE,SECONDHALFLAB,"
                    + "DAY_NAME FROM SCHEDULES JOIN DAYS ON SCHEDULES.DAYS_ID=DAYS.DAY_ID WHERE SCHEDULES.USER_ID="+userId);
             jsonobject.accumulate("Status", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
               
            while(rs.next()){
           
                
                user_id = rs.getInt("USER_ID");
                firstclassname=rs.getString("FIRSTHALF");
                firstcoursename=rs.getString("FIRSTHALFCOURSE");
                firstlabname=rs.getString("FIRSTHALFLAB");
                secondclassname=rs.getString("SECONDHALG");
                secondcoursename=rs.getString("SECONDHALFCOURSE");
                secondlabname=rs.getString("SECONDHALFLAB");
                thedays=rs.getString("DAY_NAME").trim();
                        
               
           
                
              presentstaff.accumulate("USER_ID", user_id);
              presentstaff.accumulate("FIRSTCLASSNAME",firstclassname);
              presentstaff.accumulate("FIRSTCOURSENAME",firstcoursename);
              presentstaff.accumulate("FIRSTLABNAME",firstlabname);
              presentstaff.accumulate("SECONDCLASSNAME",secondclassname);
              presentstaff.accumulate("SECONDCOURSENAME",secondcoursename);
              presentstaff.accumulate("SECONDLABNAME",secondlabname);
              presentstaff.accumulate("DAYS", thedays);
              presentarraystaff.add(presentstaff);
              presentstaff.clear();
             
             }   jsonobject.accumulate("DATA", presentarraystaff);
        
             if(user_id==0){
             
                jsonobject.clear();
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO SCHEDULE");
        
             }
             
        }catch (SQLException ex){

    
        
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
} 
    
    
    
    @GET
    @Path("deleteschedule&{email_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String presentstaffgetJson(@PathParam("email_id") String emailId) throws SQLException {
        
        int user_id;
      try {
            stm = setConnection();
            System.out.println("SELECT USER_ID FROM USERS WHERE EMAIL_ID="+emailId);
            
            

            ResultSet rs2 = stm.executeQuery("SELECT USER_ID FROM USERS WHERE EMAIL_ID='"+emailId+"'");
            
            if(rs2.next()==true){
            user_id=rs2.getInt("USER_ID");
            System.out.println("DELETE FROM SCHEDULES WHERE USER_ID="+user_id);
                    
           num=stm.executeUpdate("DELETE FROM SCHEDULES WHERE USER_ID="+user_id);
            
           System.out.println("TOTAL DELETED ROWS ="+num);
            if (user_id!=0) {
                    
            jsonobject.accumulate("Status", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            jsonobject.accumulate("MESSAGE","SCHEDULE IS SUCCESSFULLY removed");
           }
            
                }else{
    
                    jsonobject.clear();
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO SCHEDULES FOR THIS USER");
        }
            
            
        }catch (SQLException ex){

    
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}
    
    @GET
    @Path("reply&{email_id}&{message}&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String replygetJson(@PathParam("email_id") String emailId,
            @PathParam("message") String message,@PathParam("user_id")int theuser_id)
            throws SQLException {
        
        int recieveruser_id,message_id;
      try {
            stm = setConnection();
            System.out.println("SELECT USER_ID FROM USERS WHERE EMAIL_ID="+emailId);
            
            

            ResultSet rs2 = stm.executeQuery("SELECT USER_ID FROM USERS WHERE EMAIL_ID='"+emailId+"'");
            
            if(rs2.next()==true){
                
            recieveruser_id=rs2.getInt("USER_ID");
           
                
                
                    
                    rs = stm.executeQuery("SELECT MESSAGE_ID FROM MESSAGE ORDER BY MESSAGE_ID DESC");
                    rs.next();
                    message_id = rs.getInt("MESSAGE_ID");

                    System.out.println("USERID OF THE USER IS " + recieveruser_id);
                    ++message_id;
          System.out.println("INSERT INTO MESSAGE VALUES(" + message_id + "," + "'" + message + "'," + theuser_id + ","
                            + recieveruser_id + ",CURRENT_TIMESTAMP");
                    num = stm.executeUpdate("INSERT INTO MESSAGE VALUES(" + message_id + "," + "'" + message + "'," + theuser_id + ","
                            + recieveruser_id + "," + "CURRENT_TIMESTAMP" + ")");

                    System.out.println("total inserted rows" + num);
            
            
            
                if (num==1) {
                    
            jsonobject.accumulate("Status", "OK");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            jsonobject.accumulate("MESSAGE","MESSAGE IS SUCCESSFULLY SEND");
           }}else{
    
                    jsonobject.clear();
            jsonobject.accumulate("Status", "WRONG");
            jsonobject.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            jsonobject.accumulate("Message", "THERE IS NO SCHEDULES FOR THIS USER");
        }
            
        }catch (SQLException ex){

    
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        jsonobject.clear();
        jsonobject.accumulate("STATUS", "ERROR");
        jsonobject.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
        jsonobject.accumulate("MESSAGE", "Database connectivity error");

    }

    return jsonobject.toString ();
}
}
