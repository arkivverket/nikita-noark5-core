package nikita.webapp.spring;

import nikita.webapp.web.interceptor.NikitaETAGInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

@Component
public class AppWebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NikitaETAGInterceptor());
    }

}
