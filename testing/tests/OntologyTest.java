package checkers.inference;

import org.checkerframework.framework.test.TestUtilities;
import org.checkerframework.javacutil.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import checkers.inference.test.CFInferenceTest;
import constraintsolver.ConstraintSolver;

public class OntologyTest extends CFInferenceTest {

    public OntologyTest(File testFile) {
        super(testFile,  ontology.OntologyChecker.class, "ontology",
              "-Anomsgtext", "-d", "tests/build/outputdir");
    }

    @Override
    public Pair<String, List<String>> getSolverNameAndOptions() {
        return Pair.<String, List<String>> of(ConstraintSolver.class.getCanonicalName(), new ArrayList<String>());
    }

    @Parameters
    public static List<File> getTestFiles(){
        List<File> testfiles = new ArrayList<>(); //InferenceTestUtilities.findAllSystemTests();
        testfiles.addAll(TestUtilities.findRelativeNestedJavaFiles("testdata", "ontology"));
        return testfiles;
    }
}
