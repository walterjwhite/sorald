package sorald.sonar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.sonar.plugins.java.api.JavaFileScanner;
import sorald.Constants;
import sorald.FileUtils;

/** Helper class that uses Sonar to scan projects for rule violations. */
public class ProjectScanner {
    private ProjectScanner() {}

    /**
     * Scan a project for rule violations.
     *
     * @param target Targeted file or directory of the project.
     * @param baseDir Base directory of the project.
     * @param sonarCheck The check to scan with.
     * @return All violations in the target.
     */
    public static Set<RuleViolation> scanProject(
            File target, File baseDir, JavaFileScanner sonarCheck) {
        return scanProject(target, baseDir, List.of(sonarCheck));
    }

    /**
     * Scan a project for rule violations.
     *
     * @param target Targeted file or directory of the project.
     * @param baseDir Base directory of the project.
     * @param sonarChecks Checks to scan with.
     * @return All violations in the target.
     */
    public static Set<RuleViolation> scanProject(
            File target, File baseDir, List<JavaFileScanner> sonarChecks) {
        return scanProject(target, baseDir, sonarChecks, List.of());
    }

    /**
     * Scan a project for rule violations, with additional type information collected from the
     * provided classpath.
     *
     * @param target Targeted file or directory of the project.
     * @param baseDir Base directory of the project.
     * @param sonarChecks Checks to scan with.
     * @param classpath Classpath to fetch type information from.
     * @return All violations in the target.
     */
    public static Set<RuleViolation> scanProject(
            File target, File baseDir, List<JavaFileScanner> sonarChecks, List<String> classpath) {
        List<String> filesToScan = new ArrayList<>();
        if (target.isFile()) {
            filesToScan.add(target.getAbsolutePath());
        } else {
            try {
                filesToScan =
                        FileUtils.findFilesByExtension(target, Constants.JAVA_EXT).stream()
                                .map(File::getAbsolutePath)
                                .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return RuleVerifier.analyze(filesToScan, baseDir, sonarChecks, classpath);
    }
}
