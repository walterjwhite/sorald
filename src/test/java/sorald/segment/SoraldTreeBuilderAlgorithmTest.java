package sorald.segment;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import sorald.Constants;

public class SoraldTreeBuilderAlgorithmTest {

    @Test
    public void treeBuildTest() {
        String folder = Constants.PATH_TO_RESOURCES_FOLDER + "DummyTreeDir";
        Node rootNode = SoraldTreeBuilderAlgorithm.buildTree(folder);
        File rootFolder = new File(folder);
        Assert.assertEquals("DummyTreeDir", rootFolder.getName());
        Assert.assertEquals(3, rootNode.getJavaFilesNbs());
        Assert.assertEquals(2, rootNode.getChildren().size());

        Node rootFileNode = rootNode.getChildren().get(1);
        Assert.assertTrue(rootFileNode.isFileNode());
        Assert.assertEquals(1, rootFileNode.getJavaFiles().size());
        File file1 = new File(rootFileNode.getJavaFiles().get(0));
        Assert.assertEquals("DummyOne.java", file1.getName());

        Node subDirNode = rootNode.getChildren().get(0);
        File subDirFolder = new File(subDirNode.getRootPath());
        Assert.assertTrue(subDirNode.isDirNode());
        Assert.assertEquals("DummySubFolder", subDirFolder.getName());
        Assert.assertEquals(2, subDirNode.getJavaFilesNbs());
        Assert.assertEquals(1, subDirNode.getChildren().size());

        Node subDirFileNode = subDirNode.getChildren().get(0);
        File file2 = new File(subDirFileNode.getJavaFiles().get(0));
        File file3 = new File(subDirFileNode.getJavaFiles().get(1));
        List<String> dummyFileNames =
                subDirFileNode.getJavaFiles().stream()
                        .map(absolutPath -> new File(absolutPath).getName())
                        .collect(Collectors.toList());
        Assert.assertThat(dummyFileNames, containsInAnyOrder("DummyTwo.java", "DummyThree.java"));
    }
}
