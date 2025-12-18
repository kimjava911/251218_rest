package kr.java.restapi.service;

import kr.java.restapi.model.dto.ItemCreateRequest;
import kr.java.restapi.model.dto.ItemResponse;
import kr.java.restapi.model.dto.ItemUpdateRequest;
import kr.java.restapi.model.entity.Item;
import kr.java.restapi.exception.NotFoundException;
import kr.java.restapi.model.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 상품 서비스
 */
@Service
@RequiredArgsConstructor
// import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)  // 기본: 읽기 전용
public class ItemService {

    private final ItemRepository itemRepository;

    // CREATE: 상품 생성
    @Transactional  // 쓰기 트랜잭션
    public ItemResponse create(ItemCreateRequest request) {
        Item item = request.toEntity();
        Item savedItem = itemRepository.save(item);
        return ItemResponse.from(savedItem);
    }

    // READ: 단건 조회
    public ItemResponse findById(Long id) {
//        Item item = itemRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다. : " + id));
        // #(3)-3
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forItem(id));
        return ItemResponse.from(item);
    }

    // READ: 전체 목록 조회
    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::from)
                .toList();
    }

    // UPDATE: 상품 수정
    @Transactional
    public ItemResponse update(Long id, ItemUpdateRequest request) {
        // #(3)-4
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forItem(id));
        // 더티 체킹으로 자동 UPDATE
        item.update(request.name(), request.price(), request.description());

        return ItemResponse.from(item);
    }

    // DELETE: 상품 삭제
    @Transactional
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            // #(3)-5
            throw NotFoundException.forItem(id);
        }
        itemRepository.deleteById(id);
    }
}