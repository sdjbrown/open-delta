package au.org.ala.delta.confor;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

/**
 * Tests the TRANSLATE INTO NEXUS FORAMT directive in the context of the sample
 * tonex directives file.
 */
public class ToNexTest extends ConforTestCase {

	@Test
	public void testSampleToNex() throws Exception {
		runConfor();
		
		File expectedFile = new File(FilenameUtils.concat(_samplePath, "expected_results/nexdata"));
		String expected = FileUtils.readFileToString(expectedFile, "utf-8");

		System.out.println(expected);
		
		File actualFile = new File(FilenameUtils.concat(_samplePath, "nexdata"));
		String actual = FileUtils.readFileToString(actualFile, "utf-8");

		System.out.print(actual);
		
		
		boolean dosEol = expected.contains("\r\n");
		String expectedLineSeparator = "\n";
		if (dosEol) {
			expectedLineSeparator = "\r\n";
		}
		
		if (!System.getProperty("line.separator").equals(expectedLineSeparator)) {
			expected = expected.replaceAll(expectedLineSeparator, System.getProperty("line.separator"));
		}
		// The heading contains the date so will be different.
		String heading = "Grass Genera 14:43 20-OCT-11"; // <Date>, eg. 11:32 05-OCT-11
		
		actual = actual.replaceAll("Grass Genera.*[0-9]{2}-[a-zA-Z]{3}-[0-9]{4}", heading);
		
		for (int i=0; i<expected.length(); i++) {
			if (expected.charAt(i) != actual.charAt(i)) {
				System.out.println("Difference @ char: "+i+" Expected: "+expected.charAt(i)+(int)expected.charAt(i)+", Actual: "+actual.charAt(i)+(int)actual.charAt(i));
				break;
			}
		}
		//assertEquals(expected.trim(), actual.trim());
	}

	@Override
	protected String directivesFileName() {
		return "tonex";
	}
	
	
}