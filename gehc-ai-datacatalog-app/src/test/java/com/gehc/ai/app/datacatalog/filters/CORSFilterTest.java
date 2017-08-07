package com.gehc.ai.app.datacatalog.filters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sowjanyanaidu on 8/7/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
public class CORSFilterTest {
    @Test
    public void testDoFilter() throws IOException, ServletException {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(httpServletRequest.getMethod()).thenReturn("OPTIONS");

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.doFilter(httpServletRequest, httpServletResponse,
                filterChain);
       verify(httpServletResponse).containsHeader("Access-Control-Allow-Methods");
       verify(httpServletResponse).containsHeader("Access-Control-Allow-Headers");
       verify(httpServletResponse).containsHeader("Access-Control-Max-Age");

    }
}

