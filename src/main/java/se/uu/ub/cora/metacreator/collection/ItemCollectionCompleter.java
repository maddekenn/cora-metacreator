package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
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
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
	}
}
