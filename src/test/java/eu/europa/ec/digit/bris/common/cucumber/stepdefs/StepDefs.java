package eu.europa.ec.digit.bris.common.cucumber.stepdefs;

import eu.europa.ec.digit.bris.common.SkeletonApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = SkeletonApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
