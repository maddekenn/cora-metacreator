package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class ItemCollectionCompleter implements ExtendedFunctionality {

	private String implementingTextType;

	public ItemCollectionCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static ItemCollectionCompleter forTextLinkedRecordType(String linkedRecordType) {
		return new ItemCollectionCompleter(linkedRecordType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, DataGroup dataGroup) {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeDataGroupWithLinkedTexts(dataGroup, implementingTextType);
	}

	public String getImplementingTextType() {
		return implementingTextType;
	}
}
