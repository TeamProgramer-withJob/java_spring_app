package com.example.its.domain.memory;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {

    private final MemoryRepository memoryRepository;

    /**
     * 霊園に紐づく思い出一覧を取得する。
     */
    public List<MemoryEntity> findByCemeteryId(Long cemeteryId) {
        return memoryRepository.findByCemeteryId(cemeteryId);
    }

    /**
     * IDで思い出を取得する。見つからない場合は例外をスローする。
     */
    public MemoryEntity findById(Long id) {
        return memoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("思い出が見つかりません (id=" + id + ")"));
    }

    /**
     * 画像バイナリを取得する（画像配信エンドポイント用）。
     */
    public MemoryEntity findImageById(Long id) {
        return memoryRepository.findImageById(id)
                .orElseThrow(() -> new IllegalArgumentException("画像が見つかりません (id=" + id + ")"));
    }

    /**
     * 思い出を作成する。画像が添付されていれば DB に BLOB として保存する。
     */
    @Transactional
    public MemoryEntity create(MemoryForm form, Long cemeteryId, Long authorId) throws IOException {
        MultipartFile image = form.getImage();

        MemoryEntity memory = new MemoryEntity();
        memory.setCemeteryId(cemeteryId);
        memory.setAuthorId(authorId);
        memory.setTitle(form.getTitle());
        memory.setBody(form.getBody());
        memory.setVisibility("PUBLIC");

        if (image != null && !image.isEmpty()) {
            memory.setImageData(image.getBytes());
            memory.setImageContentType(image.getContentType());
        }

        memoryRepository.insert(memory);
        return memory;
    }
}
