package edu.ucsf.tools.reminder;

import java.util.*;
import java.io.Serializable;


public interface Event extends Serializable
{
    public boolean isHtml();

    public Date getDate();

    public String getLocation();

    public String getEmailAddress();

    public String getEmailSubject();
    
    public String getEmailText();

}