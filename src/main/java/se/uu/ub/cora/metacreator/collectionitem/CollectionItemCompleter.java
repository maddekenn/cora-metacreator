package se.uu.ub.cora.metacreator.collectionitem;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class CollectionItemCompleter implements ExtendedFunctionality {

	private String implementingTextType;

	public CollectionItemCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static CollectionItemCompleter forImplementingTextType(String implementingTextType) {
		return new CollectionItemCompleter(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {

		MetadataCompleter metadataCompleter = new MetadataCompleter();
		metadataCompleter.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
	}

}
