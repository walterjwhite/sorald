package sorald.sonar;

import java.nio.file.Path;
import java.util.Objects;
import org.sonarsource.sonarlint.core.analysis.api.TextRange;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;
import sorald.rule.RuleViolation;

/** Facade around {@link org.sonarsource.sonarlint.core.client.api.common.analysis.Issue} */
class ScannedViolation extends RuleViolation {
    private final Issue issue;
    private final TextRange textRange;

    ScannedViolation(Issue issue) {
        textRange = issue.getTextRange();
        this.issue = issue;
    }

    @Override
    public int getStartLine() {
        return textRange.getStartLine();
    }

    @Override
    public int getEndLine() {
        return textRange.getEndLine();
    }

    @Override
    public int getStartCol() {
        return textRange.getStartLineOffset();
    }

    @Override
    public int getEndCol() {
        return textRange.getEndLineOffset();
    }

    @Override
    public Path getAbsolutePath() {
        Path path = Objects.requireNonNull(issue.getInputFile()).getClientObject();
        return path.toAbsolutePath().normalize();
    }

    @Override
    public String getRuleKey() {
        return issue.getRuleKey().split(":")[1];
    }

    @Override
    public String getMessage() {
        return issue.getMessage();
    }
}
