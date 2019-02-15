package io.jatoms.osgi.refs;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.propertytypes.ServiceRanking;

@Component(immediate = true)
@ServiceRanking(1000)
public class MyTest implements ITest {

    @Activate
    void activate (){
        System.out.println("Hello Test");
    }

    @Deactivate
    void deactivate() {
        System.out.println("Bye Test");
    }
}