package ch.so.agi.gb2av;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.processor.idempotent.jdbc.JdbcMessageIdRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.agroal.api.AgroalDataSource;

@ApplicationScoped
public class MyJdbcMessageIdRepository {

    @Inject
    AgroalDataSource dataSource;

    @Inject
    CamelContext camelContext;

    @ConfigProperty(name = "app.dbSchema")
    private String dbSchema;

    public JdbcMessageIdRepository jdbcConsumerRepo() {
        String tableExistsString = "SELECT 1 FROM "+dbSchema+".CAMEL_MESSAGEPROCESSED WHERE 1 = 0";
        String queryString = "SELECT COUNT(*) FROM "+dbSchema+".CAMEL_MESSAGEPROCESSED WHERE processorName = ? AND messageId = ?";
        String insertString = "INSERT INTO "+dbSchema+".CAMEL_MESSAGEPROCESSED (processorName, messageId, createdAt) VALUES (?, ?, ?)";
        String deleteString = "DELETE FROM "+dbSchema+".CAMEL_MESSAGEPROCESSED WHERE processorName = ? AND messageId = ?";
        String clearString = "DELETE FROM "+dbSchema+".CAMEL_MESSAGEPROCESSED WHERE processorName = ?";
        
        JdbcMessageIdRepository jdbcConsumerRepo = null;
        jdbcConsumerRepo = new JdbcMessageIdRepository(dataSource, "gb2av");
        jdbcConsumerRepo.setCreateTableIfNotExists(false);
        jdbcConsumerRepo.setTableExistsString(tableExistsString);
        jdbcConsumerRepo.setQueryString(queryString);
        jdbcConsumerRepo.setInsertString(insertString);
        jdbcConsumerRepo.setDeleteString(deleteString);
        jdbcConsumerRepo.setClearString(clearString);
        
        return jdbcConsumerRepo;
    }

    
}
