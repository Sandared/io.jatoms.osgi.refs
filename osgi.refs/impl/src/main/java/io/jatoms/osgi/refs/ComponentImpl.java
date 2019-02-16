package io.jatoms.osgi.refs;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component
public class ComponentImpl {
    // @Reference(cardinality=ReferenceCardinality.OPTIONAL, policy=ReferencePolicy.STATIC, policyOption=ReferencePolicyOption.GREEDY)
    private volatile ITest atest;

    @Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC, policyOption=ReferencePolicyOption.RELUCTANT)
    private List<ITest> tests;


    @Activate
    void activate () {
        System.out.println("#### ACTIVATE START ####");
        System.out.println("SINGLE REF: " + atest);
        System.out.println("LIST REF: " + tests);
        if(tests != null){
            System.out.println("LIST TYPE: " + tests.getClass().getName());
        }
        System.out.println("#### ACTIVATE END ####");
    }

    @Deactivate
    void deactivate () {
        System.out.println("#### DEACTIVATE START ####");
         System.out.println("SINGLE REF: " + atest);
        System.out.println("LIST REF: " + tests);
        if(tests != null){
            System.out.println("LIST TYPE: " + tests.getClass().getName());
        }
        System.out.println("#### DEACTIVATE END ####");
    }


}
