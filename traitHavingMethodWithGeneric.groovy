import groovy.transform.CompileStatic

@CompileStatic
trait GormEntity<D> {
  static <T extends D> T getAll() { // Does not allow Child.getAll() to return either Parent or Child even though both Child & Parent implement `D = Parent`
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
