package victor.testing.designhints.functionalcore;

public class Support {
}

class A{
   private final String name;
   public A(String name) {
      this.name = name;
   }
   public String toString() {
      return name;
   }
}
interface RepoA {
   A findById(long id);
}
class B{
   private final String name;
   public B(String name) {
      this.name = name;
   }
   public String toString() {
      return name;
   }
}
interface RepoB {
   B findById(long id);
}
class C{
   private final String data;
   public C(String data) {
      this.data = data;
   }
   public String getData() {
      return data;
   }
}
interface RepoC {
   void save(C c);
}