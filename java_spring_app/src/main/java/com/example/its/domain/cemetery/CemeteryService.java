package com.example.its.domain.cemetery;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CemeteryService {

    private final CemeteryRepository cemeteryRepository;

    /**
     * 全霊園ページを作成日時の降順で取得する。
     */
    public List<CemeteryEntity> findAll() {
        return cemeteryRepository.findAll();
    }

    /**
     * IDで霊園ページを取得する。見つからない場合は例外をスローする。
     */
    public CemeteryEntity findById(Long id) {
        return cemeteryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("霊園ページが見つかりません (id=" + id + ")"));
    }

    /**
     * オーナーIDで霊園一覧を取得する（マイページ用）。
     */
    public List<CemeteryEntity> findByOwnerId(Long ownerId) {
        return cemeteryRepository.findByOwnerId(ownerId);
    }

    /**
     * 新しい霊園ページを作成する。
     */
    @Transactional
    public CemeteryEntity create(CemeteryForm form, Long ownerId) {
        CemeteryEntity cemetery = new CemeteryEntity();
        cemetery.setOwnerId(ownerId);
        cemetery.setName(form.getName());
        cemetery.setDescription(form.getDescription());
        cemeteryRepository.insert(cemetery);
        return cemetery;
    }
}
