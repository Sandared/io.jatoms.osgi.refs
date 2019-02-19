# OSGi Field References
 Get up and running in 5 minutes or less with OSGi's declarative service field references.

 ![Gitpod REFS](https://github.com/Sandared/io.jatoms.osgi.refs/blob/master/onew-refs.PNG)

 ## Get started
 * [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io#https://github.com/Sandared/io.jatoms.osgi.refs)
 * Wait for Maven to download the internet
 * See the OSGi DS references in action ;)

 ## Things to do
 * Play around with the code
 * "Debug" the example via `run app/target/debug.jar`
    * Once the application started a notfication should pop up that a port is exposed -> Choose: open in browser. The tab opened will say "not found", so just add /system/console to the url and if asked for credentials enter admin/admin.
    * Now you should see the Apache Felix Webconsole. Go to OSGi -> Components -> ComponentImpl
    * The interesting part is the line that states "Bound Service". here you can see if your reference is actually bound to another service instance, although it might have been null or empty at activation time of your component.
    * Whenever you wnt to try a new combination of attributes, first stop any running framework by typing `stop 0` in the terminal in Gipod, then type `resolve app` to rebuild your app and then again start it by typing `run app/target/debug.jar`
 * Change the values `cardinality`, `policy` and `policyOption` for each reference to see the different effects on the behavior of the component (See below for an overview of the to expect effects). It's best to only have only one reference active at a time in order to only see the effect of one reference instead of a mixture of both. Just comment out the one you want to be disabled.

## Contribute
* Any suggestions/comments/additional awesomeness? open an Issue :)
* Anything else? Write me on [Twitter](https://twitter.com/SanfteSchorle)

## How to reproduce
1) [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io#https://github.com/Sandared/io.jatoms.osgi.base)
1) Within the terminal at the bottom type `project` and fill out *groupId* and *artifactId* as you wish
1) Within your newly created project create
   1) The Component implementation referencing a service once via Scalar and once via Collection.
   1) An interface `ITest`
   1) Two components `Test` and `OtherTest` both implementing the `ITest` interface. Give `Test` a `ServiceRanking` of 1000 and `OtherTest` 100.
1) `cd ..` to go back into your parent project
1) type `resolve app` and then `run app/target/app.jar`
1) see your references implementation in action

## What's going on behind the curtains?
This section is for those who want to understand what is going on in the background so that this example works.

### What is a @Reference?
In OSGi's declarative services (DS) the `@Reference` annotation serves as an indicator for build tools like bnd that marks the given field (constructor parameter or method are also allowed, but more on that in another tutorial) to be a service reference to another service.
The type of this service is given by the type of the field, or by the generic of a used collection e.g., `List<MyType>` (Other types are also supported, but more on that later).
This annotation and the values of its attributes are then used by bnd to generate the corresponding XML in the `OSGI_INF/mycompl.xml` file.
This in turn is picked up at runtime by for example Apache Felix SCR and according to the semantic meaning of the attributes you've set references to other services are injected or not.

Now this has been a rather abstract explanation of what a reference is, let's see how such a reference works in action.

### How does a @Reference work?
As explained in [the tutorial about DS](http://bit.ly/jatoms-ds), another entity (e.g., Felix SCR) injects services depending on the values of the attributes of your @Reference annotation, but which values have which effect?

Let's start with what attributes actually exist and which are crucial to know for most cases:

#### cardinality
The cardinality of a reference tells SCR how many isntances of a service you want to get injected. You can choose between 4 different values:
* OPTIONAL (0..1): This means you do not necessarily need a service instance but will not complain if there is one. Keep in mind that no instance at all is a valid value, i.e., this can be null.
* MANDATORY (1..1): This you use when you definitly need an instance of that service so that your component works properly.
* MULTIPLE (0..n): You don't care about the number of instances and its also ok if you have none at all
* AT_LEAST_ONE (1..n): You don't really care about the upper bound of the instances you get, but you need at least one to work properly

Keep in mind that the numbers you define here have an influence on whether your component is started or not, e.g., if you defined your reference to be MANDATORY, but there is no service instance for the service you defined, then your component will not be started! Vice versa, if you defined to not care about if there is at least one instance or not, then your component might be started, but the value of your reference might be null.

#### policy
The policy of a reference tells SCR if your component shall be restarted everytime the value of your reference changes or not. Therefore there are two values:
* STATIC: This means your component will be restarted each time the value of your reference is changed, e.g.,  you've defined your reference to be MANDATORY and now the injected service instance is shut down because of reasons, then either SCR will restart your component with another instance of the service or, if there is no more instance, shut down your component entirely.
* DYNAMIC: This means your component will NOT be restarted each time the value of your reference is changed. On first sight this sounds good, but you have to kkep in mind, that now you have to deal with multithreading issues, e.g., you haveto declare your service variable to be volatile, otherwise you might not get notified when the refernce is changed by SCR. Just declare your service variables to be volatile when you define them to be DYNAMIC.

#### policyOption
The policyOption tells SCR if you always want the best and shiniest and newest of all services available or if you are satisfied with the old  and rusty one that was given to you first.
* RELUCTANT: This means you are satisfied with what you have. Sounds good but might not be what you want sometimes, e.g., a MUTLIPLE, RELUCTANT reference might end up with no services at all. Why? Well, MULTIPLE means 0 references is a valid value. So your component may start with no service instances at all and RELUCTANT means you are not interested in any other service instances that might pop up after the startup of your component.
* GREEDY: This means you want always the newest and best service instance for your reference. Sounds better than RELUCTANT? Not in all cases, e.g., you have a STATIC, GREEDY reference and and after the startup of your component several new service instances appear each with a higher service ranking than the one before. Such a scenario would end up in your component being stopped and restarted for each of these service instances as each new one is consedered "better" than the one before (Because of the higher service ranking)

So now that you know how to change the behavior of each reference to your will, you might not always want to type all of these attributes explicitly. For this some sensible defaults have been chosen so that you only have to write things explicitly if you want your reference to differ from those defaults.

#### Defaults
I will now show you some code examples that are equivalent, once with defaults and once explicitly:

##### MANDATORY/STATIC/RELUCTANT
```java
@Component
public class MyComp {
    // This...
    @Reference
    private ITest default;

    // ... is equivalent to this
    @Reference(cardinality=ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC, policyOption=ReferencePolicyOption.RELUCTANT)
    private ITest explicit;
}
```

##### MANDATORY/DYNAMIC/RELUCTANT
```java
@Component
public class MyComp {
    // This...
    @Reference
    private volatile ITest default;

    // ... is equivalent to this
    @Reference(cardinality=ReferenceCardinality.MANDATORY, policy=ReferencePolicy.DYNAMIC, policyOption=ReferencePolicyOption.RELUCTANT)
    private volatile ITest explicit;

    // NOTE:
    // With  bnd 4.1.0 there is no compile error when you do declare a DYNAMIC reference not to be volatile, but from 4.2.0 on the following code will not compile
    @Reference(policy=ReferencePolicy.DYNAMIC)
    private ITest error; // ERROR: DYNAMIC references have to be volatile!
}
```

##### MULTIPLE/STATIC/RELUCTANT
```java
@Component
public class MyComp {
    // This...
    @Reference
    private List<ITest> default;

    // ... is equivalent to this
    @Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.STATIC, policyOption=ReferencePolicyOption.RELUCTANT)
    private List<ITest> explicit;
}
```

##### MULTIPLE/DYNAMIC/RELUCTANT
```java
@Component
public class MyComp {
    // This...
    @Reference
    private volatile List<ITest> default;

    // ... is equivalent to this
    @Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC, policyOption=ReferencePolicyOption.RELUCTANT)
    private volatile List<ITest> explicit;
}
```

In case you want to change just one value from the default, then you only have to chnage this value explicitly:


##### MULTIPLE/DYNAMIC/GREEDY
```java
@Component
public class MyComp {
    // This...
    @Reference(policyOption=ReferencePolicyOption.GREEDY)
    private volatile List<ITest> default;

    // ... is equivalent to this
    @Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC, policyOption=ReferencePolicyOption.GREEDY)
    private volatile List<ITest> explicit;
}
```

NOTE: If you define a variable to be STATIC and volatile, then bnd takes the value from the annotation OVER the one of the default, i.e., such a definition would result in a STATIC reference.

### Behavior Overview

I've created a little table that shall serve as an overview about the expected behavior for all the combinations of all three attributes:

![Refs Table](https://github.com/Sandared/io.jatoms.osgi.refs/blob/master/onew-ref-table.PNG)

### Conclusion
OSGi provides you with a really powerful dependency injection mechanism. This tutorial barely scratches the surface of what you can do with DS and their references in between. However, in most cases all you need is either a static, mandatory reference to the service you need or a whiteboard (see [my tutorial about whiteboards](http://bit.ly/jatoms-whiteboard)). I would dare say that those two types make up for 70% of my use cases. The other 30% are advanced stuff, where I try to introduce OSGi dependency injection into other frameworks, e.g., for Vaadin Flow I sketched such a solution.

That said, there is still a lot of other stuff that might be of interest to you if you are working extensively with declarative services, e.g., method injection (@Reference on methods instead of fields) or target filters (in order to narrow down the number of eligible service instances for your reference). 

For those of you who can't wait for another tutorial I recommend to either have a look at Dirk Fauth's [excellent tutorials on this topic](http://blog.vogella.com/author/fipro/) or to have alook at the [DS compendium specification](https://osgi.org/specification/osgi.cmpn/7.0.0/service.component.html).

