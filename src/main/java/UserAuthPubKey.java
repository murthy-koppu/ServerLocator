/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the user authentification by public key.
 *   $ CLASSPATH=.:../build javac UserAuthPubKey.java
 *   $ CLASSPATH=.:../build java UserAuthPubKey
 * You will be asked username, hostname, privatekey(id_dsa) and passphrase. 
 * If everything works fine, you will get the shell prompt
 *
 */
import com.jcraft.jsch.*;

import java.awt.*;
import java.io.InputStream;

import javax.swing.*;

public class UserAuthPubKey{
  public static void main(String[] arg){

    try{
      JSch jsch=new JSch();
      //JFileChooser chooser = new JFileChooser();
      jsch.addIdentity("F:\\studies\\AWS\\ALinxLearnInstance_Ec2\\EC2LearnKeyPair.pem");
      /*JFileChooser chooser = new JFileChooser();
      chooser.setDialogTitle("Choose your privatekey(ex. ~/.ssh/id_dsa)");
      chooser.setFileHidingEnabled(false);
      int returnVal = chooser.showOpenDialog(null);
      if(returnVal == JFileChooser.APPROVE_OPTION) {
        System.out.println("You chose "+
			   chooser.getSelectedFile().getAbsolutePath()+".");
        System.out.println("You chose F:\\studies\\AWS\\ALinxLearnInstance_Ec2\\EC2LearnKeyPair.pem");
       // jsch.addIdentity("F:\\studies\\AWS\\ALinxLearnInstance_Ec2\\EC2LearnKeyPair.pem.");
      }*/

   /*   String host=null;
      if(arg.length>0){
        host=arg[0];
      }
      else{
        host=JOptionPane.showInputDialog("Enter ec2-user@ec2-50-19-178-220.compute-1.amazonaws.com",
                                         System.getProperty("user.name")+
                                         "@localhost"); 
      }*/
      String user="ec2-user";
      String host="ec2-50-19-178-220.compute-1.amazonaws.com";

      Session session=jsch.getSession(user, host, 22);

      // username and passphrase will be given via UserInfo interface.
      UserInfo ui=new MyUserInfo();
      session.setUserInfo(ui);
      session.connect();

 /*     Channel channel=session.openChannel("shell");
      System.out.println("ls > 23432423_asdfa.log");
      channel.setInputStream(System.in);
      channel.setOutputStream(System.out);


      channel.connect();*/
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setPty(true);
/*      ((ChannelExec)channel).setCommand("sh");
      channel.connect();*/
      ((ChannelExec)channel).setCommand("/sbin/ifconfig | grep 'inet addr' | awk -F\":\" '{print $2}' | awk 'BEGIN {ORS = \";\"} {print $1}'");
     // ((ChannelExec)channel).setCommand("visudo netstat -tnap | awk 'BEGIN {OFS=\",\"; ORS= \";\"} NR > 2 {print $4,$5,$6,$7}'");
      //((ChannelExec)channel).setCommand("/sbin/ifconfig ");
      // X Forwarding
      // channel.setXForwarding(true);

      //channel.setInputStream(System.in);
      channel.setInputStream(null);
      channel.setOutputStream(System.out);
     
      //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
      //((ChannelExec)channel).setErrStream(fos);
      ((ChannelExec)channel).setErrStream(System.err);

      InputStream in=channel.getInputStream();

      channel.connect();

      byte[] tmp=new byte[1024];
      while(true){
        while(in.available()>0){
          int i=in.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          break;
        }
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
      channel=session.openChannel("exec");
      ((ChannelExec)channel).setPty(true);
      ((ChannelExec)channel).setCommand("sudo netstat -tnap | awk 'BEGIN {OFS=\",\"; ORS= \";\"} NR > 2 {print $4,$5,$6,$7}'");
      in=channel.getInputStream();
      channel.connect();
      while(true){
          while(in.available()>0){
            int i=in.read(tmp, 0, 1024);
            if(i<0)break;
            System.out.print(new String(tmp, 0, i));
          }
          if(channel.isClosed()){
            break;
          }
          try{Thread.sleep(1000);}catch(Exception ee){}
        }
      channel.disconnect();
      session.disconnect();
    }
    catch(Exception e){
    	e.printStackTrace();
      System.out.println(e);
    }
  }


  public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return null; }
    public boolean promptYesNo(String str){
     /* Object[] options={ "yes", "no" };
      int foo=JOptionPane.showOptionDialog(null, 
             str,
             "Warning", 
             JOptionPane.DEFAULT_OPTION, 
             JOptionPane.WARNING_MESSAGE,
             null, options, options[0]);*/
       return true;
    }
  
    String passphrase;
    JTextField passphraseField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return passphrase; }
    public boolean promptPassphrase(String message){
      Object[] ob={passphraseField};
      int result=
	JOptionPane.showConfirmDialog(null, ob, message,
				      JOptionPane.OK_CANCEL_OPTION);
      if(result==JOptionPane.OK_OPTION){
        passphrase=passphraseField.getText();
        return true;
      }
      else{ return false; }
    }
    public boolean promptPassword(String message){ return true; }
    public void showMessage(String message){
      JOptionPane.showMessageDialog(null, message);
    }
    final GridBagConstraints gbc = 
      new GridBagConstraints(0,0,1,1,1,1,
                             GridBagConstraints.NORTHWEST,
                             GridBagConstraints.NONE,
                             new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      panel = new JPanel();
      panel.setLayout(new GridBagLayout());

      gbc.weightx = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.gridx = 0;
      panel.add(new JLabel(instruction), gbc);
      gbc.gridy++;

      gbc.gridwidth = GridBagConstraints.RELATIVE;

      JTextField[] texts=new JTextField[prompt.length];
      for(int i=0; i<prompt.length; i++){
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(new JLabel(prompt[i]),gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        if(echo[i]){
          texts[i]=new JTextField(20);
        }
        else{
          texts[i]=new JPasswordField(20);
        }
        panel.add(texts[i], gbc);
        gbc.gridy++;
      }

      if(JOptionPane.showConfirmDialog(null, panel, 
                                       destination+": "+name,
                                       JOptionPane.OK_CANCEL_OPTION,
                                       JOptionPane.QUESTION_MESSAGE)
         ==JOptionPane.OK_OPTION){
        String[] response=new String[prompt.length];
        for(int i=0; i<prompt.length; i++){
          response[i]=texts[i].getText();
        }
	return response;
      }
      else{
        return null;  // cancel
      }
    }
  }
}