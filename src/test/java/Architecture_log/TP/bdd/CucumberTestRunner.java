package Architecture_log.TP.bdd;

import static io.cucumber.junit.platform.engine.Constants.*;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

//@Suite
//@IncludeEngines("cucumber")
//@SelectClasspathResource("features")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "Architecture_log.TP.bdd")
//@ConfigurationParameter(key = PUBLISH_QUIET_PROPERTY_NAME, value = "true")
public class CucumberTestRunner {
  // Run tests with:
  // mvn verify -Dgroups="cucumber"
}
