/*
 * Copyright 2019 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(200);
      token.setTotal(50000000l);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      token.getToken();
    } catch (BadResumptionTokenException e) {
      Assert.fail("BadResumptionTokenException not expected");
    }
  }

  @Test
  public void testBadResumptionWithDefaultConstruktor() throws Exception {
    try {
      ResumptionToken token = new ResumptionToken();
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(null);//Must not be null!
      token.setTotal(50000000l);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setSearchMark(null);//Must not be null!
      token.setRows(200);
      token.setTotal(-50000000l);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
    try {
      ResumptionToken token = new ResumptionToken();
      token.setSearchMark(null);//Must not be null!
      token.setRows(200);
      token.setTotal(50000000l);
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
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(-200);//Must not be negative!
      token.setTotal(50000000l);
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
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(200);
      token.setTotal(-50000000l);
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
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(200);
      token.setTotal(50000000l);
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
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(200);
      token.setTotal(50000000l);
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
      token.setSearchMark("jkhskajhdkasjd");
      token.setRows(200);
      token.setTotal(50000000l);
      token.setSet("ABC");
      token.setMetadataPrefix("sd");
      
      String tokenString = token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //
    }
    
  }
  

  @Test
  public void testDateOnlyGranularityFromAndUntilAreAccepted() throws Exception {
    // AbstractCatalog.granularity=YYYY-MM-DD (this project's documented default) makes
    // AbstractCatalog pass plain "YYYY-MM-DD" from/until values, with no time component,
    // into the resumption token. Instant.parse alone rejects those; without the LocalDate
    // fallback, any harvest needing pagination under date-only granularity would fail with
    // BadResumptionTokenException instead of returning a token.
    ResumptionToken token = new ResumptionToken();
    token.setFrom("0001-01-01");
    token.setUntil("9999-12-31");
    token.setSearchMark("jkhskajhdkasjd");
    token.setRows(200);
    token.setTotal(50000000L);
    token.setSet("ABC");
    token.setMetadataPrefix("sd");

    String tokenString = token.getToken();

    ResumptionToken roundTripped = new ResumptionToken(tokenString);
    assertEquals("0001-01-01", roundTripped.getFrom());
    assertEquals("9999-12-31", roundTripped.getUntil());
  }

  @Test
  public void testDateOnlyGranularityStillRejectsFromAfterUntil() throws Exception {
    ResumptionToken token = new ResumptionToken();
    token.setFrom("2020-06-01");
    token.setUntil("2020-01-01"); // from > until is invalid, even for plain dates
    token.setSearchMark("jkhskajhdkasjd");
    token.setRows(200);
    token.setTotal(50000000L);

    try {
      token.getToken();
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      // expected
    }
  }

  @Test
  public void testTokenConstruktor() throws Exception {
    try {
      final String token = "total=50000000@@searchMark=lkjasldjadaw@@rows=200@@set=ABC@@from=2018-06-24T08:25:30z@@until=2019-06-23T08:25:30z@@metadataPrefix=sd";
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
      //Expected exception
    }

    try {
      ResumptionToken token = new ResumptionToken("");
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //Expected exception
    }

    try {
      ResumptionToken token = new ResumptionToken("asdasdyxc");
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //Expected exception
    }

    try {
      ResumptionToken token = new ResumptionToken("set");
      Assert.fail("BadResumptionTokenException expected");
    } catch (BadResumptionTokenException e) {
      //Expected exception
    }

  }

}
