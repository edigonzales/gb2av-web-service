package ch.so.agi.gb2av;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@Path("/hello")
public class GreetingResource {

    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    @ConfigProperty(name = "app.ftpUserInfogrips")
    private String ftpUserInfogrips;

    @ConfigProperty(name = "app.ftpPwdInfogrips")
    private String ftpPwdInfogrips;

    @ConfigProperty(name = "app.ftpUrlInfogrips", defaultValue = "fubar")
    private String ftpUrlInfogrips;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        System.out.println(ftpPwdInfogrips);
        return "Hello RESTEasy";
    }
}