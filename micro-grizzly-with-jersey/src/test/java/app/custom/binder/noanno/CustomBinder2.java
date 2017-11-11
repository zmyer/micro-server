package app.custom.binder.noanno;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;

public class CustomBinder2 extends AbstractBinder {

    @Override
    protected void configure() {
        // this is where the magic happens!
        bind(CustomResourceInvocationHandlerProvider.class).to(
                ResourceMethodInvocationHandlerProvider.class);
    }
}