package com.groupware.entity.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupware.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "document_path")
public class DocumentPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String documentName;

    private String documentHashName;

    private float documentSize;

    private Boolean useStatus;
    @CreationTimestamp
    private Date createdAt;
}
