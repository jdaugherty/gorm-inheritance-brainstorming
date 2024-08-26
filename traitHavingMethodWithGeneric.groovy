import groovy.transform.CompileStatic

@CompileStatic
trait GormEntity<D> {
  static <T extends D> T getAll() {
    null
  }
}

@CompileStatic
class Parent implements GormEntity<Parent> {

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
    Child c = Child.getAll()
    System.out.println("Success")
  }
}

new Testing().main()
