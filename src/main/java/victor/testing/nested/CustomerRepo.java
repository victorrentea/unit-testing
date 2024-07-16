package victor.testing.nested;

public interface CustomerRepo {
   int countByEmail(String email);

   Long save(Customer customer);
}
