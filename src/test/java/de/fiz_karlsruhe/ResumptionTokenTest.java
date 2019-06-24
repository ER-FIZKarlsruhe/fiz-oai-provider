package de.fiz_karlsruhe;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import ORG.oclc.oai.server.verb.BadResumptionTokenException;
import de.fiz_karlsruhe.model.ResumptionToken;

public class ResumptionTokenTest {

  @Test
  public void testDefaultConstruktor() throws Exception {
    try {
      ResumptionToken token = new ResumptionToken();
      token.setFrom("2018-06-24T08:25:30z");
      token.setUntil("2019-06-23T08:25:30z");
      token.setOffset(20000000);
      token.setRows(200);
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      System.out.println(tokenString);
    } catch (BadResumptionTokenException e) {
      Assert.fail("BadResumptionTokenException not expected");
    }
  }

  @Test
  public void testBadResumptionWithDefaultConstruktor() throws Exception {
    try {
      ResumptionToken token = new ResumptionToken();
      token.setOffset(20000000);
      token.setRows(null);//Must not be null!
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setOffset(null);//Must not be null!
      token.setRows(200);
      token.setTotal(-50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setOffset(null);//Must not be null!
      token.setRows(200);
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setFrom("2018-06-24T08:25:30z");
      token.setUntil("2019-06-23T08:25:30z");
      token.setOffset(20000000);
      token.setRows(-200);//Must not be negative!
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setFrom("2018-06-24T08:25:30z");
      token.setUntil("2019-06-23T08:25:30z");
      token.setOffset(20000000);//Must not be negative!
      token.setRows(200);
      token.setTotal(-50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setFrom("2018-06-24T08:25:30z");
      token.setUntil("2019-06-23T08:25:30z");
      token.setOffset(-20000000);//Must not be negative!
      token.setRows(200);
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setFrom("Hallo");//invalid!
      token.setUntil("2019-06-23T08:25:30z");
      token.setOffset(20000000);
      token.setRows(200);
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setFrom("2018-06-24T08:25:30z");
      token.setUntil("2019-16-23T08:25:30z");//invalid!
      token.setOffset(20000000);
      token.setRows(200);
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setUntil("2018-06-24T08:25:30z");
      token.setFrom("2019-11-23T08:25:30z");//from > until is invalid!
      token.setOffset(20000000);
      token.setRows(200);
      token.setTotal(50000000);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
  }
  

  @Test
  public void testTokenConstruktor() throws Exception {
    try {
      final String token = "total=50000000&offset=20000000&rows=200&set=ABC&from=2018-06-24T08:25:30z&until=2019-06-23T08:25:30z&metadataPrefix=sd";
      ResumptionToken tokenObj = new ResumptionToken(token);
      
      String tokenString = tokenObj.getToken();
      assertEquals(tokenString, tokenString);
    } catch (BadResumptionTokenException e) {
      Assert.fail("BadResumptionTokenException not expected");
    }
  }
  
  
  
  @Test
  public void testBadResumptionTokenException() throws Exception {
    try {
      ResumptionToken token = new ResumptionToken(null);
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      // TODO: handle exception
    }

    try {
      ResumptionToken token = new ResumptionToken("");
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      // TODO: handle exception
    }

    try {
      ResumptionToken token = new ResumptionToken("asdasdyxc");
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      // TODO: handle exception
    }

    try {
      ResumptionToken token = new ResumptionToken("set");
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
    }

  }

}
