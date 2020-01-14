package pl.olawa.telech.tcm.config.filter;

import java.io.*;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.Getter;

/*
 * Wrapper dla requestu niezbędny do kilkukrotnego odczytana zawartosci body. 
 * Rest controller odczytuje i parsuje body, a następnie zamyka strumień, co nie pozwalana doczytanie jego w AppLogAspect.
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	@Getter
	private final String body;

	
	public RequestWrapper(HttpServletRequest request) throws IOException {
		super(request);

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} 
		} 
		catch (IOException e) {
			throw e;
		} 
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} 
				catch (IOException e) {
					throw e;
				}
			}
		}

		body = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
		ServletInputStream servletInputStream = new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener arg0) {

			}
		};
		return servletInputStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
}
