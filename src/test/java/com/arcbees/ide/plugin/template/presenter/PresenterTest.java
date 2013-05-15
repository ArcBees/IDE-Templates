package com.arcbees.ide.plugin.template.presenter;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;

public class PresenterTest {
	private VelocityEngine velocityEngine;

	@Before
	public void testSetup() {
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
				"./src/main/resources/com/arcbees/ide/plugin/template/presenter/");
		velocityEngine.init();
	}

	@Test
	public void testModule() {
		Template t = velocityEngine.getTemplate("__name__Module.java");

		VelocityContext context = new VelocityContext();
		context.put("package", "com.arcbees.project.client.application");
		context.put("name", "ApplicationTest");

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}

	@Test
	public void testPresenter() {
		Template t = velocityEngine.getTemplate("__name__Presenter.java");

		VelocityContext context = new VelocityContext();
		context.put("package", "com.arcbees.project.client.application");
		context.put("name", "ApplicationTest");
		context.put("uihandlers", false);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}

	@Test
	public void testPresenterWithUiHandlers() {
		Template t = velocityEngine.getTemplate("__name__Presenter.java");

		VelocityContext context = new VelocityContext();
		context.put("package", "com.arcbees.project.client.application");
		context.put("name", "ApplicationTest");
		context.put("uihandlers", true);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}

	@Test
	public void testUiHandlers() {
		Template t = velocityEngine.getTemplate("__name__UiHandlers.java");

		VelocityContext context = new VelocityContext();
		context.put("package", "com.arcbees.project.client.application");
		context.put("name", "ApplicationTest");
		context.put("uihandlers", false);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}

	@Test
	public void testView() {
		Template t = velocityEngine.getTemplate("__name__View.java");

		VelocityContext context = new VelocityContext();
		context.put("name", "ApplicationTest");
		context.put("uihandlers", false);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}

	@Test
	public void testViewWithUiHandlers() {
		Template t = velocityEngine.getTemplate("__name__View.java");

		VelocityContext context = new VelocityContext();
		context.put("name", "ApplicationTest");
		context.put("uihandlers", true);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}

	@Test
	public void testViewUi() {
		Template t = velocityEngine.getTemplate("__name__View.ui.xml");

		VelocityContext context = new VelocityContext();
		context.put("package", "com.arcbees.project.client.application");
		context.put("name", "ApplicationTest");
		context.put("uihandlers", false);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		// TODO
		System.out.println(writer.toString());
	}
}
