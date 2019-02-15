package io.jatoms.osgi.refs;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component
public class ComponentImpl {

    @Reference(policyOption=ReferencePolicyOption.GREEDY)
    private List<ITest> tests;

    // @Reference(policyOption=ReferencePolicyOption.GREEDY)
    private ITest test;

    @Activate
    void activate () {
        System.out.println("#### ACTIVATE START ####");
        System.out.println("TEST: " + test);
        System.out.println("TESTS == NULL: " + (tests == null));
        System.out.println("TESTS: " + tests);
        if(tests != null){
            System.out.println("TESTS TYPE: " + tests.getClass().getName());
        }
        System.out.println("#### ACTIVATE END ####");
    }

    @Deactivate
    void deactivate () {
        System.out.println("#### DEACTIVATE START ####");
        System.out.println("TEST: " + test);
        System.out.println("TESTS == NULL: " + (tests == null));
        System.out.println("TESTS: " + tests);
        if(tests != null){
            System.out.println("TESTS TYPE: " + tests.getClass().getName());
        }
        System.out.println("#### DEACTIVATE END ####");
    }


}
