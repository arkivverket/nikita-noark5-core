package nikita.webapp.spring.wsdl;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig
        extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "wsGI")
    public SimpleWsdl11Definition defaultWsdl11Definition() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("geointegrasjon/1.1/giArkivOppdatering20120131.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "giArkivKjerne20120131")
    public XsdSchema getGIArkivKjerne20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giArkivKjerne20120131.xsd"));
    }

    @Bean(name = "giFellesGeometri20120131")
    public XsdSchema getGIFellesGeometri20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giFellesGeometri20120131.xsd"));
    }

    @Bean(name = "giFellesTeknisk20120131")
    public XsdSchema getgiFellesTeknisk20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giFellesTeknisk20120131.xsd"));
    }

    @Bean(name = "giArkivDokument20120131")
    public XsdSchema getGIArkivDokument20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giArkivDokument20120131.xsd"));
    }

    @Bean(name = "giFellesAdresse20120131")
    public XsdSchema getGIFellesAdresse20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giFellesAdresse20120131.xsd"));
    }

    @Bean(name = "giFellesKodeliste20120131")
    public XsdSchema getFellesKodeliste20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giFellesKodeliste20120131.xsd"));
    }

    @Bean(name = "giMatrikkelFelles20120131")
    public XsdSchema getGIMatrikkelFelles20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giMatrikkelFelles20120131.xsd"));
    }

    @Bean(name = "giArkivFelles20120131")
    public XsdSchema getGIArkivFelles20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giArkivFelles20120131.xsd"));
    }

    @Bean(name = "giFellesFilter20120131")
    public XsdSchema getGIFellesFilter20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giFellesFilter20120131.xsd"));
    }

    @Bean(name = "giFellesKontakt20120131")
    public XsdSchema getGIFellesKontakt20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giFellesKontakt20120131.xsd"));
    }

    @Bean(name = "giPlanFelles20120131")
    public XsdSchema getGIPlanFelles20120131Xsd() {
        return new SimpleXsdSchema(
                new ClassPathResource("geointegrasjon/1.1/giPlanFelles20120131.xsd"));
    }
}
