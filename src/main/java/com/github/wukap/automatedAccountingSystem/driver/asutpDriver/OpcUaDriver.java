package com.github.wukap.automatedAccountingSystem.driver.asutpDriver;

import com.github.wukap.automatedAccountingSystem.driver.Driver;
import com.github.wukap.automatedAccountingSystem.model.ESValue;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

/**
 * OPC UA driver implementation
 *
 * @author Yuriy Golubev
 */
@Slf4j
@Component
public class OpcUaDriver extends Driver {
    @Getter
    private final ConnectionFactoryImpl connectionFactory;


    @Autowired
    public OpcUaDriver(OpcUaDriver.OpcUaServerConnectionInfo connectionInfo) {
        this.connectionFactory = new ConnectionFactoryImpl(connectionInfo);
    }

    public List<ESValue> readNodes(String[] NodesToRead) {
        List<ESValue> tags = new ArrayList<>();
        for (int i = 0; i < NodesToRead.length; i++) {
            tags.add(read_(NodesToRead[i]));
        }
        return tags;
    }


    public ESValue read_(String tagName) {
        try {
            String opcUaTagName = getConnectionFactory().getActiveConnectionInfo().getServerPrefix() + tagName;
            var rv = new ReadValueId();
            rv.setAttributeId(Attributes.Value);
            rv.setNodeId(NodeId.get(IdType.String, getConnectionFactory().getActiveConnectionInfo().getNamespace(), opcUaTagName));
            ReadRequest req = new ReadRequest(null, 0.0, TimestampsToReturn.Both, List.of(rv).toArray(new ReadValueId[0]));
            ReadResponse res = connectionFactory.getActiveConnection().Read(req);
            DataValue result = res.getResults()[0];
            Class<?> valueType = result.getValue().getCompositeClass();
            ESValue newValue;

            ESValue.Quality quality = result.getStatusCode().isGood() ? ESValue.Quality.GOOD : ESValue.Quality.BAD;

            if (valueType != null && valueType.isAssignableFrom(Boolean.class)) {
                boolean boolValue = (boolean) result.getValue().getValue();
                newValue = new ESValue(now(), boolValue ? "1.0" : "0.0", ESValue.Type.DOUBLE, quality);
            } else {
                newValue = new ESValue( now(), result.getValue().toString(), ESValue.Type.DOUBLE, quality);

            }
            return newValue;

        } catch (Exception e) {
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
