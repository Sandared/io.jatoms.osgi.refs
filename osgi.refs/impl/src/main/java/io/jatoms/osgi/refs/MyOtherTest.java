package io.jatoms.osgi.refs;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.propertytypes.ServiceRanking;

@Component(immediate = true)
@ServiceRanking(100)
public class MyOtherTest implements ITest {
    @Activate
    void activate (){
        System.out.println("Hello Other Test");
    }

    @Deactivate
    void deactivate() {
        System.out.println("Bye Other Test");
    }
}