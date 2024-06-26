package com.github.wukap.automatedAccountingSystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESValue {
    private Long id;
    private int driverId;
    private Instant serverTime;
    private String serverTimeStr; //human readable format for logging
    private Instant sourceTime;
    private String sourceTimeStr; //human readable format for logging
    private String value;
    private Type type;
    private Quality quality;

    /**
     * Use this ctor for newly created value
     *
     * @param driverId
     * @param value
     * @param type
     */
    public ESValue(int driverId, Instant sourceTime, String value, Type type, Quality quality) {
        this(driverId, Instant.now(), sourceTime, value, type, quality);
    }

    public ESValue(int driverId, double value) {
        this(driverId, Instant.now(), String.valueOf(value), Type.DOUBLE, Quality.GOOD);
    }

    public ESValue(int driverId, int value) {
        this(driverId, Instant.now(), String.valueOf(value), Type.LONG, Quality.GOOD);
    }

    public ESValue(int driverId, String value) {
        this(driverId, Instant.now(), value, Type.STRING, Quality.GOOD);
    }

    public ESValue(int driverId, Instant serverTime, Instant sourceTime, String value, Type type, Quality quality) {
        this.driverId = driverId;
        this.serverTime = serverTime;
        this.serverTimeStr = this.serverTime.toString();
        this.sourceTime = sourceTime;
        if (sourceTime != null) {
            this.sourceTimeStr = this.sourceTime.toString();
        }
        this.value = value;
        this.type = type;
        this.quality = quality;
    }

    public Object getTypedValue() {
        try {
            return type.convert(value);
        } catch (Exception e) {
            return null;
        }
    }

    public Double getAsDouble() {
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public enum Type {
        BOOL {
            @Override
            public Object convert(String value) {
                return Boolean.valueOf(value);
            }
        },
        DOUBLE {
            @Override
            public Object convert(String value) {
                return Double.valueOf(value);
            }
        },
        LONG {
            @Override
            public Object convert(String value) {
                return Long.valueOf(value);
            }
        },
        STRING {
            @Override
            public Object convert(String value) {
                return value;
            }
        };

        public abstract Object convert(String value);
    }

    public enum Quality {
        UNKNOWN,
        GOOD,
        BAD,
        REPLACED,
        MANUALINPUT,
        CALCULATION;
    }
}