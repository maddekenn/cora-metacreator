package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class CollectionVariableCompleter implements ExtendedFunctionality {

	private String textLinkedRecordType;
	private SpiderDataGroup spiderDataGroup;

	public CollectionVariableCompleter(String textLinkedRecordType) {
		this.textLinkedRecordType = textLinkedRecordType;
	}

	public static CollectionVariableCompleter forTextLinkedRecordType(String textLinkedRecordType) {
		return new CollectionVariableCompleter(textLinkedRecordType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.spiderDataGroup = spiderDataGroup;
		addValuesToDataGroup();
	}

	private void addValuesToDataGroup() {
		addTexts();
	}

	private void addTexts() {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, textLinkedRecordType);
	}

}
