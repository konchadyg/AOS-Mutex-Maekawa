//========================================================
//|     CS6378 - PROJECT 2                               |
//|     Member 1: Piyush Makani [PXM140430]              |
//|     Member 2: Konchady Gaurav Shenoy [KXS168430]     |
//|     Instructor: Dr. Neeraj Mittal                    |
//|     Oct 2016                                         |
//|     The University of Texas at Dallas                |
//========================================================


import java.io.Serializable;
public class Message implements Comparable<Object>, Serializable {
	private static final long serialVersionUID = 5950169519310163575L;
	boolean request = false;
	boolean grant = false;
	boolean release = false;
	boolean inquire = false;
	boolean yield = false;
	boolean failed = false;
	int timestamp = 0;
	int originator;

	public int compareTo(Object anotherMessage) throws ClassCastException 
	{
	    if (!(anotherMessage instanceof Message)) throw new ClassCastException("A Person object expected.");
	    int anotherMessageTime = ((Message) anotherMessage).timestamp;  
	    return this.timestamp - anotherMessageTime;    
	}
	
}
