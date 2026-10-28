import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Example Servlet Filter that logs incoming requests, measures execution time,
 * and demonstrates lifecycle methods and init parameters.
 */
public class LoggingFilter implements Filter {

    private String logLevel;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Retrieve initialization parameters defined in web.xml
        this.logLevel = filterConfig.getInitParameter("logLevel");
        if (this.logLevel == null) {
            this.logLevel = "INFO";
        }
        System.out.println("[LoggingFilter] Initialized with logLevel: " + logLevel);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();

        // 1. Pre-processing before the servlet or next filter is invoked
        System.out.println(String.format("[LoggingFilter] [%s] Incoming Request -> %s %s from IP: %s",
                logLevel, method, uri, request.getRemoteAddr()));

        // Optional: set UTF-8 character encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 2. Pass request and response down the filter chain to servlet or next filter
        chain.doFilter(request, response);

        // 3. Post-processing after servlet execution
        long duration = System.currentTimeMillis() - startTime;
        System.out.println(String.format("[LoggingFilter] [%s] Completed Request <- %s %s [Status: %d] (%d ms)",
                logLevel, method, uri, httpResponse.getStatus(), duration));
    }

    @Override
    public void destroy() {
        System.out.println("[LoggingFilter] Destroyed.");
    }
}

