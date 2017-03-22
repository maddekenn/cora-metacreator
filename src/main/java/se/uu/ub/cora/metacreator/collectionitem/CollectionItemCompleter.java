package se.uu.ub.cora.metacreator.collectionitem;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.metacreator.text.TextCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class CollectionItemCompleter implements ExtendedFunctionality {

	private String implementingTextType;
	private SpiderDataGroup spiderDataGroup;
	private String userId;
	private String dataDividerString;

	public CollectionItemCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static CollectionItemCompleter forImplementingTextType(
			String implementingTextType) {
		return new CollectionItemCompleter(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;
		this.spiderDataGroup = spiderDataGroup;

		dataDividerString = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(spiderDataGroup);
		MetadataCompleter metadataCompleter = new MetadataCompleter();
		metadataCompleter.completeSpiderDataGroupWithTexts(spiderDataGroup);

		createTextsIfMissing();
	}

	private void createTextsIfMissing() {
		createTextIfTextIdIsMissing("textId");
		createTextIfTextIdIsMissing("defTextId");
	}

	private void createTextIfTextIdIsMissing(String id) {
		if(textIdDoesNotExist(spiderDataGroup, id)) {
			createTextWithTextIdInStorage(spiderDataGroup.extractAtomicValue(id));
		}
	}

	private boolean textIdDoesNotExist(SpiderDataGroup spiderDataGroup, String textId) {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(userId, implementingTextType,
					spiderDataGroup.extractAtomicValue(textId));
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createTextWithTextIdInStorage(String textId) {
		TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDividerString);
		SpiderDataGroup textGroup = textCreator.createText();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(userId, implementingTextType, textGroup);
	}
}
