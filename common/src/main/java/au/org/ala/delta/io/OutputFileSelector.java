package au.org.ala.delta.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.directives.ParsingContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.rtf.RTFUtils;

public class OutputFileSelector {
	
	private int _characterForOutputFiles = 0;
	private Map<String, String> _itemOutputFiles = new HashMap<String, String>();
	private DeltaDataSet _dataSet;
	private String _subjectForOutputFiles;
	private String _intkeyOutputFile;
	private String _keyOutputFile;
	private ParsingContext _context;
	
	public OutputFileSelector(DeltaDataSet dataSet) {
		_dataSet = dataSet;
	}

	public void setParsingContext(ParsingContext context) {
		_context = context;
	}
	public String getItemOutputFile(int itemNumber) {
		if (_itemOutputFiles.isEmpty() && _characterForOutputFiles == 0) {
			throw new RuntimeException("One of ITEM OUTPUT FILES or CHARACTER FOR OUTPUT FILES must be specified.");
		}
		
		String outputFile = "";
		// This basically says use the current output file if a new one is
		// not specified for this item.  (to mimic CONFOR behaviour).
		while (itemNumber >= 1 && StringUtils.isEmpty(outputFile)) {
			if (_characterForOutputFiles == 0) {
				String description = itemDescriptionFor(itemNumber);
				outputFile = _itemOutputFiles.get(description);
			}
			else {
				Attribute attribute = _dataSet.getAttribute(itemNumber, _characterForOutputFiles);
				if (attribute != null) {
					outputFile = attribute.getValueAsString();
				}
			}
			itemNumber--;
		}
		return outputFile;
	}
	
	public void setSubjectForOutputFiles(String subjectForOutputFiles) {
		_subjectForOutputFiles = subjectForOutputFiles;
	}
	
	public String getSubjectForOutputFiles() {
		return _subjectForOutputFiles;
	}
	
	public void setItemOutputFile(String itemDescription, String fileName) {
		_itemOutputFiles.put(RTFUtils.stripFormatting(itemDescription), fileName);
	}
	
	private String itemDescriptionFor(int itemNumber) {
		Item item = _dataSet.getItem(itemNumber);
		String description = RTFUtils.stripFormatting(item.getDescription());
		return description;
	}
	
	public String getIntkeyOutputFilePath() {
		return makeAbsolute(_intkeyOutputFile);
	}
	
	public void setIntkeyOutputFile(String intkeyOut) {
		_intkeyOutputFile = intkeyOut;
	}

	public String getKeyOutputFile() {
		return makeAbsolute(_keyOutputFile);
	}
	
	public void setKeyOutputFile(String keyOut) {
		_keyOutputFile = keyOut;
	}
	
	private String makeAbsolute(String fileName) {
		File file = new File(fileName);
		if (!file.isAbsolute()) {
			File workingDir = _context.getFile().getParentFile();
			file = new File(workingDir, fileName);
		}
		return file.getAbsolutePath();
	}
}
