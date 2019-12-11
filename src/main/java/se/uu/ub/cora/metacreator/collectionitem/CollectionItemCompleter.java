package se.uu.ub.cora.metacreator.collectionitem;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class CollectionItemCompleter implements ExtendedFunctionality {

	private String implementingTextType;

	public CollectionItemCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static CollectionItemCompleter forTextLinkedRecordType(String implementingTextType) {
		return new CollectionItemCompleter(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, DataGroup spiderDataGroup) {

		MetadataCompleter metadataCompleter = new MetadataCompleter();
		metadataCompleter.completeDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
	}

public String getImplementingTextType() {
		return implementingTextType;
	}
}
