/*
 *
 *
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.component.jbehave.features;

import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.IDE_CONSOLE;
import static org.jbehave.core.reporters.Format.TXT;

import java.util.Arrays;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.io.StoryPathResolver;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.parsers.gherkin.GherkinStoryParser;
import org.jbehave.core.reporters.FilePrintStreamFactory.ResolveToPackagedName;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;

@RunWith(JUnitReportingRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class AbstractSpringJBehaveStory extends JUnitStory {

	@ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
    private static final int STORY_TIMEOUT = 120;

    @Autowired
    private ApplicationContext applicationContext;

    private Configuration configuration;

    private TestContextManager testContextManager;
    
    public AbstractSpringJBehaveStory() {
    	testContextManager = new TestContextManager( getClass() );
        applicationContext = testContextManager.getTestContext().getApplicationContext();

        Embedder embedder = new Embedder();
        embedder.useMetaFilters(Arrays.asList("-skip"));
        useEmbedder(embedder);
    }

    @Override
    public Configuration configuration() {
        if (this.configuration == null) {
            this.configuration = new MostUsefulConfiguration()
                    .useStoryLoader(storyLoader())
                    .usePendingStepStrategy(new FailingUponPendingStep())
                    .useStoryParser(new GherkinStoryParser())
                    .useStoryPathResolver(storyPathResolver())
                    .useStepCollector(stepCollector())
                    .useStoryReporterBuilder(storyReporterBuilder())
                    .useParameterControls(parameterControls());
        }
        return this.configuration;
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), applicationContext);
    }

    private StepCollector stepCollector() {
        return new MarkUnmatchedStepsAsPending(new StepFinder(new StepFinder.ByLevenshteinDistance()));
    }

    private ParameterControls parameterControls() {
        return new ParameterControls()
                .useDelimiterNamedParameters(true);
    }

    private StoryPathResolver storyPathResolver() {
        return new UnderscoredCamelCaseResolver(".feature");
    }

    private StoryLoader storyLoader() {
        return new LoadFromClasspath();
    }

    private StoryReporterBuilder storyReporterBuilder() {
        return new StoryReporterBuilder()
                .withCodeLocation(CodeLocations.codeLocationFromClass(this.getClass()))
                .withPathResolver(new ResolveToPackagedName())
                .withFailureTrace(true)
                .withDefaultFormats()
                .withFormats(IDE_CONSOLE, TXT, HTML);
    }

}
