package org.vaadin.osgi.example.uifragment1;


import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.osgi.bridge.uifragments.FragmentFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class FragmentFactoryImpl1 implements FragmentFactory {

	@Override
	public Component getFragment() {
		VerticalLayout vl = new VerticalLayout();

		AtomicInteger numberOfClicks = new AtomicInteger(0);
		Label clickedTimes = new Label(numberOfClicks.toString());
		
		vl.addComponent(new Button("Click me", new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7830684640567884545L;

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Thank you for clicking!");
				clickedTimes.setValue(String.valueOf(numberOfClicks.incrementAndGet()));
			}
		}));
		
		vl.addComponent(clickedTimes);
		
		return vl;
	}

}
