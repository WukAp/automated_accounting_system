package com.github.wukap.automatedAccountingSystem.driver.opcDriver;

import com.github.wukap.automatedAccountingSystem.driver.Driver;
import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * OPC UA driver implementation
 *
 * @author Yuriy Golubev
 */
@Getter
@Slf4j
@Component
public class OpcUaDriver extends Driver<OpcValue, Object> {
    private final ConnectionFactoryImpl connectionFactory;

    @Autowired
    public OpcUaDriver(OpcUaDriver.OpcUaServerConnectionInfo connectionInfo, @Value("${opc.db.connection.timeout}") int connectionTimeout) {
        this.connectionFactory = new ConnectionFactoryImpl(connectionTimeout, connectionInfo);
    }

    public List<OpcValue> readNodes(List<String> nodesToRead) throws ServiceResultException {
        List<OpcValue> values = new ArrayList<>();
        for (String node : nodesToRead) {
            values.add(read(node));
        }
        return values;
    }

    @Override
    public OpcValue read(String tagName) throws ServiceResultException {
        try {
            String opcUaTagName = getConnectionFactory().getActiveConnectionInfo().getServerPrefix() + tagName;
            var readValue = new ReadValueId(NodeId.get(IdType.String, getConnectionFactory().getActiveConnectionInfo().getNamespace(), opcUaTagName), Attributes.Value, null, null);
            ReadRequest req = new ReadRequest(null, 0.0, TimestampsToReturn.Both, List.of(readValue).toArray(new ReadValueId[0]));
            DataValue res = connectionFactory.getActiveConnection().Read(req).getResults()[0];
            if (res.isNull()) return null;
            Double value = Double.valueOf(res.getValue().toString());
            Instant sourceTime = Instant.ofEpochMilli(res.getSourceTimestamp().getTimeInMillis());
            OpcValue.Quality quality = res.getStatusCode().isGood() ? OpcValue.Quality.GOOD : OpcValue.Quality.BAD;
            return new OpcValue(sourceTime, value, quality);
        } catch (ServiceResultException e) {
            log.error("Can't read from OPC UA server", e);
            connectionFactory.closeConnection();
            throw e;
        } catch (Exception e) {
            log.error("Can't read from OPC UA server", e);
            throw e;
        }
    }

    @Override
    protected boolean write_(Object value) {
        throw new UnsupportedOperationException();
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OpcUaServerConnectionInfo {
        private String connectionUrl;
        private int namespace;
        private String serverPrefix = "";
    }

}
