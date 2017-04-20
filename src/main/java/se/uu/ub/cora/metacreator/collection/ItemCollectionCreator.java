package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.metacreator.RecordCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class ItemCollectionCreator implements ExtendedFunctionality {

	private final String implementingTextType;
	private String authToken;
	private SpiderDataGroup spiderDataGroup;

	public ItemCollectionCreator(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static ItemCollectionCreator forImplementingTextType(String implementingTextType) {
		return new ItemCollectionCreator(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.authToken = authToken;
		this.spiderDataGroup = spiderDataGroup;

		possiblyCreateItems(authToken, spiderDataGroup);
		possiblyCreateTexts(authToken, spiderDataGroup);
	}

	private void possiblyCreateItems(String authToken, SpiderDataGroup spiderDataGroup) {
		SpiderDataGroup itemReferences = spiderDataGroup.extractGroup("collectionItemReferences");
		for (SpiderDataElement child : itemReferences.getChildren()) {
			SpiderDataGroup item = (SpiderDataGroup) child;
			createItemIfMissing(authToken, item);
		}
	}

	private void createItemIfMissing(String authToken, SpiderDataGroup item) {
		String id = extractId(item);
		if (itemDoesNotExist(authToken, id)) {
			createItem(id);
		}
	}

	private String extractId(SpiderDataGroup child) {
		SpiderDataGroup ref = child;
		return ref.extractAtomicValue("linkedRecordId");
	}

	private boolean itemDoesNotExist(String userId, String id) {
		SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
		try {
			reader.readRecord(userId, "metadataCollectionItem", id);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createItem(String id) {
		SpiderDataGroup item = SpiderDataGroup.withNameInData("metadata");
		String dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(spiderDataGroup);
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);

		item.addChild(recordInfo);
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeSpiderDataGroupWithLinkedTexts(item, "text");

		addAtomicValues(id, item);
		item.addAttributeByIdWithValue("type", "collectionItem");
		createRecord("metadataCollectionItem", item);
	}

	private void addAtomicValues(String linkedRecordId, SpiderDataGroup item) {
		String nameInData = linkedRecordId.substring(0, linkedRecordId.indexOf("Item"));
		item.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));
	}

	private void createRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToCreate) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(authToken, recordTypeToCreate,
				spiderDataGroupToCreate);
	}

	private void possiblyCreateTexts(String authToken, SpiderDataGroup spiderDataGroup) {
		RecordCreatorHelper recordCreatorHelper = RecordCreatorHelper
				.withAuthTokenSpiderDataGroupAndImplementingTextType(authToken, spiderDataGroup,
						implementingTextType);
		recordCreatorHelper.createTextsIfMissing();
	}
}
