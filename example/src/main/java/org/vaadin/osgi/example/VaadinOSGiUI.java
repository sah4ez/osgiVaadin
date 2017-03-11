package org.vaadin.osgi.example;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.vaadin.osgi.bridge.uifragments.FragmentFactory;

import java.util.LinkedHashMap;
import java.util.Map;

@Theme("valo")
@SuppressWarnings("serial")
public class VaadinOSGiUI extends UI implements
        ServiceTrackerCustomizer<FragmentFactory, FragmentFactory> {

    /**
     *
     */
    private static final long serialVersionUID = 5043459045625549368L;

    private Map<ServiceReference<FragmentFactory>, Button> srs = new LinkedHashMap<>();
    private Map<Button, Component> lastComponentDisplayedForButton = new LinkedHashMap<>();

    private HorizontalSplitPanel hl;

    private VerticalLayout buttonsOnTheLeft;

    @Override
    protected void init(VaadinRequest request) {

        setPollInterval(1000);
        hl = new HorizontalSplitPanel();
        hl.setSizeFull();
        setContent(hl);

        buttonsOnTheLeft = new VerticalLayout();
        buttonsOnTheLeft.setSizeUndefined();

        hl.setFirstComponent(buttonsOnTheLeft);
        hl.setSplitPosition(25, Unit.PERCENTAGE);

        ServiceTracker<FragmentFactory, FragmentFactory> tracker = new ServiceTracker<FragmentFactory, FragmentFactory>(
                VaadinActivator.context, FragmentFactory.class, this);
        tracker.open();

        addDetachListener((DetachListener) event -> tracker.close());
    }

    @Override
    public FragmentFactory addingService(
            ServiceReference<FragmentFactory> reference) {
        FragmentFactory ff = VaadinActivator.context.getService(reference);
        createButton(reference);

        return ff;
    }

    private void createButton(ServiceReference<FragmentFactory> reference) {
        Button b = new Button("Click me");
        b.addClickListener((Button.ClickListener) event -> {
            FragmentFactory ff = VaadinActivator.context
                    .getService(reference);
            if (ff != null) {
                Component fragment = ff.getFragment();
                hl.setSecondComponent(fragment);
                lastComponentDisplayedForButton.put(b, fragment);

            } else {
                Notification.show("Excuse me, the view is not available any more");
            }
            VaadinActivator.context.ungetService(reference);
        });
        srs.put(reference, b);
        addedButton(b);
    }

    private void addedButton(Button b) {
        access(() -> buttonsOnTheLeft.addComponent(b));
    }

    @Override
    public void modifiedService(ServiceReference<FragmentFactory> reference,
                                FragmentFactory service) {
        ;// no-op
    }

    @Override
    public void removedService(ServiceReference<FragmentFactory> reference,
                               FragmentFactory service) {
        access(() -> {
            removedButton(srs.get(reference));
            srs.remove(reference);
        });
    }

    private void removedButton(Button b) {
        buttonsOnTheLeft.removeComponent(b);
        Component lastDisplayedForThisButton = lastComponentDisplayedForButton.get(b);

        if (lastDisplayedForThisButton == hl.getSecondComponent()) {
            //need to remove it
            hl.removeAllComponents();
            hl.setFirstComponent(buttonsOnTheLeft);
        }
    }
}
