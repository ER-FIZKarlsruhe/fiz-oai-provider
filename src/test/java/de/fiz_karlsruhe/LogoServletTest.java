/*
 * Copyright 2026 FIZ Karlsruhe - Leibniz-Institut fuer Informationsinfrastruktur GmbH
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

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.fiz_karlsruhe.service.ConfigurationService;

public class LogoServletTest {

  private static class CapturingServletOutputStream extends ServletOutputStream {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    @Override
    public void write(int b) {
      buffer.write(b);
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
      // not needed for this synchronous test
    }

    byte[] toByteArray() {
      return buffer.toByteArray();
    }
  }

  @Before
  @After
  public void resetConfigurationServiceSingleton() throws Exception {
    Field instance = ConfigurationService.class.getDeclaredField("INSTANCE");
    instance.setAccessible(true);
    instance.set(null, null);

    Field properties = ConfigurationService.class.getDeclaredField("properties");
    properties.setAccessible(true);
    properties.set(null, null);
  }

  @Test
  public void doGetStreamsConfiguredLogoFileToResponse() throws IOException {
    byte[] logoBytes = "fake-jpeg-bytes".getBytes(StandardCharsets.UTF_8);
    File logoFile = File.createTempFile("logo", ".jpg");
    logoFile.deleteOnExit();
    Files.write(logoFile.toPath(), logoBytes);

    Properties properties = new Properties();
    properties.setProperty("branding.logo", logoFile.getAbsolutePath());
    ConfigurationService.getInstance(properties);

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    CapturingServletOutputStream out = new CapturingServletOutputStream();
    when(response.getOutputStream()).thenReturn(out);

    new LogoServlet().doGet(request, response);

    verify(response).setContentType("image/jpeg");
    assertArrayEquals(logoBytes, out.toByteArray());

    logoFile.delete();
  }
}
