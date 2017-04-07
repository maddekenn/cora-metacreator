package se.uu.ub.cora.metacreator.collectionitem;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class CollectionItemCompleter implements ExtendedFunctionality {

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {

		MetadataCompleter metadataCompleter = new MetadataCompleter();
		metadataCompleter.completeSpiderDataGroupWithTexts(spiderDataGroup);
	}
}
