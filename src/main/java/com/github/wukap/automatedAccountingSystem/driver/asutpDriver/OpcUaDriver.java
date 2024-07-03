package com.github.wukap.automatedAccountingSystem.driver.asutpDriver;

import com.github.wukap.automatedAccountingSystem.driver.Driver;
import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

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
    public OpcUaDriver(OpcUaDriver.OpcUaServerConnectionInfo connectionInfo) {
        this.connectionFactory = new ConnectionFactoryImpl(connectionInfo);
    }

    public List<OpcValue> readNodes(String[] NodesToRead) {
        List<OpcValue> tags = new ArrayList<>();
        for (int i = 0; i < NodesToRead.length; i++) {
            tags.add(read_(NodesToRead[i]));
        }
        return tags;
    }

    @Override
    public OpcValue read_(String tagName) {
        try {
            String opcUaTagName = getConnectionFactory().getActiveConnectionInfo().getServerPrefix() + tagName;
            var readValue = new ReadValueId(NodeId.get(IdType.String, getConnectionFactory().getActiveConnectionInfo().getNamespace(), opcUaTagName), Attributes.Value, null, null);
            ReadRequest req = new ReadRequest(null, 0.0, TimestampsToReturn.Both, List.of(readValue).toArray(new ReadValueId[0]));
            DataValue res = connectionFactory.getActiveConnection().Read(req).getResults()[0];

            Double value = res.getValue().doubleValue();
            Instant sourceTime = Instant.ofEpochMilli(res.getSourceTimestamp().getMilliSeconds());
            OpcValue.Quality quality = res.getStatusCode().isGood() ? OpcValue.Quality.GOOD : OpcValue.Quality.BAD;
            return new OpcValue(sourceTime, value, quality);
        } catch (ServiceResultException e) {
            log.error("Can't read from OPC UA server", e);
        } finally {
            connectionFactory.closeConnection();
        }
        return null;
    }

    @Override
    protected void write_(String tagname, Object value) {
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
