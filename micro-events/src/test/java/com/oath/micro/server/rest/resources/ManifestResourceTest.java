package com.oath.micro.server.rest.resources;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

public class ManifestResourceTest {
	ManifestResource resource;

	ServletContext context;
	MockAsyncResponse<Map<String, String>> response;
	@Before
	public void setUp() throws Exception {
		context = mock(ServletContext.class);
		response = new MockAsyncResponse<>();
		resource = new ManifestResource();

	}

	@Test
	public void testMainfest() {
		when(context.getResourceAsStream(any(String.class))).thenReturn(
				new ByteArrayInputStream(manifest
						.getBytes()));
		resource.mainfest(response,context);
		Map<String, String> manifest =response.response() ;
		assertThat(manifest.get("Implementation-Build"), is("281837"));

	}

	private String manifest = "Manifest-Version: 1.0\n"
			+ "Archiver-Version: Plexus Archiver\n" 
			+ "Created-By: Apache Maven\n"
			+ "Implementation-Build: 281837\n";

}
