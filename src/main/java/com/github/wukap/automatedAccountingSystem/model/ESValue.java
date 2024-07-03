package com.github.wukap.automatedAccountingSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.time.Instant;

/**
 * POJO for storing tag values
 *
 * @author Yuriy Golubev
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESValue {
    private Long id;
    private Instant serverTime;
    private String serverTimeStr; //human readable format for logging
    private Instant sourceTime;
    private String sourceTimeStr; //human readable format for logging
    private String value;
    private Type type;
    private Quality quality;

    /**
     *
     * @param value
     * @param type
     */
    public ESValue(Instant sourceTime, String value, Type type, Quality quality) {
        this( Instant.now(), sourceTime, value, type, quality);
    }

    public ESValue( double value) {
        this( Instant.now(), String.valueOf(value), Type.DOUBLE, Quality.GOOD);
    }

    public ESValue( int value) {
        this( Instant.now(), String.valueOf(value), Type.LONG, Quality.GOOD);
    }

    public ESValue( String value) {
        this(Instant.now(), value, Type.STRING, Quality.GOOD);
    }

    public ESValue( Instant serverTime, Instant sourceTime, String value, Type type, Quality quality) {

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
