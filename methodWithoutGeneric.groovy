import groovy.transform.CompileStatic

@CompileStatic
trait GormEntity<D> {
  
}

@CompileStatic
class Parent implements GormEntity<Parent> {
  static <T extends Parent> T getAll() {
    null
  }
}

@CompileStatic
class Child extends Parent implements GormEntity<Parent> {

}

@CompileStatic
class Foo implements GormEntity<Foo> {

}

@CompileStatic
class Testing {
  static main() {
    Child c = Child.getAll() // Will pass compile , but we can't auto-implement 
    System.out.println("Success")
  }
}

new Testing().main()