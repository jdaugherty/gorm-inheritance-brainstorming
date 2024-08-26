import groovy.transform.CompileStatic

@CompileStatic
trait GormEntity<D> {
  static D getAll() {
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
    Child c = Child.getAll() // will fail to compile, requires casting to Child
    //Foo f = Child.getAll()
  }
}
