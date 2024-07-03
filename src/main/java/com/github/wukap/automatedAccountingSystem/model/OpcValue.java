package com.github.wukap.automatedAccountingSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.generic.Type;

import java.time.Instant;

/**
 * POJO for storing tag values
 *
 * @author Yuriy Golubev
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpcValue {
    private Instant sourceTime;
    private Double value;
    private Quality quality;


    public enum Quality {
        UNKNOWN, GOOD, BAD, REPLACED, MANUALINPUT, CALCULATION;
    }
}
