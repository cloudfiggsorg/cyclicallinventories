package com.gmodelo.beans.test;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		Response<ArrayList<String>> resp = new Response<>();
//		
//		AbstractResults asdasd = new AbstractResults();
//		asdasd.setResultId(1);
//		asdasd.setResultMsgAbs("");
//		resp.setAbstractResult(asdasd);
//
//	}
	private static final Logger LOGGER = Logger.getLogger( Test.class.getName() );
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String pattern = "^ROLE_INV_BK_?_WS_?$";
		pattern = pattern.replace("?", "([A-Za-z0-9]+)");		
		Pattern patron = Pattern.compile(pattern);
		
		String role = "ROLE_INV_BK_23P2_WS_we324";
		
		Matcher m = patron.matcher(role);
		
		if(m.matches()){
			System.out.println(m.group(1));
			System.out.println(m.group(2));
		}
				

	}

}
