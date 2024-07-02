package com.kingmartinien.iutnotifyapi.repository;

import com.kingmartinien.iutnotifyapi.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
