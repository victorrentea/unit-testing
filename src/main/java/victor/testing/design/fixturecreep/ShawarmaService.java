package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

//@Service
// class PaymentService { 667 lines
// class ConsumerService { 1000 lines
// class ConsumerValidationService { or better:
// class ValidateCustomerService {

@RequiredArgsConstructor
public class ShawarmaService {
   private final Dependency dependency;
   private final FeatureFlags featureFlags;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      if (featureFlags.isActive(PORK_SHAWARMA)) {
         // stuff
      }
      // complex logic: 7 ifs
   }

}
