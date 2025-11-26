package com.visma.feature.kalmar.api.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Role", indexes = {
        @Index(name = "Role_Name", columnList = "Name"),
        @Index(name = "Role_InvariantKey", columnList = "InvariantKey")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "IdRole", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idRole;

    @Column(name = "Name", nullable = false, length = 255)
    private String name;

    @Column(name = "InvariantKey", nullable = false, length = 50)
    private String invariantKey;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "RecordVersion", nullable = false)
    @Version
    private Long recordVersion;

    @Column(name = "WhenEdited", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date whenEdited;

    @PrePersist
    public void prePersist() {
        if (idRole == null) {
            idRole = UUID.randomUUID();
        }
    }
}
