package com.vitral.business.infraestructure.entry_points;

import com.vitral.business.domain.model.Business;
import com.vitral.business.domain.usecase.BusinessUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessUseCase businessUseCase;

    @PostMapping
    public ResponseEntity<Business> create(@Valid @RequestBody Business business) {
        return ResponseEntity.ok(businessUseCase.save(business));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Business> update(@PathVariable String slug, @Valid @RequestBody Business business) {
        business.setSlug(slug);
        return ResponseEntity.ok(businessUseCase.update(business));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Business> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(businessUseCase.findBySlug(slug));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<Business> findByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(businessUseCase.findByTenantId(tenantId));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        businessUseCase.deleteBySlug(slug);
        return ResponseEntity.noContent().build();
    }
}
