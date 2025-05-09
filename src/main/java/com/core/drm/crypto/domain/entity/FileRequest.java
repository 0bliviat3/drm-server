package com.core.drm.crypto.domain.entity;

import com.core.drm.crypto.constant.CipherType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_file_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRequest {

    @Id
    @GeneratedValue
    @Column(name = "request_id")
    private UUID requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "request_type")
    private CipherType requestType;

    @Column(nullable = false, name = "request_ip")
    private String requestIP;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, name = "file_extension")
    private String fileExtension;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Transient
    private MultipartFile file;

    public boolean isEncrypt() {
        return this.requestType == CipherType.ENCRYPT;
    }

}
