package com.github.wukap.automatedAccountingSystem.driver.asutpDriver;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.opcfoundation.ua.utils.EndpointUtil.*;


@Slf4j
public class ConnectionFactoryImpl implements ConnectionFactory<OpcUaDriver.OpcUaServerConnectionInfo, SessionChannel> {
    private final OpcUaDriver.OpcUaServerConnectionInfo connectionInfo;
    private SessionChannel activeChannel;

    private final HashMap<String, String> nodeNamesToId = new HashMap<>();

    public ConnectionFactoryImpl(OpcUaDriver.OpcUaServerConnectionInfo connectionInfos) {
        this.connectionInfo = connectionInfos;
    }

    @SneakyThrows
    @Synchronized
    @Override
    public SessionChannel getActiveConnection() {
        if (activeChannel == null) {
            try {
                activeChannel = setupChannel();
            } catch (ServiceResultException e) {
                log.error("Can't connect ot OPC UA server", e);
                throw e;
            }
        }
        return activeChannel;
    }

    @Synchronized
    @Override
    public void closeConnection() {
        try {
            activeChannel.close();
        } catch (ServiceResultException e) {
            log.warn("Can't close connection to OPC UA server", e);
        } finally {
            activeChannel = null;
        }
    }

    public OpcUaDriver.OpcUaServerConnectionInfo getActiveConnectionInfo() {
        return connectionInfo;
    }

    private SessionChannel setupChannel() throws ServiceResultException {
        Client myClient = Client.createClientApplication(null);
        String connectionUrl = getActiveConnectionInfo().getConnectionUrl();
        EndpointDescription[] endpoints = myClient.discoverEndpoints(connectionUrl);
        if (connectionUrl.startsWith("opc.tcp")) {
            endpoints = selectByProtocol(endpoints, "opc.tcp");
            endpoints = selectByMessageSecurityMode(endpoints, MessageSecurityMode.None);
            endpoints = sortBySecurityLevel(endpoints);
        } else {
            endpoints = selectByProtocol(endpoints, "https");
        }
        EndpointDescription endpoint = endpoints[endpoints.length - 1];
        //TODO
        endpoint.setEndpointUrl("opc.tcp://192.168.31.222:16550");
        log.info("Selected endpoint: {}", endpoint);
        SessionChannel activeChannel = myClient.createSessionChannel(endpoint);
        activeChannel.activate();


        //extraSetup(activeChannel);
        return activeChannel;
    }

    private void extraSetup(SessionChannel activeChannel) throws ServiceResultException {
        log.info("BrowseResponse for Root {}", requestBrowse(activeChannel, null, null, ""));
        log.info("Browse Configured Aliases: {} aliases found", requestBrowse(activeChannel, null, null, "Configured Aliases").getResults()[0].getReferences().length);
    }

    private BrowseResponse requestBrowse(SessionChannel activeChannel, RequestHeader requestHeader, ViewDescription view, String nodeName) throws ServiceResultException {
        BrowseDescription browse = new BrowseDescription();
        browse.setNodeId(NodeId.get(IdType.String, getActiveConnectionInfo().getNamespace(), getActiveConnectionInfo().getServerPrefix() + nodeName));
        browse.setBrowseDirection(BrowseDirection.Forward);
        browse.setIncludeSubtypes(true);
        browse.setNodeClassMask(NodeClass.Object, NodeClass.ReferenceType, NodeClass.Variable);
        browse.setResultMask(BrowseResultMask.All);
        return activeChannel.Browse(requestHeader, view, UnsignedInteger.getFromBits(0), browse);
    }

    private void hashmapToCsv(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<String, String> node : nodeNamesToId.entrySet()) {
                writer.write(node.getKey() + ";" + node.getValue() + ";" + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<ReferenceDescription[]> browseNode(SessionChannel mySession, String nodeIdStr) throws ServiceResultException {
        List<ReferenceDescription[]> list = new ArrayList<>();
        NodeId nodeId;
        try {
            nodeId = NodeId.get(getIdType(nodeIdStr), getNs(nodeIdStr), getId(nodeIdStr));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
        BrowseDescription browse = new BrowseDescription();
        browse.setNodeId(nodeId);
        browse.setBrowseDirection(BrowseDirection.Forward);
        browse.setIncludeSubtypes(true);
        browse.setNodeClassMask(NodeClass.Object, NodeClass.ReferenceType, NodeClass.Variable);
        browse.setResultMask(BrowseResultMask.All);//optional
        BrowseResponse res3 = mySession.Browse(null, null, null, browse);
        BrowseResult[] browseResults = res3.getResults();

        for (BrowseResult re : browseResults) {
            ReferenceDescription[] reference = re.getReferences();
            list.add(reference);
        }
        return list;
    }


    public Object getId(String nodeIdStr) {
        String[] addrArr = nodeIdStr.split(";");
        String[] idArr;
        if (addrArr.length > 1) {
            idArr = addrArr[1].split("=");
        } else {
            idArr = addrArr[0].split("=");
        }
        String type = idArr[0];
        if (type.equals("i")) return UnsignedInteger.getFromBits(Integer.parseInt(idArr[1]));
//		if (type.equals("s"))
//			return idArr.String;
        if (type.equals("g")) return UUID.fromString(idArr[1]);
//		if (type.equals("b")  ) {
//			return UUID.fromString(idArr[1]);
//		}
        return idArr[1];
    }

    public IdType getIdType(String nodeIdStr) {
        String[] addrArr = nodeIdStr.split(";");
        String[] addr;
        if (addrArr.length > 1) {
            addr = addrArr[1].split("=");
        } else {
            addr = addrArr[0].split("=");
        }
        String type = addr[0];
        if (type.equals("i")) return IdType.Numeric;
        if (type.equals("s")) return IdType.String;
        if (type.equals("g")) return IdType.Guid;
        if (type.equals("b")) {
            return IdType.Opaque;
        }
        return IdType.String;
    }

    public String getIdType2(IdType type) {
        if (type.equals(IdType.Numeric)) return "i";
        if (type.equals(IdType.String)) return "s";
        if (type.equals(IdType.Guid)) return "g";
        if (type.equals(IdType.Opaque)) {
            return "b";
        }
        return "s";
    }

    public int getNs(String nodeIdStr) {
        String[] addrArr = nodeIdStr.split(";");
        String[] nsArr = addrArr[0].split("=");
        if (addrArr.length > 1) {
            return Integer.parseInt(nsArr[1]);
        } else {
            return 0;
        }
    }
}
