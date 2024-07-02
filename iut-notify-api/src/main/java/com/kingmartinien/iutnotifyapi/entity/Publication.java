package com.kingmartinien.iutnotifyapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_PUBLICATIONS")
@EntityListeners(AuditingEntityListener.class)
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publication_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "publication_type")
    private String publicationType;

    @OneToMany(mappedBy = "publication", fetch = FetchType.EAGER)
    private Set<ExternalLink> externalLinks;

    @OneToMany(mappedBy = "publication", fetch = FetchType.EAGER)
    private Set<Attachment> attachments;

    @ManyToOne
    @JoinColumn(name = "channel_id_fk", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_id_fk", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", insertable = false)
    private String updatedBy;

    @Version
    @Column(name = "version")
    private Long version;

}
