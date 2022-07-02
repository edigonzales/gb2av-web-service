package ch.so.agi.gb2av;

import java.nio.ByteBuffer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Gb2avRoute extends RouteBuilder {
    
    @ConfigProperty(name = "app.ftpUserInfogrips")
    private String ftpUserInfogrips;

    @ConfigProperty(name = "app.ftpPwdInfogrips")
    private String ftpPwdInfogrips;
    
    @ConfigProperty(name = "app.ftpUrlInfogrips", defaultValue = "fubar")
    private String ftpUrlInfogrips;

    @ConfigProperty(name = "app.pathToDownloadFolder")
    private String pathToDownloadFolder;

    @ConfigProperty(name = "app.pathToUnzipFolder")
    private String pathToUnzipFolder;
    
    @ConfigProperty(name = "app.pathToErrorFolder")
    private String pathToErrorFolder;

    @ConfigProperty(name = "app.downloadDelayGb2Av")
    private String downloadDelayGb2Av;
    
    @ConfigProperty(name = "app.downloadDelayAv2Gb")
    private String downloadDelayAv2Gb;    

    @ConfigProperty(name = "app.uploadDelay")
    private String uploadDelay;
    
    @ConfigProperty(name = "app.importDelay")
    private String importDelay;    
    
    @ConfigProperty(name = "app.initialDownloadDelay")
    private String initialDownloadDelay;

    @ConfigProperty(name = "app.initialUploadDelay")
    private String initialUploadDelay;

    @ConfigProperty(name = "app.initialImportDelay")
    private String initialImportDelay;

    @Inject
    MyJdbcMessageIdRepository myJdbcMessageIdRepository;

    @Override
    public void configure() throws Exception {
        // Download Vollzugsmeldungen from Infogrips FTP.
        from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\gb2av\\?password="+ftpPwdInfogrips+"&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&readLock=changed&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay="+downloadDelayGb2Av+"&initialDelay="+initialDownloadDelay)
        .idempotentConsumer(simple("ftp-${file:name}"), myJdbcMessageIdRepository.jdbcConsumerRepo())
        .routeId("*downloadVollzugsmeldung*")
        .to("file://"+pathToDownloadFolder)
        .split(new ZipSplitter())
        .streaming().convertBodyTo(ByteBuffer.class)
            .choice()
                .when(body().isNotNull())
                    .to("file://"+pathToUnzipFolder) 
            .end()
        .end();   
    }

}
