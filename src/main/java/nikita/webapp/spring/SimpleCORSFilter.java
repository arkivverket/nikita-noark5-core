package nikita.webapp.spring;

import nikita.common.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;

@Component
@Order(Integer.MIN_VALUE)
public class SimpleCORSFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter init");
    }

    public String stringJoin(Object[] collection, String separator)
    {
       if (collection == null) {
           return null;
       }
       StringBuilder builder = new StringBuilder();
       for (int i = 0; i < collection.length; i++) {
           if (i > 0) {
               builder.append(separator);
           }
           if (collection[i] != null) {
               builder.append(collection[i]);
           }
       }
       return builder.toString();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Make sure this is a request for something that exists. If it's not just pass it on in the filter
        HttpMethod[] allowMethods = CommonUtils.WebUtils.getMethodsForRequest(request.getServletPath());
        if (allowMethods != null) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", stringJoin(allowMethods, ","));
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type," +
                    " Accept, X-Requested-With, remember-me, Authorization, " +
                    "Origin, ETAG, grant_type, X-File-Name, Allow");
            response.setHeader("Access-Control-Expose-Headers", "Allow, ETAG");
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            String headerContents = getMethodsAsString(
                    getMethodsForRequestOrThrow(
                            request.getServletPath()));
            response.setHeader("Allow", headerContents);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    private String getMethodsAsString(HttpMethod[] httpMethods) {

        StringBuilder methodsAsString = new StringBuilder();
        for (HttpMethod httpMethod : httpMethods) {
            methodsAsString.append(httpMethod.toString());
            methodsAsString.append(",");
        }
        methodsAsString.delete(
                methodsAsString.lastIndexOf(","),
                methodsAsString.length());
        return methodsAsString.toString();
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
