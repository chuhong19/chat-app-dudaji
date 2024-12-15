package vn.giabaochatapp.giabaochatappserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    @Value("${vn.giabaochatapp.giabaochatappserver.contact-email}")
    private String contactEmail;

    @Value("${vn.giabaochatapp.giabaochatappserver.contact-name}")
    private String contactName;

    @Value("${vn.giabaochatapp.giabaochatappserver.contact-url}")
    private String contactUrl;

    @Value("${vn.giabaochatapp.giabaochatappserver.name}")
    private String name;

    @Value("${vn.giabaochatapp.giabaochatappserver.description}")
    private String description;

    @Value("${vn.giabaochatapp.giabaochatappserver.version}")
    private String version;

    @Value("${vn.giabaochatapp.giabaochatappserver.termsOfService}")
    private String termsOfService;

    @Value("${vn.giabaochatapp.giabaochatappserver.licenseName}")
    private String licenseName;

    @Value("${vn.giabaochatapp.giabaochatappserver.licenseUrl}")
    private String licenseUrl;

    @Value("${vn.giabaochatapp.giabaochatappserver.dev-url}")
    private String devUrl;

    @Value("${vn.giabaochatapp.giabaochatappserver.staging-url}")
    private String stagingUrl;

    @Value("${vn.giabaochatapp.giabaochatappserver.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server stagingServer = new Server();
        stagingServer.setUrl(stagingUrl);
        stagingServer.setDescription("Server URL in Staging environment");

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail(contactEmail);
        contact.setName(contactName);
        contact.setUrl(contactUrl);

        License mitLicense = new License().name(licenseName).url(licenseUrl);

        Info info = new Info().title(name)
                .version(version)
                .contact(contact)
                .description(description)
                .termsOfService(termsOfService)
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(stagingServer, devServer, prodServer));
    }
}
