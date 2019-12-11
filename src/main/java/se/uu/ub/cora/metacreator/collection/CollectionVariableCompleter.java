package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.RecordCompleter;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class CollectionVariableCompleter extends RecordCompleter implements ExtendedFunctionality {

	public CollectionVariableCompleter(String implementingTextType) {
		super(implementingTextType);
	}

	public static CollectionVariableCompleter forTextLinkedRecordType(String textLinkedRecordType) {
		return new CollectionVariableCompleter(textLinkedRecordType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, DataGroup spiderDataGroup) {
		this.dataGroup = spiderDataGroup;
		addValuesToDataGroup();
	}
}
