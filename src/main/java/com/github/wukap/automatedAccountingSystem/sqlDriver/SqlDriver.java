package com.github.wukap.automatedAccountingSystem.sqlDriver;

import com.github.wukap.automatedAccountingSystem.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SqlDriver extends ESDriver {
    private final Config config;
    private final Map<String,Set<WriteonlyQuery>> writeOnlyQueries;
    private final ConcurrentHashMap<String, Function<ESValue, Boolean>> callbacks = new ConcurrentHashMap<>();
    private final ESStatistics esStatistics;
    private final HikariDataSource dataSource;
    private ScheduledExecutorService executorService;

    @SneakyThrows
    public SqlDriver(Config config, DriverSupervisor driverSupervisor, MeterRegistry meterRegistry) {
        super(driverSupervisor, meterRegistry);
        this.config=config;

        writeOnlyQueries=Optional.ofNullable(config.getWriteonlyQueries())
                .orElse(new HashSet<>())
                .stream()
                .collect(Collectors.toMap(
                        WriteonlyQuery::getEsTag,
                        Collections::singleton,
                        (l,r)-> Stream.of(l,r).flatMap(Collection::stream).collect(Collectors.toSet())
                ));

        dataSource=new HikariDataSource(
                new HikariConfig(){{
                    setJdbcUrl("jdbc:jtds:sqlserver://"+config.getBindAddress()+"/"+config.getDbName()+";instance=SQLEXPRESS");
                    setUsername(config.getUser());
                    setPassword(config.getPassword());
                    setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
                    setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
                    setConnectionTimeout(120_000L);
                    setValidationTimeout(60*1000);
                    setIdleTimeout(600*1000);
                    setConnectionTestQuery("select 1");
                }}
        );
        esStatistics=new ESStatistics("XXX SQLDriver with driverId="+config.getDriverId(),60*1000);
    }

    @Override
    public boolean isOutput(){
        return true;
    }

    @Override
    public boolean isInput() {
        return true;
    }

    @Override
    protected void start_()
    {

    }


    private void doReadonlyQuery(Instant time, ReadonlyQuery readonlyQuery){
        String sql=readonlyQuery.getSql();
        try(Connection connection=dataSource.getConnection();Statement statement=connection.createStatement();ResultSet rs=statement.executeQuery(sql)) {
            boolean resultIsNotEmpty=rs.next();
            if(!resultIsNotEmpty){
                log.info("readonly query: ("+readonlyQuery.getName()+") - result is empty");
                return;
            }

            for(Map.Entry<String,String> sqlEs:readonlyQuery.getSqlColToEsTag().entrySet()){
                double value=rs.getDouble(sqlEs.getKey());
                ESValue esValue=new ESValue(config.getDriverId(),time,value+"", ESValue.Type.DOUBLE, ESValue.Quality.GOOD);
                Function<ESValue,Boolean> callback=callbacks.get(sqlEs.getValue());
                if(callback!=null)callback.apply(esValue);
            }

            esStatistics.updateMeasurement("readonly queries done");
        }
        catch (Exception e){
            log.info("readonly query: ("+readonlyQuery.getName()+") - throws error when execute",e);
        }
    }

    @Override
    @SneakyThrows
    public void stop() {
        started = false;
        executorService.shutdown();
        dataSource.close();
    }

    @Override
    protected void listen_(String tagname, Function<ESValue, Boolean> callback)
    {

    }

    @Override
    protected ESValue read_(String tagname)
    {
        return null;
    }

    @Override
    protected void write_(String tagname, ESValue value)
    {

    }


    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Config {
        @Positive
        private int driverId;
        @NotEmpty
        private String bindAddress;
        @NotEmpty
        private String user;
        @NotEmpty
        private String password;
        @NotEmpty
        private String dbName;
        @Positive
        private int updateInterval;
        @Valid
        @Singular
        private Map<String, TagParams> tagMappings;

        private Set<ReadonlyQuery> readonlyQueries;
        @NotNull
        private Set<WriteonlyQuery> writeonlyQueries;

    }

    @Value
    public static class WriteonlyQuery{
        @NotNull
        String name;
        @NotNull
        String sqlCommand;
        @NotNull
        String esTag;
        @NotNull
        String sqlTag;
        @NotNull
        String dateFormat;

        public String renderSql(Instant time, Double value){
            return sqlCommand
                    .replace("%tagname",sqlTag)
                    .replace("%date",time.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(dateFormat)))
                    .replace("%value",Optional.ofNullable(value).map(Objects::toString).orElse("null"));
        }
    }

    @Value
    public static class ReadonlyQuery{
        @NotNull
        String name;
        @NotNull
        String cron;
        @NotNull
        String sql;
        @NotNull
        Map<String, String> sqlColToEsTag;
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TagParams {
        @NotEmpty
        private String table;
        @NotEmpty
        private String timeColumn;
        @NotEmpty
        private String valueColumn;
        @NotNull
        private ESValue.Type valueType;
        private Map<String,String> additionalFields;
    }
}