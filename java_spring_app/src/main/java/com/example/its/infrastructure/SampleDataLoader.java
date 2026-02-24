package com.example.its.infrastructure;

import java.io.IOException;
import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.example.its.domain.memory.MemoryRepository;

/**
 * アプリ起動時にサンプル画像を memories テーブルへ投入する。
 * 対象レコードの image_data が NULL の場合のみ更新するため、
 * 手動アップロード済みのデータは上書きしない。
 *
 * 画像ファイルの格納場所: src/main/resources/sample-images/
 */
@Component
public class SampleDataLoader implements CommandLineRunner {

    private final MemoryRepository memoryRepository;

    public SampleDataLoader(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadSampleImage(1L, "sample-images/memory1.jpg", "image/jpeg");
        loadSampleImage(2L, "sample-images/memory2.jpg", "image/jpeg");
        loadSampleImage(3L, "sample-images/memory3.jpg", "image/jpeg");
    }

    private void loadSampleImage(Long memoryId, String resourcePath, String contentType) {
        ClassPathResource resource = new ClassPathResource(Objects.requireNonNull(resourcePath));
        if (!resource.exists()) {
            return; // ファイルがなければスキップ（画像なしで起動できる）
        }
        try {
            byte[] imageData = resource.getInputStream().readAllBytes();
            memoryRepository.updateImageIfAbsent(memoryId, imageData, contentType);
        } catch (IOException e) {
            System.err.println("サンプル画像の読み込みに失敗しました: " + resourcePath + " - " + e.getMessage());
        }
    }
}
