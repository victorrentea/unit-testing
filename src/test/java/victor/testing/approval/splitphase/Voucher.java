package victor.testing.approval.splitphase;

import lombok.Data;

@Data
public class Voucher {
   public enum Measure {
      EUR, PERCENT
   }
   private final String code;
   private final int value;
   private final Measure measure;
   private final String department;

   public Voucher(String code, int value, Measure measure) {
      this(code, value, measure, null);
   }

   public Voucher(String code, int value, Measure measure, String department) {
      this.code = code;
      this.value = value;
      this.measure = measure;
      this.department = department;
   }
}
