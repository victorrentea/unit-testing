package ro.victor.unittest.spring.facade;


import lombok.Data;

@Data
public class ProductSearchResult {
    // Tip: final fields if using JPA
    public final Long id;
    public final String name;
}
