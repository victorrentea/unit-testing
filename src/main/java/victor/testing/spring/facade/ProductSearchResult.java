package victor.testing.spring.facade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResult {
    // Tip: final fields if using JPA
    public Long id;
    public String name;


}
