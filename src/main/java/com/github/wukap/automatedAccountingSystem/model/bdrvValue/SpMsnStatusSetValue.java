package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class SpMsnStatusSetValue extends BdrvValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;



    @NonNull
    private String pMsnId;

    @NonNull
    private String pMnsId;

    @NonNull
    private String pSetTime;

}
