/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

/**
 *
 * @author USER01
 */
import lombok.*;
import java.time.Instant;
import java.util.List;

// ── Réponse paginée générique ─────────────────────────────────────────────────
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {
    private List<T> data;
    private long total;
    private int page;
    private int limit;
    private int totalPages;

    public static <T> PagedResponse<T> of(List<T> data, long total, int page, int limit) {
        return PagedResponse.<T>builder()
                .data(data)
                .total(total)
                .page(page)
                .limit(limit)
                .totalPages((int) Math.ceil((double) total / limit))
                .build();
    }
    
}
