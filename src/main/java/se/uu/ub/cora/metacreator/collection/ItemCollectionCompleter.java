package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class ItemCollectionCompleter implements ExtendedFunctionality {

	private SpiderDataGroup spiderDataGroup;

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.spiderDataGroup = spiderDataGroup;
		addValuesToDataGroup();
	}

	private void addValuesToDataGroup() {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
		String id = recordInfoGroup.extractAtomicValue("id");
		addTexts(id);
	}

	private void addTexts(String id) {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup);
	}

}
