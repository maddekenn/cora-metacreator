package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.metacreator.RecordCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class CollectionVariableCompleter extends RecordCompleter implements ExtendedFunctionality {

	public CollectionVariableCompleter(String implementingTextType) {
		super(implementingTextType);
	}

	public static CollectionVariableCompleter forTextLinkedRecordType(String textLinkedRecordType) {
		return new CollectionVariableCompleter(textLinkedRecordType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.spiderDataGroup = spiderDataGroup;
		addValuesToDataGroup();
	}
}
