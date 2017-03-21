package se.uu.ub.cora.metacreator.collectionItem;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataCompleterSpy implements MetadataCompleter {

	public List<SpiderDataGroup> calledWithMetadataGroups = new ArrayList<>();

	@Override
	public void completeSpiderDataGroupWithTexts(SpiderDataGroup metadataDataGroup) {
		calledWithMetadataGroups.add(metadataDataGroup);
	}

}
