package victor.testing.design.fixturecreep;

public interface FeatureFlags {
  enum Feature {
    PORK_SHAWARMA
  }
  boolean isActive(Feature feature);
}
