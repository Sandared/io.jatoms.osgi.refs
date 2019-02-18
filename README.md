# OSGi Declarative Services References
 Get up and running in 5 minutes or less with OSGi's declarative service references.

 ![TODO IMAGE]()

 ## Get started
 * [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io#https://github.com/Sandared/io.jatoms.osgi.refs)
 * Wait for Maven to download the internet
 * See the OSGi DS references in action ;)

 ## Things to do
 * Play around with the code
 * Debug the whiteboard via `debug app/target/app.jar` (More information on [how to debug in GitPod](https://github.com/Sandared/io.jatoms.osgi.base/blob/master/README.md#how-to-debug-an-application-without-a-main-method))
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
In OSGi's declarative services (DS) the `@Reference` annotation serves as an indicator for build tools like bnd that marks the given field (constructor parameter or method are also allowed, but more on that in another tutorial) as a service reference to another service.
The type of this service is given by the type of the field, or by 