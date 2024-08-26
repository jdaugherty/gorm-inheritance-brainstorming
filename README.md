Prior to Groovy 4, an interface could be implemented with multiple generic types.  Given a class `Parent` and a class `Child`, this is allowed: 

	class Parent extends GormEntity<Parent>


and: 

 
	class Child extends GormEntity<Child>
 

In groovy 4, this fails with: 

`The interface GormEntity cannot be implemented more than once with different arguments: GormEntity<Child> and GormEntity<Parent>`

The ticket for this change was: https://issues.apache.org/jira/browse/GROOVY-5106 

The implementation in Groovy 3.x would copy each trait method to each class and the child method would effectively override the parent method.  For example, if the trait `GormEntity` was defined as: 

	trait GormEntity<D> {
	  D getAll() {
	    null
	  }
	}

the child would have a `getAll()` method of:

	@TraitBridge(
        traitClass = GormEntity.class,
        desc = "()Ljava/lang/Object;"
    )
    public Child getAll() {
        CallSite[] var1 = $getCallSiteArray();
        return (Child)ScriptBytecodeAdapter.castToType(var1[1].call(GormEntity$Trait$Helper.class, this), Child.class);
    }

While the parent would have: 

	public Parent getAll() {
        CallSite[] var1 = $getCallSiteArray();
        return (Parent)ScriptBytecodeAdapter.castToType(var1[1].call(GormEntity$Trait$Helper.class, this), Parent.class);
    }

This override behavior is how GORM implemented multiple inheritance.  Given, 1L = Parent, and 2L = Child, the following would be valid in GORM:

	Parent p1 = Parent.get(1L) // finds the parent row
	Child c1 = Parent.get(2L) as Child // finds the child since it's a valid Parent
	Child c2 = Child.get(2L) // finds the child
	Parent p2 = Child.get(1L) // does not find the Parent since it's not a child

This repository is trying to find work arounds for the new validation.

* `inheritanceRequiringCast.groovy` - takes the approach of having GormEntity always refer to the `Parent` type as an argument on both `Parent` and `Child`.  Since the methods on the trait get the current class, calling Child methods will still return children, and callers will just have to cast the type. 
* `methodWithoutGeneric.groovy` isn't a solution for Gorm, but demonstrates that a method can be written on the `Parent` that will return a Parent or Child.  The caller's assignment will determine what the method expects to return (and if it mismatches at runtime, an error will likely occur).
* `traitHavingMethodWithGeneric.groovy` may be a possible solution, but groovy does not compile.  This file is the same as `methodWithoutGeneric.groovy` but tries to move the Parent's method to the trait and use the generic `D` since D will be Parent.  However, groovy fails to compile with: `Cannot assign value of type Parent to variable of type Child`
