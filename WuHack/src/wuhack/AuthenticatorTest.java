/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author Julius
 */
public class AuthenticatorTest extends Authenticator
{
  String user = "baujuc12";
  String password = "fwpts5fz";

  @Override
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(user, password.toCharArray());
  } 
  
}
